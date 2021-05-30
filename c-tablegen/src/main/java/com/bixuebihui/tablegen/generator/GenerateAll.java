package com.bixuebihui.tablegen.generator;

import com.bixuebihui.jdbc.IDbHelper;
import com.bixuebihui.tablegen.DbDiff;
import com.bixuebihui.tablegen.ProjectConfig;
import com.bixuebihui.tablegen.TableGen;
import com.bixuebihui.tablegen.diffhandler.DiffHandler;
import com.bixuebihui.tablegen.entry.ColumnData;
import com.bixuebihui.tablegen.entry.TableInfo;
import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.bixuebihui.tablegen.TableGen.getTableDataFromLocalCache;
import static com.bixuebihui.tablegen.TableGen.saveTableDataToLocalCache;

public class GenerateAll implements DiffHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateAll.class);
    boolean generateSuccess = true;
    Generator[] perTableGenerator = new Generator[]{
           new PojoGenerator(), new DalGenerator(), new BusinessGenerator(),
    };

    Generator[] perViewGenerator = new Generator[]{
            new ViewGenerator(), new DalViewGenerator(), new BusinessViewGenerator(),
    };
    Generator[] onceGenerator = new Generator[]{
      new BaseListGenerator(),
            // new PomGenerator(),
    };
    private BaseListGenerator generator;


    public void run(String filename) throws SQLException {
        run(filename, System.out);
    }

    public void run(String filename, OutputStream out) {

        if (filename != null && filename.length() > 0) {
            init(filename);
        }


        try {

            /*
             * 如果generator_all == yes，则生成所有表 否则，进行本地快照和数据库的比对
             */
            generateOnceFiles();
            if (generator.config.isGenerateAll()) {
                generatorTables(generator.setInfo.getTableInfos());

                if(generator.setInfo.getViewInfos()!=null)
                    generatorViews(generator.setInfo.getViewInfos());

               // generateWebUI();
               // generateTest();

//                if (config.jspDir != null) {
//                    generateJsp();
//                }
//                generateSpringXml();

//                if (config.generate_procedures) {
//                    generateStoreProcedures(meta);
//                }
            } else {
                info("generate_all is off, generate incrementally, " +
                        "if want regenerate, you can remove cache file 'target/gen_table_data.cache'");

                IDbHelper helper = TableGen.getDbHelper(generator.dbConfig);

                Connection conn =  helper.getConnection();
                try {
                    DbDiff dd = new DbDiff(getTableDataFromLocalCache(), conn, generator.config.getCatalog(),
                            generator.config.getSchema());
                    dd.addDiffHandler(this);
                    dd.compareTables();
                } finally {
                    DbUtils.close(conn);
                }
            }

        } catch (SQLException | IOException e) {
            generateSuccess = false;
            LOG.error("", e);
        }

        info("Successfully complected!");
    }

    private void info(String s) {
        System.out.println(s);
        LOG.info(s);
    }

    private void generatorTables(LinkedHashMap<String, TableInfo> tableInfos) throws IOException {
        for(Generator g: perTableGenerator){
            for(TableInfo info: tableInfos.values()) {
                info(info.getName());
                g.generateToFile(info.getName());
            }
        }
    }

    private void generatorViews(LinkedHashMap<String, TableInfo> viewInfos) throws IOException {
        for(Generator g: perViewGenerator){
            for(TableInfo info: viewInfos.values()) {
                info(info.getName());
                g.generateToFile(info.getName());
            }
        }
    }

    private void generateOnceFiles() throws IOException {
        for(Generator g: onceGenerator){
            g.generateToFile("");
        }
    }

    void init(String filename) {
        generator = new BaseListGenerator();
        generator.init(filename);

        for(Generator g: perTableGenerator){
            g.init(generator.config, generator.dbConfig, generator.setInfo);
        }

        for(Generator g: perViewGenerator){
            g.init(generator.config, generator.dbConfig, generator.setInfo);
        }

        for(Generator g: onceGenerator){
            g.init(generator.config, generator.dbConfig, generator.setInfo);
        }

    }


    @Override
    public void processTableDiff(String tableName) throws IOException {
        ProjectConfig config = generator.config;
        if (config.getExcludeTablesList() != null &&
                config.getExcludeTablesList().containsKey(tableName)) {
            return;
        }

        if (!CollectionUtils.isEmpty(config.getTablesList()) && !config.getTablesList().containsKey(tableName)) {
            return;
        }

        for(Generator g: perTableGenerator){
            g.generateToFile(tableName);
        }
// view is not generated?
///        for(Generator g: perViewGenerator){
//            g.generate(tableName);
//        }

    }

    @Override
    public void processEnd(HashMap<String, List<ColumnData>> tableData) {
        if (generateSuccess) {
            saveTableDataToLocalCache(tableData);
        }
    }
}
