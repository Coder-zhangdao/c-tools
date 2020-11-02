package com.bixuebihui.cache;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bixuebihui.BeanFactory;
import com.bixuebihui.algorithm.LRULinkedHashMap;
import com.bixuebihui.jdbc.IBaseListService;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * 对象缓存将以"表名.ID"为索引，快速查找出对象的缓存方式，对常用表采用大内存的缓存方式
 * 最大记录数要在10万条左右，因内存消耗大量内存，缓存要精细组织，不能有重复记录
 *
 * @author xwx
 *
 */
public class ObjectCache {
	static Log mLog;

	public static final String KEY_SEPARATOR = ".";
	public static final String CONDITION_SEPARATOR = "@";

	static GeneralCacheAdministrator admin;

	static {
		mLog = LogFactory.getLog(ObjectCache.class);
		if (admin == null) {
			mLog.info("create new cache administrator");
			// Properties property = new Properties();
			// property.setProperty("cache.blocking", "false");
			// admin = new GeneralCacheAdministrator(property);
			admin = new GeneralCacheAdministrator();
		}
	}

	class Dictionary {
		String tableName; // 表名
		String keyName; // 字段值
		String condition; // 附加条件

		public String getStoreKey() {
			String storekey;
			if (condition != null) {
				storekey = tableName + CONDITION_SEPARATOR + condition;
			} else {
				storekey = tableName;
			}
			return storekey;
		}

		public DictionaryDefine getDictDef() {
			DictionaryDefine def = (DictionaryDefine) BeanFactory
					.createObjectById(tableName);
			if (def == null) {
				def = new DictionaryDefine(tableName);
			}

			if (condition != null) {
				def.setConditionValue(condition);
			}
			return def;
		}
	}

	private Dictionary parseKey(String key) {
		Dictionary dict = new Dictionary();

		String[] keys = key.split("\\" + KEY_SEPARATOR);
		if (keys.length > 0) {
			String[] names = keys[0].split("\\" + CONDITION_SEPARATOR);
			dict.tableName = names[0];
			if (names.length > 1) {
				dict.condition = names[1];
			} else {
				dict.condition = null;
			}
		} else {
			mLog.error("No key find! " + key);
		}
		if (keys.length > 1) {
			dict.keyName = keys[1];
		}

		return dict;
	}

	// 过期时间(单位为秒);
	private int refreshPeriod = 3600;

	private static final long serialVersionUID = -4397192926052141162L;

	public ObjectCache(int refreshPeriod) {
		super();
		this.refreshPeriod = refreshPeriod;
	}

	public ObjectCache() {
	}

	@SuppressWarnings("unchecked")
	Map<String, ?> batchGetFromCache(String storeKey, List<String> arr) {
		Map<String, Object> m = new HashMap<>();
		try {
			Map<String, ?> all;
			all = (Map<String, ?>) admin.getFromCache(storeKey);//, refreshPeriod);
			int count = arr.size();
			for (int i = count - 1; i >= 0; i--)
				if (all.containsKey(arr.get(i))) {
					m.put(arr.get(i), all.get(arr.get(i)));
					arr.remove(i);
				}
		} catch (NeedsRefreshException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			mLog.debug("not find in cache "+storeKey);
			admin.cancelUpdate(storeKey);
		}
		return m;
	}

	Map<String,?> getMap(String storeKey, int maxCapacity){
		Map mm = null;
		try {
			mm = (Map) admin.getFromCache(storeKey);//, refreshPeriod);
		} catch (NeedsRefreshException e) {
			mLog.debug("not find in cache "+storeKey);
			admin.cancelUpdate(storeKey);
			//e.printStackTrace();
		}
		mLog.debug("mm="+mm);
		if (mm == null) {
			mm = new LRULinkedHashMap(maxCapacity);
		}
		return mm;
	}

	@SuppressWarnings("unchecked")
	Map<String, ?> batchGetFromDb(String storekey, DictionaryDefine def,
			List<String> arr) throws  SQLException {

		IBaseListService<DictionaryItem, String> list = def.getServiceClass();
		Map<String, DictionaryItem> li = list.selectByIds(def.getId_name(), arr);

		mLog.debug("dump data:"+li);

		Map mm =getMap(storekey, def.getMaxCapacity());
		mm.putAll(li);
		admin.putInCache(storekey, mm);
		return mm; // LinkedHashMap
	}

	@SuppressWarnings("unchecked")
	public Map<String, ?> batchGet(String key, List<String> arr)
			throws SQLException {

		Dictionary dict = parseKey(key);
		mLog.debug("get from cache " + arr.size());
		Map m = batchGetFromCache(dict.getStoreKey(), arr);

		System.out.println("from cache "+m);

		if (arr.size() > 0) {
			mLog.debug("get from db " + arr.size());
			m.putAll(batchGetFromDb(dict.getStoreKey(), dict
							.getDictDef(), arr));
		}
		System.out.println("+ from db"+m);
		return m;
	}

	public Object selectById(String key, String id) throws
			 SQLException {
		List<String> l = new ArrayList<String>();
		l.add(id);
		Map<String, ?> m = batchGet(key, l);
		if (m == null)
			return null;
		return m.get(id);
	}

	@SuppressWarnings("unchecked")
	public boolean updateByKey(String key, String id, Object info) throws SQLException {
		Dictionary dict = parseKey(key);
		DictionaryDefine def = dict.getDictDef();
		IBaseListService list = def.getServiceClass();
		boolean res = list.updateByKey(info);

		Map<String, Object> all = (Map<String, Object>) getMap(dict.getStoreKey(), def.getMaxCapacity());

		all.put(id, info);

		return res;
	}

	public boolean deleteByKey(String key, String id) throws SQLException {
		Dictionary dict = parseKey(key);
		DictionaryDefine def = dict.getDictDef();
		IBaseListService list = def.getServiceClass();
		boolean res = list.deleteByKey(id);

		Map<String, Object> all = (Map<String, Object>) getMap(dict.getStoreKey(), def.getMaxCapacity());
		all.remove(id);
		return res;
	}

	public boolean insertAutoNewKey(String key, Object info) throws SQLException {
		Dictionary dict = parseKey(key);
		DictionaryDefine def = dict.getDictDef();
		IBaseListService list = def.getServiceClass();
		boolean res = list.insertAutoNewKey(info);

		//TODO 如何自动获取新增ID
		//Map<String, Object> all = (Map<String, Object>) getMap(dict.getStoreKey(), def.getMaxCapacity());
		//all.put(id, info);

		return res;
	}

	public void destroy() {
		// TODO Auto-generated method stub

		admin.destroy();
	}

}
