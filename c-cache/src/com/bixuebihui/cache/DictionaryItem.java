package com.bixuebihui.cache;

import com.bixuebihui.util.other.CMyString;

public class DictionaryItem {

	public String getMs_id() {
		return ms_id;
	}
	public void setMs_id(String ms_id) {
		this.ms_id = ms_id;
	}
	public String getMs_value() {
		return ms_value;
	}
	public void setMs_value(String ms_value) {
		this.ms_value = ms_value;
	}
	public String getMs_sort() {
		return ms_sort;
	}
	public void setMs_sort(String ms_sort) {
		this.ms_sort = ms_sort;
	}
	private String ms_id;
	private String ms_value;
	private String ms_sort;

	public String toXml(){
		 StringBuffer s= new StringBuffer();
	 	 String ln = System.getProperty("line.separator");
	 	 s.append("<DICTIONARYITEM ");
	     s.append("MS_ID=\"").append(this.getMs_id()).append("\" ");
	     s.append("MS_VALUE=\"").append(CMyString.filterForXML(this.getMs_value())).append("\" ");
	     s.append("MS_SORT=\"").append(this.getMs_sort()).append("\" ");
	     s.append(" />");
	     s.append(ln);
	    return s.toString();
	}

	@Override
    public String toString(){
		return "{"+ms_id+","+ms_value+","+ms_sort+"}";
	}
}
