package com.bixuebihui.cache;

/**
 * DictionaryItem
 *
 * WARNING! Automatically generated file!
 * Do not edit the pojo and dal packages,use bixuebihui-smartable!
 * Code Generator by J.A.Carter
 * Modified by Xing Wanxiang 2008
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bixuebihui.jdbc.BaseDao;
import com.bixuebihui.jdbc.RowMapperResultReader;
import com.bixuebihui.sequence.SequenceUtils;

/**
 * @author xwx
 */
public class DictionaryList extends BaseList<DictionaryItem, String>  {

	private final DictionaryDefine def;

	public DictionaryList(DictionaryDefine define){
		this.def = define;
	}

    DictionaryList(){//for test
		def = new DictionaryDefine();
	}

	/**
	 * Updates the object from a selected ResultSet.
	 */
	@Override
	public DictionaryItem mapRow(ResultSet r, int index) throws SQLException {
		DictionaryItem res = new DictionaryItem();
		res.setMs_id(r.getString(def.getId_name()));
		res.setMs_value(r.getString(def.getValue_name()));
		res.setMs_sort(r.getString(def.getSort_name()));
		return res;
	}

	private String getSelectSql(){
		if(def.getId_name().equalsIgnoreCase(def.getSort_name())||
				def.getValue_name().equalsIgnoreCase(def.getSort_name())) {
			return "select "+def.getId_name()+", "+def.getValue_name()
			+" from ";
		} else {
			return "select "+def.getId_name()+", "+def.getValue_name()
			+", "+def.getSort_name() +" from ";
		}
	}

	/**
	 * Select from the database for table "getTableName()"
	 */
	@Override
    public List<DictionaryItem> select(String whereClause, String orderbyClause,
                                       int beginNum, int endNum) throws SQLException {

		if(StringUtils.trimToNull(whereClause)==null) {
			whereClause=" where 1=1 ";
		}

		String query = getSelectSql() + getTableName() + " " + whereClause+" "+def.getSqlCondition();
		if (this.getDBTYPE() != BaseDao.DERBY) {
			query += orderbyClause;
		}
		query = getPagingSql(query, beginNum, endNum);
		return dbHelper.executeQuery(query, null, new RowMapperResultReader<DictionaryItem>(
				this));
	}


	/**
	 * Select from the database for table "DictionaryItem"
	 */
	@Override
	public List<DictionaryItem> selectAll() throws SQLException {
		String query = getSelectSql() + getTableName()+" where 1=1 "+def.getSqlCondition();

		if(StringUtils.trimToNull(def.getSort_name())!=null){
			query += " order by "+def.getSort_name();
		}
		return dbHelper.executeQuery(query, null, new RowMapperResultReader<DictionaryItem>(
				this));
	}

	/**
	 * Select from the database for table "dictionary"
	 * @param id 扩展表 id
	 * @return DictionaryItem
	 */
	public DictionaryItem selectByKey(long id) throws SQLException {
		String query = getSelectSql() + getTableName() + " where "+def.getId_name()+"=?"+" "+def.getSqlCondition();
		List<DictionaryItem> info = dbHelper.executeQuery(query, new Object[] { id },
				new RowMapperResultReader<>(this));
		if (info != null && info.size() > 0) {
			return info.get(0);
		}
		return null;
	}

	/**
	 * Select from the database for table "getTableName()"
	 * @param id id
	 * @return List of DictionaryItem
	 */
	public List<DictionaryItem> selectAllLikeKey(long id)
			throws SQLException {
		String query = getSelectSql() + getTableName() + " where "+def.getId_name()+"=?"+" "+def.getSqlCondition();
		return dbHelper.executeQuery(query, new Object[] { id },
				new RowMapperResultReader<>(this));
	}

	/**
	 * Updates the current object values into the database.
	 * @param info DictionaryItem
	 * @return true success
	 */
	@Override
	public boolean updateByKey(DictionaryItem info) throws SQLException {
		String query = "update " + getTableName() + " set " + def.getId_name()+"=?" + ","
				+def.getValue_name()+ "=?" + "," + def.getSort_name()+"=?" + " where "+def.getId_name()+"=?"+" "+def.getSqlCondition();
		return 1 == dbHelper.executeNoQuery(query, new Object[] {
				info.getMs_id(), info.getMs_value(), info.getMs_sort(),
				info.getMs_id() });
	}



	/**
	 * Deletes from the database for table "KUOZHANBIAOMING"
	 * @param  id id
	 * @return true if succcess
	 */
	public boolean deleteByKey(long id) throws SQLException {
		String query = "delete from " + getTableName() + " where "+def.getId_name()+"=?"+" "+def.getSqlCondition();
		return 1 <= dbHelper.executeNoQuery(query, new Object[] { id });
	}

	@Override
	public String getNextKey(){
		return SequenceUtils.getInstance().getNextKeyValue(this.getTableName() + "_ms_id", dbHelper)+"";
	}



	/**
	 * Counts the number of entries for this table in the database.
	 * @param id id
	 * @return count
	 */
	public int countByKey(long id) throws SQLException {
		String query = "select count(*) from " + getTableName()
				+ " where "+def.getId_name()+"=?"+" "+def.getSqlCondition();
		Object o = dbHelper.executeScalar(query, new Object[] { id });
		return o == null ? 0 : Integer.parseInt(o.toString());
	}

	/**
	 * Counts the number of entries for this table in the database.
	 * @param kzbm_id id
	 * @return  count
	 */
	public int countLikeKey(long kzbm_id) throws SQLException {
		String query = "select count(*) from " + getTableName()
				+ " where "+def.getId_name()+"=?"+" "+def.getSqlCondition();
		Object o = dbHelper.executeScalar(query, new Object[] { kzbm_id });
		return o == null ? 0 : Integer.parseInt(o.toString());
	}



	/**
	 * Inserts the DictionaryItem object values into the database.
	 * @param info DictionaryItem
	 * @return  true if success
	 */
	@Override
	public boolean insert(DictionaryItem info) throws SQLException {
		String query = "insert into " + getTableName()
				+ " ( "+def.getId_name()+","+def.getValue_name()+","+def.getSort_name()+" ) values ( ?,?,? )";
		return 1 == dbHelper.executeNoQuery(query, new Object[] {
				info.getMs_id(), info.getMs_value(), info.getMs_sort() });
	}

	/**
	 * Inserts the dummy record of DictionaryItem object values into the
	 * database.
	 * @return true if success
	 */
	@Override
	public boolean insertDummy() throws SQLException {
		DictionaryItem info = new DictionaryItem();
		 java.util.Random rnd = new java.util.Random();
		info.setMs_value(Integer.toString(Math.abs(rnd.nextInt(Integer.MAX_VALUE)), 36));
		info.setMs_id(getNextKey()+"");
		return this.insert(info);
	}


	@Override
	public String getTableName() {
		return def.getTableName();
	}


	@Override
	public String getId(DictionaryItem info) {
		return info.getMs_id();
	}

	@Override
	public void setId(DictionaryItem info, String id) {
		 info.setMs_id(id);
	}

	@Override
	public String getKeyName() {
		return def.getId_name();
	}



}
