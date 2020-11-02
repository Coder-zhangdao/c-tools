package com.bixuebihui.jmesa;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.jmesa.limit.Limit;
import org.jmesa.model.PageItems;

import com.bixuebihui.jdbc.IReaderService;
import com.bixuebihui.jdbc.SqlFilter;
import com.bixuebihui.jdbc.SqlSort;

public class ItemsProvider<T> implements PageItems {
	/**
	 *
	 */
	private final String uniquePropertyName;
	IReaderService<T> service;

	public ItemsProvider(String uniquePropertyName, IReaderService<T> service){
		this.uniquePropertyName = uniquePropertyName;
		this.service = service;
	}

	public int getTotalRows(Limit limit) {

		SqlFilter filter = AbstractWebUI.getFilter(limit);
		int totalRows = 0;
		try {
			totalRows = service.count(filter.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalRows;

	}

	public Collection<T> getItems(Limit limit) {
		SqlSort sort = AbstractWebUI.getSort(limit);
		SqlFilter filter = AbstractWebUI.getFilter(limit);

		int rowStart = limit.getRowSelect().getRowStart();
		int rowEnd = limit.getRowSelect().getRowEnd();

		String sortsql = sort.toString();

		if (StringUtils.trimToNull(sortsql) == null
				&& uniquePropertyName != null)
			sortsql = "order by " + uniquePropertyName;
		else
			sortsql = "";

		Collection<T> items = Collections.emptyList();
		try {
			items = service.select(filter.toString(), sortsql,
					rowStart, rowEnd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;

	}
}
