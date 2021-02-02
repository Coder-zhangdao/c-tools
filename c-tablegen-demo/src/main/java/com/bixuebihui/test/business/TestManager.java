package com.bixuebihui.test.business;
/**
  * test
  *
  * WARNING! Automatically generated file!
  * Do not edit the pojo and dal packages,use `maven tablegen:gen`!
  * Code Generator by J.A.Carter
  * Modified by Xing Wanxiang 2008-2021
  * (c) www@qsn.so
  */

import java.sql.*;
import java.util.List;
import com.bixuebihui.test.dal.*;
import com.bixuebihui.test.pojo.*;
import com.bixuebihui.test.BaseList;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * @author xwx
 */
@Repository
public class TestManager  extends TestList
{

    /**
     * Don't direct use the TestList, use TestManager instead.
     *
     * @param ds
     */
    protected TestManager(DataSource ds) {
        super(ds);
    }
}
