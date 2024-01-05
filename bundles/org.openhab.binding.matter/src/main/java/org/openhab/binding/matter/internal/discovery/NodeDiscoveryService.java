/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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

import static org.openhab.binding.matter.internal.MatterBindingConstants.*;

import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.config.discovery.DiscoveryService;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dan Cunningham
 *
 */
public class NodeDiscoveryService extends AbstractDiscoveryService implements DiscoveryService, ThingHandlerService {
    private final Logger logger = LoggerFactory.getLogger(NodeDiscoveryService.class);

    private static final Set<ThingTypeUID> SUPPORTED_DISCOVERY_THING_TYPES_UIDS = Set.of(THING_TYPE_NODE,
            THING_TYPE_ENDPOINT, THING_TYPE_CLUSTER);

    private @Nullable ThingHandler thingHandler;

    public NodeDiscoveryService() throws IllegalArgumentException {
        super(SUPPORTED_DISCOVERY_THING_TYPES_UIDS, 5, false);
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
    protected void startScan() {
        ThingHandler handler = this.thingHandler;
        if (handler != null) {
            ((NodeDiscoveryHandler) handler).startDiscovery();
        }
    }

    @Override
    public void activate() {
        super.activate(null);
    }

    @Override
    public void deactivate() {
        super.deactivate();
    }

    public void discoverhildThing(ThingUID thingUID, ThingUID bridgeUID, Long id, String label) {
        logger.trace("discoverChildThing: {} {} {} {}", thingUID, bridgeUID, id, label);
        DiscoveryResult result = DiscoveryResultBuilder.create(thingUID).withLabel(label).withProperty("id", id)
                .withRepresentationProperty("id").withBridge(bridgeUID).build();
        thingDiscovered(result);
    }
}
