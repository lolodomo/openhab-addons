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
package org.openhab.binding.matter.internal.handler;

import java.util.Collection;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.matter.internal.discovery.NodeDiscoveryHandler;
import org.openhab.binding.matter.internal.discovery.NodeDiscoveryService;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.thing.binding.ThingHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dan Cunningham
 *
 */
public abstract class AbstractMatterBridgeHandler extends BaseBridgeHandler implements NodeDiscoveryHandler {
    private final Logger logger = LoggerFactory.getLogger(AbstractMatterBridgeHandler.class);
    protected @Nullable NodeDiscoveryService discoveryService;

    public AbstractMatterBridgeHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public Collection<Class<? extends ThingHandlerService>> getServices() {
        return Set.of(NodeDiscoveryService.class);
    }

    @Override
    public void setDiscoveryService(@Nullable NodeDiscoveryService service) {
        this.discoveryService = service;
    }

    @Override
    public void startDiscovery() {
    }

    // @Override
    // public void childHandlerInitialized(ThingHandler childHandler, Thing childThing) {
    // logger.debug("childHandlerInitialized: {} {}", childHandler, childThing);
    // MatterClient client = this.client;
    // if (childHandler instanceof AbstractMatterBridgeHandler && client != null) {
    // ((AbstractMatterBridgeHandler) childHandler).setMatterClient(client);
    // }
    // }
}
