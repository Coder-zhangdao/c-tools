/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.bixuebihui.shardingjdbc.core.hint;

import com.bixuebihui.shardingjdbc.core.api.HintManager;
import com.bixuebihui.shardingjdbc.core.api.algorithm.sharding.ShardingValue;

/**
 * Hint manager holder.
 *
 * <p>Use thread-local to manage hint.</p>
 *
 * @author zhangliang
 * @version $Id: $Id
 */
public final class HintManagerHolder {

    /**
     * Constant <code>DB_TABLE_NAME="DB_TABLE_NAME"</code>
     */
    public static final String DB_TABLE_NAME = "DB_TABLE_NAME";

    /** Constant <code>DB_COLUMN_NAME="DB_COLUMN_NAME"</code> */
    public static final String DB_COLUMN_NAME = "DB_COLUMN_NAME";

    private static final ThreadLocal<HintManager> HINT_MANAGER_HOLDER = new ThreadLocal<HintManager>();

    /**
     * Set hint manager.
     *
     * @param hintManager hint manager instance
     */
    public static void setHintManager(final HintManager hintManager) {
        if(null != HINT_MANAGER_HOLDER.get()) {
            throw new RuntimeException( "HintManagerHolder has previous value, please clear first.");
        }
        HINT_MANAGER_HOLDER.set(hintManager);
    }

    /**
     * Adjust use sharding hint in current thread.
     *
     * @return use sharding hint in current thread or not
     */
    public static boolean isUseShardingHint() {
        return null != HINT_MANAGER_HOLDER.get() && HINT_MANAGER_HOLDER.get().isShardingHint();
    }

    /**
     * Get database sharding value.
     *
     * @param shardingKey sharding key
     * @return database sharding value
     */
    public static ShardingValue getDatabaseShardingValue(final ShardingKey shardingKey) {
        return isUseShardingHint() ? HINT_MANAGER_HOLDER.get().getDatabaseShardingValue(shardingKey)
        		: null;
    }

    /**
     * Get table sharding value.
     *
     * @param shardingKey sharding key
     * @return table sharding value
     */
    public static ShardingValue getTableShardingValue(final ShardingKey shardingKey) {
        return isUseShardingHint() ? HINT_MANAGER_HOLDER.get().getTableShardingValue(shardingKey)
        		:null;
    }

    /**
     * Adjust is force route to master database only or not.
     *
     * @return is force route to master database only or not
     */
    public static boolean isMasterRouteOnly() {
        return null != HINT_MANAGER_HOLDER.get() && HINT_MANAGER_HOLDER.get().isMasterRouteOnly();
    }

    /**
     * Adjust database sharding only.
     *
     * @return database sharding only or not
     */
    public static boolean isDatabaseShardingOnly() {
        return null != HINT_MANAGER_HOLDER.get() && HINT_MANAGER_HOLDER.get().isDatabaseShardingOnly();
    }

    /**
     * Clear hint manager for current thread-local.
     */
    public static void clear() {
        HINT_MANAGER_HOLDER.remove();
    }
}
