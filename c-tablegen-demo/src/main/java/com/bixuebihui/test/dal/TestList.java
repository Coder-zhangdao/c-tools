package com.bixuebihui.test.dal;
/*
  * test
  * 
  * Notice! Automatically generated file!
  * Do not edit the pojo and dal packages,use `maven tablegen:gen`!
  * Code Generator originally by J.A.Carter
  * Modified by Xing Wanxiang 2008-2021
  * email: www@qsn.so
  */

import java.sql.*;
import java.util.List;
import com.bixuebihui.test.business.*;
import com.bixuebihui.test.pojo.*;
import com.bixuebihui.jdbc.RowMapperResultReader;
import com.bixuebihui.test.BaseList;

public class TestList  extends BaseList<Test,Long>
{
/**
  * Don't direct use the TestList, use TestManager instead.
  */
protected TestList()
{
}

protected String getDeleteSql(){
    return "delete from " + getTableName() + " where id=?";
}

    public static final class F{
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String[] getAllFields() { return new String[] {ID,NAME};}
    }

@Override
protected String getInsertSql(){
    return "insert into " + getTableName() + " ( id,name ) values ( ?,? )";
}

@Override
protected String getUpdateSql(){
    return "update " + getTableName() + " set id=?,name=?"
    +" where id=?";
}

@Override
protected Object[] getInsertObjs(Test info){
    return new Object[]{info.getId(),info.getName()};
}

@Override
protected Object[] getUpdateObjs(Test info){
    return new Object[]{info.getId(),info.getName(),info.getId()};
}

/**
  * Get table name.
  */
@Override
public String getTableName()
{
    return "test";
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
public Test mapRow (ResultSet r, int index) throws SQLException
{
      Test res = new Test();
      res.setId(r.getLong(F.ID));
      res.setName(r.getString(F.NAME));
      return res;
}

@Override
public Long getId(Test info) {
    return  info.getId();
}


@Override
public void setId(Test info, Long id) {
    info.setId(id);
}


/**
  * Inserts the dummy record of Test object values into the database.
  */
@Override
public boolean insertDummy() throws SQLException
{
     Test  info = new Test();
    info.setId(getNextKey());
    return this.insert(info);
}

}
