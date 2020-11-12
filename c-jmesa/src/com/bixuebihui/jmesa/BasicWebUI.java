package com.bixuebihui.jmesa;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmesa.core.preference.Preferences;
import org.jmesa.model.ExportTypes;
import org.jmesa.model.TableModel;
import org.jmesa.model.TableModelUtils;
import org.jmesa.view.component.Column;
import org.jmesa.view.component.Row;
import org.jmesa.view.component.Table;

import com.bixuebihui.jdbc.IBaseListService;
import com.bixuebihui.util.ParameterUtils;

/**
 * @author xwx
 */
public class BasicWebUI extends AbstractWebUI<Object, Long> {

	private String uniquePropertyName;

	/**
	 * @see   SimpleView
	 */
	private boolean useSimpleView = false;

	/**
	 * 是否使用日期区间控件，如使用则每个控件增加两个参数：起始和截止日期，与coreSql里的参数相对应
	 * 参数需按顺序放在最后
	 * // every dateRange increase the params by 2
	 */
	private int useDateRange =0;

	protected int maxRows=0;
	protected int[] maxRowsIncrements=new int[]{maxRows};

	@Override
	protected String getUniquePropertyName() {
		return uniquePropertyName;
	}

	/**
	 * comma separated string
 	 */
	private String colsList;

	@Override
	protected String render(HttpServletRequest request,
			HttpServletResponse response) {

		TableModel tableFacade = new TableModel(id, request, response);

		tableFacade.setExportTypes(ExportTypes.CSV,ExportTypes.EXCEL);

		Preferences preferences = null;
		tableFacade.setPreferences(preferences);

		if(maxRows>0){
			tableFacade.setMaxRows(maxRows);
			tableFacade.setMaxRowsIncrements(maxRowsIncrements);
		}

		// exports to use.
		String[] colNames = getColNames();
		Table table = tableFacade.isExporting()? TableModelUtils.createTable(colNames):TableModelUtils.createHtmlTable(colNames);

		tableFacade.setTable(table);

		setDataAndLimitVariables(tableFacade);


        if(isUseSimpleView()) {
			tableFacade.setView(new SimpleView());
		}

		this.setTitles(table, id, getColNames(), true);
		final Row row = table.getRow();
		row.setUniqueProperty(getUniquePropertyName());

		List<Column> cols = row.getColumns();

		Map<String, Object> context = getContext(request);

		for(Column col : cols){
			renderCell(col, context);
		}


		if (tableFacade.isExporting()) {
			 // Will write the export data out to the response.
			exportRender(tableFacade);
			// In Spring return null tells the controller not to do anything.
			return null;
		} else {
			return tableFacade.render();
		}
	}


	public String getColsList() {
		return colsList;
	}

	public void setColsList(String colsList) {
		this.colsList = colsList;
	}

	public void setUniquePropertyName(String uniquePropertyName) {
		this.uniquePropertyName = uniquePropertyName;
	}

	public IBaseListService<?, ?> getListService() {
		return this.service;
	}

	@Override
	protected String[] getColNames() {
		return getColsList().split("\\,");
	}

	@Override
	protected Long[] getKeys(HttpServletRequest request) {
		return ParameterUtils.getArrayLong(request,checkboxName);
	}

	public boolean isUseSimpleView() {
		return useSimpleView;
	}

	public void setUseSimpleView(boolean useSimpleView) {
		this.useSimpleView = useSimpleView;
	}

	public int getUseDateRange() {
		return useDateRange;
	}

	public void setUseDateRange(int useDateRange) {
		this.useDateRange = useDateRange;
	}


}
