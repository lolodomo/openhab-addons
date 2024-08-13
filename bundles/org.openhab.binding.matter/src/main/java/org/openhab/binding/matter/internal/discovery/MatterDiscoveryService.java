/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.matter.internal.discovery;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.matter.internal.client.model.Endpoint;
import org.openhab.binding.matter.internal.client.model.Node;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.BasicInformationCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.ClusterThingTypes;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dan Cunningham
 *
 */
@NonNullByDefault
public class MatterDiscoveryService extends AbstractDiscoveryService implements ThingHandlerService {
    private final Logger logger = LoggerFactory.getLogger(MatterDiscoveryService.class);
    private @Nullable ThingHandler thingHandler;

    public MatterDiscoveryService() throws IllegalArgumentException {
        super(ClusterThingTypes.SUPPORTED_DISCOVERY_THING_TYPES_UIDS, 5, false);
    }

    @Override
    public void setThingHandler(ThingHandler handler) {
        logger.debug("setThingHandler {}", handler);
        if (handler instanceof MatterDiscoveryHandler childDiscoveryHandler) {
            childDiscoveryHandler.setDiscoveryService(this);
            this.thingHandler = handler;
        }
    }

    @Override
    public @Nullable ThingHandler getThingHandler() {
        return thingHandler;
    }

    @Override
    public void activate() {
        super.activate(null);
    }

    @Override
    public void deactivate() {
        super.deactivate();
    }

    public void discoverChildEndpointThing(ThingUID thingUID, ThingUID bridgeUID, Node node, Integer endpointId) {
        logger.debug("discoverChildEndpointThing: {} {} {}", thingUID, bridgeUID, endpointId);
        String vendorName = "";
        String productName = "";
        Endpoint root = node.endpoints.get(Integer.valueOf(0));
        if (root != null) {
            BaseCluster cluster = root.clusters.get(BasicInformationCluster.CLUSTER_NAME);
            if (cluster != null && cluster instanceof BasicInformationCluster basicCluster) {
                vendorName = basicCluster.vendorName;
                productName = basicCluster.productName;
            }
        }
        String idSting = node.id.toString();
        String shortId = (idSting.length() > 5 ? idSting.substring(idSting.length() - 5) : idSting) + "-" + endpointId;
        String label = "Matter Device " + shortId + " " + (vendorName + " " + productName).trim();
        String path = idSting + ":" + endpointId;
        DiscoveryResult result = DiscoveryResultBuilder.create(thingUID).withLabel(label)
                .withProperty("nodeId", node.id).withProperty("endpointId", endpointId).withProperty("path", path)
                .withRepresentationProperty("path").withBridge(bridgeUID).build();
        thingDiscovered(result);
    }

    @Override
    protected void startScan() {
        ThingHandler handler = this.thingHandler;
        if (handler != null && handler instanceof MatterDiscoveryHandler childDiscoveryHandler) {
            childDiscoveryHandler.startScan();
        }
    }
}
