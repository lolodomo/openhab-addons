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
package org.openhab.binding.matter.internal.handler;

import static org.openhab.binding.matter.internal.MatterBindingConstants.CHANNEL_1;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.config.ClusterConfiguration;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.thing.binding.BridgeHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ClusterHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Dan Cunningham - Initial contribution
 */
@NonNullByDefault
public class ClusterHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(ClusterHandler.class);
    private int clusterId;
    private @Nullable BaseCluster cluster;

    public ClusterHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (CHANNEL_1.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                EndpointHandler handler = endpointHandler();
                if (handler != null) {
                    handler.refresh();
                }
            }
        }
    }

    @Override
    public void initialize() {
        ClusterConfiguration config = getConfigAs(ClusterConfiguration.class);
        clusterId = config.id;
        logger.debug("initialize cluster {}", clusterId);
        initializeCluster();
    }

    private void initializeCluster() {
        EndpointHandler handler = endpointHandler();
        if (handler == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_UNINITIALIZED);
        } else if (handler.getThing().getStatus() != ThingStatus.ONLINE) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE);
        } else {
            updateStatus(ThingStatus.ONLINE);
            scheduler.execute(() -> {
                handler.refresh();
            });
        }
    }

    @Override
    public void dispose() {
    }

    public int getClusterId() {
        return clusterId;
    }

    public void updateCluster(BaseCluster cluster) {
        this.cluster = cluster;
        logger.debug("Endpoint: {} Cluster: {} ", cluster.id, cluster.name);
        logger.debug("{} ", cluster);
    }

    private @Nullable EndpointHandler endpointHandler() {
        Bridge bridge = getBridge();
        if (bridge != null) {
            BridgeHandler handler = bridge.getHandler();
            if (handler instanceof EndpointHandler endpointHandler) {
                return endpointHandler;
            }
        }
        return null;
    }
}
