// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.segmentedpaths.events;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.Event;

/**
 * Event called when an entity that has {@link org.terasology.segmentedpaths.components.PathFollowerComponent} leaves a
 * segment.
 */
public class OnExitSegment implements Event {

    private final EntityRef pathFollowingEntity;

    /**
     * Event constructor.
     *
     * @param pathFollowingEntity The entity that left the segment.
     */
    public OnExitSegment(EntityRef pathFollowingEntity) {
        this.pathFollowingEntity = pathFollowingEntity;
    }

    /**
     * Returns the entity that left the segment.
     *
     * @return The entity that left the segment.
     */
    public EntityRef getPathFollowingEntity() {
        return pathFollowingEntity;
    }
}
