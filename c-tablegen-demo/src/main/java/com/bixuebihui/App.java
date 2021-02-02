package com.bixuebihui;

import com.bixuebihui.datasource.BitmechanicDataSource;
import com.bixuebihui.dbcon.DatabaseConfig;
import com.bixuebihui.test.business.TestManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * @author xwx
 */
@SpringBootApplication
public class App {



    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

    }


}
