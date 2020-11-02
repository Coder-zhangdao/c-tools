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

package com.bixuebihui.shardingjdbc.core.api;

import com.bixuebihui.shardingjdbc.core.api.algorithm.sharding.ListShardingValue;
import com.bixuebihui.shardingjdbc.core.api.algorithm.sharding.RangeShardingValue;
import com.bixuebihui.shardingjdbc.core.api.algorithm.sharding.ShardingValue;
import com.bixuebihui.shardingjdbc.core.constant.ShardingOperator;
import com.bixuebihui.shardingjdbc.core.hint.HintManagerHolder;
import com.bixuebihui.shardingjdbc.core.hint.ShardingKey;
import com.google.common.base.Preconditions;
import com.google.common.collect.BoundType;
import com.google.common.collect.Range;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The manager that use hint to inject sharding key directly through {@code ThreadLocal}.
 *
 * @author gaohongtao
 * @author zhangliang
 * @version $Id: $Id
 */
public final class HintManager implements AutoCloseable {

    private final Map<ShardingKey, ShardingValue> databaseShardingValues = new HashMap<ShardingKey, ShardingValue>();

    private final Map<ShardingKey, ShardingValue> tableShardingValues = new HashMap<ShardingKey, ShardingValue>();

    private boolean shardingHint;

    private boolean masterRouteOnly;

    private boolean databaseShardingOnly;

    /**
     * Get a new instance for {@code HintManager}.
     *
     * @return  {@code HintManager} instance
     */
    public static HintManager getInstance() {
        HintManager result = new HintManager();
        HintManagerHolder.setHintManager(result);
        return result;
    }

    /**
     * Set sharding value for database sharding only.
     *
     * <p>The sharding operator is {@code =}</p>
     *
     * @param value sharding value
     */
    public void setDatabaseShardingValue(final Comparable<?> value) {
        databaseShardingOnly = true;
        addDatabaseShardingValue(HintManagerHolder.DB_TABLE_NAME, HintManagerHolder.DB_COLUMN_NAME, value);
    }

    /**
     * Add sharding value for database.
     *
     * <p>The sharding operator is {@code =}</p>
     *
     * @param logicTable logic table name
     * @param shardingColumn sharding column name
     * @param value sharding value
     */
    public void addDatabaseShardingValue(final String logicTable, final String shardingColumn, final Comparable<?> value) {
        addDatabaseShardingValue(logicTable, shardingColumn, ShardingOperator.EQUAL, value);
    }

    /**
     * Add sharding value for database.
     *
     * @param logicTable logic table name
     * @param shardingColumn sharding column name
     * @param operator sharding operator
     * @param values sharding value
     */
    public void addDatabaseShardingValue(final String logicTable, final String shardingColumn, final ShardingOperator operator, final Comparable<?>... values) {
        shardingHint = true;
        databaseShardingValues.put(new ShardingKey(logicTable, shardingColumn), getShardingValue(logicTable, shardingColumn, operator, values));
    }

    /**
     * Add sharding value for table.
     *
     * <p>The sharding operator is {@code =}</p>
     *
     * @param logicTable logic table name
     * @param shardingColumn sharding column name
     * @param value sharding value
     */
    public void addTableShardingValue(final String logicTable, final String shardingColumn, final Comparable<?> value) {
        addTableShardingValue(logicTable, shardingColumn, ShardingOperator.EQUAL, value);
    }

    /**
     * Add sharding value for table.
     *
     * @param logicTable logic table name
     * @param shardingColumn sharding column name
     * @param operator sharding operator
     * @param values sharding value
     */
    public void addTableShardingValue(final String logicTable, final String shardingColumn, final ShardingOperator operator, final Comparable<?>... values) {
        shardingHint = true;
        tableShardingValues.put(new ShardingKey(logicTable, shardingColumn), getShardingValue(logicTable, shardingColumn, operator, values));
    }

    @SuppressWarnings("unchecked")
    private ShardingValue getShardingValue(final String logicTable, final String shardingColumn, final ShardingOperator operator, final Comparable<?>[] values) {
        Preconditions.checkArgument(null != values && values.length > 0);
        switch (operator) {
            case EQUAL:
            case IN:
                return new ListShardingValue(logicTable, shardingColumn, Arrays.asList(values));
            case BETWEEN:
                return new RangeShardingValue(logicTable, shardingColumn, Range.range(values[0], BoundType.CLOSED, values[1], BoundType.CLOSED));
            default:
                throw new UnsupportedOperationException(operator.getExpression());
        }
    }

    /**
     * Get sharding value for database.
     *
     * @param shardingKey sharding key
     * @return sharding value for database
     */
    public ShardingValue getDatabaseShardingValue(final ShardingKey shardingKey) {
        return databaseShardingValues.get(shardingKey);
    }

    /**
     * Get sharding value for table.
     *
     * @param shardingKey sharding key
     * @return sharding value for table
     */
    public ShardingValue getTableShardingValue(final ShardingKey shardingKey) {
        return tableShardingValues.get(shardingKey);
    }

    /**
     * Set CRUD operation force route to master database only.
     */
    public void setMasterRouteOnly() {
        masterRouteOnly = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        HintManagerHolder.clear();
    }

    /**
     * <p>isShardingHint.</p>
     *
     * @return a boolean.
     */
    public boolean isShardingHint() {
        return shardingHint;
    }

    /**
     * <p>isMasterRouteOnly.</p>
     *
     * @return a boolean.
     */
    public boolean isMasterRouteOnly() {
        return masterRouteOnly;
    }

    /**
     * <p>isDatabaseShardingOnly.</p>
     *
     * @return a boolean.
     */
    public boolean isDatabaseShardingOnly() {
        return databaseShardingOnly;
    }
}
