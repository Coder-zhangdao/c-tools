package com.bixuebihui.generated.tablegen.dal;
/**
  * T_metacolumn
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
import com.bixuebihui.generated.tablegen.pojo.T_metacolumn;

public class T_metacolumnList  extends BaseList<T_metacolumn,Long>
{
/**
  * Don't direct use the T_metacolumnListList, use T_metacolumnListManager instead.
  */
protected T_metacolumnList()
{
}

/**
  * Get table name / key name.
  */
public String getTableName()
{
    return "t_metacolumn";
}

/**
  * Get table name / key name.
  */
@Override
public String getKeyName()
{
    return "cid";
}

/**
  * Updates the object from a selected ResultSet.
  */
public T_metacolumn mapRow (ResultSet r, int index) throws SQLException
{
      T_metacolumn res = new T_metacolumn();
      res.setCid(r.getLong("cid"));
      res.setTid(r.getLong("tid"));
      res.setCname(r.getString("cname"));
      res.setType(r.getLong("type"));
      res.setColumns(r.getLong("columns"));
      res.setDecimaldigits(r.getLong("decimaldigits"));
      res.setIsnullable(r.getBoolean("isnullable"));
      res.setIsauto_increment(r.getBoolean("isauto_increment"));
      res.setDescription(r.getString("description"));
      return res;
}

public Long getId(T_metacolumn info) {
	return  info.getCid();
}


public void setId(T_metacolumn info, Long id) {
	info.setCid(id);
}




/**
  * Updates the current object values into the database.
  */
@Override
public boolean updateByKey(T_metacolumn info) throws SQLException
{
    String query="update " + getTableName() + " set "+
"cid=?"+","+
"tid=?"+","+
"cname=?"+","+
"type=?"+","+
"columns=?"+","+
"decimaldigits=?"+","+
"isnullable=?"+","+
"isauto_increment=?"+","+
"description=?"+" where cid=?";
    return 1 == dbHelper.executeNoQuery(query, new Object[]{ info.getCid(), info.getTid(), info.getCname(), info.getType(), info.getColumns(), info.getDecimaldigits(), info.getIsnullable(), info.getIsauto_increment(), info.getDescription(),info.getCid()});
}

/**
  * Updates the current object values into the database.
  */
@Override
public boolean updateByKey(T_metacolumn info, Connection cn) throws SQLException
{
    String query="update " + getTableName() + " set "+
"cid=?"+","+
"tid=?"+","+
"cname=?"+","+
"type=?"+","+
"columns=?"+","+
"decimaldigits=?"+","+
"isnullable=?"+","+
"isauto_increment=?"+","+
"description=?"+" where cid=?";
    return 1 == dbHelper.executeNoQuery(query, new Object[]{ info.getCid(), info.getTid(), info.getCname(), info.getType(), info.getColumns(), info.getDecimaldigits(), info.getIsnullable(), info.getIsauto_increment(), info.getDescription(),info.getCid()}, cn);
}

/**
  * Deletes from the database for table "t_metacolumn"
  */
@Override
public boolean deleteByKey(Long cid) throws SQLException
{

    String query = "delete from " + getTableName() + " where cid=?";
    return 1 <= dbHelper.executeNoQuery(query, new Object[]{ cid});
}

/**
  * Deletes from the database for table "t_metacolumn"
  */
@Override
public boolean deleteByKey(Long cid, Connection cn) throws SQLException
{

    String query = "delete from " + getTableName() + " where cid=?";
    return 1 <= dbHelper.executeNoQuery(query, new Object[]{ cid}, cn);
}

/**
  * Inserts the T_metacolumn object values into the database.
  */
@Override
public boolean insert (T_metacolumn info) throws SQLException
{
    String query="insert into " + getTableName() + " ( cid,tid,cname,type,columns,decimaldigits,isnullable,isauto_increment,description ) values ( ?,?,?,?,?,?,?,?,? )";
    return 1== dbHelper.executeNoQuery(query, new Object[]{info.getCid(),info.getTid(),info.getCname(),info.getType(),info.getColumns(),info.getDecimaldigits(),info.getIsnullable(),info.getIsauto_increment(),info.getDescription()});
}

/**
  * Inserts the T_metacolumn object values into the database.
  */
@Override
public boolean insert (T_metacolumn info, Connection cn) throws SQLException
{
    String query="insert into " + getTableName() + " ( cid,tid,cname,type,columns,decimaldigits,isnullable,isauto_increment,description ) values ( ?,?,?,?,?,?,?,?,? )";
    return 1== dbHelper.executeNoQuery(query, new Object[]{info.getCid(),info.getTid(),info.getCname(),info.getType(),info.getColumns(),info.getDecimaldigits(),info.getIsnullable(),info.getIsauto_increment(),info.getDescription()}, cn);
}

/**
  * Inserts the T_metacolumn object values into the database.
  */
@Override
public boolean insertBatch (T_metacolumn[] infos, Connection cn) throws SQLException
{
    String query="insert into " + getTableName() + " ( cid,tid,cname,type,columns,decimaldigits,isnullable,isauto_increment,description ) values ( ?,?,?,?,?,?,?,?,? )";
    List<Object[]> a = new java.util.ArrayList<Object[]>();
    for(T_metacolumn info:infos){
     Object[] os = new Object[]{info.getCid(),info.getTid(),info.getCname(),info.getType(),info.getColumns(),info.getDecimaldigits(),info.getIsnullable(),info.getIsauto_increment(),info.getDescription()};
     a.add(os);
    }
    return infos.length== dbHelper.executeNoQueryBatch(query, a, cn);
}

/**
  * Inserts the dummy record of T_metacolumn object values into the database.
  */
public boolean insertDummy() throws SQLException
{
     T_metacolumn  info = new T_metacolumn();
     java.util.Random rnd = new java.util.Random();
    info.setCname(Integer.toString(Math.abs(rnd.nextInt(Integer.MAX_VALUE)), 36));
    info.setCid(getNextKey());
    return this.insert(info);
}

}
