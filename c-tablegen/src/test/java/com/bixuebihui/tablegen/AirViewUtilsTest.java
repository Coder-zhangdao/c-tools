package com.bixuebihui.tablegen;

import com.bixuebihui.jdbc.BaseDao;
import com.bixuebihui.jdbc.IDbHelper;
import com.bixuebihui.jdbc.RowMapperResultReader;
import com.bixuebihui.tablegen.entry.ColumnData;
import com.bixuebihui.tablegen.entry.TableInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AirViewUtilsTest {
    static IDbHelper dbHelper;

    @BeforeAll
    public static void setUp() {
        TableGen t = new TableGen();
        t.init(TableUtilsTest.class.getResource("/tablegen.properties").getPath());
        dbHelper = t.getDbHelper(t.getDbConfig());
    }

    @Test
    void getColumnData() throws SQLException {
        String sql = "select test_gen.*, degree from test_gen left join t_edu on test_gen.edu_id=t_edu.id limit 1";
        List<TableInfo> list = dbHelper.executeQuery(sql, null, new RowMapperResultReader<>((rs, index) -> AirViewUtils.getColumnData(rs.getMetaData(), "test", BaseDao.MYSQL)));

        TableInfo cols = list.get(0);
        assertEquals("Type : INT(11) Name : id", cols.getFields().get(0).toString());
        for (ColumnData col : cols.getFields()) {
            System.out.println(col);
        }

    }
}
