package com.bixuebihui.tablegen;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * @author xwx
 */
public class ProjectConfig {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectConfig.class);

    /**
     * used for DataSource alias for spring autowired @Qualifer('alias') annotation
     */
    String alias;

    String catalog;
    String tableOwner;
    String packageName;
    String baseDir;
    String srcDir;
    String testDir;
    String resourceDir;
    String jspDir;
    String schema;
    String addPrefix;
    String removePrefix;
    String versionColName = "version";
    boolean indexes;
    boolean overWriteAll = false;
    boolean useCustomMetaTable = false;
    boolean use_annotation = false;
    boolean use_swagger = true;
    boolean use_autoincrement = false;
    boolean generate_procedures = true;
    boolean generateAll=false;
    /**
     * additional settings, used for comments, data checking, interfaces, etc.
     */
    String extra_setting;
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
    Map<String, String> viewList;
    Map<String, String> excludeTablesList;

    public static ProjectConfig readFrom(Properties props, String baseDir) {
        ProjectConfig c = new ProjectConfig();

        c.alias = props.getProperty("alias");
        c.baseDir = baseDir;

        c.srcDir = baseDir + props.getProperty("src_dir");
        LOG.debug("src_dir:" +  c.srcDir);
        c.resourceDir = baseDir + props.getProperty("resource_dir");
        LOG.debug("resource_dir:" +  c.resourceDir);

        if (StringUtils.isEmpty( c.resourceDir)) {
            c.resourceDir =  c.srcDir;
        }

        c.testDir = baseDir + props.getProperty("test_dir");
        LOG.debug("test_dir:" +  c.testDir);
        c.jspDir = baseDir + props.getProperty("jsp_dir");
        LOG.debug("jsp_dir:" +  c.jspDir);
        c.packageName = props.getProperty("package_name");
        LOG.debug("package_name:" +  c.packageName);
        c.schema = props.getProperty("schema");
        LOG.debug("schema:" +  c.schema);
        c.tableOwner = props.getProperty("table_owner");
        LOG.debug("table_owner:" +  c.tableOwner);

        c.indexes = getBooleanCfg(props, "indexes");
        c.useCustomMetaTable = getBooleanCfg(props, "use_custom_meta_table");

        c.generate_procedures = getBooleanCfg(props, "generate_procedures");

        c.addPrefix = props.getProperty("add_prefix");
        if ( c.addPrefix == null) {
            c.addPrefix = "";
        }
        c.removePrefix = props.getProperty("remove_prefix");
        if ( c.removePrefix == null) {
            c.removePrefix = "";
        }

        c.parseTableNames(props.getProperty("table_list"));
        c.parseViewNames(props.getProperty("view_list"));

        c.parseExcludeTableNames(props.getProperty("exclude_table_list"));

        // 有扩展表时用以下interface的设置值
        c.pojo_node_interface = props.getProperty("pojo_node_interface");
        c.pojo_version_interface = props.getProperty("pojo_version_interface");
        c.pojo_state_interface = props.getProperty("pojo_state_interface");
        c.pojo_uuid_interface = props.getProperty("pojo_uuid_interface");
        c.pojo_modifydate_interface = props.getProperty("pojo_modifydate_interface");

        c.pojo_node_interface_list =  makeList(props.getProperty("pojo_node_interface_list"));
        c.pojo_version_interface_list = makeList(props.getProperty("pojo_version_interface_list"));
        c.pojo_state_interface_list = makeList(props.getProperty("pojo_state_interface_list"));
        c.pojo_uuid_interface_list = makeList(props.getProperty("pojo_uuid_interface_list"));
        c.pojo_modifydate_interface_list = makeList(props.getProperty("pojo_modifydate_interface_list"));

        c.overWriteAll = getBooleanCfg(props, "over_write_all");
        c.use_annotation = getBooleanCfg(props, "use_annotation");
        c.use_autoincrement = getBooleanCfg(props, "use_autoincrement");
        c.generateAll = getBooleanCfg(props, "generate_all");

        c.extra_setting = props.getProperty("extra_setting");

        return c;
    }

    private static boolean getBooleanCfg(Properties props, String key) {
        return "yes".equalsIgnoreCase(props.getProperty(key));
    }

    private static List<String> makeList(String property) {
        List<String> res = new ArrayList<>();
        if (StringUtils.isNotEmpty(property)) {
            res.addAll(Arrays.asList(property.trim().split(",")));
        }
        return res;
    }

    public Map<String, String> getViewList() {
        return viewList;
    }

    public void setViewList(Map<String, String> viewList) {
        this.viewList = viewList;
    }

    public boolean isUse_swagger() {
        return use_swagger;
    }

    public void setUse_swagger(boolean use_swagger) {
        this.use_swagger = use_swagger;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getTableOwner() {
        return tableOwner;
    }

    public void setTableOwner(String tableOwner) {
        this.tableOwner = tableOwner;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSrcDir() {
        return srcDir;
    }

    public void setSrcDir(String srcDir) {
        this.srcDir = srcDir;
    }

    public String getTestDir() {
        return testDir;
    }

    public void setTestDir(String testDir) {
        this.testDir = testDir;
    }

    public String getResourceDir() {
        return resourceDir;
    }

    public void setResourceDir(String resourceDir) {
        this.resourceDir = resourceDir;
    }

    public String getJspDir() {
        return jspDir;
    }

    public void setJspDir(String jspDir) {
        this.jspDir = jspDir;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getAddPrefix() {
        return addPrefix;
    }

    public void setAddPrefix(String addPrefix) {
        this.addPrefix = addPrefix;
    }

    public String getVersionColName() {
        return versionColName;
    }

    public void setVersionColName(String versionColName) {
        this.versionColName = versionColName;
    }

    public boolean isIndexes() {
        return indexes;
    }

    public void setIndexes(boolean indexes) {
        this.indexes = indexes;
    }

    public boolean isOverWriteAll() {
        return overWriteAll;
    }

    public void setOverWriteAll(boolean overWriteAll) {
        this.overWriteAll = overWriteAll;
    }

    public boolean isUseCustomMetaTable() {
        return useCustomMetaTable;
    }

    public void setUseCustomMetaTable(boolean useCustomMetaTable) {
        this.useCustomMetaTable = useCustomMetaTable;
    }

    public boolean isUse_annotation() {
        return use_annotation;
    }

    public void setUse_annotation(boolean use_annotation) {
        this.use_annotation = use_annotation;
    }

    public boolean isUse_autoincrement() {
        return use_autoincrement;
    }

    public void setUse_autoincrement(boolean use_autoincrement) {
        this.use_autoincrement = use_autoincrement;
    }

    public boolean isGenerate_procedures() {
        return generate_procedures;
    }

    public void setGenerate_procedures(boolean generate_procedures) {
        this.generate_procedures = generate_procedures;
    }

    public boolean isGenerateAll() {
        return generateAll;
    }

    public void setGenerateAll(boolean generateAll) {
        this.generateAll = generateAll;
    }

    public String getExtra_setting() {
        return extra_setting;
    }

    public void setExtra_setting(String extra_setting) {
        this.extra_setting = extra_setting;
    }

    public String getPojo_node_interface() {
        return pojo_node_interface;
    }

    public void setPojo_node_interface(String pojo_node_interface) {
        this.pojo_node_interface = pojo_node_interface;
    }

    public String getPojo_version_interface() {
        return pojo_version_interface;
    }

    public void setPojo_version_interface(String pojo_version_interface) {
        this.pojo_version_interface = pojo_version_interface;
    }

    public String getPojo_state_interface() {
        return pojo_state_interface;
    }

    public void setPojo_state_interface(String pojo_state_interface) {
        this.pojo_state_interface = pojo_state_interface;
    }

    public String getPojo_uuid_interface() {
        return pojo_uuid_interface;
    }

    public void setPojo_uuid_interface(String pojo_uuid_interface) {
        this.pojo_uuid_interface = pojo_uuid_interface;
    }

    public String getPojo_modifydate_interface() {
        return pojo_modifydate_interface;
    }

    public void setPojo_modifydate_interface(String pojo_modifydate_interface) {
        this.pojo_modifydate_interface = pojo_modifydate_interface;
    }

    public List<String> getPojo_node_interface_list() {
        return pojo_node_interface_list;
    }

    public void setPojo_node_interface_list(List<String> pojo_node_interface_list) {
        this.pojo_node_interface_list = pojo_node_interface_list;
    }

    public List<String> getPojo_version_interface_list() {
        return pojo_version_interface_list;
    }

    public void setPojo_version_interface_list(List<String> pojo_version_interface_list) {
        this.pojo_version_interface_list = pojo_version_interface_list;
    }

    public List<String> getPojo_state_interface_list() {
        return pojo_state_interface_list;
    }

    public void setPojo_state_interface_list(List<String> pojo_state_interface_list) {
        this.pojo_state_interface_list = pojo_state_interface_list;
    }

    public List<String> getPojo_uuid_interface_list() {
        return pojo_uuid_interface_list;
    }

    public void setPojo_uuid_interface_list(List<String> pojo_uuid_interface_list) {
        this.pojo_uuid_interface_list = pojo_uuid_interface_list;
    }

    public List<String> getPojo_modifydate_interface_list() {
        return pojo_modifydate_interface_list;
    }

    public void setPojo_modifydate_interface_list(List<String> pojo_modifydate_interface_list) {
        this.pojo_modifydate_interface_list = pojo_modifydate_interface_list;
    }

    public Map<String, String> getTablesList() {
        return tablesList;
    }

    public void setTablesList(Map<String, String> tablesList) {
        this.tablesList = tablesList;
    }

    public Map<String, String> getExcludeTablesList() {
        return excludeTablesList;
    }

    public void setExcludeTablesList(Map<String, String> excludeTablesList) {
        this.excludeTablesList = excludeTablesList;
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
                LOG.info(name);
            }
        }
    }

    /**
     *  view list is separated by semicolon(;) and before first colon(:) is the view name.
     *  The view is virtual view, i.e. it is a select SQL.
     */
    void parseViewNames(String names) {
        if (names != null) {
            if (viewList == null) {
                viewList = new HashMap<>();
            }
            StringTokenizer st = new StringTokenizer(names, ";");

            while (st.hasMoreElements()) {
                String name = st.nextToken().trim();

                viewList.put(name.substring(0,name.indexOf(":")), name.substring(name.indexOf(":")+1));
                LOG.info(name);
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
                LOG.info("exclude table: " + name);
            }
        }
    }

    public String getBaseSrcDir() {
        return srcDir + File.separator + packageName2Dir(packageName);
    }

    public String packageName2Dir(String packageName) {
        if (packageName == null) {
            return null;
        } else {
            return packageName.replaceAll("\\.", "\\" + File.separator);
        }
    }

    public String getRemovePrefix() {
        return removePrefix;
    }
}
