package com.bixuebihui.generated.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author xwx
 */
public class XmlUtils {
	public static Map<String, String> xmltoMap(String xml, boolean useAttribute) {
	        try {
	            Map<String, String> map = new HashMap<String, String>();
	            Document document = DocumentHelper.parseText(xml);
	            Element nodeElement = document.getRootElement();
	            List node = nodeElement.elements();
	            for (Iterator it = node.iterator(); it.hasNext();) {
	                Element elm = (Element) it.next();
	                if(useAttribute){
	                	map.put(elm.attributeValue("label"), elm.getText());
	                }else{
	                	map.put(elm.getName(), elm.getData().toString());
	                }
				}
				return map;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	public static Map<String, String> xmltoMap(String xml, String subNodeName, boolean useAttribute) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            Document document = DocumentHelper.parseText(xml);
            Element nodeElement = document.getRootElement();
            Element subNodeElement = getSubNode(subNodeName, nodeElement);
            List node = subNodeElement.elements();
            for (Iterator it = node.iterator(); it.hasNext();) {
                Element elm = (Element) it.next();
                if(useAttribute){
                	map.put(elm.attributeValue("label"), elm.getText());
                }else{
                	map.put(elm.getName(), elm.getData().toString());
                }
			}
			return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	    /**
	     * xml to list xml <nodes><node><key label="key1">value1</key><key
	     * label="key2">value2</key>......</node><node><key
	     * label="key1">value1</key><key
	     * label="key2">value2</key>......</node></nodes>
	     *
	     * @param xml
	     * @return
	     */
	    public static List xmltoList(String xml) {
	        try {
	            List<Map> list = new ArrayList<Map>();
	            Document document = DocumentHelper.parseText(xml);
	            Element nodesElement = document.getRootElement();
	            List nodes = nodesElement.elements();
	            for (Iterator its = nodes.iterator(); its.hasNext();) {
	                Element nodeElement = (Element) its.next();
	                Map map = xmltoMap(nodeElement.asXML(), true);
	                list.add(map);
				}
				return list;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	    /**
	     * xml to list xml <nodes><node><key label="key1">value1</key><key
	     * label="key2">value2</key>......</node><node><key
	     * label="key1">value1</key><key
	     * label="key2">value2</key>......</node></nodes>
	     *
	     * @param xml
	     * @return
	     */
	    public static List<Map<String, String>> xmltoList(String xml, String subNodeName, boolean useAttribute) {
	        try {
	            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	            Document document = DocumentHelper.parseText(xml);
	            Element nodesElement = document.getRootElement();

	            Element subNodeElement = getSubNode(subNodeName, nodesElement);
	            if(subNodeElement==null)
	            	return null;

	            List nodes = subNodeElement.elements();
	            for (Iterator its = nodes.iterator(); its.hasNext();) {
	                Element nodeElement = (Element) its.next();
	                Map<String, String> map = xmltoMap(nodeElement.asXML(), useAttribute);
	                list.add(map);
				}
				return list;
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.err.println(xml);
	        }
	        return null;
	    }

		private static Element getSubNode(String subNodeName,
				Element nodesElement) {
			Element subNodeElement =null;
			if(subNodeName.indexOf('/')<0)
				subNodeElement = nodesElement.element(subNodeName);
			else{
				String[] paths = subNodeName.split("\\/");
				subNodeElement = nodesElement;
				for(String p:paths){
					subNodeElement = subNodeElement.element(p);
					if(subNodeElement==null) break;
				}
			}
			return subNodeElement;
		}
}

