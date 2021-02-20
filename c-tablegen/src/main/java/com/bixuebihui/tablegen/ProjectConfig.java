package com.bixuebihui.tablegen;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.*;

/**
 * @author xwx
 */
public class ProjectConfig {
    private static final Log LOG = LogFactory.getLog(ProjectConfig.class);


    String catalog;
    String tableOwner;
    String packageName;
    String baseDir;
    String srcDir;
    String testDir;
    String resourceDir;
    String jspDir;
    String schema;
    String prefix;
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
    Map<String, String> excludeTablesList;

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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
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

    public void readFrom(Properties props, String baseDir) {
        this.baseDir = baseDir;

        srcDir = baseDir + props.getProperty("src_dir");
        LOG.debug("src_dir:" + srcDir);
        resourceDir = baseDir + props.getProperty("resource_dir");
        LOG.debug("resource_dir:" + resourceDir);

        if (StringUtils.isEmpty(resourceDir)) {
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
        useCustomMetaTable = getBooleanCfg(props, "kuozhanbiao");

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
        use_autoincrement = getBooleanCfg(props, "use_autoincrement");
        generateAll = getBooleanCfg(props, "generate_all");

        extra_setting = props.getProperty("extra_setting");

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

    private boolean getBooleanCfg(Properties props, String key) {
        return "yes".equalsIgnoreCase(props.getProperty(key));
    }

    private List<String> makeList(String property) {
        List<String> res = new ArrayList<>();
        if (StringUtils.isNotEmpty(property)) {
            res.addAll(Arrays.asList(property.trim().split(",")));
        }
        return res;
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

}
