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

package com.bixuebihui.shardingjdbc.core.constant;


/**
 * Sharding properties constant.
 *
 * @author gaohongtao
 * @author caohao
 */
public enum ShardingPropertiesConstant {

    /**
     * Enable or Disable to show SQL details.
     *
     * <p>
     * Print SQL details can help developers debug easier.
     * The details includes: logic SQL, parse context and rewrote actual SQL list.
     * Enable this property will log into log topic: {@code Sharding-JDBC-SQL}, log level is {@code INFO}.
     * Default: false
     * </p>
     */
    SQL_SHOW("sql.show", Boolean.FALSE.toString(), boolean.class),

    /**
     * Worker thread max size.
     *
     * <p>
     * Execute SQL Statement and PrepareStatement will use this thread pool.
     * One sharding data source will use a independent thread pool, it does not share thread pool even different data source in same JVM.
     * Default: same with CPU cores.
     * </p>
     */
    EXECUTOR_SIZE("executor.size", String.valueOf(Runtime.getRuntime().availableProcessors()), int.class);

    private ShardingPropertiesConstant(String key, String defaultValue, Class<?> type) {
		this.key = key;
		this.defaultValue = defaultValue;
		this.type = type;
	}

	private final String key;

    private final String defaultValue;

    private final Class<?> type;

    /**
     * Find value via property key.
     *
     * @param key property key
     * @return value enum, return {@code null} if not found
     */
    public static ShardingPropertiesConstant findByKey(final String key) {
        for (ShardingPropertiesConstant each : ShardingPropertiesConstant.values()) {
            if (each.getKey().equals(key)) {
                return each;
            }
        }
        return null;
    }

	public String getKey() {
		return key;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public Class<?> getType() {
		return type;
	}
}
