// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.segmentedpaths.blocks;

import org.terasology.engine.math.Rotation;
import org.terasology.engine.world.block.BlockUri;

/**
 * To be implemented by blocks that use the segment system. Used by the segment system to get the rotation of the
 * segment for the associated block.
 */
public interface PathFamily {
    /**
     * @return the rotation of the segment for the associated block.
     */
    Rotation getRotationFor(BlockUri blockUri);
}
