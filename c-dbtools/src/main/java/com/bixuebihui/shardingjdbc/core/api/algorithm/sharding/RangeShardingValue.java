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

package com.bixuebihui.shardingjdbc.core.api.algorithm.sharding;

import com.google.common.collect.Range;

/**
 * Sharding value for range values.
 *
 * @author zhangliang
 * @version $Id: $Id
 */
public final class RangeShardingValue<T extends Comparable<?>> implements ShardingValue {

    private final String logicTableName;

    private final String columnName;

    /**
     * <p>Constructor for RangeShardingValue.</p>
     *
     * @param logicTableName a {@link java.lang.String} object.
     * @param columnName     a {@link java.lang.String} object.
     * @param valueRange     a {@link com.google.common.collect.Range} object.
     */
    public RangeShardingValue(String logicTableName, String columnName, Range<T> valueRange) {
        super();
        this.logicTableName = logicTableName;
        this.columnName = columnName;
        this.valueRange = valueRange;
    }

	private final Range<T> valueRange;

    /**
     * <p>Getter for the field <code>logicTableName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLogicTableName() {
        return logicTableName;
    }

    /**
     * <p>Getter for the field <code>columnName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * <p>Getter for the field <code>valueRange</code>.</p>
     *
     * @return a {@link com.google.common.collect.Range} object.
     */
    public Range<T> getValueRange() {
        return valueRange;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "RangeShardingValue [logicTableName=" + logicTableName + ", columnName=" + columnName + ", valueRange="
                + valueRange + "]";
    }
}
