// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.segmentedpaths.components;

import org.joml.Vector3f;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.segmentedpaths.SegmentMeta;

/**
 * Used to create entities that follow paths.
 */
public class PathFollowerComponent implements Component<PathFollowerComponent> {
    @Replicate
    public SegmentMeta segmentMeta;
    @Replicate
    public Vector3f heading;

    @Override
    public void copy(PathFollowerComponent other) {
        this.segmentMeta = new SegmentMeta(other.segmentMeta);
        this.segmentMeta.sign = other.segmentMeta.sign;
        this.heading = new Vector3f(other.heading);
    }
}
