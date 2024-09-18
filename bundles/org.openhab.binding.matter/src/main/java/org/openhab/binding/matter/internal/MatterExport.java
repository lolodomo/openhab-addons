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
package org.openhab.binding.matter.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.items.MetadataRegistry;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link MatterExport}
 *
 * @author Dan Cunningham - Initial contribution
 */
@Component(immediate = true, service = MatterExport.class)
@NonNullByDefault
public class MatterExport {
    private final Logger logger = LoggerFactory.getLogger(MatterExport.class);

    @Reference
    protected @NonNullByDefault({}) ItemRegistry itemRegistry;

    @Reference
    protected @NonNullByDefault({}) MetadataRegistry metadataRegistry;

    @Activate
    protected void activate() {
        // this is working, commented out until i can work on it more.
        // metadataRegistry.getAll().forEach(metadata -> {
        // final MetadataKey uid = metadata.getUID();
        // if ("homekit".equals(uid.getNamespace())) {
        // try {
        // Item item = itemRegistry.getItem(uid.getItemName());
        // logger.debug("Item {} : {} ", item.getLabel(), item.getType());
        // if (item instanceof GroupItem group) {
        // group.getAllMembers().forEach(gi -> logger.debug(" {} : {}", gi.getName(), gi.getType()));
        // }
        // } catch (ItemNotFoundException e) {
        // logger.debug("Could not find item {}", uid.getItemName(), e);
        // }
        // }
        // });
    }
}
