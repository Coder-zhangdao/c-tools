package com.bixuebihui.tablegen;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.bixuebihui.generated.tablegen.pojo.T_metacolumn;
import com.bixuebihui.generated.tablegen.pojo.T_metatable;

public class PojoPropertiesParser {

	public static Map<String, T_metatable> parse(String xml) {
		try {
			Map<String, T_metatable> res = new Hashtable<String, T_metatable>();
			Document document = DocumentHelper.parseText(xml);
			Element nodesElement = document.getRootElement(); // pojos
			List nodes = nodesElement.elements();
			for (Iterator its = nodes.iterator(); its.hasNext();) {
				Element nodeElement = (Element) its.next(); // pojo
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
			nodes = null;
			nodesElement = null;
			document = null;
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Map<String, T_metacolumn> getProperties(List<Element> properties) {
		try {
			Map<String, T_metacolumn> res = new Hashtable<String, T_metacolumn>();

			for (Iterator<Element> its = properties.iterator(); its.hasNext();) {
				Element nodeElement = its.next(); // property
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
		if(to==null)return from;
		if(from!=null)
		for(Map.Entry<String, T_metatable> entry:from.entrySet()){
			if(to.containsKey(entry.getKey())){
				T_metatable t_from = entry.getValue();
				T_metatable t_to = to.get(entry.getKey());

				if(StringUtils.isNotEmpty(t_from.getClassname()))
					t_to.setClassname(t_from.getClassname());

				if(StringUtils.isNotEmpty(t_from.getDescription()))
					t_to.setDescription(t_from.getDescription());

				if(StringUtils.isNotEmpty(t_from.getExtrainterfaces()))
					t_to.setExtrainterfaces(t_from.getExtrainterfaces());

				if(StringUtils.isNotEmpty(t_from.getExtrasuperclasses()))
					t_to.setExtrasuperclasses(t_from.getExtrasuperclasses());


				t_to.setColumns( mergeColumns(t_to.getColumns(), t_from.getColumns()));

				//BeanUtils.copyProperties(t_from, t_to);
			}
			else{
				to.put(entry.getKey(), entry.getValue());
			}
		}
		return to;

	}

	private static Map<String, T_metacolumn> mergeColumns(
			Map<String, T_metacolumn> to,
			Map<String, T_metacolumn> from) {

		if(to==null)return from;
		if(from!=null)
		for(Map.Entry<String, T_metacolumn> entry:from.entrySet()){
			if(to.containsKey(entry.getKey())){
				T_metacolumn t_from = entry.getValue();
				T_metacolumn t_to = to.get(entry.getKey());

				if(StringUtils.isNotEmpty(t_from.getAnnotation()))
					t_to.setAnnotation(t_from.getAnnotation());

				if(StringUtils.isNotEmpty(t_from.getDescription()))
					t_to.setDescription(t_from.getDescription());

			}
			else{
				to.put(entry.getKey(), entry.getValue());
			}
		}
		return to;
	}

}
