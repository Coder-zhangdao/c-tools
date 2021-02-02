package com.bixuebihui.test.pojo;
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
import java.io.Serializable;
import javax.validation.constraints.*;
import org.apache.commons.text.StringEscapeUtils;
public class Test  implements Serializable
{
/**
  * Type : INT Name : id
  * id
  */
  @NotNull
  protected Long id;

/**
  * Type : VARCHAR(100) Name : name
  * name
  */
  @Size(max=100)
  protected String name;

/**
  * Sets the value for id id
  */
public void setId(Long id)
{
  this.id=id;
}

/**
  * Gets the value for id
  */
public Long getId()
{
  return id;
}

/**
  * Sets the value for name name
  */
public void setName(String name)
{
  this.name=name;
}

/**
  * Gets the value for name
  */
public String getName()
{
  return name;
}

 public Test()
     {
      id=0L;
      name="";
     }
 public String toXml()
     {
      StringBuilder s= new StringBuilder();
      String ln = System.getProperty("line.separator");
      s.append("<test ");
     s.append("id=\"").append(this.getId()).append("\" ");
     s.append("name=\"").append(StringEscapeUtils.escapeXml11(this.getName())).append("\" ");
     s.append(" />");
     s.append(ln);
    return s.toString();
     }
}
