package com.bixuebihui.test.business;

import com.bixuebihui.datasource.BitmechanicDataSource;
import com.bixuebihui.dbcon.DatabaseConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestGenManagerTest {

    @Autowired
    TestGenManager man;

    @Test
    public void testManInsert() throws SQLException {
        com.bixuebihui.test.pojo.TestGen info = new com.bixuebihui.test.pojo.TestGen();
        info.setName("abc");
        man.save(info);
        assertTrue(info.getId()>=1);
    }

    @TestConfiguration
    public static class Ds {
        @Bean
        public DataSource getDataSource() {
            BitmechanicDataSource res = new BitmechanicDataSource();
            DatabaseConfig cfg = new DatabaseConfig();
            cfg.setClassName("com.mysql.jdbc.Driver");
            cfg.setDburl("jdbc:mysql://localhost:3306/test?allowMultiQueries=true");
            cfg.setUsername("test");
            cfg.setPassword("test123");
            res.setDatabaseConfig(cfg);
            return res;
        }
    }

}
