package com.bixuebihui.generated.tablegen.pojo;
/**
  * T_metacolumn
  *
  * WARNING! Automatically generated file!
  * Do not edit the pojo and dal packages,use AutoCode / bixuebihui-smartable!
  * Code Generator by J.A.Carter
  * Modified by Xing Wanxiang 2008-2012
  * (c) www.goldjetty.com
  */

import com.bixuebihui.util.other.CMyString;

import java.io.Serializable;
public class T_metacolumn  implements Serializable
{
/**
  * Type : BIGINT Name : cid
  * cid
  */
  protected Long cid;

/**
  * Type : INT Name : tid
  * tid
  */
  protected Long tid;

/**
  * Type : VARCHAR(64) Name : cname
  * cname
  */
  protected String cname;

/**
  * Type : INT Name : type
  * type
  */
  protected Long type;

/**
  * Type : INT Name : columns
  * columns
  */
  protected Long columns;

/**
  * Type : INT Name : decimaldigits
  * decimaldigits
  */
  protected Long decimaldigits;

/**
  * Type : BIT Name : isnullable
  * isnullable
  */
  protected Boolean isnullable;

/**
  * Type : BIT Name : isauto_increment
  * isauto_increment
  */
  protected Boolean isauto_increment;

/**
  * Type : VARCHAR(1000) Name : description
  * description
  */
  protected String description;


  protected String annotation;



/**
  * Sets the value for cid cid
  */
public void setCid(Long cid)
{
  this.cid=cid;
}

/**
  * Gets the value for cid
  */
public Long getCid()
{
  return cid;
}

/**
  * Sets the value for tid tid
  */
public void setTid(Long tid)
{
  this.tid=tid;
}

/**
  * Gets the value for tid
  */
public Long getTid()
{
  return tid;
}

/**
  * Sets the value for cname cname
  */
public void setCname(String cname)
{
  this.cname=cname;
}

/**
  * Gets the value for cname
  */
public String getCname()
{
  return cname;
}

/**
  * Sets the value for type type
  */
public void setType(Long type)
{
  this.type=type;
}

/**
  * Gets the value for type
  */
public Long getType()
{
  return type;
}

/**
  * Sets the value for columns columns
  */
public void setColumns(Long columns)
{
  this.columns=columns;
}

/**
  * Gets the value for columns
  */
public Long getColumns()
{
  return columns;
}

/**
  * Sets the value for decimaldigits decimaldigits
  */
public void setDecimaldigits(Long decimaldigits)
{
  this.decimaldigits=decimaldigits;
}

/**
  * Gets the value for decimaldigits
  */
public Long getDecimaldigits()
{
  return decimaldigits;
}

/**
  * Sets the value for isnullable isnullable
  */
public void setIsnullable(Boolean isnullable)
{
  this.isnullable=isnullable;
}

/**
  * Gets the value for isnullable
  */
public Boolean getIsnullable()
{
  return isnullable;
}

/**
  * Sets the value for isauto_increment isauto_increment
  */
public void setIsauto_increment(Boolean isauto_increment)
{
  this.isauto_increment=isauto_increment;
}

/**
  * Gets the value for isauto_increment
  */
public Boolean getIsauto_increment()
{
  return isauto_increment;
}

/**
  * Sets the value for description description
  */
public void setDescription(String description)
{
  this.description=description;
}

/**
  * Gets the value for description
  */
public String getDescription()
{
  return description;
}

 public T_metacolumn()
 	{
      cid=Long.valueOf(0);
      tid=Long.valueOf(0);
      cname="*";
      type=Long.valueOf(0);
      columns=Long.valueOf(0);
      decimaldigits=Long.valueOf(0);
      description="";
 	}
 public String toXml()
 	{
 	 StringBuilder s= new StringBuilder();
 	 String ln = System.getProperty("line.separator");
 	 s.append("<T_METACOLUMN ");
     s.append("CID=\"").append(this.getCid()).append("\" ");
     s.append("TID=\"").append(this.getTid()).append("\" ");
     s.append("CNAME=\"").append(CMyString.filterForXML(this.getCname())).append("\" ");
     s.append("TYPE=\"").append(this.getType()).append("\" ");
     s.append("COLUMNS=\"").append(this.getColumns()).append("\" ");
     s.append("DECIMALDIGITS=\"").append(this.getDecimaldigits()).append("\" ");
     s.append("ISNULLABLE=\"").append(this.getIsnullable()).append("\" ");
     s.append("ISAUTO_INCREMENT=\"").append(this.getIsauto_increment()).append("\" ");
     s.append("ANNOTATION=\"").append(CMyString.filterForXML(this.getAnnotation())).append("\" ");
     s.append("DESCRIPTION=\"").append(CMyString.filterForXML(this.getDescription())).append("\" ");
     s.append(" />");
     s.append(ln);
    return s.toString();
 	}

public String getAnnotation() {
	return annotation;
}

public void setAnnotation(String annotation) {
	this.annotation = annotation;
}


}
