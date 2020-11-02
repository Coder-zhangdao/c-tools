package com.bixuebihui.tablegen;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.*;

import javax.sql.DataSource;

import com.bixuebihui.tablegen.diffhandler.DiffHandler;

import org.apache.commons.collections.CollectionUtils;

import com.bixuebihui.datasource.BitmechanicDataSource;
import com.bixuebihui.dbcon.DatabaseConfig;

public class DbDiff {

	Database db1;
	Database db2;

    private Map<String,List<ColumnData>> cachedTableData = null;


    private HashMap<String,List<ColumnData>> tableData = new HashMap<>() ;

    private void addTableData(String tableName,List<ColumnData> columnDatas )
    {
        tableData.put(tableName,columnDatas);
    }



    /**
     * 数据库不一致的处理程序
     */
    private List<DiffHandler> diffHandlerList = new ArrayList<>();


    public void addDiffHandler(DiffHandler dh)
    {
        diffHandlerList.add(dh);
    }


	public static class Database {
		public Database(DatabaseMetaData metaData2) {
			this.metaData = metaData2;
		}

		DatabaseMetaData metaData;
		String schema = null;
		String catalog = null;
		String tableOwner = "";
		Map<String, String> includeTablesList = null;
		Map<String, String> excludeTablesList = null;
		boolean types_are_strings = false;
	}

	public DbDiff(Map<String,List<ColumnData>> cachedTableData, Connection dsdstcfg) throws SQLException {

        this.cachedTableData =  cachedTableData;

        this.db2 = new Database(dsdstcfg.getMetaData());
	}

    public DbDiff() {

    }


	protected DataSource makeDataSource(DatabaseConfig dbconfig) {
		BitmechanicDataSource ds = new BitmechanicDataSource();
		ds.setDatabaseConfig(dbconfig);
		return ds;
	}

	List<String> getTableData(Database db1) throws SQLException {
		return TableUtils.getTableData(db1.metaData, db1.catalog, db1.schema,
				db1.tableOwner, db1.includeTablesList, db1.excludeTablesList);
	}


    private List<String> getCachedTables()
    {

        Iterator i =  cachedTableData.entrySet().iterator();

        List<String> v = new ArrayList<>();

        while (i.hasNext())
        {
            Map.Entry m = (Map.Entry)i.next();

            v.add((String) m.getKey());
        }




        return v;
    }

    private List<ColumnData> getCachedColumns(String tableName)
    {
        return cachedTableData.get(tableName);
    }


	public void compareTables() throws SQLException {


        List<String>  tab1;


        if(db1==null)
        {
            tab1 = getCachedTables();

        }else
        {
            tab1 = getTableData(db1);
        }


		List<String> tab2 = getTableData(db2);

        storeTableData(tab2);

        compareTableData(tab1,tab2);
        for(DiffHandler dh:diffHandlerList)
        {
            dh.processEnd(tableData);
        }
	}

    private void storeTableData(List<String> tab2) throws SQLException {

        for ( String t:tab2)
        {

            List<ColumnData> c = getColumns(db2,t);

            addTableData(t,c);
        }




    }


    /**
     * 比对表数据
     */
    public void compareTableData(List<String> tab1,List<String> tab2) throws SQLException {
        dumpDiffTabs(tab1, tab2);

        Collection res4 = CollectionUtils.intersection(tab1, tab2);
        compareTablesColumns(res4);

    }



	public void compareTablesColumns(Collection<String> tabs)
			throws SQLException {
		for (String tableName : tabs) {
			compareColumns(tableName);
		}
	}

	protected List<ColumnData> getColumns(Database db1, String tableName)
			throws SQLException {


		return TableUtils.getColumnData(db1.metaData, db1.catalog, db1.schema,
				tableName, db1.types_are_strings);
	}

	public void compareColumns(String tableName) throws SQLException {

        List<ColumnData> cols1;

        if(db1==null)
        {


            cols1 =  getCachedColumns(tableName);

            if(cols1 ==null)
            {
                cols1 = new ArrayList<>();
            }

        }else
        {
            cols1 = getColumns(db1, tableName);
        }


		List<ColumnData> cols2 = getColumns(db2, tableName);





		if(dumpDiffCols(cols1, cols2)){

            /**
             * 当表不一致时，通知所有handler处理表的不一致
             */
            for(DiffHandler dh:diffHandlerList)
            {
                dh.processTableDiff(tableName);
            }

			System.out.println("====>"+tableName+" changed");
		}
	}

	/**
	 * 比较两个集合
	 *
	 * @param tab1
	 * @param tab2
	 * @return
	 */
	protected void dumpDiffTabs(List<String> tab1, List<String> tab2) {
		Collection<String> res1 = CollectionUtils.subtract(tab1, tab2);
		Collection<String> res2 = CollectionUtils.subtract(tab2, tab1);

		if (!res1.isEmpty()) {
			System.out.println("db1 - db2 =");
			outputSortedList(res1);
		}
		if (!res2.isEmpty()) {
			System.out.println("db2 - db1 =");
			outputSortedList(res2);

			for (String tableName:res2)
            {
                /**
                 * 当原表没有生成时，进行处理
                 */
                for(DiffHandler dh:diffHandlerList)
                {
                    dh.processTableDiff(tableName);
                }
            }

		}
		Collection res3 = CollectionUtils.disjunction(tab1, tab2);
		if (!res3.isEmpty()) {
			System.out.println("|db1-db2|=");
			outputSortedList(res3);
		}


	}

	private void outputSortedList(Collection<String> res2) {
		ArrayList<String> list = new ArrayList<>(res2);
		Collections.sort(list);
		System.out.println(list);
	}

	/**
	 * 比较两个集合
	 *
	 * @param cols1
	 * @param cols2
	 * @return
	 */
	protected boolean dumpDiffCols(List<ColumnData> cols1,
			List<ColumnData> cols2) {
		Collection<ColumnData> res1 = CollectionUtils.subtract(cols1, cols2);
		Collection<ColumnData> res2 = CollectionUtils.subtract(cols2, cols1);
		boolean changed =false;
		if (!res1.isEmpty()) {
			System.out.println("cols1 - cols2 =");
			System.out.println(res1);
			changed = true;
		}

		if (!res2.isEmpty()) {
			System.out.println("cols2 - cols1 =");
			System.out.println(res2);
			changed = true;
		}

		return changed;

	}

}
