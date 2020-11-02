package com.bixuebihui.sequence;

import com.bixuebihui.jdbc.IDbHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.<br>
 * <b>User</b>: leizhimin<br>
 * <b>Date</b>: 2008-4-2 15:21:30<br>
 * <b>Note</b>: Java实现的Sequence工具
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class SequenceUtils {
	private static SequenceUtils INSTANCE = new SequenceUtils();
	private Map<String, KeyInfo> keyMap = new Hashtable<>(20); // Sequence载体容器
	private static final int POOL_SIZE = 10; // Sequence值缓存大小
	private static final Log LOG = LogFactory.getLog(SequenceUtils.class);

	/**
	 * 禁止外部实例化
	 */
	private SequenceUtils() {
	}

	/**
	 * 获取SequenceUtils的单例对象
     *
     * @return SequenceUtils的单例对象
	 */
	public static SequenceUtils getInstance() {
		return INSTANCE;
	}


	/**
	 * 获取下一个Sequence键值
     *
     * @param keyName
	 *            Sequence名称
	 * @param dbHelper 数据库连接
	 * @return 下一个Sequence键值
	 */
	public synchronized long getNextKeyValue(String keyName, IDbHelper dbHelper) {
		try {
			return getOrCreateKeyInfo(keyName, dbHelper).getNextKey();
		} catch (SQLException e) {
			LOG.error(e);
		}
		return 0;
	}

    /**
     * <p>moveKeyValueToCurrent.</p>
     *
     * @param keyName  a {@link java.lang.String} object.
     * @param max      a {@link java.lang.Long} object.
     * @param dbHelper a {@link IDbHelper} object.
     */
    public synchronized void moveKeyValueToCurrent(String keyName, Long max, IDbHelper dbHelper) {
        try {
            getOrCreateKeyInfo(keyName, dbHelper).moveTo(max);
        } catch (SQLException e) {
            LOG.error(e);
        }

    }

	private KeyInfo getOrCreateKeyInfo(String keyName, IDbHelper dbHelper) throws SQLException {
		KeyInfo keyInfo;
		if (keyMap.containsKey(keyName)) {
			keyInfo = keyMap.get(keyName);
		} else {
			keyInfo = new KeyInfo(keyName, POOL_SIZE, dbHelper);
			keyMap.put(keyName, keyInfo);
		}
		return keyInfo;
	}
}
