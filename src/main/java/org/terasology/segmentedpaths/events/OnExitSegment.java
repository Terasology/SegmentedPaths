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

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;

/**
 * Event called when an entity which has {@link org.terasology.segmentedpaths.components.PathFollowerComponent} leaves a segment.
 */
public class OnExitSegment implements Event {

    /**
     * EntityRef to an entity, which leaves a segment.
     */
    private EntityRef pathFollowingEntity;

    /**
     * Event constructor.
     * @param pathFollowingEntity which leaves a segment.
     */
    public OnExitSegment(EntityRef pathFollowingEntity){
        this.pathFollowingEntity = pathFollowingEntity;
    }

    /**
     * @return the entity, which leaves a segment.
     */
    public EntityRef getPathFollowingEntity() {
        return pathFollowingEntity;
    }
}
