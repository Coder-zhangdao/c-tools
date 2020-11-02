package com.bixuebihui.generated.tablegen.business;
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

import org.junit.Assert;
import org.junit.Test;
import com.bixuebihui.generated.tablegen.pojo.T_metacolumn;

public class T_metacolumnManagerTest {
/**
  * Select from the database for table "t_metacolumn"
 */
@Test
public void testSelect() throws SQLException
{
  T_metacolumnManager man = new T_metacolumnManager();
    List<T_metacolumn> list = man.select("","", 0, 10);
    for(T_metacolumn info: list){
     System.out.println(info.toXml());
    }
}

@Test
public void testCount() throws SQLException
{
  T_metacolumnManager man = new T_metacolumnManager();
    Assert.assertTrue(man.count("") >= 0);
}

@Test
public void testInsertdummy() throws SQLException
{
  T_metacolumnManager man = new T_metacolumnManager();
    Assert.assertTrue(man.insertDummy());
}

}
