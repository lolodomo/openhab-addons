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
package org.openhab.binding.matter.internal.client.model.cluster.types;

/**
 * @author Dan Cunningham
 *
 */
public class FloatType implements JsonSerializable {

    public final Float value;

    public FloatType(Float value) {
        this.value = value;
    }

    @Override
    public String toJson() {
        return "" + value.floatValue();
    }
}
