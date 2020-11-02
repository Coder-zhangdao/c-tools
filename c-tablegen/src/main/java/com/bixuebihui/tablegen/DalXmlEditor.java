package com.bixuebihui.tablegen;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * @author xwx
 */
public class DalXmlEditor {
	static final Log log = LogFactory.getLog(DalXmlEditor.class);

	/**
	 * 对spring beans配置文件增加business/xxxManager的定义
	 * @param xml　准备修改的文件名
	 * @param beans　key为bean的id,value为full.class.name
	 * @return
	 */
	public static Document  addBeans(String xml, Map<String, String> beans) {
		try {

			Document document = DocumentHelper.parseText(xml);

			// beans
			Element nodesElement = document.getRootElement();

			List<?> beanNodes = nodesElement.elements();

			List<String> ids= new ArrayList<>();

			for(Object el :beanNodes){
				ids.add(((Element)el).attribute("id").getValue());
			}

			for (Iterator<String> its = beans.keySet().iterator(); its.hasNext();) {
				String key = its.next();
				if(!ids.contains(key)){
					Element el = nodesElement.addElement("bean");
					el.addAttribute("id", key);
					el.addAttribute("class", beans.get(key));
				}
			}
			return document;

		} catch (Exception e) {
		    log.error(e);
		}
		return null;
	}

	public static void write(Document document, String xmlFileName) throws IOException {

        // lets write to a file
		OutputFormat format = OutputFormat.createCompactFormat();
        format.setEncoding(TableGenConfig.FILE_ENCODING);

        XMLWriter writer = new XMLWriter(
        		new FileOutputStream(xmlFileName),format
        );
        writer.write( document );
        writer.close();

        // Compact format to System.out
        if(log.isDebugEnabled()){
	        format = OutputFormat.createPrettyPrint();
	        writer = new XMLWriter( System.out, format );
	        writer.write( document );
	        writer.close();
        }
    }
}
