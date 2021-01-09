package com.bixuebihui.generated.tablegen.dal;
/**
  * T_metatable
  *
  * WARNING! Automatically generated file!
  * Do not edit the pojo and dal packages,use AutoCode / bixuebihui-smartable!
  * Code Generator by J.A.Carter
  * Modified by Xing Wanxiang 2008-2012
  * (c) www.goldjetty.com
  */

import java.sql.*;
import java.util.List;

import com.bixuebihui.sequence.SequenceUtils;
import com.bixuebihui.generated.tablegen.BaseList;
import com.bixuebihui.generated.tablegen.pojo.T_metatable;
import org.springframework.stereotype.Service;

@Service
public class T_metatableList  extends BaseList<T_metatable,Long>
{
/**
  * Don't direct use the T_metatableListList, use T_metatableListManager instead.
  */
protected T_metatableList()
{
}

/**
  * Get table name / key name.
  */
@Override
public String getTableName()
{
    return "t_metatable";
}

/**
  * Get table name / key name.
  */
@Override
public String getKeyName()
{
    return "tid";
}

/**
  * Updates the object from a selected ResultSet.
  */
@Override
public T_metatable mapRow (ResultSet r, int index) throws SQLException
{
      T_metatable res = new T_metatable();
      res.setTid(r.getLong("tid"));
      res.setTname(r.getString("tname"));
      res.setIsnode(r.getBoolean("isnode"));
      res.setIsstate(r.getBoolean("isstate"));
      res.setIsversion(r.getBoolean("isversion"));
      res.setIsuuid(r.getBoolean("isuuid"));
      res.setIsmodifydate(r.getBoolean("ismodifydate"));
      res.setExtrainterfaces(r.getString("extrainterfaces"));
      res.setExtrasuperclasses(r.getString("extrasuperclasses"));
      res.setDescription(r.getString("description"));
      return res;
}

@Override
public Long getId(T_metatable info) {
	return  info.getTid();
}


@Override
public void setId(T_metatable info, Long id) {
	info.setTid(id);
}



/**
  * Updates the current object values into the database.
  */
@Override
public boolean updateByKey(T_metatable info) throws SQLException
{
    String query="update " + getTableName() + " set "+
"tid=?"+","+
"tname=?"+","+
"isnode=?"+","+
"isstate=?"+","+
"isversion=?"+","+
"isuuid=?"+","+
"ismodifydate=?"+","+
"extrainterfaces=?"+","+
"extrasuperclasses=?"+","+
"description=?"+" where tid=?";
    return 1 == dbHelper.executeNoQuery(query, new Object[]{ info.getTid(), info.getTname(), info.getIsnode(), info.getIsstate(), info.getIsversion(), info.getIsuuid(), info.getIsmodifydate(), info.getExtrainterfaces(), info.getExtrasuperclasses(), info.getDescription(),info.getTid()});
}

/**
  * Updates the current object values into the database.
  */
@Override
public boolean updateByKey(T_metatable info, Connection cn) throws SQLException
{
    String query="update " + getTableName() + " set "+
"tid=?"+","+
"tname=?"+","+
"isnode=?"+","+
"isstate=?"+","+
"isversion=?"+","+
"isuuid=?"+","+
"ismodifydate=?"+","+
"extrainterfaces=?"+","+
"extrasuperclasses=?"+","+
"description=?"+" where tid=?";
    return 1 == dbHelper.executeNoQuery(query, new Object[]{ info.getTid(), info.getTname(), info.getIsnode(), info.getIsstate(), info.getIsversion(), info.getIsuuid(), info.getIsmodifydate(), info.getExtrainterfaces(), info.getExtrasuperclasses(), info.getDescription(),info.getTid()}, cn);
}

/**
  * Deletes from the database for table "t_metatable"
  */
@Override
public boolean deleteByKey(Long tid) throws SQLException
{

    String query = "delete from " + getTableName() + " where tid=?";
    return 1 <= dbHelper.executeNoQuery(query, new Object[]{ tid});
}

/**
  * Deletes from the database for table "t_metatable"
  */
@Override
public boolean deleteByKey(Long tid, Connection cn) throws SQLException
{

    String query = "delete from " + getTableName() + " where tid=?";
    return 1 <= dbHelper.executeNoQuery(query, new Object[]{ tid}, cn);
}

/**
  * Inserts the T_metatable object values into the database.
  */
@Override
public boolean insert (T_metatable info) throws SQLException
{
    String query="insert into " + getTableName() + " ( tid,tname,isnode,isstate,isversion,isuuid,ismodifydate,extrainterfaces,extrasuperclasses,description ) values ( ?,?,?,?,?,?,?,?,?,? )";
    return 1== dbHelper.executeNoQuery(query, new Object[]{info.getTid(),info.getTname(),info.getIsnode(),info.getIsstate(),info.getIsversion(),info.getIsuuid(),info.getIsmodifydate(),info.getExtrainterfaces(),info.getExtrasuperclasses(),info.getDescription()});
}

/**
  * Inserts the T_metatable object values into the database.
  */
@Override
public boolean insert (T_metatable info, Connection cn) throws SQLException
{
    String query="insert into " + getTableName() + " ( tid,tname,isnode,isstate,isversion,isuuid,ismodifydate,extrainterfaces,extrasuperclasses,description ) values ( ?,?,?,?,?,?,?,?,?,? )";
    return 1== dbHelper.executeNoQuery(query, new Object[]{info.getTid(),info.getTname(),info.getIsnode(),info.getIsstate(),info.getIsversion(),info.getIsuuid(),info.getIsmodifydate(),info.getExtrainterfaces(),info.getExtrasuperclasses(),info.getDescription()}, cn);
}

/**
  * Inserts the T_metatable object values into the database.
  */
@Override
public boolean insertBatch (T_metatable[] infos, Connection cn) throws SQLException
{
    String query="insert into " + getTableName() + " ( tid,tname,isnode,isstate,isversion,isuuid,ismodifydate,extrainterfaces,extrasuperclasses,description ) values ( ?,?,?,?,?,?,?,?,?,? )";
    List<Object[]> a = new java.util.ArrayList<Object[]>();
    for(T_metatable info:infos){
     Object[] os = new Object[]{info.getTid(),info.getTname(),info.getIsnode(),info.getIsstate(),info.getIsversion(),info.getIsuuid(),info.getIsmodifydate(),info.getExtrainterfaces(),info.getExtrasuperclasses(),info.getDescription()};
     a.add(os);
    }
    return infos.length== dbHelper.executeNoQueryBatch(query, a, cn);
}

/**
  * Inserts the dummy record of T_metatable object values into the database.
  */
@Override
public boolean insertDummy() throws SQLException
{
     T_metatable  info = new T_metatable();
    info.setTid(getNextKey());
    return this.insert(info);
}

}
