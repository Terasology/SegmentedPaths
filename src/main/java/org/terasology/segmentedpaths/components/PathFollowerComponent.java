// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.segmentedpaths.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;
import org.terasology.math.geom.Vector3f;
import org.terasology.segmentedpaths.SegmentMeta;

/**
 * Used to create entities that follow paths.
 */
public class PathFollowerComponent implements Component {
    @Replicate
    public SegmentMeta segmentMeta;
    @Replicate
    public Vector3f heading;
}
