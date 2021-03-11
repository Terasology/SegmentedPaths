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

import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.Share;
import org.terasology.segmentedpaths.components.CurvedPathComponent;
import org.terasology.segmentedpaths.components.LinearPathComponent;
import org.terasology.segmentedpaths.segments.CurvedSegment;
import org.terasology.segmentedpaths.segments.LinearSegment;
import org.terasology.segmentedpaths.segments.Segment;

import java.util.HashMap;

/**
 * Cache system for segments. Constructs, stores and supplies segments based off their prefabs.
 */
@RegisterSystem(RegisterMode.AUTHORITY)
@Share(value = SegmentCacheSystem.class)
public class SegmentCacheSystem extends BaseComponentSystem {

    //A map that stores already constructed segments with their prefab's name as the key.
    private HashMap<String, Segment> segments = new HashMap<>();

    /**
     * Gets a segment based off the given prefab. If the specified segment has already been constructed, returns it from
     * the cache, otherwise constructs it from the prefab.
     *
     * @param prefab A prefab that describes the segment.
     * @return A segment based on the given prefab, or null if the given prefab is missing a path-describing component.
     */
    public Segment getSegment(Prefab prefab) {

        Segment segment = segments.get(prefab.getName());
        if (segment != null) {
            return segment;
        }

        if (prefab.hasComponent(CurvedPathComponent.class)) {
            CurvedPathComponent pathComponent = prefab.getComponent(CurvedPathComponent.class);
            if (pathComponent == null) {
                return null;
            }

            CurvedPathComponent.CubicBezier[] c = new CurvedPathComponent.CubicBezier[pathComponent.path.size()];
            pathComponent.path.toArray(c);
            segment = new CurvedSegment(c, pathComponent.binormal);
            segments.put(prefab.getName(), segment);
        } else if (prefab.hasComponent(LinearPathComponent.class)) {
            LinearPathComponent pathComponent = prefab.getComponent(LinearPathComponent.class);
            if (pathComponent == null) {
                return null;
            }
            LinearPathComponent.Linear[] c = new LinearPathComponent.Linear[pathComponent.path.size()];
            pathComponent.path.toArray(c);
            segment = new LinearSegment(c);
            segments.put(prefab.getName(), segment);

        }

        return segment;
    }

}
