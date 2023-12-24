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
package org.openhab.binding.matter.internal;

import static org.openhab.binding.matter.internal.MatterBindingConstants.*;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.matter.internal.handler.ControllerHandler;
import org.openhab.binding.matter.internal.handler.EndpointHandler;
import org.openhab.binding.matter.internal.handler.LevelControlHandler;
import org.openhab.binding.matter.internal.handler.NodeHandler;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.BaseThingHandlerFactory;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerFactory;
import org.osgi.service.component.annotations.Component;

/**
 * The {@link MatterHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Dan Cunningham - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.matter", service = ThingHandlerFactory.class)
public class MatterHandlerFactory extends BaseThingHandlerFactory {
    // private final Logger logger = LoggerFactory.getLogger(MatterHandlerFactory.class);

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Set.of(THING_TYPE_CONTROLLER, THING_TYPE_NODE,
            THING_TYPE_ENDPOINT, THING_TYPE_CLUSTER, THING_TYPE_LEVEL_CONTROL);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (THING_TYPE_CONTROLLER.equals(thingTypeUID)) {
            return new ControllerHandler((Bridge) thing);
        }

        if (THING_TYPE_NODE.equals(thingTypeUID)) {
            return new NodeHandler((Bridge) thing);
        }

        if (THING_TYPE_ENDPOINT.equals(thingTypeUID)) {
            return new EndpointHandler((Bridge) thing);
        }

        if (THING_TYPE_LEVEL_CONTROL.equals(thingTypeUID)) {
            return new LevelControlHandler(thing);
        }
        return null;
        // try {
        // String handlerName = THING_TYPE_HANDLER_MAPPING.get(thingTypeUID);
        // if (handlerName == null) {
        // logger.warn("No handler found for {}", thingTypeUID);
        // return null;
        // }
        // Class<?> clazz = Class.forName(MatterHandlerFactory.class.getPackageName() + ".handler." + handlerName);
        // Constructor<?> constructor = clazz.getConstructor(Thing.class);
        // Object instance = constructor.newInstance(thing);
        // return (ThingHandler) instance;
        // } catch (ClassNotFoundException e) {
        // logger.warn("No handler found for {}", thingTypeUID);
        // return null;
        // } catch (Exception e) {
        // logger.warn("Could not create handler", e);
        // return null;
        // }
    }
}
