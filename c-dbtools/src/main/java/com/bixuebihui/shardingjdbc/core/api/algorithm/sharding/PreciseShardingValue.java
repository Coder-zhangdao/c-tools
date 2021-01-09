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


/**
 * Sharding value for precise value.
 *
 * @author zhangliang
 * @version $Id: $Id
 */
public final class PreciseShardingValue<T extends Comparable<?>> implements ShardingValue {

    /**
     * <p>Constructor for PreciseShardingValue.</p>
     *
     * @param logicTableName a {@link java.lang.String} object.
     * @param columnName     a {@link java.lang.String} object.
     * @param value          a T object.
     */
    public PreciseShardingValue(String logicTableName, String columnName, T value) {
        super();
        this.logicTableName = logicTableName;
        this.columnName = columnName;
        this.value = value;
    }

	private final String logicTableName;

    private final String columnName;

    private final T value;

    /**
     * <p>Getter for the field <code>logicTableName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String getLogicTableName() {
        return logicTableName;
    }

    /**
     * <p>Getter for the field <code>columnName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String getColumnName() {
        return columnName;
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a T object.
     */
    public T getValue() {
        return value;
    }
}
