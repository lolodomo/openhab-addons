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
package org.openhab.binding.matter.internal.converter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.matter.internal.client.AttributeListener;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.ColorControlCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.LevelControlCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.OnOffCluster;
import org.openhab.binding.matter.internal.handler.EndpointHandler;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.binding.builder.ChannelBuilder;
import org.openhab.core.thing.binding.builder.ThingBuilder;
import org.openhab.core.thing.type.ChannelTypeUID;
import org.openhab.core.types.Command;
import org.openhab.core.types.StateDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ClusterConverter} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Dan Cunningham - Initial contribution
 */
@NonNullByDefault
public abstract class ClusterConverter implements AttributeListener {

    private final Logger logger = LoggerFactory.getLogger(ClusterConverter.class);
    private static final Map<Integer, Class<? extends ClusterConverter>> converterMapping = new HashMap();
    protected BigInteger nodeId;
    protected int endpointId;
    // protected @Nullable BaseCluster cluster;
    protected EndpointHandler handler;

    static {
        converterMapping.put(LevelControlCluster.CLUSTER_ID, LevelControlConverter.class);
        converterMapping.put(OnOffCluster.CLUSTER_ID, OnOffConverter.class);
        converterMapping.put(ColorControlCluster.CLUSTER_ID, ColorControlConverter.class);
    }

    public static @Nullable Class<? extends ClusterConverter> getConverterClass(int clusterId) {
        return converterMapping.get(clusterId);
    }

    public ClusterConverter(EndpointHandler handler) {
        this.handler = handler;
        this.nodeId = handler.getNodeId();
        this.endpointId = handler.getEndpointId();
    }

    public abstract List<Integer> supportedClusters();

    public abstract void handleCommand(ChannelUID channelUID, Command command);

    public abstract void updateCluster(BaseCluster cluster);

    public abstract List<ChannelUID> createChannels(BaseCluster cluster);

    protected Map<ChannelUID, StateDescription> stateDescriptions = new HashMap<>();

    public @Nullable StateDescription getStateDescription(ChannelUID channelUID) {
        return stateDescriptions.get(channelUID);
    }

    protected ChannelUID createChannel(BaseCluster cluster, String channelName, ChannelTypeUID channelType,
            String channelLabel, String itemType) {

        Channel channel = ChannelBuilder.create(createChannelUID(cluster, channelName), itemType).withType(channelType)
                .withLabel(channelLabel).build();
        List<Channel> channels = new ArrayList<>(handler.getThing().getChannels());

        boolean hasMatchingUID = channels.stream().anyMatch(c -> channel.getUID().equals(c.getUID()));
        if (!hasMatchingUID) {
            logger.debug("createChannel: adding channel {}", channel.getUID());
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

    /**
     * Converts a ZigBee 8 bit level as used in Level Control cluster and others to a percentage
     *
     * @param level an integer between 0 and 254
     * @return the scaled {@link PercentType}
     */
    protected PercentType levelToPercent(int level) {
        return new PercentType((int) (level * 100.0 / 254.0 + 0.5));
    }

    /**
     * Converts a {@link DecimalType} to an 8 bit level scaled between 0 and 254
     *
     * @param percent the {@link DecimalType} to convert
     * @return a scaled value between 0 and 254
     */

    protected int percentToLevel(PercentType percent) {
        return (int) (percent.floatValue() * 254.0f / 100.0f + 0.5f);
    }
}
