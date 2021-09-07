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
package org.terasology.segmentedpaths.events;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.gestalt.entitysystem.event.Event;

/**
 * Event called when an entity that has {@link org.terasology.segmentedpaths.components.PathFollowerComponent} enters a segment.
 */
public class OnVisitSegment implements Event {

    private EntityRef pathFollowingEntity;

    /**
     * Event constructor.
     * @param pathFollowingEntity The entity that entered the segment.
     */
    public OnVisitSegment(EntityRef pathFollowingEntity){
        this.pathFollowingEntity = pathFollowingEntity;
    }

    /**
     * Returns the entity that entered the segment.
     * @return The entity that entered the segment.
     */
    public EntityRef getPathFollowingEntity() {
        return pathFollowingEntity;
    }
}
