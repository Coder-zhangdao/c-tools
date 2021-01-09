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


import java.util.Objects;

/**
 * Sharding key.
 *
 * @author zhangliang
 * @version $Id: $Id
 */
// TODO move to a suitable package
public final class ShardingKey {

    /**
     * Logic table name.
     */
    private final String logicTable;

    /**
     * Sharding column name.
     */
    private final String shardingColumn;

    /**
     * <p>Constructor for ShardingKey.</p>
     *
     * @param logicTable     a {@link java.lang.String} object.
     * @param shardingColumn a {@link java.lang.String} object.
     */
    public ShardingKey(final String logicTable, final String shardingColumn) {
        this.logicTable = logicTable.toLowerCase();
        this.shardingColumn = shardingColumn.toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShardingKey that = (ShardingKey) o;
        return logicTable.equals(that.logicTable) && shardingColumn.equals(that.shardingColumn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logicTable, shardingColumn);
    }
}
