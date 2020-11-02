package com.bixuebihui.cache;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bixuebihui.BeanFactory;
import com.bixuebihui.util.html.FormControl;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * 字典表一般认为是记录较少，一般不超过5000条，多数情况下仅为几十条，字段很少， 一般仅三、四个，同时改动很少，查询却很多的数据表
 * 标准字典表的定义为：表名－DMS_XXXXXX, 字段名
 * MS_ID，MS_VALUE，MS_SORT，缓存在内存中的只有MS_ID，和MS_VALUE
 *
 * @author xwx
 *
 */
public class DictionaryCache {
	static Log mLog;

	public static final String KEY_SEPARATOR = ".";
	public static final String CONDITION_SEPARATOR = "@";

	static GeneralCacheAdministrator admin;

	static {
		mLog = LogFactory.getLog(DictionaryCache.class);
		if (admin == null) {
			mLog.info("create new cache administrator");
			admin = new GeneralCacheAdministrator();
		}
	}

	class Dictionary {
		String tableName; // 表名
		String keyName; // 字段值
		String condition; // 附加条件
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

	@SuppressWarnings( { "unchecked" })
	static Map sortByValue(Map map) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, (Comparator) (o1, o2) -> ((Comparable) ((Map.Entry) (o1)).getValue())
				.compareTo(((Map.Entry) (o2)).getValue()));
		Map result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/**
	 * 字典表数据在缓存内分为二级存储 一级为整个表，二级为单条记录，皆为Map数据结构（LinkedHashMap） key的定义为
	 * “，如果仅是表名，则返回整个表，否则只返回表的一条记录
	 *
	 * @param key 表名.键值
	 * @return “表名.键值”，如果仅是表名，则返回整个表，否则只返回表的一条记录
	 */
	@SuppressWarnings("unchecked")
	public DictionaryItem getItemById(String key) {
		return (DictionaryItem) getFromCache(key, true);
	}

	public DictionaryItem getItemByValue(String key) {
		return (DictionaryItem) getFromCache(key, false);
	}

	public DictionaryItem getItemByValue(String key, boolean doRefreshIfNotFindInCache) {
		return (DictionaryItem) getFromCache(key, false, doRefreshIfNotFindInCache);
	}

	public Map<String, DictionaryItem> getDict(String tableBeanName) {
		return (Map<String, DictionaryItem>) getFromCache(tableBeanName, true);
	}



	/**
	 * 得到 "&lt;option&gt;&lt;/option&gt;"
	 * @param tableBeanName table name
	 * @param strDefaultSelect default selected item,if no condition item is selected.
	 * @param strConditionSelect condition selected item
	 * @return　HTML option tag 字符串
	 */
	public String getOptionList(String tableBeanName, String strDefaultSelect,
			String strConditionSelect) {
		Map<String, DictionaryItem> map = getDict(tableBeanName);
		String[] ids = new String[map.size()];
		String[] values = new String[map.size()];
		Iterator<DictionaryItem> iter = map.values().iterator();
		for (int i = 0; i < map.size(); i++) {
			DictionaryItem item = iter.next();
			ids[i] = item.getMs_id() + "";
			values[i] = item.getMs_value();
		}

		return FormControl.getOptionList(values, ids, strConditionSelect,
				strConditionSelect);
	}


	public static String makeKey(String tableName, String condition,
			String idValue) {
		if (idValue == null && condition == null) {
			return tableName;
		} else if (idValue == null) {
			return tableName + CONDITION_SEPARATOR + condition;
		} else if (condition == null) {
			return tableName + KEY_SEPARATOR + idValue;
		} else {
			return tableName + CONDITION_SEPARATOR + condition + KEY_SEPARATOR
					+ idValue;
		}
	}

	private Object getFromCache(String key, boolean isById) {
		return getFromCache( key,  isById, true);
	}

	@SuppressWarnings("unchecked")
	private Object getFromCache(String key, boolean isById, boolean doRefreshIfNotFind) {
	Object[] myValue;

		Dictionary dict = parseKey(key);
		String storekey;
		if (dict.condition != null) {
			storekey = dict.tableName + CONDITION_SEPARATOR + dict.condition;
		} else {
			storekey = dict.tableName;
		}

		try {
			// Get from the cache]
			myValue = (Map[]) admin.getFromCache(storekey, (int) (CacheConfig
					.getUpdateIntervalMilliseconds() / 1000));
			if (dict.keyName == null) {
				// LinkedHashMap
				return myValue[isById ? 0 : 1];
			} else {
				// DictionaryItem
				Object o = ((Map<String, DictionaryItem>) myValue[isById ? 0 : 1]).get(dict.keyName);

				if (o == null) {
					throw new NeedsRefreshException("not find " + dict.keyName);
				} else {
					return o;
				}
			}

		} catch (NeedsRefreshException nre) {
			try {
				if(!doRefreshIfNotFind) {
					return null;
				}
				// Get the value (probably from the database)
				// myValue = "This is the content retrieved.";
				// Store in the cache

				DictionaryDefine def = (DictionaryDefine) BeanFactory
						.createObjectById(dict.tableName);
				if (def == null) {
					def = new DictionaryDefine(dict.tableName);
				}

				if (dict.condition != null) {
					def.setConditionValue(dict.condition);
				}

				DictionaryList list = new DictionaryList(def);
				try {
					List<DictionaryItem> li = list.selectAll();
					Map<String, DictionaryItem>[] mm = new LinkedHashMap[2];
					mm[0] = new LinkedHashMap<>(li.size());
					mm[1] = new LinkedHashMap<>(li.size());

					for (int i = 0; i < li.size(); i++) {
						mm[0].put(li.get(i).getMs_id() + "",
								li.get(i));
						mm[1].put(li.get(i).getMs_value()
								+ "", li.get(i));
					}

					admin.putInCache(storekey, mm);

					if (dict.keyName == null) {
						return mm[isById ? 0 : 1];
					} else {
						// DictionaryItem
						return mm[isById ? 0 : 1].get(dict.keyName);
					}
				} catch (SQLException e) {
					mLog.error("查询"+dict.tableName+"时数据库出错：");
					e.printStackTrace();
					admin.cancelUpdate(key);
					return null;
				}

			}catch(Exception e){
				e.printStackTrace();
				admin.cancelUpdate(key);
				// We have the current content if we want fail-over.
				Object value = nre.getCacheContent();
				// It is essential that cancelUpdate is called if the
				// cached content is not rebuilt
				return value;
			}
		}
	}

	public void destroy(){
		if(admin!=null) {
			admin.destroy();
		}
	}

}
