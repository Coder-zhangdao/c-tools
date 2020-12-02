package com.bixuebihui.jmesa;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmesa.worksheet.WorksheetColumn;

import org.junit.jupiter.api.Test;

public class AbstractWebUITest{

	@Test
	public void testConvert() {
		AbstractWebUI<?, ?> ui = new AbstractWebUI<Object, Object>(){

			protected String getUniquePropertyName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
            protected String render(HttpServletRequest request,
                                    HttpServletResponse response) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected void validateColumn(WorksheetColumn worksheetColumn,
										  String changedValue) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String[] getColNames() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected Object[] getKeys(HttpServletRequest request) {
				// TODO Auto-generated method stub
				return null;
			}};

			Class clazz = java.sql.Timestamp.class;
			String value="2010-11-30 00:00:00.0";
			Object v = ui.convert(value, clazz);
			System.out.println(v);

			 clazz = java.sql.Timestamp.class;
			 value="2010-11-30 12:00:00";
			 v = ui.convert(value, clazz);
			System.out.println(v);

			 clazz = java.sql.Date.class;
			 value="2010-11-30";
			 v = ui.convert(value, clazz);
			System.out.println(v);
	}

}
