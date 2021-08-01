package com.bixuebihui.test.pojo;

/*
 *  TestGen
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
import javax.annotation.processing.Generated;

@Generated("com.github.yujiaao:tablegen")
@ApiModel(description ="测试表")
public class TestGen  implements Serializable {



    /**
    * id
    */
        @ApiModelProperty(value = "这里是id!")
    protected Integer  id;


    /**
    * name
    */
        @Size(max=100)
    @ApiModelProperty(value = "这里是名称！")
    protected String  name;


    /**
    * age
    */
        @ApiModelProperty(value = "这里是年龄")
    protected Short  age;


    /**
    * birth
    */
        @ApiModelProperty(value = "这里是日期！")
    protected Timestamp  birth;


    /**
    * edu_id
    */
        @ApiModelProperty(value = "教育程度")
    protected Integer  eduId;




    public void setId(Integer id)
    {
      this.id = id;
    }
    public TestGen()
    {

            id=0;

            name="";

            age=0;

            birth=new Timestamp(System.currentTimeMillis());

            eduId=0;

    }

    public void setName(String name)
    {
      this.name = name;
    }

    public Integer getId()
    {
      return this.id;
    }

    public void setAge(Short age)
    {
      this.age = age;
    }

    public String getName()
    {
      return this.name;
    }

    public void setBirth(Timestamp birth)
    {
      this.birth = birth;
    }

    public Short getAge()
    {
      return this.age;
    }

    public void setEduId(Integer eduId)
    {
      this.eduId = eduId;
    }

    public Timestamp getBirth()
    {
      return this.birth;
    }

    public Integer getEduId()
    {
      return this.eduId;
    }

    public String toXml()
    {
        StringBuilder s= new StringBuilder();
        String ln = System.getProperty("line.separator");
        s.append("<test_gen ");

        s.append(" id=\"").append(this.getId()).append("\"");

        s.append(" name=\"").append(StringEscapeUtils.escapeXml11(this.getName())).append("\"");
        s.append(" age=\"").append(this.getAge()).append("\"");

        s.append(" birth=\"").append(this.getBirth()).append("\"");

        s.append(" eduId=\"").append(this.getEduId()).append("\"");
        s.append(" />");
        s.append(ln);
        return s.toString();
    }
}
