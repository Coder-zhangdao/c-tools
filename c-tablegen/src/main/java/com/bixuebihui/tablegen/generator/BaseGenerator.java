package com.bixuebihui.tablegen.generator;


import com.bixuebihui.dbcon.DatabaseConfig;
import com.bixuebihui.jdbc.IDbHelper;
import com.bixuebihui.tablegen.NameUtils;
import com.bixuebihui.tablegen.ProjectConfig;
import com.bixuebihui.tablegen.TableGen;
import com.bixuebihui.tablegen.TableUtils;
import com.bixuebihui.tablegen.entry.TableInfo;
import com.bixuebihui.tablegen.entry.TableSetInfo;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.helper.ConditionalHelpers;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.bixuebihui.tablegen.NameUtils.firstUp;
import static com.bixuebihui.tablegen.NameUtils.getConfigBaseDir;
import static com.bixuebihui.tablegen.TableGenConfig.PROPERTIES_FILENAME;

/**
 * @author xwx
 */
public abstract class BaseGenerator {
    protected static final String TEMPLATE_ROOT = "/templates";
    private static final Log LOG = LogFactory.getLog(BaseGenerator.class);

    ProjectConfig config;
    DatabaseConfig dbConfig;
    TableSetInfo setInfo;

    public BaseGenerator(){
        config = new ProjectConfig();
        dbConfig = new DatabaseConfig();
        setInfo = new TableSetInfo();
    }

    public void init(ProjectConfig config, DatabaseConfig dbConfig, TableSetInfo setInfo){
        this.config = config;
        this.dbConfig = dbConfig;
        this.setInfo = setInfo;
    }

    public synchronized void  readDb(DatabaseConfig dbConfig) throws SQLException, InstantiationException, IOException, IllegalAccessException {

        IDbHelper helper = TableGen.getDbHelper(dbConfig);

        DatabaseMetaData metaData = helper.getConnection().getMetaData();
        setInfo.getTableData(config, helper, metaData);
    }

    public synchronized void init(String filename) {
        String propertiesFilename = filename!=null ? filename : PROPERTIES_FILENAME;

        try ( FileInputStream fis = new FileInputStream(propertiesFilename) ){
            Properties props = new Properties();

            props.load(fis);

            dbConfig.readDbConfig(props);

            config.readFrom(props, getConfigBaseDir(propertiesFilename));

            readDb(dbConfig);

        } catch (IOException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    abstract String getTargetFileName(String tableName);
    abstract String getTemplateFileName();

    public String getPojoClassName(String tableName) {
        String classname = setInfo.tableName2ClassName(tableName);
        if (tableName.equals(classname)) {
            return config.getPrefix() + firstUp(tableName);
        } else {
            return config.getPrefix() + classname;
        }
    }

    abstract String getClassName(String tableName);
    protected void additionalSetting(Handlebars handlebars){}

    protected String getInterface(String tableName) {
        return setInfo.getInterface(tableName, config);
    }

    protected String getExtendsClasses(String tableName) {
        return "";
    }


    public String generate(String tableName) throws IOException {
        Handlebars handlebars = getHandlebars();

        Template template = handlebars.compile(File.separator + getTemplateFileName());

        Map<String, Object> v = getContextMap(tableName);
        return template.apply(Context.newContext(v));
    }

    protected Map<String, Object> getContextMap(String tableName) {
        Map<String, Object> v = new HashMap<>(10);
        v.put("tableInfo", setInfo.getTableInfos().get(tableName));
        v.put("fields", setInfo.getTableCols(tableName));
        v.put("setInfo", setInfo);
        v.put("config", config);
        return v;
    }

    protected Handlebars getHandlebars() {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix(TEMPLATE_ROOT);
        loader.setSuffix(".hbs");

        Handlebars handlebars = new Handlebars(loader);
        handlebars.registerHelper("firstUp", (name, options) -> NameUtils.firstUp((String) name));
        handlebars.registerHelper("firstLow", (name, options) -> NameUtils.firstLow((String) name));
        handlebars.registerHelper("className", (tableName, options) -> this.getClassName((String) tableName));
        handlebars.registerHelper("interface", (tableName, options) -> this.getInterface((String) tableName));
        handlebars.registerHelper("extends", (tableName, options) -> this.getExtendsClasses((String) tableName));
        handlebars.registerHelper("typeDefaultValue", (tableName, options) -> TableGen.defaultTypeValue().get(tableName));
        handlebars.registerHelpers(ConditionalHelpers.class);

        //copied from AssignHelper
        handlebars.registerHelper("let", (String variableName, Options options) -> {
            CharSequence finalValue = options.apply(options.fn);
            options.context.data(variableName, finalValue.toString().trim());
            return null;
        });

        additionalSetting(handlebars);
        return handlebars;
    }


}
