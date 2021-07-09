// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.segmentedpaths.components;

import org.terasology.engine.math.Side;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * Created by michaelpollind on 4/3/17.
 */
public class BlockMappingComponent implements Component<BlockMappingComponent> {
    public Side s1;
    public Side s2;

    @Override
    public void copy(BlockMappingComponent other) {
        this.s1 = other.s1;
        this.s2 = other.s2;
    }
}
