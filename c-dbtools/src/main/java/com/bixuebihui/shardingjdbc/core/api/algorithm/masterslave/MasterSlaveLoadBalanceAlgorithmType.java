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

package com.bixuebihui.shardingjdbc.core.api.algorithm.masterslave;


/**
 * Master-slave database load-balance algorithm type.
 *
 * @author zhangliang
 */
public enum MasterSlaveLoadBalanceAlgorithmType {

    ROUND_ROBIN(new RoundRobinMasterSlaveLoadBalanceAlgorithm()),
    RANDOM(new RandomMasterSlaveLoadBalanceAlgorithm());

    private MasterSlaveLoadBalanceAlgorithmType(MasterSlaveLoadBalanceAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	private final MasterSlaveLoadBalanceAlgorithm algorithm;

    /**
     * Get default master-slave database load-balance algorithm type.
     *
     * @return default master-slave database load-balance algorithm type
     */
    public static MasterSlaveLoadBalanceAlgorithmType getDefaultAlgorithmType() {
        return ROUND_ROBIN;
    }
}
