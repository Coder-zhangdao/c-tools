package com.bixuebihui.cache;

import com.bixuebihui.jdbc.IBaseListService;
import com.bixuebihui.util.other.CMyString;

public class DictionaryDefine {

	private String addCondition="";

	private String conditionValue;

	private String id_name="MS_ID";
	private String value_name="MS_VALUE";
	private String sort_name="MS_SORT";
	private IBaseListService<DictionaryItem, String> serviceClass;
	private int maxCapacity=5000;

	public String getId_name() {
		return id_name;
	}

	public void setId_name(String id_name) {
		this.id_name = id_name;
	}

	public String getValue_name() {
		return value_name;
	}

	public void setValue_name(String value_name) {
		this.value_name = value_name;
	}



	public String getSort_name() {
		return sort_name;
	}

	public void setSort_name(String sort_name) {
		this.sort_name = sort_name;
	}

	private String tableName;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public DictionaryDefine(String tableName, String idName, String valueName, String sortName, String addCondition){
		this.tableName = tableName;
		this.id_name = idName;
		this.value_name = valueName;
		this.sort_name = sortName;
		this.addCondition = addCondition;
	}
	public DictionaryDefine(String tableName){
		this.tableName = tableName;
	}
	public DictionaryDefine(){
	}

	protected String getAddCondition() {
		return addCondition;
	}

	public void setAddCondition(String addCondition) {
		this.addCondition = addCondition;
	}

	public String getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}

	public String getSqlCondition(){
		if(this.conditionValue!=null){
			//return	MessageFormat.format( this.getAddCondition(), this.conditionValue);
			return CMyString.replaceStr(this.getAddCondition(), "{0}", this.conditionValue);
		}
		return this.getAddCondition();
	}

	public IBaseListService<DictionaryItem, String> getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(IBaseListService<DictionaryItem, String> serviceClass) {
		this.serviceClass = serviceClass;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(int  maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
}
