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
package org.openhab.binding.matter.internal.mapper;

import static org.openhab.binding.matter.internal.MatterBindingConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.matter.internal.client.MatterWebsocketClient;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.ClusterCommand;
import org.openhab.binding.matter.internal.client.model.cluster.gen.ColorControlCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.ColorControlCluster.Options;
import org.openhab.binding.matter.internal.client.model.cluster.gen.DeviceTypes;
import org.openhab.binding.matter.internal.client.model.cluster.gen.LevelControlCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.LevelControlCluster.OptionsBitmap;
import org.openhab.binding.matter.internal.client.model.cluster.gen.OnOffCluster;
import org.openhab.binding.matter.internal.client.model.ws.AttributeChangedMessage;
import org.openhab.binding.matter.internal.handler.EndpointHandler;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dan Cunningham
 *
 */
@NonNullByDefault
public class LightingType extends DeviceType {
    private final Logger logger = LoggerFactory.getLogger(LightingType.class);
    private PercentType lastLevel = new PercentType(0);
    private OnOffType lastOnOff = OnOffType.OFF;
    private boolean supportsHue = false;
    private int lastHue = -1;
    private int lastSaturation = -1;
    private boolean hueChanged = false;
    private boolean saturationChanged = false;
    private @Nullable ScheduledFuture<?> colorUpdateTimer = null;
    private int lastX = -1;
    private int lastY = -1;
    private boolean xChanged = false;
    private boolean yChanged = false;
    private HSBType lastHSB = new HSBType("0,0,100");
    private Options optionsMask = new Options(true);
    private ScheduledExecutorService colorUpdateScheduler = Executors.newSingleThreadScheduledExecutor();

    public LightingType(EndpointHandler handler, Integer deviceType) {
        super(handler, deviceType);
    }

    public static List<Integer> supportedTypes() {
        return List.of(DeviceTypes.OnOffLight, DeviceTypes.OnOffLightSwitch, DeviceTypes.DimmableLight,
                DeviceTypes.DimmablePlugInUnit, DeviceTypes.DimmerSwitch, DeviceTypes.ColorDimmerSwitch,
                DeviceTypes.ExtendedColorLight, DeviceTypes.ColorTemperatureLight);
    }

    @Override
    public List<Integer> supportedClusters() {
        return List.of(LevelControlCluster.CLUSTER_ID, OnOffCluster.CLUSTER_ID, ColorControlCluster.CLUSTER_ID);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        MatterWebsocketClient client = handler.getClient();
        if (client == null) {
            return;
        }
        if (command instanceof HSBType color) {
            PercentType brightness = color.getBrightness();

            sendLevel(client, brightness);

            if (supportsHue) {
                changeColorHueSaturation(client, color);
            } else {
                changeColorXY(client, color);
            }
        } else if (command instanceof PercentType percentType) {
            sendLevel(client, percentType);
        } else if (command instanceof OnOffType onOffType) {
            sendOnOff(client, onOffType == OnOffType.ON);
        }
    }

    @Override
    public void updateCluster(BaseCluster cluster) {
        if (cluster instanceof ColorControlCluster colorControlCluster) {
            supportsHue = colorControlCluster.featureMap.hueSaturation;
            lastX = colorControlCluster.currentX;
            lastY = colorControlCluster.currentY;
            lastHue = colorControlCluster.currentHue;
            lastSaturation = colorControlCluster.currentSaturation;
            if (supportsHue) {
                updateColorHSB();
            } else {
                updateColorXY();
            }
        }
        if (cluster instanceof LevelControlCluster levelControlCluster) {
            lastLevel = levelToPercent(levelControlCluster.currentLevel);
            updateState(CHANNEL_SWITCH_LEVEL, lastLevel);
            updateState(CHANNEL_SWITCH_ONOFF, OnOffType.from(lastLevel.intValue() > 0));
        }
        if (cluster instanceof OnOffCluster onOffCluster) {
            lastOnOff = OnOffType.from(Boolean.valueOf(onOffCluster.onOff));
            logger.debug("OnOff {}", lastOnOff);
            updateState(CHANNEL_SWITCH_LEVEL, lastOnOff);
            updateState(CHANNEL_SWITCH_ONOFF, lastOnOff == OnOffType.ON ? lastLevel : new PercentType(0));
        }
    }

    @Override
    public List<ChannelUID> createChannels(Map<String, BaseCluster> clusters) {
        List<ChannelUID> channels = new ArrayList<>();
        clusters.forEach((name, cluster) -> {
            switch (name) {
                case OnOffCluster.CLUSTER_NAME:
                    // don't add a switch to dimmable devices
                    if (deviceType.equals(DeviceTypes.OnOffLight) || deviceType.equals(DeviceTypes.OnOffLightSwitch)) {
                        channels.add(createChannel(cluster, CHANNEL_NAME_SWITCH_ONOFF, CHANNEL_SWITCH_ONOFF,
                                CHANNEL_LABEL_SWITCH_ONOFF, ITEM_TYPE_SWITCH));
                    }
                    break;
                case LevelControlCluster.CLUSTER_NAME:
                    // don't add a dimmer to only switchable devices
                    if (!deviceType.equals(DeviceTypes.OnOffLight)
                            && !deviceType.equals(DeviceTypes.OnOffLightSwitch)) {
                        channels.add(createChannel(cluster, CHANNEL_NAME_SWITCH_LEVEL, CHANNEL_SWITCH_LEVEL,
                                CHANNEL_LABEL_SWITCH_LEVEL, ITEM_TYPE_DIMMER));
                    }
                    break;
                case ColorControlCluster.CLUSTER_NAME:
                    channels.add(createChannel(cluster, CHANNEL_NAME_COLOR_COLOR, CHANNEL_COLOR_COLOR,
                            CHANNEL_LABEL_COLOR_COLOR, ITEM_TYPE_COLOR));
                    break;
            }
        });
        return channels;
    }

    @Override
    public void onEvent(AttributeChangedMessage message) {
        logger.debug("OnEvent: {}", message.path.attributeName);
        Integer numberValue = message.value instanceof Number number ? number.intValue() : 0;
        switch (message.path.attributeName) {
            case "currentX":
                lastX = numberValue;
                xChanged = true;
                break;
            case "currentY":
                lastY = numberValue;
                yChanged = true;
                break;
            case "currentHue":
                lastHue = numberValue;
                hueChanged = true;
                break;
            case "currentSaturation":
                lastSaturation = numberValue;
                saturationChanged = true;
                break;
            case "colorTemperatureMireds":
                break;
            case "enhancedCurrentHue":
                break;
            case "onOff":
                lastOnOff = OnOffType.from((Boolean) message.value);
                updateState(CHANNEL_SWITCH_ONOFF, lastOnOff);
                updateState(CHANNEL_SWITCH_LEVEL, lastOnOff == OnOffType.ON ? lastLevel : new PercentType(0));
                break;
            case "currentLevel":
                logger.debug("currentLevel {}", message.value);
                lastLevel = levelToPercent(numberValue);
                updateState(CHANNEL_SWITCH_LEVEL, lastLevel);
                updateState(CHANNEL_SWITCH_ONOFF, OnOffType.from(lastLevel.intValue() > 0));
                updateBrightness(lastLevel);
                break;
            default:
                logger.debug("Unknown attribute {}", message.path.attributeName);
        }
        if (supportsHue && (hueChanged || saturationChanged)) {
            if (colorUpdateTimer != null) {
                colorUpdateTimer.cancel(true);
            }
            colorUpdateTimer = colorUpdateScheduler.schedule(() -> updateColorHSB(), 500, TimeUnit.MILLISECONDS);
        }
        if (!supportsHue && (xChanged || yChanged)) {
            if (colorUpdateTimer != null) {
                colorUpdateTimer.cancel(true);
            }
            colorUpdateTimer = colorUpdateScheduler.schedule(() -> updateColorXY(), 500, TimeUnit.MILLISECONDS);
        }
    }

    // These functions are borrowed from the Zigbee openHAB binding

    private void updateBrightness(PercentType brightness) {
        // Extra temp variable to avoid thread sync concurrency issues on lastHSB
        HSBType oldHSB = lastHSB;
        HSBType newHSB = new HSBType(oldHSB.getHue(), oldHSB.getSaturation(), brightness);
        lastHSB = newHSB;
        updateState(CHANNEL_COLOR_COLOR, newHSB);
    }

    private void updateColorHSB(DecimalType hue, PercentType saturation) {
        // Extra temp variable to avoid thread sync concurrency issues on lastHSB
        HSBType oldHSB = lastHSB;
        HSBType newHSB = new HSBType(hue, saturation, oldHSB.getBrightness());
        lastHSB = newHSB;
        updateState(CHANNEL_COLOR_COLOR, newHSB);
    }

    private void updateColorXY(PercentType x, PercentType y) {
        HSBType color = HSBType.fromXY(x.floatValue() / 100.0f, y.floatValue() / 100.0f);
        updateColorHSB(color.getHue(), color.getSaturation());
    }

    private void updateColorHSB() {
        float hueValue = lastHue * 360.0f / 254.0f;
        float saturationValue = lastSaturation * 100.0f / 254.0f;
        DecimalType hue = new DecimalType(Float.valueOf(hueValue).toString());
        PercentType saturation = new PercentType(Float.valueOf(saturationValue).toString());
        updateColorHSB(hue, saturation);
        hueChanged = false;
        saturationChanged = false;
    }

    private void updateColorXY() {
        float xValue = lastX / 65536.0f;
        float yValue = lastY / 65536.0f;
        PercentType x = new PercentType(Float.valueOf(xValue * 100.0f).toString());
        PercentType y = new PercentType(Float.valueOf(yValue * 100.0f).toString());
        updateColorXY(x, y);
        xChanged = false;
        yChanged = false;
    }

    private void changeColorHueSaturation(MatterWebsocketClient client, HSBType color) {
        int hue = (int) (color.getHue().floatValue() * 254.0f / 360.0f + 0.5f);
        int saturation = percentToLevel(color.getSaturation());
        client.clusterCommand(handler.getNodeId(), handler.getEndpointId(), ColorControlCluster.CLUSTER_NAME,
                ColorControlCluster.moveToHueAndSaturation(hue, saturation, 0, optionsMask, optionsMask));
    }

    private void changeColorXY(MatterWebsocketClient client, HSBType color) {
        PercentType xy[] = color.toXY();
        int x = (int) (xy[0].floatValue() / 100.0f * 65536.0f + 0.5f); // up to 65279
        int y = (int) (xy[1].floatValue() / 100.0f * 65536.0f + 0.5f); // up to 65279
        client.clusterCommand(handler.getNodeId(), handler.getEndpointId(), ColorControlCluster.CLUSTER_NAME,
                ColorControlCluster.moveToColor(x, y, 0, optionsMask, optionsMask));
    }

    private void sendOnOff(MatterWebsocketClient client, boolean on) {
        ClusterCommand onOffCommand = on ? OnOffCluster.on() : OnOffCluster.off();
        client.clusterCommand(handler.getNodeId(), handler.getEndpointId(), OnOffCluster.CLUSTER_NAME, onOffCommand);
    }

    private void sendLevel(MatterWebsocketClient client, PercentType level) {
        ClusterCommand levelCommand = LevelControlCluster.moveToLevelWithOnOff(percentToLevel(level), 0,
                new OptionsBitmap(true, true), new OptionsBitmap(true, true));
        client.clusterCommand(handler.getNodeId(), handler.getEndpointId(), LevelControlCluster.CLUSTER_NAME,
                levelCommand);
    }
}
