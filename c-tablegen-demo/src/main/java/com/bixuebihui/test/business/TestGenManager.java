package com.bixuebihui.test.business;
/*
  * test_gen: TABLE
  * 
  * Notice! Automatically generated file!
  * Do not edit the pojo and dal packages,use `maven tablegen:gen`!
  * Code Generator originally by J.A.Carter
  * Modified by Xing Wanxiang 2008-2021
  * email: www@qsn.so
  */

import java.sql.*;
import java.util.List;
import javax.sql.DataSource;
import com.bixuebihui.test.dal.*;
import com.bixuebihui.test.pojo.*;
import com.bixuebihui.test.BaseList;

import org.springframework.stereotype.Repository;

@Repository
public class TestGenManager  extends TestGenList
{
    /**
     * @param ds datasource for injecting
     */
    public TestGenManager(DataSource ds) {
        super(ds);
    }
}
