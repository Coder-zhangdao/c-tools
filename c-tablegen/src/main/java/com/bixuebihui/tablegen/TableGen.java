package com.bixuebihui.tablegen;

/**
 * TableGen
 * <p>A generator of container classes for a database.
 * This application uses the database metadata to create
 * one object per table designed to hold the type of data
 * stored within that table.</p>
 * It also generate methods to update,retrieve and insert
 * the object data within the database. The update and
 * retrieve methods come in two flavours, ByKey and ByIndex.</p>
 * <p>If keys are found on a table then updateByKey and
 * retrieveByKey methods are created</p>
 * <p>If foreign_keys are found on the table retrieveByXXX methods are created for each key
 * as well
 * <p>If indexes are found on a table then updateByIndex and
 * retrieveByIndex methods are created</p>
 * insert and getFromResultSet methods are always created.
 *
 *
 * @author  J.A.Carter, Xing Wanxiang
 * @version 1.6
 * (c) Joe Carter 1998 , Modified by Xwx
 * Released under GPL. See LICENSE for full details.
 */

import com.bixuebihui.algorithm.LRULinkedHashMap;
import com.bixuebihui.cache.DictionaryCache;
import com.bixuebihui.cache.DictionaryItem;
import com.bixuebihui.datasource.BitmechanicDataSource;
import com.bixuebihui.dbcon.DatabaseConfig;
import com.bixuebihui.generated.tablegen.business.T_metatableManager;
import com.bixuebihui.generated.tablegen.pojo.T_metacolumn;
import com.bixuebihui.generated.tablegen.pojo.T_metatable;
import com.bixuebihui.jdbc.*;
import com.bixuebihui.tablegen.dbinfo.ProcedureGen;
import com.bixuebihui.tablegen.dbinfo.ProcedureInfo;
import com.bixuebihui.tablegen.dbinfo.ProcedureParameterInfo;
import com.bixuebihui.tablegen.dbinfo.ProcedureUtils;
import com.bixuebihui.tablegen.diffhandler.DiffHandler;
import com.bixuebihui.util.other.CMyFile;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.bixuebihui.tablegen.NameUtils.columnNameToFieldName;

/**
 * 思路决定出路，创新才有发展，整合才能壮大
 *
 * @author xingwx
 *
 */
public class TableGen implements DiffHandler {
	public static final String DB_ERROR = "[GEN]Error setup database";
	public static final String MANAGER_SUFFIX = "Manager";
	public static final String INDENT = "      ";
	public static final String BUSINESS = "business";
	private static final Log LOG = LogFactory.getLog(TableGen.class);
	static String[] genericFiles = { "BaseList" };
	String catalog;
	String packageName;
	String srcDir;
	String testDir;
	String resourceDir;
	String jspDir;
	String schema;
	String prefix;
	String versionColName = "version";
	boolean indexes;
	boolean overWriteAll = false;
	boolean kuozhanbiao = false;
	boolean use_annotation = false;
	boolean keep_case = false;
	boolean use_autoincrement = false;
	boolean generate_procedures = true;
	DatabaseMetaData metaData;
	BufferedWriter currentOutput;
	Map<String, T_metatable> tableData;
	/**
	 * If the extended table is available, each table can implement the following interfaces
	 */
	String pojo_node_interface;
	String pojo_version_interface;
	String pojo_state_interface;
	String pojo_uuid_interface;
	String pojo_modifydate_interface;
	List<String> pojo_node_interface_list;
	List<String> pojo_version_interface_list;
	List<String> pojo_state_interface_list;
	List<String> pojo_uuid_interface_list;
	List<String> pojo_modifydate_interface_list;

	/**
	 * names of tables to generate for. If null we do all.
 	 */
	Map<String, String> tablesList;
	Map<String, String> excludeTablesList;
	/**
	 * additional settings, used for comments, data checking, interfaces, etc.
	 */
	String extra_setting;
	IDbHelper dbHelper = null;
	T_metatableManager daoMetaTable = null;
	/**
	 * Writes out the variables initial value if not null able.
	 */
	Map<String,String> typeDefaultValue = initDefaultTypeValue();
	/**
	 * Whether the generation is successful
	 */
	private boolean generateFlag = true;
	private String tableOwner;
	private boolean traceEnable = false;
	private Connection connection;
	private List<String> tableNames;
	private final Map<String, List<String>> keyCache = new LRULinkedHashMap<>(500);
	private final Map<String, List<ColumnData>> colDataCache = new LRULinkedHashMap<>(500);
	private final Map<String, List<String>> indexCache = new LRULinkedHashMap<>(200);
	private final Map<String, List<FKDefinition>> foreignKeyImCache = new LRULinkedHashMap<>(
			400);
	private final Map<String, List<FKDefinition>> foreignKeyExCache = new LRULinkedHashMap<>(
			400);
	private DatabaseConfig dbconfig;
	private PrintStream console = System.out;
	private String propertiesFilename;
	private Properties configProperties;

	public TableGen(OutputStream outputStream) {
		initConsole(outputStream);
	}

	public TableGen() {
		LOG.warn("use this constructor in TableGen is deprecated!");
	}

	/**
	 * Application start.
	 *
	 * @throws SQLException
	 */
	public static void main(String[] argv) throws SQLException {
		TableGen us = new TableGen();

		if (argv.length >= 1) {
			us.run(argv[0]);
		} else {
			us.run(null);
		}

	}

	/**
	 * Displays the usage of the TableGen program and exits with the error code.
	 */
	public static void usageAndExit(int code) {
		System.err.println("Usage: java TableGen properties_file_name");
		System.exit(code);
	}


	static Map<String ,String> initDefaultTypeValue(){
		Map<String,String> map = new HashMap<>();
		map.put("String", "\"\"");
		map.put("int", "0");
		map.put("byte", "0");
		map.put("long", "0");
		map.put("Integer", "0");
		map.put("Short", "0");
		map.put("Byte", "(byte)0");
		map.put("Long", "0L");
		map.put("Date", "new Date(0)");
		map.put("Timestamp", "new Timestamp(new java.util.Date().getTime())");
		map.put("Boolean", "Boolean.FALSE");
		map.put("Double", "0.0");
		map.put("Float", "0.0F");
		return  map;
	}

	private void initConsole(OutputStream out) {
		if (out instanceof PrintStream) {
			console = (PrintStream) out;
		} else if (out == System.out) {
		    //do nothing
		} else {
			console = new PrintStream(out);
		}

	}

	public IDbHelper getDbHelper() {
		if (dbHelper != null) {
			return dbHelper;
		} else {
			DbHelper db = new DbHelper();
			BitmechanicDataSource ds = new BitmechanicDataSource();
			ds.setDatabaseConfig(dbconfig);
			db.setDataSource(ds);
			dbHelper = db;
			daoMetaTable = new T_metatableManager();
			daoMetaTable.setDbHelper(dbHelper);
		}
		return dbHelper;
	}

	public void info(String message) {
		console.println(message);
		LOG.info(message);
	}

	public void trace(String message) {
		if (traceEnable) {
			console.println(message);
			LOG.info(message);
		}
	}

	public void run(String filename) throws SQLException {
		run(filename, System.out);
	}

	public void run(String filename, OutputStream out) throws SQLException {
		initConsole(out);

		if (filename != null && filename.length() > 0) {
			init(filename);
		}
		try {
			makeDir();
			connect(); // get MetaData as well
			getTableData();

			/**
			 * 如果generator_all == yes，则生成所有表 否则，进行本地快照和数据库的比对
			 */
			generateBaseList();
			if ("yes".equals(configProperties.getProperty("generate_all"))) {


				generatePojos();
				generateDALs();

				generateBusinesses();

				generateWebUI();

				generateTest();
				if(jspDir!=null) {
					generateJsp();
				}
				generateSpringXml();

				if (generate_procedures) {
					generateStoreProcedures();
				}
			} else {

				Connection conn = getDbHelper().getConnection();
				try {
					DbDiff dd = new DbDiff(getTableDataFromLocalCache(), conn);
					dd.addDiffHandler(this);
					dd.compareTables();
				} finally {
					DbUtils.close(conn);
				}
			}

		} catch (SQLException | InstantiationException | IOException ex1) {
			ex1.printStackTrace(console);
		} catch (IllegalAccessException e) {
		    LOG.error(e);
		} finally {
			DbUtils.close(getConnection());
		}

		info("Successfully complected!");
	}

	private void generateSpringXml(){

		info("Generating spring-dal.xml (if it exists before) : ");

		String fileName = this.resourceDir + File.separator + "config" + File.separator + "spring-dal.xml";
		try {
			Map<String, String> beans = new HashMap<>();
			for (String tableName:getTableNames()) {
				beans.put(NameUtils.firstLow(tableName, false) + MANAGER_SUFFIX, getBusFullClassName(tableName));
			}
			File f = new File(fileName);
			String xml = null;
			if (f.exists()) {
				xml = FileUtils.readFileToString(f,"UTF-8");
			}

			if (StringUtils.trimToNull(xml) == null) {
				xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n"
						+ "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:context=\"http://www.springframework.org/schema/context\"\n"
						+ "    xmlns:aop=\"http://www.springframework.org/schema/aop\" xmlns:tx=\"http://www.springframework.org/schema/tx\"\n"
						+ "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans \n"
						+ "    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd\">\n" + "</beans>\n";
			}

			DalXmlEditor.write(DalXmlEditor.addBeans(xml, beans), fileName);

		} catch (IOException e) {
			LOG.warn(e.getMessage());
		}
	}

	private void generateStoreProcedures() throws SQLException {

		try {
			info("Generating Store Procedures : ");

			String tableName = "Procedures";
			String fileName = getBaseSrcDir() + File.separator + "stub" + File.separator + tableName + ".java";
			currentOutput = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(fileName), TableGenConfig.FILE_ENCODING));// new

			writeHeader(tableName, "stub", "extends BaseList<Object, Object>");

			List<ProcedureInfo> proc = ProcedureUtils.getProcedure(metaData, catalog, schema, tableOwner, null, null);

			for (ProcedureInfo info : proc) {
				List<ProcedureParameterInfo> rs = ProcedureUtils.getProcedureColumns(metaData, info);
				String str = ProcedureGen.process(info, rs, keep_case);
				out(str);
			}

			out("public Object mapRow(ResultSet arg0, int arg1) throws SQLException {\n"
					+ "	// TODO Auto-generated method stub\n" + "	return null;\n" + "}\n"
					+ "public Object getId(Object arg0) {\n" + "	// TODO Auto-generated method stub\n"
					+ "	return null;\n" + "}\n" + "public Object getNextKey() {\n"
					+ "	// TODO Auto-generated method stub\n" + "	return null;\n" + "}\n"
					+ "public boolean insertDummy() throws SQLException {\n"
					+ "	// TODO Auto-generated method stub\n" + "	return false;\n" + "}\n"
					+ "public void setId(Object arg0, Object arg1) {\n" + "	// TODO Auto-generated method stub\n"
					+ "	\n" + "}\n" + "@Override\n" + "public String getKeyName() {\n"
					+ "	// TODO Auto-generated method stub\n" + "	return null;\n" + "}\n" + "@Override\n"
					+ "public String getTableName() {\n" + "	// TODO Auto-generated method stub\n"
					+ "	return null;\n" + "}");
			out("}");
			currentOutput.close();

		} catch (IOException e) {
			e.printStackTrace(console);
			LOG.warn(e);
		}
	}

	@SuppressWarnings("unchecked")
	private synchronized Map<String, List<ColumnData>> getTableDataFromLocalCache() {

		String path = getCachedTableDataFilePath();
		File file = new File(path);
		if(file.exists()) {
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {

				Map<String, List<ColumnData>> v = (Map<String, List<ColumnData>>) in.readObject();

				if (v == null) {
					return new HashMap<>();
				}
				return v;

			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace(console);
				LOG.warn(e);
			}
		}

		return new HashMap<>();

	}

	@SuppressWarnings("unchecked")
	private synchronized List<ColumnData> getColumnDataFromLocalCache() {

		String path = getCachedColumnDataFilePath();
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))){
			return  (List<ColumnData>) in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			LOG.warn(e);
		}
		return new ArrayList<>();

	}

	private String getCachedColumnDataFilePath() {

		String baseDir = getConfigBaseDir(propertiesFilename);
		String src_dir = "target";

		return  baseDir + File.separator + src_dir + File.separator + "gen_column_data.cache";
	}

	private String getCachedTableDataFilePath() {
		String baseDir = System.getProperty("user.dir");

		String src_dir = "target";

		return baseDir + File.separator + src_dir + File.separator + "gen_table_data.cache";
	}

	private synchronized void saveTableDataToLocalCache(HashMap<String, List<ColumnData>> data) {

		String path = getCachedTableDataFilePath();
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path)); ){
			out.writeObject(data);
		} catch (IOException e) {
			LOG.warn(e);
		}

	}

	private synchronized void saveColumnDataToLocalCache(ArrayList<ColumnData> data) {
		String path = getCachedColumnDataFilePath();
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))){
			out.writeObject(data);
		} catch (IOException e) {
		    LOG.warn(e);
		}

	}

	protected String getTableComment(String tableName) {
		if (kuozhanbiao) {
			DictionaryItem item = DictionaryCache
					.byId(TableGenConfig.METATABLE_DICT + DictionaryCache.KEY_SEPARATOR + tableName);
			return item == null ? tableName : item.getValue();
		} else {
			return tableName;
		}
	}

	private String getColumnDescription(String tableName, String columnName) {
		String res;
		if (kuozhanbiao) {
			DictionaryItem item = null;
			try {
				item = DictionaryCache.byId(TableGenConfig.METACOLUMN_DICT + DictionaryCache.CONDITION_SEPARATOR
						+ getTableIdByName(tableName) + DictionaryCache.KEY_SEPARATOR + columnName.toUpperCase());
			} catch (Exception e) {
				e.printStackTrace(console);
			}
			res = item == null ? columnName : item.getValue();
		} else {
			res = columnName;
		}

		if (tableData != null && tableData.get(tableName) != null) {
			Map<String, T_metacolumn> cols = tableData.get(tableName).getColumns();
			if (cols != null && cols.get(columnName) != null) {
				T_metacolumn col = cols.get(columnName);
				res += col == null ? "" : ": " + col.getDescription();
			}
		}
		return res;
	}

	private int getTableIdByName(String tableName) {
		if (kuozhanbiao) {
			DictionaryItem item = null;
			try {
				item = DictionaryCache.byValue(
						TableGenConfig.TABLENAME_DICT + DictionaryCache.KEY_SEPARATOR + tableName.toUpperCase());
			} catch (Exception e) {
				e.printStackTrace(console);
			}
			return item == null ? 0 : Integer.parseInt(item.getValue());
		} else {
			return 0;
		}
	}

	private void generateJsp() throws SQLException {

		try {

			for (String tableName: getTableNames()) {
				info("Generating JSPs : " + tableName);

				String fileName = jspDir + File.separator + "list" + File.separator + prefix + tableName.toLowerCase()
						+ "_list.jsp";
				currentOutput = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(fileName), TableGenConfig.FILE_ENCODING));// new

				getColumnData(tableName);
				String baseDir = packageName + ".";
				String serviceName = getPojoClassName(tableName) + "Manager";
				String controlerName = getPojoClassName(tableName) + "WebUI";

				out("<%@ page language=\"java\" import=\"java.util.*\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>"
						+ "<%@ \npage import=\"" + baseDir + "business." + serviceName + "\"%>" + "<%@ \npage import=\""
						+ baseDir + "web." + controlerName + "\"%><%\n" + "String tableid=\"" + tableName.toLowerCase()
						+ "\";\n" + "\t" + controlerName + " c = new " + controlerName + "();\n" + "\n"
						+ "\tc.setService(new " + serviceName + "());\n" + "\tc.setId(tableid);\n"
						+ "\tc.setSuccessView(\"welcome\");\n"
						+ "\tif(null==c.handleRequestInternal(request, response)){ return;}\n" + "%>\n" + "<html>\n"
						+ "<head>\n" + "<script type=\"text/javascript\">\n" + "function onInvokeAction(id, code) {\n"
						+ "    $('#ac').val(code);\n" + "    if('edit'==code){\n"
						+ "   $('#editable').val($('#editable').val()==\"true\"?\"false\":\"true\");" + "    }\n"
						+ "\tcreateHiddenInputFieldsForLimitAndSubmit(id);\n" + "}\n"
						+ "function onInvokeAction1(id) {\n" + "    setExportToLimit(id, '');\n"
						+ "    var parameterString = createParameterStringForLimit(id);\n" + "    var url = '"
						+ tableName.toLowerCase() + "_list.jsp?decorator=ajax&ajax=true&' + parameterString;\n"
						+ "   // alert(url);\n" + "    $.get(url, function(data) {\n"
						+ "        $(\"#<%=tableid%>\").html(data)\n" + "    });\n" + "}\n" + "\n"
						+ "function onInvokeExportAction(id) {\n"
						+ "\tvar parameterString = createParameterStringForLimit(id);\n" + "\tlocation.href = '"
						+ tableName.toLowerCase() + "_list.jsp?decorator=none&ajax=true&' + parameterString;\n" + "}\n"
						+ "$(document).ready(function() {$('#selectAllChkBox').click(function(){ShiftCheck('chk', this.checked);})});"
						+ "</script>\n" + "</head>\n" + "<body>\t<form id='<%=tableid %>Form' name=\""
						+ tableName.toLowerCase() + "Form\" action=\"" + tableName.toLowerCase() + "_list.jsp\">\n"
						+ "<%=request.getAttribute(tableid)%>\n"
						+ "\t<input id=\"editable\" name=\"editable\" type=\"hidden\" value=\"<%=request.getParameter(\"editable\") %>\" />\n"
						+ "\t<input id=\"ac\" name=\"ac\" type=\"hidden\" value=\"\" />\n" + "</form>\n" + "</body>\n"
						+ "</html>");

				currentOutput.close();
			}

			// generate index list of all jsp

			info("Generating JSP index list : " + this.packageName + File.separator + "list" + File.separator
					+ "index.jsp");

			String fileName = jspDir + File.separator + "list" + File.separator + "index.jsp";
			currentOutput = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(fileName), TableGenConfig.FILE_ENCODING));// new
			// BufferedWriter(new
			out("<%@ page language=\"java\" import=\"java.util.*\" pageEncoding=\"UTF-8\"%>\n" + "<html>\n" + "<head>\n"
					+ "</head>\n" + "<body><ul>\n");

			for (String tableName:getTableNames()) {
				String href = prefix + tableName.toLowerCase() + "_list.jsp";

				out("\t<li><a href='" + href + "' target=\"mainForm\" >" + tableName.toLowerCase()
						+ "</a>&nbsp;<a href='" + href + "?editable=true' target=\"mainForm\" >" + "编辑</a></li>\n");

			}
			out("</ul></body>\n" + "</html>");

			currentOutput.close();

		} catch (IOException e) {
			e.printStackTrace(console);
		}
	}

	private void generateTest() throws SQLException {
		try {
			for (String tableName: getTableNames()) {
				info("Generating TESTs : " + tableName);
				String baseDir = testDir + File.separator + packageName2Dir(packageName);
				String fileName = baseDir + File.separator + BUSINESS + File.separator + getPojoClassName(tableName)
						+ "ManagerTest.java";
				File f = new File(fileName);
				boolean fileExists = f.exists();
				if (!fileExists) {
					currentOutput = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fileName), TableGenConfig.FILE_ENCODING));

					//getColumnData(tableName);
					writeHeader(tableName, BUSINESS, " extends TestCase");

					writeSelectPageTest(tableName, "select");
					writeCountWhereTest(tableName, "count");

					List<String> keyData = this.getTableKeys(tableName);
					if (keyData != null && keyData.size() == 1) {
						writeInsertDummyTest(tableName, "insertDummy");
					}

					out("}");
					currentOutput.close();
				}
			}

		} catch (IOException e) {
			e.printStackTrace(console);
		}
	}

	private void writeInsertDummyTest(String tableName, String methodName) throws IOException {
		out("public void test" + firstUp(methodName) + "() throws SQLException");
		out("{");
		String className = this.getPojoClassName(tableName) + "Manager";
		out("  " + className + " man = new " + className + "();");
		out("    assertTrue(man." + methodName + "());");
		out("}");
		out("");
	}

	/**
	 * Constructor from a properties file. Default file is "tablegen.prop"
	 *
	 * <TABLE>
	 * <TR>
	 * <TH>Parameter
	 * <TH>Description
	 * </TR>
	 * <TR>
	 * <TD>tables_dir</TD>
	 * <TD>directory where generated classes are placed.</TD>
	 * </TR>
	 * <TR>
	 * <TD>package_name</TD>
	 * <TD>package name to use for gen classes.</TD>
	 * </TR>
	 * <TR>
	 * <TD>foreign_keys</TD>
	 * <TD>(optional) Generate retrieve methods for each foreign_key
	 * available</TD>
	 * </TR>
	 * <TR>
	 * <TD>indexes</TD>
	 * <TD>(optional) Generate index methods if indexes available</TD>
	 * </TR>
	 * <TR>
	 * <TD>schema</TD>
	 * <TD>(optional) DB schema to use</TD>
	 * </TR>
	 * <TR>
	 * <TD>table_list</TD>
	 * <TD>(optional) Comma separated list of tables to build</TD>
	 * </TR>
	 * <TR>
	 * <TD>types_are_strings</TD>
	 * <TD>Some drivers (e.g. MM MySql) return the types as String. If so set
	 * this to yes. Leave out otherwise</TD>
	 * <TR>
	 * <TD>prefix</TD>
	 * <TD>A string to prefix to classes names. To prevent namespace
	 * problems</TD>
	 * </TR>
	 * </TABLE>
	 */
	public synchronized void init(String filename) {
		if (filename == null) {
			propertiesFilename = "tablegen.properties";
		} else {
			propertiesFilename = filename;
		}

		String baseDir = getConfigBaseDir(propertiesFilename);

		try ( FileInputStream fis = new FileInputStream(propertiesFilename) ){
			Properties props = new Properties();

			props.load(fis);

			configProperties = props;

			dbconfig = new DatabaseConfig();
			dbconfig.setAlias(props.getProperty("alias"));
			dbconfig.setClassName(props.getProperty("className"));
			dbconfig.setDburl(props.getProperty("dburl"));
			dbconfig.setUsername(props.getProperty("username"));
			dbconfig.setPassword(props.getProperty("password"));
			if (StringUtils.trimToNull(dbconfig.getAlias()) == null) {
				dbconfig.setAlias("defaultdbcpalias");
			}

			srcDir = baseDir + props.getProperty("src_dir");
			LOG.debug("src_dir:" + srcDir);
			resourceDir = baseDir + props.getProperty("resource_dir");
			LOG.debug("resource_dir:" + resourceDir);

			if(StringUtils.isEmpty(resourceDir)) {
				resourceDir = srcDir;
			}

			testDir = baseDir + props.getProperty("test_dir");
			LOG.debug("test_dir:" + testDir);
			jspDir = baseDir + props.getProperty("jsp_dir");
			LOG.debug("jsp_dir:" + jspDir);
			packageName = props.getProperty("package_name");
			LOG.debug("package_name:" + packageName);
			schema = props.getProperty("schema");
			LOG.debug("schema:" + schema);
			tableOwner = props.getProperty("table_owner");
			LOG.debug("table_owner:" + tableOwner);

			indexes = getBooleanCfg(props, "indexes");
			kuozhanbiao = getBooleanCfg(props, "kuozhanbiao");

			generate_procedures = getBooleanCfg(props, "generate_procedures");

			prefix = props.getProperty("prefix");
			if (prefix == null) {
				prefix = "";
			}

			parseTableNames(props.getProperty("table_list"));
			parseExcludeTableNames(props.getProperty("exclude_table_list"));

			// 有扩展表时用以下interface的设置值
			pojo_node_interface = props.getProperty("pojo_node_interface");
			pojo_version_interface = props.getProperty("pojo_version_interface");
			pojo_state_interface = props.getProperty("pojo_state_interface");
			pojo_uuid_interface = props.getProperty("pojo_uuid_interface");
			pojo_modifydate_interface = props.getProperty("pojo_modifydate_interface");

			pojo_node_interface_list = makeList(props.getProperty("pojo_node_interface_list"));
			pojo_version_interface_list = makeList(props.getProperty("pojo_version_interface_list"));
			pojo_state_interface_list = makeList(props.getProperty("pojo_state_interface_list"));
			pojo_uuid_interface_list = makeList(props.getProperty("pojo_uuid_interface_list"));
			pojo_modifydate_interface_list = makeList(props.getProperty("pojo_modifydate_interface_list"));

			overWriteAll = getBooleanCfg(props, "over_write_all");
			use_annotation = getBooleanCfg(props, "use_annotation");
			keep_case = getBooleanCfg(props, "keep_case");
			use_autoincrement = getBooleanCfg(props, "use_autoincrement");

			extra_setting = props.getProperty("extra_setting");

		} catch (IOException e) {
			e.printStackTrace(console);
		}
	}

	private List<String> makeList(String property) {
		List<String> res = new ArrayList<>();
		if (StringUtils.isNotEmpty(property)) {
			res.addAll(Arrays.asList(property.trim().split(",")));
		}
		return res;
	}

	private String getConfigBaseDir(String propertiesFilename) {
		int i = propertiesFilename.lastIndexOf(File.separator) + 1;
		return  propertiesFilename.substring(0, i);
	}

	private boolean getBooleanCfg(Properties props, String key) {
		return "yes".equalsIgnoreCase(props.getProperty(key));
	}

	public String getInterface(String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append(" implements Serializable");

		T_metatable tab = getTableDetail(tableName);
		if (tab != null) {
			if (tab.getIsnode()) {
				sb.append(", ").append(this.pojo_node_interface);
			}
			if (tab.getIsstate()) {
				sb.append(", ").append(this.pojo_state_interface);
			}
			if (tab.getIsversion()) {
				sb.append(", ").append(this.pojo_version_interface);
			}
			if (tab.getIsmodifydate()) {
				sb.append(", ").append(this.pojo_modifydate_interface);
			}
			if (tab.getIsuuid()) {
				sb.append(", ").append(this.pojo_uuid_interface);
			}
			if (StringUtils.isNotBlank(tab.getExtrainterfaces())) {
				sb.append(", ").append(tab.getExtrainterfaces());
			}
		}
		return sb.toString();
	}

	private T_metatable getTableDetail(String tableName) {
		T_metatable res;
		if (tableData != null) {
			res = tableData.get(tableName);
		} else {
			res = new T_metatable();
			boolean any = false;
			if (this.pojo_node_interface_list.contains(tableName)) {
				res.setIsnode(true);
				any = true;
			}
			if (this.pojo_state_interface_list.contains(tableName)) {
				res.setIsstate(true);
				any = true;
			}
			if (this.pojo_version_interface_list.contains(tableName)) {
				res.setIsversion(true);
				any = true;
			}
			if (this.pojo_modifydate_interface_list.contains(tableName)) {
				res.setIsmodifydate(true);
				any = true;
			}
			if (this.pojo_uuid_interface_list.contains(tableName)) {
				res.setIsuuid(true);
				any = true;
			}
			if (!any) {
				res = null;
			}
		}
		return res;
	}

	private String getPojoExtendsClasses(String tableName) {
		if (tableData != null) {
			T_metatable tab = tableData.get(tableName);
			if (tab != null) {
				StringBuilder sb = new StringBuilder();
				if (StringUtils.isNotBlank(tab.getExtrasuperclasses())) {
					sb.append(", extends ").append(tab.getExtrasuperclasses());
				}
				return sb.toString();
			}
		}
		return "";
	}

	/**
	 * Parses a comma separated list of table names into the tablesList List.
	 */
	void parseTableNames(String names) {
		if (names != null) {
			if (tablesList == null) {
				tablesList = new Hashtable<>();
			}
			StringTokenizer st = new StringTokenizer(names, ",");
			String name;
			while (st.hasMoreElements()) {
				name = st.nextToken().trim();
				tablesList.put(name, name);
				info(name);
			}
		}
	}

	/**
	 * Parses a comma separated list of table names into the tablesList List.
	 */
	void parseExcludeTableNames(String names) {
		if (names != null) {
			if (excludeTablesList == null) {
				excludeTablesList = new Hashtable<>();
			}
			StringTokenizer st = new StringTokenizer(names, ",");
			String name;
			while (st.hasMoreElements()) {
				name = st.nextToken().trim();
				excludeTablesList.put(name, name);
				info("exclude table: " + name);
			}
		}
	}

	/**
	 * Connects to the database a get the MetaData.
	 */
	public void connect() throws SQLException {
		Connection cn  = getDbHelper().getConnection();
		setConnection(cn);

		metaData = cn.getMetaData();

		info("");
		info("TableGen Database JavaBean Generator v1.91");
		info("-----------------------------------------");
		info("Database version : " + metaData.getDatabaseProductVersion());
		info("Driver Name : " + metaData.getDriverName());
		info("Driver Version : " + metaData.getDriverMajorVersion() + "." + metaData.getDriverMinorVersion());
	}

	public Map<String, T_metatable> getTableDataExt() {
		return tableData;
	}

	/**
	 * Retrieves all the table data required.
	 *
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IOException
	 */
	public synchronized void getTableData() throws SQLException, InstantiationException, IllegalAccessException, IOException {

		if (dbconfig == null) {
			LOG.warn("数据库连接没有初始化");
			init(null);
		}

		if (metaData == null) {
			connect();
		}

		List<String> col = getTableNames();

		if (CollectionUtils.isEmpty(col)) {
			setTableNames(
					TableUtils.getTableData(metaData, catalog, schema, tableOwner, tablesList, excludeTablesList));
		} else {
			info("tableNames already set, retrieve from db skipped.");
		}

		if (this.kuozhanbiao) {
			tableData = getTableDataExt(getTableNames());
		}

		if (StringUtils.isNotEmpty(this.extra_setting)) {
			String baseDir = getConfigBaseDir(propertiesFilename);
			tableData = getExtraTableDataFromXml(baseDir + this.extra_setting, tableData);
		}

	}

	protected Map<String, T_metatable> getExtraTableDataFromXml(String extraSettingFileName,
			Map<String, T_metatable> settingFromDb) throws IOException {
		File file = new File(extraSettingFileName);
		if (file.exists()) {
			info("Find extra setting file: " + extraSettingFileName);
			if (settingFromDb == null) {
				settingFromDb = new HashMap<>();
			}
			String xml = FileUtils.readFileToString(file, "UTF-8");

			Map<String, T_metatable> res = PojoPropertiesParser.parse(xml);

			return PojoPropertiesParser.mergeTableSetting(settingFromDb, res);
		} else {
			info("WARNING: File for value extra_setting not exists:" + extraSettingFileName);
			return settingFromDb;
		}
	}

	public boolean makeDir() {
		boolean res = true;
		String baseDir = getBaseSrcDir();
		res = res && CMyFile.makeDir(baseDir, true);
		res = res && CMyFile.makeDir(resourceDir + File.separator + "config", true);

		res = CMyFile.makeDir(baseDir + File.separator + "pojo", true) && res;
		res = CMyFile.makeDir(baseDir + File.separator + "dal", false) && res;
		res = CMyFile.makeDir(baseDir + File.separator + BUSINESS, false) && res;
		res = CMyFile.makeDir(baseDir + File.separator + "web", true) && res;
		res = CMyFile.makeDir(testDir + File.separator + packageName2Dir(packageName) + File.separator + BUSINESS,
				true) && res;
		res = CMyFile.makeDir(jspDir + File.separator + "list", true) && res;

		res = CMyFile.makeDir(baseDir + File.separator + "stub", false) && res;
		return res;
	}

	private String getBaseSrcDir() {
		return srcDir + File.separator + packageName2Dir(packageName);
	}

	private String packageName2Dir(String packageName) {
		if (packageName == null) {
			return null;
		} else {
			return packageName.replaceAll("\\.", "\\" + File.separator);
		}
	}

	public void generateBaseList() throws SQLException {
		try {
			info("Generating BaseList : ");
			String baseDir = getBaseSrcDir();

			String fileName = baseDir + File.separator + "BaseList.java";
			File f = new File(fileName);

			if (overWriteAll || !f.exists()) {
				currentOutput = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(fileName), TableGenConfig.FILE_ENCODING));// new

				writeBaseHeader();
				out("      @Autowired");
				out("      DataSource ds;");
				out(" public BaseList(){");
				String lastName = packageName.substring(packageName.lastIndexOf('.') + 1);
				out("// try {");
				out("//    dbHelper = (IDbHelper) BeanFactory.createObjectById(\"" + lastName + "DbHelper\");");
				out("//    }catch (Exception e ) { ");
				out("    	MSDbHelper dbHelper0 = new MSDbHelper(); ");
				out("    	dbHelper0.setMasterDatasource(ds); ");
				out("    	dbHelper0.setDataSource(ds);");
				out("    	if (mLog.isDebugEnabled()) {");
				out("    			ProxyFactory obj = new ProxyFactory(dbHelper0);");
				out("    			obj.addAdvice(new DbHelperAroundAdvice());");
				out("    			dbHelper = (IDbHelper) obj.getProxy();");
				out("    		} else {");
				out("    			dbHelper = dbHelper0;");
				out("    		}");
				out("    	}");
				out("// }");

				out("}");
				currentOutput.close();

			} else {
				info("File " + fileName + " allready exists! Skip rewrite.");
			}

		} catch (IOException e) {
			e.printStackTrace(console);
		}
	}

	/**
	 * Generates the plane java model for each table.
	 */
	public void generatePojos() throws SQLException {

		for ( String tableName: getTableNames()) {
			info("Generating pojos : " + tableName);
			generatePojo(tableName);
		}

	}

	/**
	 * 生成单个pojo类
	 *
	 * @param tableName
	 */
	public void generatePojo(String tableName) {

		try {

			String baseDir = getBaseSrcDir();

			String fileName = baseDir + File.separator + "pojo" + File.separator + getPojoClassName(tableName)
					+ ".java";
			currentOutput = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(fileName), TableGenConfig.FILE_ENCODING));// new

			List<ColumnData> colData = getColumnData(tableName);

			String interfaces = this.getInterface(tableName);
			writeHeader(tableName, "pojo", interfaces + this.getPojoExtendsClasses(tableName));

			// handle the variables
			for (ColumnData e2 : colData) {
				writeVariable(tableName, e2);
			}

			boolean findNode = false;
			// handle the variable access functions (set/get)
			for (ColumnData e2 : colData) {
				writeSetGet(tableName, e2);
				if ("relation_code".equalsIgnoreCase(e2.name)) {
					findNode = true;
				}
			}

			if (interfaces.contains("NodeInterface") && !findNode) {
				ColumnData col = new ColumnData("relation_code", 12, 0, false, false, 0, "",
						"hack code for relation_code( to fit NodeInterface), not for use!");
				out("// " + col.remarks);
				writeVariable(tableName, col);
				writeSetGet(tableName, col);
			}

			// handle the constructor
			out(" public " + getPojoClassName(tableName) + "()");
			out("     {");
			for (ColumnData e2 : colData) {
				writeInit(e2);
			}
			out("     }");// toXml

			// handle the toXml functions
			out(" public String toXml()");
			out("     {");
			out("      StringBuilder s= new StringBuilder();");
			out("      String ln = System.getProperty(\"line.separator\");");
			out("      s.append(\"<" + tableName + " \");");
			for (ColumnData col : colData) {
				if ("String".equals(col.getJavaType())) {
					out("     s.append(\"" + col.getName()
							+ "=\\\"\").append(StringEscapeUtils.escapeXml11(this.get"
							+ firstUp(col.getName()) + "())).append(\"\\\" \");");
				} else {
					out("     s.append(\"" + col.getName() + "=\\\"\").append(this.get"
							+ firstUp(col.getName()) + "()).append(\"\\\" \");");
				}
			}
			out("     s.append(\" />\");");
			out("     s.append(ln);");
			out("    return s.toString();");
			out("     }");// toXml

			out("}");
			currentOutput.close();

		} catch (IOException | SQLException ex) {
			generateFlag = false;
			ex.printStackTrace(console);
		}
	}

	/**
	 * 生成单个dal
	 *
	 * @param tableName database table name
	 */
	private void generateDAL(String tableName) {

		StopWatch sw = new StopWatch();
		sw.start();
		try {
			info("Generating DALs : " + tableName);
			String baseDir = getBaseSrcDir();
			String fileName = baseDir + File.separator + "dal" + File.separator + getPojoClassName(tableName)
					+ "List.java";
			currentOutput = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(fileName), TableGenConfig.FILE_ENCODING), 10240);// new

			List<ColumnData> colData = getColumnData(tableName);

			List<String> keyData = getTableKeys(tableName); // updates the keyData variable

			writeHeader(tableName, "dal", " extends " + genericFiles[0] + getGeneticType(tableName));

			// +
			// " implements RowMapper<"+getPojoClassName(tableName)+">, IBaseListService");

			// do the retrieve,insert and update functions
			// We have pair of updates and retrieve, depending
			// upon whether keys and/or indexes are present on
			// the database table.
			//
			// If no keys/indexes are present, then you'll just have to
			// write your own retrieve and update functions.
			//
			writeDALConstructor(tableName);

			writeSql(colData, keyData);

			writeObjs(tableName, keyData, colData);

			writeGetTableName(tableName, "getTableName", false);
			writeGetKeyName(getFirstKeyName(keyData), "getKeyName");
			writeMapRow(tableName, colData);


			writeGetSetId(tableName, keyData, colData);
			writeWraper(keyData, colData);

			if (isNotEmpty(keyData)) {

				/* optimistic lock update ! */
				if (containsVersion(colData)) {
					boolean withVersion = true;
					writeUpdate(tableName, keyData, "updateByKeyAndVersion", false, withVersion, colData);
					writeUpdate(tableName, keyData, "updateByKeyAndVersion", true, withVersion, colData);
				}
				if (keyData.size() > 1) {
					writeDelete(tableName, keyData, "deleteByKey", false, colData);
					writeDelete(tableName, keyData, "deleteByKey", true, colData);
				}
			} else {
				writeDummyUpdate(tableName, "updateByKey");
				writeDummyDelete(tableName, keyData, "deleteByKey", colData);
			}

			sw.split();
			trace("after keys:" + sw.getSplitTime());

			//foreign key
			List<FKDefinition> foreignKeyData = getTableImportedKeys(tableName);
			for (FKDefinition fkEnum : foreignKeyData) {
				writeImportedMethods(tableName, fkEnum, colData);
			}

			foreignKeyData = getTableExportedKeys(tableName);
			for (FKDefinition fkEnum : foreignKeyData) {
				writeExportedMethods( fkEnum, getColumnData(fkEnum.PKtable));
			}


			sw.split();
			trace("foreignKeys :" + sw.getSplitTime());

			if (indexes) {
				List<String> indexData = getTableIndexes(tableName); // updates the indexData
				// variable
				if (isNotEmpty(indexData)) {
					writeSelect(tableName, indexData, "selectByIndex", colData);
					writeSelectAll(tableName, indexData, false, "selectAllLikeIndex", true, colData);

					writeUpdate(tableName, indexData, "updateByIndex", false, false, colData);
					writeUpdate(tableName, indexData, "updateByIndex", true, false, colData);

					writeDelete(tableName, indexData, "deleteByIndex", false, colData);
					writeDelete(tableName, indexData, "deleteByIndex", true, colData);
					writeCount(indexData, false, "countByIndex", colData);
					writeCount(indexData, true, "countLikeIndex",colData);

				}
			}

			writeInsertDummy(tableName, keyData, "insertDummy", colData);
			out("}");

			currentOutput.close();

		} catch (SQLException | IOException | GenException e) {
			e.printStackTrace(console);
		}
		sw.split();
		trace("after close:" + sw.getSplitTime());
	}

	private boolean containsVersion(List<ColumnData> cols) {
		for (ColumnData col : cols) {
			if (col.getName().equalsIgnoreCase(versionColName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Generates the Data Access Layer for each table.
	 */
	public void generateDALs() {
		for (String tableName :  getTableNames()) {
			generateDAL(tableName);
		}

	}

	private void writeSql(List<ColumnData> colData, List<String> params) throws IOException, GenException {
		StringBuilder columnss = new StringBuilder("( ");
		StringBuilder values = new StringBuilder("");

		String where = createPreparedWhereClause(params, false, colData);

		StringBuilder update = new StringBuilder("");
		StringBuilder fields = new StringBuilder("    public static final class F{\n");
		StringBuilder fieldsAll = new StringBuilder("        public static final String[] getAllFields() { return new String[] {");

		int i=0;

		for (ColumnData cd: colData) {
			i++;
			boolean isNotLast = i < colData.size();
			String field = columnNameToFieldName(cd.getName());

			fields.append("        public static final String ").append(field)
					.append(" = \"" + cd.getName() + "\";\n");
			fieldsAll.append(field);

			if(isNotLast) {
				fieldsAll.append(",");
			}

			if (!(use_autoincrement && cd.isAutoIncrement)) {
				if (cd.getName().equalsIgnoreCase(versionColName)){
					update.append(cd.getName() + " = " + cd.getName() + "+1");
				}else{
					update.append(cd.getName() + "=?");
				}

				values.append("?");
				columnss.append(cd.getName());

				if (isNotLast) {
					update.append(",");
					values.append(",");
					columnss.append(",");
				}
			}
		}

		values.append(" )\";");
		columnss.append(" )");
		update.append("\"\n    " + where + ";");
		fieldsAll.append("};}\n");

		StringBuilder query = new StringBuilder("delete from \" + getTableName() + \" where ");

		query.append(StringUtils.join(params,"=? and ")).append("=?");

		out("protected String getDeleteSql(){\n    return \"" + query + "\";\n}\n");

		fields.append(fieldsAll);
		fields.append("    }\n");
		out(fields.toString());

		out("@Override\nprotected String getInsertSql(){\n    return \"insert into \" + getTableName() + \" " + columnss
				+ " values ( " + values + "\n}\n");

		out("@Override\nprotected String getUpdateSql(){\n    return \"update \" + getTableName() + \" set " + update + "\n}\n");

	}

	private void writeObjs(String tableName, List<String> params, List<ColumnData> columnData) throws IOException {

		out("@Override\nprotected Object[] getInsertObjs(" + this.getPojoClassName(tableName) + " info){\n    return new Object[]{"
				+ this.makeInsertObjects(use_autoincrement, columnData) + "};\n}\n");
		out("@Override\nprotected Object[] getUpdateObjs(" + this.getPojoClassName(tableName) + " info){\n    return new Object[]{"
				+ this.makeUpdateObjects(params, columnData) + "};\n}\n");

	}

	private boolean isNotEmpty(Collection<?> col){
		return !CollectionUtils.isEmpty(col);
	}

	private String getFirstKeyType(List<String> keyData2, List<ColumnData> columnData) throws GenException {
		return (isNotEmpty(keyData2)) ? getColType(keyData2.get(0), columnData) : "Object";
	}

	private String getFirstKeyName(List<String> keyData2) {
		if (isNotEmpty(keyData2)) {
			return keyData2.get(0);
		}
		return null;
	}

	private void writeGetSetId(String tableName, List<String> keyData, List<ColumnData> columnData) throws IOException, GenException {
		out("public " + getFirstKeyType(keyData, columnData) + " getId(" + this.getPojoClassName(tableName) + " info) {");
		out("    return " + this.getOneId(keyData) + ";");
		out("}");
		out("\n");

		out("public void setId(" + this.getPojoClassName(tableName) + " info, " + getFirstKeyType(keyData, columnData) + " id) {");
		if (isNotEmpty(keyData)) {
			out("    info.set" + firstUp(keyData.get(0)) + "(id);");
		} else {
			out("   //no key to set, don't this method!");
		}
		out("}");
		out("\n");

	}

	private void writeDummyUpdate(String tableName, String methodName) throws IOException {
		out("");
		out("public boolean " + methodName + "(" + this.getPojoClassName(tableName) + " info) throws SQLException {");
		out("    throw new SQLException(\"This operation is not supported, because talbe " + tableName
				+ " not have a unique key!\");");
		out("}");

	}

	private void writeDummyDelete(String tableName, List<String> keyData2, String methodName,List<ColumnData> columnData) throws IOException, GenException {
		out("");
		out("public boolean " + methodName + "(" + this.getFirstKeyType(keyData2, columnData) + " key) throws SQLException {");
		out("    throw new SQLException(\"This operation is not supported, because talbe " + tableName
				+ " not have a unique key!\");");
		out("}");

	}

	String getPojoClassName(String tableName) {
		String classname = tableName2ClassName(tableName);
		if (tableName.equals(classname)) {
			return prefix + firstUp(tableName);
		} else {
			return prefix + classname;
		}
	}

	private String tableName2ClassName(String tableName) {
		if (tableData != null && tableData.get(tableName) != null) {
			String alias = tableData.get(tableName).getClassname();

			LOG.debug("Pojo class alias is: " + alias);

			if (StringUtils.isNotEmpty(alias)) {
				return alias.trim();
			}
		}
		return tableName;
	}

	public void generateBusiness(String tableName) {
		info("Generating business : " + tableName);
		String baseDir = getBaseSrcDir();
		String fileName = baseDir + File.separator + BUSINESS + File.separator + getPojoClassName(tableName)
				+ "Manager.java";
		File f = new File(fileName);
		boolean fileExists = f.exists();
		if (!fileExists) {

			try {
				currentOutput = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(fileName), TableGenConfig.FILE_ENCODING));

				getColumnData(tableName);
				writeHeader(tableName, BUSINESS, " extends " + getPojoClassName(tableName) + "List");
				out("// Write your code here!");

				out("}");
				currentOutput.close();
			} catch (SQLException | IOException e) {
				e.printStackTrace(console);
				LOG.warn(e);
			}

		} else {
			info("File " + fileName + " allready exists! Skip rewrite.");
		}

	}

	/**
	 * Generates the plane java model for each table.
	 */
	public void generateBusinesses() {
		for (String tableName : getTableNames()) {
			generateBusiness(tableName);
		}
	}

	/**
	 * Generates the plane java model for each table.
	 */
	public void generateWebUI() throws SQLException {
		try {
			for (String tableName: getTableNames()) {
				info("Generating web UI : " + tableName);
				String baseDir = getBaseSrcDir();
				String fileName = baseDir + File.separator + "web" + File.separator + getPojoClassName(tableName)
						+ "WebUI.java";
				File f = new File(fileName);
				boolean fileExists = f.exists();
				if (overWriteAll || !fileExists) {
					currentOutput = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fileName), TableGenConfig.FILE_ENCODING));// new

					List<ColumnData> colData = getColumnData(tableName);
					List<String> keyData = getTableKeys(tableName);
					writeHeader(tableName, "web", " extends AbstractWebUI" + getGeneticType(tableName));
					out("");
					out("");
					out("protected String getUniquePropertyName(){return \""
							+ (isNotEmpty(keyData)  ? keyData.get(0).toLowerCase() : "null") + "\";}");

					// getColNames
					out("");
					out("@Override");
					out("protected String[] getColNames() {");
					out("\t\treturn new String[] {\"chkbox\"," + formFieldsString(colData) + "};");
					out("}");
					out("");

					// getKeys
					out("\t@Override");
					if (isNotEmpty(keyData)) {
						String keyType = getFirstKeyType(keyData, colData);
						out("\tprotected " + keyType + "[] getKeys(HttpServletRequest request) {");
						out("\t\tString[] res = request.getParameterValues(checkboxName);");
						out("\t\tif(res==null) return new " + keyType + "[0];");
						out("\t\treturn (" + keyType + "[]) converter.convert(\n"
								+ "                request.getParameterValues(checkboxName), " + keyType + ".class);");
						out("\t}");
						out("");
					} else {
						out("\tprotected Object[] getKeys(HttpServletRequest request) {    \n"
								+ "\t\treturn request.getParameterValues(checkboxName);\n" + "\t}");

					}

					// setService
					out(" public void setService(" + this.getPojoClassName(tableName) + "Manager service) {");
					out("        this.service = service;");
					out("    }");

					// validateColumn
					out("/**");
					out("* An example of how to validate the worksheet column cells.");
					out("*");
					out("* protected void validateColumn(WorksheetColumn worksheetColumn, String changedValue) {");
					out("*    if (changedValue.equals(\"foo\")) {");
					out("*        worksheetColumn.setErrorKey(\"foo.error\");");

					out("*    } else {");
					out("*        worksheetColumn.removeError();");
					out("*    }");
					out("* } ");
					out("*/");

					out("}");
				} else {
					info("File " + fileName + " allready exists! Skip rewrite.");
				}
				currentOutput.close();
			}

		} catch (IOException e) {
			e.printStackTrace(console);
		} catch (GenException e) {
			e.printStackTrace();
		}
	}

	private String formFieldsString(List<ColumnData> columnData) {
		return
				columnData.stream()
						.map(ColumnData::getName)
						.map(it -> "\""+it+"\"")
						.reduce((a,b)->a+","+b)
						.get()
						.toLowerCase()
				;

	}

	private String getGeneticType(String tableName) throws GenException, SQLException {
		List<String> keyData = getTableKeys(tableName);
		List<ColumnData> columnData= getColumnData(tableName);
		return "<" + getPojoClassName(tableName) + "," + getFirstKeyType(keyData, columnData) + ">";
	}

	void writeBaseHeader() throws IOException {
		out("package " + packageName + ";");
		out("/**");
		out("  * BaseList");
		writeHeaderComment();
		out("*/");
		out("  import com.bixuebihui.BeanFactory;");
		out("  import com.bixuebihui.jdbc.BaseDao;");
		out("  import com.bixuebihui.jdbc.IDbHelper;");
		out("  import com.bixuebihui.jdbc.MSDbHelper;");
		out("  import com.bixuebihui.jdbc.aop.DbHelperAroundAdvice;");
		out("  import org.springframework.aop.framework.ProxyFactory;");
		out("  import org.springframework.beans.factory.annotation.Autowired;");

		out("  import javax.sql.DataSource;");
		out("  import java.sql.SQLException;");
		out("");

		out("public abstract class BaseList<T,V> extends BaseDao<T,V>");
		out("{");
	}

	/**
	 * Generates the container/database access functions for each table.
	 */
	void writeHeader(String tableName, String subPackage, String extClass) throws IOException {
		out("package " + packageName + "." + subPackage + ";");
		out("/**");
		out("  * " + tableName);
		writeHeaderComment();
		out("  */");
		out("");

		out("import java.sql.*;");

		if ("pojo".equals(subPackage)) {
			out("import java.io.Serializable;");
		}

		if ("pojo".equals(subPackage) && this.use_annotation) {
			out("import javax.validation.constraints.*;");
			out("import org.apache.commons.text.StringEscapeUtils;");
		}

		if (!"pojo".equals(subPackage)) {

			out("import java.util.List;");

			if (!"dal".equals(subPackage) && !"web".equals(subPackage)) {
				out("import " + packageName + ".dal.*;");
			} else {
				out("import " + packageName + ".business.*;");
			}
			out("import " + packageName + ".pojo.*;");

			if ("dal".equals(subPackage) || "stub".equals(subPackage)) {
				out("import com.bixuebihui.jdbc.RowMapperResultReader;");
			}

			if ("stub".equals(subPackage)) {
				out("import org.apache.commons.dbutils.DbUtils;");
			}

			if (extClass.contains("TestCase")) {
				out("import junit.framework.TestCase;");
			}

			if ("web".equals(subPackage)) {
				out("import " + packageName + ".business." + this.getPojoClassName(tableName) + "Manager;");

				out("import org.jmesa.worksheet.WorksheetColumn;");
				out("import javax.servlet.http.HttpServletRequest;");

				out("import org.apache.commons.beanutils.ConvertUtilsBean;");
			}

			for (String genericFile : genericFiles) {
				out("import " + packageName + "." + genericFile + ";");
			}

			if ("web".equals(subPackage)) {
				out("import com.bixuebihui.jmesa.AbstractWebUI;");
			}
			out("");
		}

		extClass = extClass == null ? "" : extClass;
		if (this.use_annotation && BUSINESS.equals(subPackage) && !extClass.contains("TestCase")) {
			out("import org.springframework.stereotype.Repository;");
			out("");
			out("@Repository");
		}

		if ("dal".equals(subPackage)) {
			out("public class " + getPojoClassName(tableName)
					+ "List"
					+ ((extClass.contains("TestCase")) ? "Test" : "") + " " + (extClass));
		}else{
			out("public class " + getPojoClassName(tableName)
					+ (BUSINESS.equals(subPackage) ? "Manager" : ("web".equals(subPackage) ? "WebUI" : ""))
					+ ((extClass.contains("TestCase")) ? "Test" : "") + " " + (extClass));
		}
		out("{");
	}

	private String getBusFullClassName(String tableName) {
		return packageName + ".business." + this.getPojoClassName(tableName) + "Manager";
	}

	private void writeHeaderComment() throws IOException {
		out("  * ");
		out("  * WARNING! Automatically generated file!");
		out("  * Do not edit the pojo and dal packages,use Auto" + "Code / tablegen!");
		out("  * Code Generator by J.A.Carter");
		out("  * Modified by Xing Wanxiang 2008-2019");
		out("  * (c) www@qsn.so");
	}

	void writeInit(ColumnData cd) throws IOException {
		String name = cd.getName().toLowerCase();
		String type = cd.getJavaType();
		String code = typeDefaultValue.get(type);

		if ("String".equals(type) && !cd.isNullable) {
				code="\"*\"";
		}


		if(code!=null) {
			out(INDENT+name+"="+code+";");
		}
	}

	/**
	 * Writes out the variables with javadoc info.
	 */
	void writeVariable(String tableName, ColumnData cd) throws IOException {
		out("/**");
		out("  * " + cd);
		out("  * " + getColumnDescription(tableName, cd.getName()));
		out("  */");
		out(getColumnAnnotation(tableName, cd));
		out("  protected " + cd.getJavaType() + " " + cd.getName().toLowerCase() + ";");
		out("");
	}

	String getColumnAnnotation(String tableName, ColumnData cd) {
		StringBuilder sb = new StringBuilder();
		if (this.use_annotation) {

			if (tableData != null && tableData.get(tableName) != null) {
				Map<String, T_metacolumn> cols = tableData.get(tableName).getColumns();
				if (cols != null && cols.get(cd.getName()) != null) {
					T_metacolumn col = cols.get(cd.getName());
					sb.append(col.getAnnotation() == null ? "" : col.getAnnotation() + "\n");
				} else {
					info("There NO settings for table columns:" + tableName);
				}
			}

			if ("String".equals(cd.getJavaType())) {
				//if columns is JSON type, there is no column size.
				if (sb.indexOf("@Size") < 0 && cd.columns>0) {
					sb.append("  @Size(max=").append(cd.columns).append(")\n");
				}
			}
			if (!cd.isNullable && cd.defaultValue == null) {
				if (sb.indexOf("@NotNull") < 0) {
					sb.append("  @NotNull\n");
				}
			} else if (cd.defaultValue != null) {
				if (sb.indexOf("//@NotNull") < 0) {
					sb.append("  //@NotNull, but has default value :").append(cd.defaultValue).append("\n");
				}

			}
		}

		return StringUtils.stripEnd(sb.toString(), "\n");
	}

	/**
	 * Writes out the set and get functions for each variable.
	 */
	void writeSetGet(String tableName, ColumnData cd) throws IOException {
		String name = cd.getName().toLowerCase();
		String newName = firstUp(cd.getName());

		out("/**");
		out("  * Sets the value for " + name + " " + getColumnDescription(tableName, name));
		out("  */");
		out("public void set" + newName + "(" + cd.getJavaType() + " " + name + ")");
		out("{");
		out("  this." + name + "=" + name + ";");
		out("}");
		out("");
		out("/**");
		out("  * Gets the value for " + name);
		out("  */");
		out("public " + cd.getJavaType() + " get" + newName + "()");
		out("{");
		if ("Timestamp".equals(cd.getJavaType())) {
			out("  return " + name + "==null ? null: new Timestamp(" + name + ".getTime());");
		} else if ("Date".equals(cd.getJavaType())) {
			out("  return " + name + "==null ? null: new Date(" + name + ".getTime());");
		} else {
			out("  return " + name + ";");
		}

		out("}");
		out("");
	}

	/**
	 * Writes out the retrieveAll function. This retrieves all the rows that
	 * match the search pattern.
	 */
	void writeSelectAll(String tableName, List<String> params, boolean customWhere, String methodName, boolean bLike, List<ColumnData> columnData)
			throws IOException, GenException {
		String query = "select * from \" + getTableName() + \" \"";
		String where = createPreparedWhereClause(params, bLike, columnData);
		String objs = createPreparedObjects(params, bLike, columnData);

		out("/**");
		out("  * Select from the database for table \"" + firstUp(tableName) + "\"");
		out("  */");

		if (customWhere) {
			out("public List<" + getPojoClassName(tableName) + "> " + methodName
					+ "(String where) throws SQLException");
		} else {
			out(createMethodLine(methodName, params, "List<" + getPojoClassName(tableName) + ">", columnData));
		}

		out("{");
		out("    String query = \"" + query + where + (customWhere ? "+ where;" : ";"));
		out("    return dbHelper.executeQuery(query, " + (objs == null ? "null" : objs) + ", new RowMapperResultReader<"
				+ getPojoClassName(tableName) + ">(this));");
		out("}");
		out("");
	}

	/**
	 * Writes out the Select function.
	 */
	void writeSelect(String tableName, List<String> params, String methodName, List<ColumnData> columnData) throws IOException, GenException {
		String query = "select * from \" + getTableName() + \" \"";
		String where = createPreparedWhereClause(params, false, columnData);
		String objs = createPreparedObjects(params, false, columnData);

		out("/**");
		out("  * Select from the database for table \"" + tableName + "\"");
		out(" */");
		out(createMethodLine(methodName, params, getPojoClassName(tableName), columnData));
		out("{");
		out("    String query = \"" + query + where + ";");
		out("    List<" + getPojoClassName(tableName) + "> info = dbHelper.executeQuery(query, " + objs
				+ ", new RowMapperResultReader<" + getPojoClassName(tableName) + ">(this));");
		out("    if(info!=null && info.size()>0)");
		out("         return (" + getPojoClassName(tableName) + ") info.get(0);");
		out(" return null;");
		out("}");
		out("");
	}

	/**
	 * Writes out the Select function.
	 */
	void writeSelectPage(String tableName, String methodName) throws IOException {
		String query = "select * from \" + getTableName() + \" \"";

		out("/**");
		out("  * Select from the database for table \"" + tableName + "\"");
		out(" */");
		out("public List<" + getPojoClassName(tableName) + "> " + methodName
				+ "(String whereClause, Object[] params, String orderbyClause, int beginNum, int endNum) throws SQLException");
		out("{");
		out("    String query = \"" + query + "+\" \"" + "+whereClause;");
		out("    if(this.getDBTYPE()!=BaseDao.DERBY)");
		out("        query+= orderbyClause;");
		out("    query = getPagingSql(query, beginNum, endNum);");
		out("    return dbHelper.executeQuery(query, params " + ", new RowMapperResultReader<"
				+ getPojoClassName(tableName) + ">(this));");
		out("}");
		out("");

		// 省略params的简略形式
		out("public List<" + getPojoClassName(tableName) + "> " + methodName
				+ "(String whereClause, String orderbyClause, int beginNum, int endNum) throws SQLException");
		out("{");
		out("         return " + methodName + "(whereClause, null, orderbyClause, beginNum, endNum);");
		out("}");
		out("");

	}

	void writeSelectPageTest(String tableName, String methodName) throws IOException {

		out("/**");
		out("  * Select from the database for table \"" + tableName + "\"");
		out(" */");
		out("public void test" + firstUp(methodName) + "() throws SQLException");
		out("{");
		String className = this.getPojoClassName(tableName) + "Manager";
		out("  " + className + " man = new " + className + "();");

		out("    List<" + getPojoClassName(tableName) + "> list = man." + methodName + "(\"\",\"\", 0, 10);");
		out("    for(" + getPojoClassName(tableName) + " info: list){");
		out("     System.out.println(info.toXml());");
		out("    }");
		out("}");
		out("");
	}


	private String getOneId(List<String> keyData2) {
		if (isNotEmpty(keyData2 )) {
			String key = this.getFirstKeyName(keyData2);
			if(key!=null) {
				return " info.get" + firstUp( key)+ "()";
			}
		}
		return "0L";
	}

	private String getIds(List<String> keyData) {

		StringBuilder paramString = new StringBuilder();

		if (keyData != null) {
			// work out the query string and the method parameters.
            Iterator<String> iterator = keyData.iterator();
			for (String key = iterator.next(); iterator.hasNext(); key = iterator.next()) {
				paramString.append(" info.get").append(firstUp(key)).append("()");

				if (iterator.hasNext()) {
					paramString.append("+\"_\"+");
				}
			}
		}
		return paramString.toString();
	}

	/**
	 * Writes out the Select function.
	 */
	void writeDelete(String tableName, List<String> params, String methodName, boolean isWithConn, List<ColumnData> columnData)
			throws IOException, GenException {
		String keyType;
		StringBuilder tmp = new StringBuilder("public boolean " + methodName + "(");

		String objs = this.createPreparedObjects(params, false, columnData);

		// work out the query string and the method parameters.
        Iterator<String> iterator = params.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			keyType = getColType(key, columnData);
			tmp.append(keyType).append(" ").append(key.toLowerCase());
			if (iterator.hasNext()) {
				tmp.append(",");
			} else {
				tmp.append((isWithConn ? ", Connection cn" : "") + ") throws SQLException");
			}
		}

		out("/**");
		out("  * Deletes from the database for table \"" + tableName + "\"");
		out("  */");
		out(tmp.toString());
		out("{");
		out("");
		out("    return 1 <= dbHelper.executeNoQuery(getDeleteSql()," + objs + (isWithConn ? ", cn" : "") + ");");
		out("}");
		out("");
	}

	/**
	 * Writes out the mapRow method. The mapRow method interprets the returned
	 * result set from a Select and update the object with those values.
	 */
	void writeMapRow(String tableName, List<ColumnData> columnData) throws IOException {
		String col;
		String colType;
		List<String> gets = new ArrayList<>();
		String get;

		for (ColumnData cd: columnData) {
			col = cd.getName();
			colType = cd.getJavaType();

			// work out which data type we are getting for each variable
			String ucol = "F." + columnNameToFieldName(col);

			if (colType.compareTo("String") == 0) {
				get = "(r.getString(" + ucol + "));";
			} else if (colType.compareTo("byte") == 0 || colType.compareTo("Byte") == 0) {
				get = "(r.getByte(" + ucol + "));";
			} else if (colType.compareTo("long") == 0 || colType.compareTo("Long") == 0) {
				get = "(r.getLong(" + ucol + "));";
			} else if (colType.compareTo("int") == 0 || colType.compareTo("Integer") == 0) {
				get = "(r.getInt(" + ucol + "));";
			} else if (colType.compareTo("short") == 0 || colType.compareTo("Short") == 0) {
				get = "(r.getShort(" + ucol + "));";
			} else if (colType.compareTo("float") == 0 || colType.compareTo("Float") == 0) {
				get = "(r.getFloat(" + ucol + "));";
			} else if (colType.compareTo("double") == 0 || colType.compareTo("Double") == 0) {
				get = "(r.getDouble(" + ucol + "));";
			} else if (colType.compareTo("Date") == 0) {
				get = "(r.getDate(" + ucol + "));";
			} else if (colType.compareTo("Timestamp") == 0) {
				get = "(r.getTimestamp(" + ucol + "));";
			} else if (colType.compareTo("Time") == 0) {
				get = "(r.getTime(" + ucol + "));";
			} else if (colType.compareTo("char") == 0 || colType.compareTo("Char") == 0) {
				get = "(r.getString(" + ucol + ").charAt(0);";
			} else if (colType.compareTo("byte[]") == 0) {
				get = "(r.getBytes(" + ucol + "));";
			} else if (colType.compareTo("Boolean") == 0) {
				get = "(r.getBoolean(" + ucol + "));";
			} else if (colType.compareTo("Byte[]") == 0) {
				get = "(org.apache.commons.lang.ArrayUtils.toObject(r.getBytes(" + ucol + ")));";
			} else if (colType.compareTo("com.bixuebihui.jdbc.ClobString") == 0) {
				get = "(new com.bixuebihui.jdbc.ClobString(com.bixuebihui.jdbc.JDBCUtils.oracleClob2Str((Clob)r.getObject("
						+ ucol + "))));";
			} else {
				get = "(r.getObject(" + ucol + "));";
				LOG.error("Warning! Unknown type : " + colType + " in write mapRow");
			}

			get = firstUp(cd.getName()) + get;
			gets.add(get);
		}

		out("/**");
		out("  * Updates the object from a selected ResultSet.");
		out("  */");
		out("@Override");
		out("public " + getPojoClassName(tableName) + " mapRow (ResultSet r, int index) throws SQLException");
		out("{");
		out(INDENT + getPojoClassName(tableName) + " res = new " + getPojoClassName(tableName) + "();");

		for (String e : gets) {
			out("      res.set" + e);
		}

		out("      return res;");
		out("}");
		out("");
	}

	/**
	 * Writes out the dummy insert (blank record) function.
	 *
	 * @param methodName
	 * @param keyData2
	 */
	void writeInsertDummy(String tableName, List<String> keyData2, String methodName, List<ColumnData> columnData) throws IOException {

		out("/**");
		out("  * Inserts the dummy record of " + this.getPojoClassName(tableName)
				+ " object values into the database.");
		out("  */");
		out("@Override");
		out("public boolean " + methodName + "() throws SQLException");
		out("{");
		out("     " + this.getPojoClassName(tableName) + "  info = new " + this.getPojoClassName(tableName) + "();");

		boolean isFirst = true;
		for (ColumnData columnDatum : columnData) {
			if (!columnDatum.isNullable &&
					"String".equals(columnDatum.getJavaType())) {
				if (isFirst) {
					out("     java.util.Random rnd = new java.util.Random();");
					isFirst = false;
				}
				out("    info.set" + firstUp(columnDatum.name)
						+ "(Integer.toString(Math.abs(rnd.nextInt(Integer.MAX_VALUE)), 36));");
			}
		}

		if (isNotEmpty(keyData2)) {
			out("    info.set" + firstUp(keyData2.get(0)) + "(getNextKey());");
		}
		out("    return this.insert(info);");

		out("}");
		out("");
	}

	private String makeInsertObjects(boolean useAutoincrement, List<ColumnData> columnData) {
		return makeInsertObjects(useAutoincrement, false, columnData);
	}
	private String makeInsertObjects(boolean useAutoincrement, boolean skipVersionColumn, List<ColumnData> columnData) {
		StringBuilder objs = new StringBuilder("");
		boolean useVersion  = skipVersionColumn && containsVersion(columnData);
		Iterator<ColumnData> iterator = columnData.iterator();
		while (iterator.hasNext()) {
			ColumnData cd=iterator.next();
			if (useAutoincrement && cd.isAutoIncrement
					|| (useVersion && cd.getName().equalsIgnoreCase(versionColName))) {
				continue;
			}

			objs.append("info.get").append(firstUp(cd.getName())).append("()");

			if (iterator.hasNext()) {
				objs.append(",");
			}
		}
		if(objs.lastIndexOf(",") == objs.length()-1) {
			objs.deleteCharAt(objs.length()-1);
		}
		return objs.toString();
	}

	private String makeUpdateObjects(List<String> params, List<ColumnData> columnData) {
		return "" + makeInsertObjects(use_autoincrement, true, columnData) + "," +
				createKeyObjects(params);
	}

	public String createKeyObjects(List<String> params) {
		StringBuilder objs = new StringBuilder();
		if (params != null) {
			String key;
			for (Iterator<String> e = params.iterator(); e.hasNext();) {
				key = e.next();

				objs.append("info.get").append(firstUp(key)).append("()");

				if (e.hasNext()) {
					objs.append(" , ");
				}
			}

		} else {
			objs = null;
		}

		return objs == null ? null : objs.toString();
	}

	/**
	 * Writes out the count function. If "key" is not null then a search based
	 * on the key is added. If "withLike" then the search is based on a "like"
	 * not an exact match.
	 */
	void writeDALConstructor(String tableName) throws IOException {
        String classPojo = this.getPojoClassName(tableName);
		String className = classPojo + "List";

		out("/**");
		out("  * Don't direct use the " + className + ", use " + classPojo + "Manager instead.");
		out("  */");
		out("protected " + className + "()");
		out("{");
		out("}");
		out("");
	}

	/**
	 * Writes out the update function.
	 */
	void writeUpdate(String tableName, List<String> params, String methodName, boolean isWithConn,
			boolean isWithVersion, List<ColumnData> columnData) throws IOException {
		// work out the "where" part of the update first..
		String paramObjs = this.createKeyObjects(params)
				+ (isWithVersion ? ",info.get" + firstUp(versionColName) + "() " : "");

		out("/**");
		out("  * Updates the current object values into the database with version condition as an optimistic database lock.");
		out("  */");
		out("public boolean " + methodName + "(" + this.getPojoClassName(tableName) + " info"
				+ (isWithConn ? ", Connection cn" : "") + ") throws SQLException");
		out("{");
		out("    String updateSql = getUpdateSql()+\" and version=?\";");

		StringBuilder objs = new StringBuilder();
		for (ColumnData cd:columnData) {
			boolean added = true;
			if (isWithVersion && cd.getName().equalsIgnoreCase(versionColName)) {
				added = false;
			} else {
				objs.append(" info.get").append(firstUp(cd.getName())).append("()");
			}
			if (added) {
				objs.append(",");
			}
		}

		out("    return 1 == dbHelper.executeNoQuery(updateSql, new Object[]{" + objs + "\n\t " + paramObjs + "}"
				+ (isWithConn ? ", cn" : "") + ");");

		out("}");
		out("");

	}

	/**
	 * since bixuebihui-dbtools 0.7.1, only generate getNextKey for Timestamp and String/UUID
	 * @param keyData
	 * @throws IOException
	 */
	void writeWraper(List<String> keyData, List<ColumnData> columnData) throws IOException, GenException {

		String type = this.getFirstKeyType(keyData, columnData);

		if ("Timestamp".equals(type) || "String".equals(type)) {
			out("public " + type + " getNextKey(){\n");
			if (keyData != null && keyData.size() == 1) {
				if ("Timestamp".equals(type)) {
					out("\treturn new Timestamp(new java.util.Date().getTime());");
				} else if ("String".equals(type)) {
					out("\treturn java.util.UUID.randomUUID().toString();");
				}

			} else if (keyData != null && keyData.size() > 1){
				out("\tthrow new IllegalStateException(\"联合主键时，无法生成自增长主键\");");
			}
			else{//没有主键
				out("\treturn new java.util.Date().getTime();");
			}
			out("}\n");
			out("\n");
		}


	}

	/**
	 * Writes out the count function. If "key" is not null then a search based
	 * on the key is added. If "withLike" then the search is based on a "like"
	 * not an exact match.
	 */
	void writeCount(List<String> params, boolean withLike, String methodName, List<ColumnData> columnData) throws IOException, GenException {
		String where;

		where = createPreparedWhereClause(params, withLike, columnData);
		String objs = this.createPreparedObjects(params, withLike, columnData);

		out("/**");
		out("  * Counts the number of entries for this table in the database.");
		out("  */");
		out(createMethodLine(methodName, params, "int", columnData));
		out("{");
		out("    String query=\"select count(*) from \" + getTableName() + \" \" " + where + ";");
		out("    Object o = dbHelper.executeScalar(query," + objs + ");");
		out("  return o==null?0:Integer.parseInt(o.toString());");

		out("}");
		out("");
	}

	void writeGetTableName(String tableOrKeyName, String methodName, boolean isKey) throws IOException {
		out("/**");
		out("  * Get "+(isKey?"key":"table")+" name.");
		out("  */");
		out("@Override");
		out("public String " + methodName + "()");
		out("{");
		if (tableOrKeyName == null) {
			out("    return " + "\"\"" + ";");
		}
		else{
			out("    return " + (isKey ?
					"F." + columnNameToFieldName(tableOrKeyName)
					: "\"" + tableOrKeyName + "\""
			) + ";");
		}
		out("}");
		out("");
	}

	void writeGetKeyName(String keyName, String methodName) throws IOException {
		writeGetTableName(keyName, methodName, true);
	}

	void writeCountWhereTest(String tableName, String methodName) throws IOException {

		out("public void test" + firstUp(methodName) + "() throws SQLException");
		out("{");
		String className = this.getPojoClassName(tableName) + "Manager";
		out("  " + className + " man = new " + className + "();");
		out("    assertTrue(man." + methodName + "(\"\")>=0);");
		out("}");
		out("");
	}

	public String createPreparedWhereClause(List<String> params, boolean withLike, List<ColumnData> columnData) throws GenException {
		StringBuilder where = new StringBuilder();

		// if we have keys passed in then we add a "where" to the count
		// e.g. se
		//
		if (params != null) {
			// work out the "where" part of the update first..
			//
			where.append("+\" where ");
			String key;
			String keyType;
			for (Iterator<String> e = params.iterator(); e.hasNext();) {
				key = e.next();
				keyType = getColType(key, columnData);

				// only allow likes on String types...
				// Hmmm should we allow more than this???
				//
				if ((withLike) && ("String".equals(keyType))) {
					where.append(key + " like ?");
				} else {
					where.append(key + "=?");
				}

				if (e.hasNext()) {
					where.append(" and ");
				}
			}

			where.append("\"");
		}

		return where.toString();
	}

	public String createPreparedObjects(List<String> params, boolean withLike, List<ColumnData> columnData) throws GenException {
		StringBuilder objs = new StringBuilder("");

		// if we have keys passed in then we add a "where" to the count
		// e.g. se
		//
		if (params != null) {
			// work out the "where" part of the update first..
			//
			objs.append(" new Object[]{ ");
			String key;
			String keyType;
			for (Iterator<String> e = params.iterator(); e.hasNext();) {
				key = e.next();
				keyType = getColType(key, columnData);

				// only allow likes on String types...
				// Hmmm should we allow more than this???
				//
				if ((withLike) && ("String".equals(keyType))) {
					objs.append("\"%\"+" + key.toLowerCase() + "+\"%\"");
				} else {
					objs.append(key.toLowerCase());
				}

				if (e.hasNext()) {
					objs.append(" , ");
				}
			}

			objs.append("}");
		} else {
			objs = null;
		}

		return objs == null ? null : objs.toString();
	}

	/**
	 * Creates a the parameter block for a method.
	 */
	public String createMethodLine(String methodName, List<String> params, String returnType, List<ColumnData> columnData) throws GenException {
		StringBuilder paramString = new StringBuilder("public " + returnType + " " + methodName + "(");
		String key;
		String keyType;

		if (params != null) {
			// work out the query string and the method parameters.
			//
			for (Iterator<String> e = params.iterator(); e.hasNext();) {
				key = e.next();
				try {
					keyType = getColType(key, columnData);
				} catch (GenException e1) {
				    LOG.error("unsupported future when generate method "+methodName+" return type "+returnType, e1);
				    throw e1;
				}
				paramString.append(keyType + " " + key.toLowerCase());

				if (e.hasNext()) {
					paramString.append(",");
				} else {
					paramString.append(") throws SQLException");
				}
			}
		} else {
			paramString.append(") throws SQLException");
		}

		return paramString.toString();
	}

	/**
	 * Gets the column data for a specified table.
	 */
	public @NotNull List<ColumnData> getColumnData(String tableName) throws SQLException {

		List<ColumnData> colData;
		if (colDataCache.containsKey(tableName)) {
			return colDataCache.get(tableName);
		}

		colData = TableUtils.getColumnData(metaData, catalog, schema, tableName);

		colDataCache.put(tableName, colData);
		return colData;
	}

	/**
	 * Selects the primary keys for a particular table.
	 *
	 * @throws SQLException
	 */
	public @NotNull  List<String> getTableKeys(String tableName) throws SQLException {

		List<String> keyData;
		if (keyCache.containsKey(tableName)) {
			return keyCache.get(tableName);
		}

		keyData = TableUtils.getTableKeys(metaData, catalog, schema, tableName);

		keyCache.put(tableName, keyData);
		return keyData;
	}

	/**
	 * Selects the Exported Keys defined for a particular table.
	 */
	protected @NotNull  List<FKDefinition> getTableExportedKeys(String tableName) throws SQLException {
		if (foreignKeyExCache.containsKey(tableName)) {
			return foreignKeyExCache.get(tableName);
		}

		if(isMysql()){
			//针对mysql的优化, 一次加载全部外键
			if (foreignKeyExCache.isEmpty()) {
				foreignKeyExCache.putAll(TableUtils.getAllMySQLExportKeys(getDbHelper(),tableOwner));
			}
			if(foreignKeyExCache.containsKey(tableName)) {
				LOG.info(tableName +" has  ExportKey in cache:"+ foreignKeyExCache.get(tableName));
				return foreignKeyExCache.get(tableName);
			}else{
				LOG.debug(tableName +" does not have a ExportKey in cache");
			}
			return Collections.emptyList();
		}
		List<FKDefinition> foreignKeyData = TableUtils.getTableExportedKeys(metaData, catalog, tableOwner, tableName);
		foreignKeyExCache.put(tableName, foreignKeyData);
		return foreignKeyData;
	}

	private boolean isMysql() {
		return dbconfig.getClassName().contains("mysql");
	}

	/**
	 * Selects the Imported Keys defined for a particular table.
	 */
	protected  @NotNull  List<FKDefinition> getTableImportedKeys(String tableName) throws SQLException {

		if (foreignKeyImCache.containsKey(tableName)) {
			return foreignKeyImCache.get(tableName);
		}
		if(isMysql()){
			//针对mysql的优化, 一次加载全部外键
			if (foreignKeyImCache.isEmpty()) {
				foreignKeyImCache.putAll(TableUtils.getAllMySQLImportKeys(getDbHelper(),tableOwner));
			}
			if(foreignKeyImCache.containsKey(tableName)) {
				LOG.info(tableName +" has  ImportKey in cache:"+ foreignKeyImCache.get(tableName));
				return foreignKeyImCache.get(tableName);
			}else{
				LOG.debug(tableName +"have not a ImportKey in cache");
			}
			return Collections.emptyList();
		}
		List<FKDefinition> foreignKeyData = TableUtils.getTableImportedKeys(metaData, catalog, tableOwner, tableName);
		foreignKeyImCache.put(tableName, foreignKeyData);
		return foreignKeyData;
	}

	/**
	 *
	 * write the methods associated to this Exported Foreign key entry.
	 */
	protected void writeExportedMethods(FKDefinition def, List<ColumnData> columnData) throws IOException, GenException {
		out("/**");
		out("  * Get all related  " + def.getFKTableName() + " which have same " + def.getFKColList());
		out("  */");

		out(createMethodLine("getRelated" + def.getFKTableName() + "__" + def.getFKColumnName(), def.getPKFields(),
				"List<" + this.getPojoClassName(def.getFKTableName()) + "> ", columnData));
		out("{");
		out("\t" + firstUp(def.getFKTableName()) + "Manager x = new " + firstUp(def.getFKTableName()) + "Manager();");
		out("\treturn x.selectBy" + def.getFKTableName() + "__" + def.getFKColumnName() + "(" + def.getPKColList() + ");");
		out("}");
	}

	/**
	 *
	 * write the methods associated to this Foreign key entry.
	 */
	protected void writeImportedMethods(String tableName, FKDefinition def, List<ColumnData> columnData) throws IOException, GenException {
		out("/**");
		out("  * Imported " + tableName + " PK:" + def.getPKTableName() + " FK:" + def.getFKTableName());
		out("  */");
		out(createMethodLine("get" + firstUp(def.getPKTableName()) + "By" + def.getFKColumnName(), def.getFKFields(),
				firstUp(def.getPKTableName()), columnData));
		out("{");
		out("\t" + firstUp(def.getPKTableName()) + "Manager x = new " + firstUp(def.getPKTableName()) + "Manager();");
		out("\treturn x.selectByKey(" + def.getFKColList() + ");");
		out("}");
		writeSelectAll(tableName, def.getFKFields(), false,
				"selectBy" + def.getFKTableName() + "__" + def.getFKColumnName(),
				false, columnData);
	}

	/**
	 * Selects the indexes for a particular table.
	 */
	public List<String> getTableIndexes(String tableName) throws SQLException {
		// use a hashmap to temporarily store the indexes as well
		// so we can avoid duplicate values.
		HashMap<String, String> checkIndexes = new HashMap<>();
		String index;
		short indexType;
		List<String> indexData;
		if (indexCache.containsKey(tableName)) {
			return indexCache.get(tableName);
		}
		indexData = new ArrayList<>();

		try( ResultSet r = metaData.getIndexInfo(catalog, schema, tableName, false, false) ) {
			while (r.next()) {
				indexType = r.getShort(7);
				index = r.getString(9);
				if (indexType != DatabaseMetaData.tableIndexStatistic &&
						// ensure that it is not a duplicate value.
						checkIndexes.get(index) == null) {
					indexData.add(index);
					checkIndexes.put(index, index);
				}
			}
		}
		indexCache.put(tableName, indexData);
		return indexData;
	}

	/**
	 * Selects the type of a particular column name. Cannot use Hashtables to
	 * store columns as it screws up the ordering, so we have to do a crap
	 * search. (and yes I know it could be better - it's good enuf).
	 */
	public String getColType(String key, List<ColumnData> columnData) throws GenException {
		String type = "unknown";
		for (ColumnData tmp: columnData) {
			if (tmp.getName().equalsIgnoreCase(key)) {
				type = tmp.getJavaType();
				break;
			}
		}
		if("unknown".equals(type))
		{
			LOG.error("unknown type of key:"+key);
			throw new GenException("error unknown type of key:"+key);
		}

		return type;
	}

	/**
	 * Wrapper for text output. So we can change where the output goes easily!
	 */
	public void out(String text) throws IOException {
		currentOutput.write(text + "\n");
	}

	/**
	 * Reads in the generic file into the target directory. Also sets the
	 * package names correctly.
	 */
	public boolean importGenericFiles() {
		boolean status = false;

		try {

			for (int i = 0; i < genericFiles.length; i++) {
			    try(
				BufferedReader br = new BufferedReader(new FileReader("dbgeneric" + File.separator + genericFiles[i] + ".java"));
				BufferedWriter bw = new BufferedWriter(new FileWriter(srcDir + File.separator + genericFiles[i] + ".java"));
				){
					// replaces the first line with the
					// correct package name
					//
					bw.write("package " + packageName + ";\n");
					String line = br.readLine();

					while (line != null) {
						line = br.readLine();
						if (line == null) {
							continue;
						}
						bw.write(line + "\n");
					}
				}
			}
			status = true;
		} catch (IOException ie) {
			ie.printStackTrace(console);
		}

		return status;
	}

	public List<String> getTableNames() {
		return tableNames;
	}

	public void setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Map<String, T_metatable> getTableDataExt(List<String> tableNames)
			throws SQLException, InstantiationException, IllegalAccessException {
		HashMap<String, T_metatable> ht = new HashMap<>();

		setupMetatable();

		Collection<T_metatable> c = daoMetaTable.getTableDataExt(tableNames);
		for (T_metatable t : c) {
			ht.put(t.getTname(), t);
		}
		return ht;
	}

	public boolean initTableData(List<String> tableNames) throws SQLException {
		if (tableNames != null) {
			T_metatable[] infos = new T_metatable[tableNames.size()];
			int i = 0;
			for (String name : tableNames) {
				infos[i] = new T_metatable();
				infos[i].setTname(name);
				infos[i].setIsnode(false);
				infos[i].setIsstate(false);
				infos[i].setIsversion(false);
				infos[i].setIsmodifydate(false);
				infos[i].setIsuuid(false);
				infos[i].setTid(daoMetaTable.getNextKey());
				i++;
			}
			return daoMetaTable.insertBatch(infos, null);
		} else {
			return false;
		}
	}

	public boolean setupMetatable() {
		Connection conn = null;
		boolean res = false;
		try {
			LOG.info("[CYC]数据库安装...");
			conn = dbHelper.getConnection();
			if (!JDBCUtils.tableOrViewExists(null, null, daoMetaTable.getTableName(), conn)) {
				SqlFileExecutor ex = new SqlFileExecutor();
				String filename = "";

				if (daoMetaTable.getDbType() == BaseDao.POSTGRESQL) {
					filename = "postgresql";
				} else {
					filename = "mysql";
				}

				DefaultResourceLoader loader = new DefaultResourceLoader();
				String beanConfigFile = "classpath:dbscript/ext." + filename + ".sql";
				LOG.info(loader.getResource(beanConfigFile).getFilename());
				ex.execute(conn, loader.getResource(beanConfigFile).getInputStream());

				res = initTableData(tableNames);
			}
			if (res) {
				LOG.info("[CYC]数据库已成功安装");
			} else {
				LOG.info(DB_ERROR);
			}
		} catch (Exception e) {
			LOG.info(DB_ERROR, e);
		} finally {
			JDBCUtils.close(conn);
		}
		return false;
	}

	public boolean insertOrUpdateMetatable(T_metatable metatable) throws SQLException {
		if (metatable.getTid() <= 0) {
			return daoMetaTable.insertAutoNewKey(metatable);
		} else {
			return daoMetaTable.updateByKey(metatable);
		}
	}

	@Override
	public void processTableDiff(String tableName) {

		if (excludeTablesList != null && excludeTablesList.containsKey(tableName)) {
			return;
		}

		if (!CollectionUtils.isEmpty(tablesList) && !tablesList.containsKey(tableName)) {
			return;
		}

		generatePojo(tableName);
		generateDAL(tableName);
		generateBusiness(tableName);
	}

	@Override
	public void processEnd(HashMap<String, List<ColumnData>> tableData) {//NOSONAR

		if (generateFlag) {
			saveTableDataToLocalCache(tableData);
		}

	}

	private String firstUp(String src) {
		return NameUtils.firstUp(src, keep_case);
	}

}
