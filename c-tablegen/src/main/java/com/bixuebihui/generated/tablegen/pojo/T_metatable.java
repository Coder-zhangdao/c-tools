package com.bixuebihui.generated.tablegen.pojo;
/**
  * T_metatable
  *
  * WARNING! Automatically generated file!
  * Do not edit the pojo and dal packages,use AutoCode / bixuebihui-smartable!
  * Code Generator by J.A.Carter
  * Modified by Xing Wanxiang 2008-2012
  * (c) www.goldjetty.com
  */

import com.bixuebihui.util.other.CMyString;

import java.util.Map;
import java.io.Serializable;
public class T_metatable  implements Serializable
{
/**
  * Type : BIGINT Name : tid
  * tid
  */
  protected Long tid;

/**
  * Type : VARCHAR(64) Name : tname
  * tname
  */
  protected String tname;

/**
  * Type : BIT Name : isnode
  * isnode
  */
  protected Boolean isnode;

/**
  * Type : BIT Name : isstate
  * isstate
  */
  protected Boolean isstate;

/**
  * Type : BIT Name : isversion
  * isversion
  */
  protected Boolean isversion;

/**
  * Type : BIT Name : isuuid
  * isuuid
  */
  protected Boolean isuuid;

/**
  * Type : BIT Name : ismodifydate
  * ismodifydate
  */
  protected Boolean ismodifydate;

/**
  * Type : VARCHAR(1000) Name : extrainterfaces
  * extrainterfaces
  */
  protected String extrainterfaces;

/**
  * Type : VARCHAR(1000) Name : extrasuperclasses
  * extrasuperclasses
  */
  protected String extrasuperclasses;

/**
  * Type : VARCHAR(1000) Name : description
  * description
  */
  protected String description;


  protected String classname;


  private Map<String, T_metacolumn> columns;


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
  * Sets the value for tname tname
  */
public void setTname(String tname)
{
  this.tname=tname;
}

/**
  * Gets the value for tname
  */
public String getTname()
{
  return tname;
}

/**
  * Sets the value for isnode isnode
  */
public void setIsnode(Boolean isnode)
{
  this.isnode=isnode;
}

/**
  * Gets the value for isnode
  */
public Boolean getIsnode()
{
  return isnode;
}

/**
  * Sets the value for isstate isstate
  */
public void setIsstate(Boolean isstate)
{
  this.isstate=isstate;
}

/**
  * Gets the value for isstate
  */
public Boolean getIsstate()
{
  return isstate;
}

/**
  * Sets the value for isversion isversion
  */
public void setIsversion(Boolean isversion)
{
  this.isversion=isversion;
}

/**
  * Gets the value for isversion
  */
public Boolean getIsversion()
{
  return isversion;
}

/**
  * Sets the value for isuuid isuuid
  */
public void setIsuuid(Boolean isuuid)
{
  this.isuuid=isuuid;
}

/**
  * Gets the value for isuuid
  */
public Boolean getIsuuid()
{
  return isuuid;
}

/**
  * Sets the value for ismodifydate ismodifydate
  */
public void setIsmodifydate(Boolean ismodifydate)
{
  this.ismodifydate=ismodifydate;
}

/**
  * Gets the value for ismodifydate
  */
public Boolean getIsmodifydate()
{
  return ismodifydate;
}

/**
  * Sets the value for extrainterfaces extrainterfaces
  */
public void setExtrainterfaces(String extrainterfaces)
{
  this.extrainterfaces=extrainterfaces;
}

/**
  * Gets the value for extrainterfaces
  */
public String getExtrainterfaces()
{
  return extrainterfaces;
}

/**
  * Sets the value for extrasuperclasses extrasuperclasses
  */
public void setExtrasuperclasses(String extrasuperclasses)
{
  this.extrasuperclasses=extrasuperclasses;
}

/**
  * Gets the value for extrasuperclasses
  */
public String getExtrasuperclasses()
{
  return extrasuperclasses;
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

 public T_metatable()
 	{
      tid=Long.valueOf(0);
      this.ismodifydate=Boolean.FALSE;
      this.isnode = Boolean.FALSE;
      this.isstate = Boolean.FALSE;
      this.isuuid = Boolean.FALSE;
      this.isversion = Boolean.FALSE;

      tname="";
      extrainterfaces="";
      extrasuperclasses="";
      description="";
 	}
 public String toXml()
 	{
 	 StringBuilder s= new StringBuilder();
 	 String ln = System.getProperty("line.separator");
 	 s.append("<T_METATABLE ");
     s.append("TID=\"").append(this.getTid()).append("\" ");
     s.append("CLASSNAME=\"").append(CMyString.filterForXML(this.getClassname())).append("\" ");
     s.append("TNAME=\"").append(CMyString.filterForXML(this.getTname())).append("\" ");
     s.append("ISNODE=\"").append(this.getIsnode()).append("\" ");
     s.append("ISSTATE=\"").append(this.getIsstate()).append("\" ");
     s.append("ISVERSION=\"").append(this.getIsversion()).append("\" ");
     s.append("ISUUID=\"").append(this.getIsuuid()).append("\" ");
     s.append("ISMODIFYDATE=\"").append(this.getIsmodifydate()).append("\" ");
     s.append("EXTRAINTERFACES=\"").append(CMyString.filterForXML(this.getExtrainterfaces())).append("\" ");
     s.append("EXTRASUPERCLASSES=\"").append(CMyString.filterForXML(this.getExtrasuperclasses())).append("\" ");
     s.append("DESCRIPTION=\"").append(CMyString.filterForXML(this.getDescription())).append("\" ");
     s.append(">").append(ln);
     if(columns!=null) {
         for(T_metacolumn col:columns.values()){
             s.append(col.toXml()).append(ln);
         }
     }
     s.append("</T_METATABLE>");
     s.append(ln);
    return s.toString();
 	}
 @Override
 public String toString(){
	 return this.toXml();
 }

public Map<String, T_metacolumn> getColumns() {
	return columns;
}

public void setColumns(Map<String, T_metacolumn> columns) {
	this.columns = columns;
}

public String getClassname() {
	return classname;
}

public void setClassname(String classname) {
	this.classname = classname;
}
}
