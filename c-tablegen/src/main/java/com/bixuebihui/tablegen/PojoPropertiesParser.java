package com.bixuebihui.tablegen;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.bixuebihui.generated.tablegen.pojo.T_metacolumn;
import com.bixuebihui.generated.tablegen.pojo.T_metatable;

/**
 * @author xwx
 */
public class PojoPropertiesParser {

	public static Map<String, T_metatable> parse(String xml) {
		try {
			Map<String, T_metatable> res = new Hashtable<>();
			Document document = DocumentHelper.parseText(xml);
			// pojos
			Element nodesElement = document.getRootElement();
			List<Element> nodes = nodesElement.elements();
			for (Element nodeElement : nodes) {
				// pojo
				T_metatable t = new T_metatable();

				String tableName = nodeElement.attributeValue("table-name");
				if (StringUtils.isNotEmpty(tableName)) {
					t.setTname(tableName);
					t.setExtrainterfaces(nodeElement
							.attributeValue("implements"));
					t.setExtrasuperclasses(nodeElement
							.attributeValue("extends"));
					t.setClassname(nodeElement.attributeValue("name"));

					Map<String, T_metacolumn> map = getProperties(
							nodeElement.element("properties").elements());

					t.setColumns(map);
					res.put(tableName, t);
				}

			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Map<String, T_metacolumn> getProperties(List<Element> properties) {
		try {
			Map<String, T_metacolumn> res = new Hashtable<>();

			for (Element nodeElement : properties) {
				// property
				T_metacolumn t = new T_metacolumn();

				String columnName = nodeElement.attributeValue("name");
				if (StringUtils.isNotEmpty(columnName)) {
					t.setCname(columnName);
					t.setAnnotation(nodeElement.elementText("annotation"));
					t.setDescription(nodeElement.elementText("comment"));

					res.put(columnName, t);
				}

			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public static Map<String, T_metatable> mergeTableSetting(Map<String, T_metatable> to,
			Map<String, T_metatable> from) {
		if(to==null) {
			return from;
		}
		if(from!=null) {
			for(Map.Entry<String, T_metatable> entry:from.entrySet()){
				if(to.containsKey(entry.getKey())){
					T_metatable tableFrom = entry.getValue();
					T_metatable tableTo = to.get(entry.getKey());

					if(StringUtils.isNotEmpty(tableFrom.getClassname())) {
						tableTo.setClassname(tableFrom.getClassname());
					}

					if(StringUtils.isNotEmpty(tableFrom.getDescription())) {
						tableTo.setDescription(tableFrom.getDescription());
					}

					if(StringUtils.isNotEmpty(tableFrom.getExtrainterfaces())) {
						tableTo.setExtrainterfaces(tableFrom.getExtrainterfaces());
					}

					if(StringUtils.isNotEmpty(tableFrom.getExtrasuperclasses())) {
						tableTo.setExtrasuperclasses(tableFrom.getExtrasuperclasses());
					}


					tableTo.setColumns( mergeColumns(tableTo.getColumns(), tableFrom.getColumns()));

				}
				else{
					to.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return to;

	}

	private static Map<String, T_metacolumn> mergeColumns(
			Map<String, T_metacolumn> to,
			Map<String, T_metacolumn> from) {

		if(to==null) {
			return from;
		}
		if(from!=null) {
			for(Map.Entry<String, T_metacolumn> entry:from.entrySet()){
				if(to.containsKey(entry.getKey())){
					T_metacolumn tableFrom = entry.getValue();
					T_metacolumn tableTo = to.get(entry.getKey());

					if(StringUtils.isNotEmpty(tableFrom.getAnnotation())) {
						tableTo.setAnnotation(tableFrom.getAnnotation());
					}

					if(StringUtils.isNotEmpty(tableFrom.getDescription())) {
						tableTo.setDescription(tableFrom.getDescription());
					}

				}
				else{
					to.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return to;
	}

}
