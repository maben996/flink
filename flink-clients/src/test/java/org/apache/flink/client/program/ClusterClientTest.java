/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.client.program;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.highavailability.HighAvailabilityServices;
import org.apache.flink.util.TestLogger;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Tests for the {@link ClusterClient}.
 */
public class ClusterClientTest extends TestLogger {

	/**
	 * FLINK-6641
	 *
	 * <p>Tests that the {@link ClusterClient} does not clean up HA data when being shut down.
	 */
	@Test
	public void testClusterClientShutdown() throws Exception {
		Configuration config = new Configuration();
		HighAvailabilityServices highAvailabilityServices = mock(HighAvailabilityServices.class);

		ClusterClient clusterClient = new StandaloneClusterClient(config, highAvailabilityServices);

		clusterClient.shutdown();

		// check that the client does not clean up HA data but closes the services
		verify(highAvailabilityServices, never()).closeAndCleanupAllData();
		verify(highAvailabilityServices).close();
	}
}
