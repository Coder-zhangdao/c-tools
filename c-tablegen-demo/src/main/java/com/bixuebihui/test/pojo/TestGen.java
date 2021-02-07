package com.bixuebihui.test.pojo;
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
import java.io.Serializable;
import javax.validation.constraints.*;
import org.apache.commons.text.StringEscapeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(value = "测试表")
public class TestGen  implements Serializable
{
/**
  * Type : INT Name : id: 这里是id!
  * id
  */
  @ApiModelProperty(value = "这里是id!")
  protected Long id;

/**
  * Type : VARCHAR(100) Name : name: 这里是名称！
  * name
  */
  @Size(max=100)
  @ApiModelProperty(value = "这里是名称！")
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

 public TestGen()
     {
      id=0L;
      name="";
     }
 public String toXml()
     {
      StringBuilder s= new StringBuilder();
      String ln = System.getProperty("line.separator");
      s.append("<test_gen ");
     s.append("id=\"").append(this.getId()).append("\" ");
     s.append("name=\"").append(StringEscapeUtils.escapeXml11(this.getName())).append("\" ");
     s.append(" />");
     s.append(ln);
    return s.toString();
     }
}
