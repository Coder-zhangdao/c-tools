package com.bixuebihui.test.dal;
/*
  * test_gen: 测试表
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
import com.bixuebihui.test.business.*;
import com.bixuebihui.test.pojo.*;
import com.bixuebihui.jdbc.RowMapperResultReader;
import com.bixuebihui.test.BaseList;

public class TestGenList  extends BaseList<TestGen,Long>
{
/**
  * Don't direct use the TestGenList, use TestGenManager instead.
  */
protected TestGenList(DataSource ds)
{
    super(ds);
}

protected String getDeleteSql(){
    return "delete from " + getTableName() + " where id=?";
}

    public static final class F{
        public static final String ID = "id";
        public static final String NAME = "name";
        public static String[] getAllFields() { return new String[] {ID,NAME};}
    }

@Override
protected String getInsertSql(){
    return "insert into " + getTableName() + " ( name ) values ( ? )";
}

@Override
protected String getUpdateSql(){
    return "update " + getTableName() + " set name=?"
    +" where id=?";
}

@Override
protected Object[] getInsertObjs(TestGen info){
    return new Object[]{info.getName()};
}

@Override
protected Object[] getUpdateObjs(TestGen info){
    return new Object[]{info.getName(),info.getId()};
}

/**
  * Get table name.
  */
@Override
public String getTableName()
{
    return "test_gen";
}

/**
  * Get key name.
  */
@Override
public String getKeyName()
{
    return F.ID;
}

/**
  * Updates the object from a selected ResultSet.
  */
@Override
public TestGen mapRow (ResultSet r, int index) throws SQLException
{
      TestGen res = new TestGen();
      res.setId(r.getLong(F.ID));
      res.setName(r.getString(F.NAME));
      return res;
}

@Override
public Long getId(TestGen info) {
    return  info.getId();
}


@Override
public void setId(TestGen info, Long id) {
    info.setId(id);
}


@Override
public void setIdLong(TestGen info, long id) {
    info.setId((Long)id);
}


/**
  * Inserts the dummy record of TestGen object values into the database.
  */
@Override
public boolean insertDummy() throws SQLException
{
     TestGen  info = new TestGen();
    info.setId(getNextKey());
    return this.insert(info);
}

}
