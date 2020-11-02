package com.bixuebihui.jmesa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.StringUtils;

import com.bixuebihui.jdbc.IDbHelper;
import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.CursorNode;
import com.foundationdb.sql.parser.FromList;
import com.foundationdb.sql.parser.ResultColumnList;
import com.foundationdb.sql.parser.ResultSetNode;
import com.foundationdb.sql.parser.SQLParser;
import com.foundationdb.sql.parser.SelectNode;
import com.foundationdb.sql.parser.StatementNode;

/**
 * For more easy use with jmesa
 */
public class EasyTable extends BasicWebUI {

	//用来解析sql并保存解析结果
	MiniSqlParser msp;

	private boolean onePage=true;

	/**
	 * use com.foundationdb.sql.parser.SQLParser to detect columns and key
	 * @param dbhelper use as data source
	 * @param tableCaption  表名描述
	 * @param baseSql 核心sql语句
	 */
	public EasyTable(IDbHelper dbhelper, String tableCaption, String baseSql) {
		super.setTableCaption(tableCaption);

		init(dbhelper, baseSql);

		msp = initMeta(dbhelper, baseSql);
		if(msp!=null){
			this.setUniquePropertyName(msp.uniquePropertyName);
		}

		if(msp!=null && msp.tableName!=null)
			this.setId(msp.tableName);
		else
			this.setId(tableCaption);

	}

	protected void init(IDbHelper dbhelper, String baseSql){
		this.service = new BasicListService(dbhelper);
		if(isOnePage()){
			this.maxRows =500;
			this.maxRowsIncrements[0]=maxRows;
			log.info("maxRows="+maxRows+"  maxRowsIncrements[0]="+this.maxRowsIncrements[0]);
		}
		((BasicListService) this.service).setCoreSql(baseSql);
	}

	/**
	 * 通过数据库返回的元数据确定列名
	 * @param dbhelper 数据源
	 * @param tableName 表名
	 * @param pkName   主键名
	 * @param baseSql  核心sql语句
	 * @throws SQLException
	 */
	public EasyTable(IDbHelper dbhelper, String tableName,String pkName, String baseSql) throws SQLException {
		init(dbhelper, baseSql);

		this.id = tableName;

		this.setUniquePropertyName(pkName);

		msp = MiniSqlParser.getByDb(dbhelper.getConnection(), baseSql);
	}


	protected MiniSqlParser initMeta(IDbHelper dbHelper, String baseSql) {

		try {
			return MiniSqlParser.parse(baseSql);
		} catch (StandardException e) {
			 log.error("sql can't be parsed:"+baseSql);
			 log.error(e);
		}
		return null;
	}

	@Override
	protected String[] getColNames() {
		return msp.colNames;
	}

	@Override
	public String getColsList() {
		return StringUtils.join(msp.colNames,",").toUpperCase();
	}

	static class MiniSqlParser {
		public String[] colNames;
		public String[] colLabels;
		public String tableName;
		public String uniquePropertyName;

		public static MiniSqlParser parse(String sql) throws StandardException {
			MiniSqlParser sp = new MiniSqlParser();
			SQLParser parser = new SQLParser();

			StatementNode stmt = parser.parseStatement(sql);
			//stmt.treePrint();

			if(stmt instanceof CursorNode) {
				CursorNode cn = (CursorNode)stmt;

				//System.out.println(cn.statementToString());//must be SELECT

				//System.out.println(cn.getResultSetNode());
				ResultSetNode rn =  cn.getResultSetNode();
				ResultColumnList cl = rn.getResultColumns();

				String[] tmpColNames = cl.getColumnNames();

				sp.colLabels = new String[cl.size()];

				//if( tmpColNames.length < cl.size()){
				//	sp.colNames = new String[cl.size()];
				//}else{
					sp.colNames = tmpColNames;
				//}
				if(sp.colNames!=null && sp.colNames.length>0)
				for(int i =0;  i <cl.size() && tmpColNames.length >i && sp.colNames[i]!=null; i++) {

					sp.colNames[i] =  sp.colNames[i].toUpperCase() ;

					//System.out.println(cl.get(i));
					//System.out.println(cl.get(i).getExpression());
					//sp.colNames[i] = cl.get(i).getExpression().getName();
					sp.colLabels[i]= cl.get(i).getExpression().toString();
				}
				//System.out.println(cl);
				//cn.printSubNodes(1);
				if(rn instanceof SelectNode) {
					SelectNode sn = (SelectNode)rn;
					//System.out.println("from list = "+sn.getFromList());
					FromList fromList = sn.getFromList();

					sp.tableName = fromList.get(0).getExposedName();
					//System.out.println(fromList.get(0));

				}else {
					log.debug("rn = "+rn);
				}

			}else {
				log.warn("not a select!");
			}

			if(sp.colNames.length>0)
				sp.uniquePropertyName = sp.colNames[0];

			return sp;
		}

		public static MiniSqlParser getByDb(Connection cn, String sql)
				throws SQLException {
			MiniSqlParser sp = new MiniSqlParser();
			Statement stmt = null;
			ResultSet rs = null;
			ResultSetMetaData rsmd = null;
			try {
				stmt = cn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_READ_ONLY);
				rs = stmt.executeQuery(sql + " limit 0,0");
				rsmd = rs.getMetaData();
				sp.colNames = new String[rsmd.getColumnCount()];
				sp.colLabels = new String[rsmd.getColumnCount()];
				sp.tableName = rsmd.getTableName(1);

				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					sp.colNames[i - 1] = rsmd.getColumnName(i).toUpperCase();
					sp.colLabels[i - 1] = rsmd.getColumnLabel(i);
				}
			} finally {

				// DbUtils.close(rsmd);
				DbUtils.close(rs);
				DbUtils.close(stmt);
			}

			return sp;
		}

	}

	public String getId() {
		return id;
	}


	public boolean isOnePage() {
		return onePage;
	}


	public void setOnePage(boolean onePage) {
		this.onePage = onePage;
	}

}
