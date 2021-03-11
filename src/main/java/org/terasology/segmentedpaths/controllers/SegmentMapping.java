/*
 * Copyright 2017 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.segmentedpaths.controllers;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.segmentedpaths.SegmentMeta;

/**
 * Interface for working with segments chained together.
 */
public interface SegmentMapping {

    /**
     * A class packing together the result of the mapping, composing of {@link Prefab} representing given segment and
     * {@link EntityRef} referencing its instance.
     */
    class MappingResult {
        public MappingResult(Prefab prefab, EntityRef entity) {
            this.prefab = prefab;
            this.entity = entity;
        }

        Prefab prefab;
        EntityRef entity;
    }

    /**
     * Enum representing the two ends of each segment.
     */
    enum SegmentEnd {
        START, END
    }

    /**
     * Returns segment that's chained to given segment at given end.
     *
     * @param meta Segment relative to which we want to get the next segment
     * @param ends End we want this segment to be chained with the next at
     * @return The next segment
     */
    MappingResult nextSegment(SegmentMeta meta, SegmentEnd ends);

}
