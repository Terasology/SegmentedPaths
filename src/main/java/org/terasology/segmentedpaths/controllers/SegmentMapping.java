// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.segmentedpaths.controllers;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.segmentedpaths.SegmentMeta;

/**
 * Interface for working with segments chained together.
 */
public interface SegmentMapping {

    /**
     * Returns segment that's chained to given segment at given end.
     *
     * @param meta Segment relative to which we want to get the next segment
     * @param ends End we want this segment to be chained with the next at
     * @return The next segment
     */
    MappingResult nextSegment(SegmentMeta meta, SegmentEnd ends);

    /**
     * Enum representing the two ends of each segment.
     */
    enum SegmentEnd {
        START, END
    }

    /**
     * A class packing together the result of the mapping, composing of {@link Prefab} representing given segment and
     * {@link EntityRef} referencing its instance.
     */
    class MappingResult {
        Prefab prefab;
        EntityRef entity;
        public MappingResult(Prefab prefab, EntityRef entity) {
            this.prefab = prefab;
            this.entity = entity;
        }
    }

}
