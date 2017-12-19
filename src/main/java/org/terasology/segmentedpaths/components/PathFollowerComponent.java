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
package org.terasology.segmentedpaths.components;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.math.geom.Vector3f;
import org.terasology.network.Replicate;

/**
 * Used to create entities that follow paths.
 */
public class PathFollowerComponent implements Component {
    /**
     * The position of this path follower entity along the current segment, ranges from 0 (start) to 1 (end).
     */
    @Replicate
    public float segmentPosition;
    /**
     * Descriptor for the currently followed path that stores possible paths.
     */
    @Replicate
    public Prefab descriptor;
    /**
     * The entity of the segment currently being followed
     */
    @Replicate
    public EntityRef segmentEntity;
    /**
     * The heading (forward-facing look) vector of the path follower in it's current position along a segment.
     */
    @Replicate
    public Vector3f heading;
}
