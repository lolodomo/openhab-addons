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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.matter.internal.client.MatterClient;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.config.ClusterConfiguration;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.thing.binding.BridgeHandler;
import org.openhab.core.thing.binding.builder.ChannelBuilder;
import org.openhab.core.thing.binding.builder.ThingBuilder;
import org.openhab.core.thing.type.ChannelTypeUID;
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
public abstract class ClusterHandler extends BaseThingHandler implements MatterClientSetter {

    private final Logger logger = LoggerFactory.getLogger(ClusterHandler.class);
    protected long nodeId;
    protected long endpointId;
    protected int clusterId;
    protected @Nullable BaseCluster cluster;
    protected @Nullable MatterClient client;

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

    @Override
    public void dispose() {
    }

    @Override
    public void setMatterClient(MatterClient client) {
        this.client = client;
    }

    public int getClusterId() {
        return clusterId;
    }

    public void updateCluster(BaseCluster cluster) {
        this.cluster = cluster;
        logger.debug("Endpoint: {} Cluster: {} ", cluster.id, cluster.name);
        logger.debug("{} ", cluster);
        createChannels();
    }

    protected @Nullable EndpointHandler endpointHandler() {
        Bridge bridge = getBridge();
        if (bridge != null) {
            BridgeHandler handler = bridge.getHandler();
            if (handler instanceof EndpointHandler endpointHandler) {
                return endpointHandler;
            }
        }
        return null;
    }

    protected abstract void createChannels();

    protected abstract void registerListeners();

    protected void createChannel(String channelName, ChannelTypeUID channelType, String channelLabel, String itemType) {
        BaseCluster cluster = this.cluster;
        if (cluster != null) {
            Channel channel = ChannelBuilder.create(createChannelUID(cluster, channelName), itemType)
                    .withType(channelType).withLabel(channelLabel).build();
            // replace existing buttonPress with updated one
            List<Channel> newChannels = new ArrayList<>();
            for (Channel c : getThing().getChannels()) {
                if (!c.getUID().equals(channel.getUID())) {
                    newChannels.add(c);
                }
            }
            newChannels.add(channel);
            ThingBuilder thingBuilder = editThing();
            thingBuilder.withChannels(newChannels);
            updateThing(thingBuilder.build());
        }
    }

    protected ChannelUID createChannelUID(BaseCluster cluster, String channelName) {
        return new ChannelUID(getThing().getUID(), cluster.name + "_" + channelName);
    }

    private void initializeCluster() {
        EndpointHandler handler = endpointHandler();
        if (handler == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_UNINITIALIZED);
        } else if (handler.getThing().getStatus() != ThingStatus.ONLINE) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE);
        } else {
            updateStatus(ThingStatus.ONLINE);
            EndpointHandler eh = endpointHandler();
            if (eh != null) {

            }
            scheduler.execute(() -> {
                handler.refresh();
            });
        }
    }
}
