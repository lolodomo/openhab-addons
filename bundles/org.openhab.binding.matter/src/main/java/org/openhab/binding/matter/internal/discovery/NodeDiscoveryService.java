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
public class NodeDiscoveryService extends AbstractDiscoveryService implements ThingHandlerService {
    private final Logger logger = LoggerFactory.getLogger(NodeDiscoveryService.class);
    private @Nullable ThingHandler thingHandler;

    public NodeDiscoveryService() throws IllegalArgumentException {
        super(ClusterThingTypes.SUPPORTED_DISCOVERY_THING_TYPES_UIDS, 5, false);
    }

    @Override
    public void setThingHandler(ThingHandler handler) {
        if (handler instanceof NodeDiscoveryHandler childDiscoveryHandler) {
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

    public void discoverChildEndpointThing(ThingUID thingUID, ThingUID bridgeUID, String nodeId, Integer id) {
        logger.trace("discoverChildEndpointThing: {} {} {}", thingUID, bridgeUID, id);
        String fullId = nodeId + ":" + id;
        DiscoveryResult result = DiscoveryResultBuilder.create(thingUID).withLabel("Matter " + fullId)
                .withProperty("id", fullId).withRepresentationProperty("id").withBridge(bridgeUID).build();
        thingDiscovered(result);
    }

    @Override
    protected void startScan() {
    }
}
