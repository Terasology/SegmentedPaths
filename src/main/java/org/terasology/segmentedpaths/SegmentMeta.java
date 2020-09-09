// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.segmentedpaths;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.network.Replicate;
import org.terasology.reflection.MappedContainer;

@MappedContainer
public class SegmentMeta {
    @Replicate
    public float position;
    @Replicate
    public EntityRef association;
    @Replicate
    public Prefab prefab;
    @Replicate
    public int sign = 1;

    public SegmentMeta() {

    }

    public SegmentMeta(SegmentMeta meta) {
        this.position = meta.position;
        this.association = meta.association;
        this.prefab = meta.prefab;
    }

    public SegmentMeta(float position, EntityRef association, Prefab prefab) {
        this.prefab = prefab;
        this.association = association;
        this.position = position;
    }
}
