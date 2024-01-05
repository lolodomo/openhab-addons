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

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.matter.internal.client.MatterClient;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.binding.builder.ChannelBuilder;
import org.openhab.core.thing.binding.builder.ThingBuilder;
import org.openhab.core.thing.type.ChannelTypeUID;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ClusterHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Dan Cunningham - Initial contribution
 */
@NonNullByDefault
public abstract class ClusterHandler {

    private final Logger logger = LoggerFactory.getLogger(ClusterHandler.class);
    protected long nodeId;
    protected int endpointId;
    protected int clusterId;
    // protected @Nullable BaseCluster cluster;
    protected @Nullable MatterClient client;
    protected EndpointHandler handler;

    public ClusterHandler(EndpointHandler handler, long nodeId, int endpointId, int clusterId) {
        this.handler = handler;
        this.nodeId = nodeId;
        this.endpointId = endpointId;
        this.clusterId = clusterId;
    }

    public int getClusterId() {
        return clusterId;
    }

    public abstract void handleCommand(ChannelUID channelUID, Command command);

    public abstract void updateCluster(BaseCluster cluster);

    public abstract List<ChannelUID> createChannels(BaseCluster cluster);

    protected abstract void registerListeners();

    protected ChannelUID createChannel(BaseCluster cluster, String channelName, ChannelTypeUID channelType,
            String channelLabel, String itemType) {

        Channel channel = ChannelBuilder.create(createChannelUID(cluster, channelName), itemType).withType(channelType)
                .withLabel(channelLabel).build();
        List<Channel> channels = handler.getThing().getChannels();

        boolean hasMatchingUID = channels.stream().anyMatch(c -> channel.getUID().equals(c.getUID()));
        if (!hasMatchingUID) {
            channels.add(channel);
            ThingBuilder thingBuilder = handler.editThing();
            thingBuilder.withChannels(channels);
            handler.updateThing(thingBuilder.build());
        }
        return channel.getUID();
    }

    protected ChannelUID createChannelUID(BaseCluster cluster, String channelName) {
        return new ChannelUID(handler.getThing().getUID(), cluster.name + "_" + channelName);
    }

}
