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


@ApiModel(description = "测试表")
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
  * Type : SMALLINT Name : age: 这里是年龄
  * age
  */
  @ApiModelProperty(value = "这里是年龄")
  protected Short age;

/**
  * Type : TIMESTAMP Name : birth: 这里是日期！
  * birth
  */
  @ApiModelProperty(value = "这里是日期！")
  protected Timestamp birth;

/**
  * Type : INT Name : edu_id: 教育程度
  * edu_id
  */
  @ApiModelProperty(value = "教育程度")
  protected Long eduId;

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

/**
  * Sets the value for age age
  */
public void setAge(Short age)
{
  this.age=age;
}

/**
  * Gets the value for age
  */
public Short getAge()
{
  return age;
}

/**
  * Sets the value for birth birth
  */
public void setBirth(Timestamp birth)
{
  this.birth=birth;
}

/**
  * Gets the value for birth
  */
public Timestamp getBirth()
{
  return birth==null ? null: new Timestamp(birth.getTime());
}

/**
  * Sets the value for eduId edu_id
  */
public void setEduId(Long eduId)
{
  this.eduId=eduId;
}

/**
  * Gets the value for eduId
  */
public Long getEduId()
{
  return eduId;
}

 public TestGen()
     {
      id=0L;
      name="";
      age=0;
      birth=new Timestamp(new java.util.Date().getTime());
      eduId=0L;
     }
 public String toXml()
     {
      StringBuilder s= new StringBuilder();
      String ln = System.getProperty("line.separator");
      s.append("<test_gen ");
     s.append("id=\"").append(this.getId()).append("\" ");
     s.append("name=\"").append(StringEscapeUtils.escapeXml11(this.getName())).append("\" ");
     s.append("age=\"").append(this.getAge()).append("\" ");
     s.append("birth=\"").append(this.getBirth()).append("\" ");
     s.append("eduId=\"").append(this.getEduId()).append("\" ");
     s.append(" />");
     s.append(ln);
    return s.toString();
     }
}
