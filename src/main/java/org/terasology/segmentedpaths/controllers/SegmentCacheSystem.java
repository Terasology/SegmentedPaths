// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
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
    private final HashMap<String, Segment> segments = new HashMap<>();

    /**
     * Gets a segment based off the given prefab. If the specified segment has already been constructed, returns it from
     * the cache, otherwise constructs it from the prefab.
     *
     * @param prefab A prefab that describes the segment.
     * @return A segment based on the given prefab, or null if the given prefab is missing a path-describing component.
     */
    public Segment getSegment(Prefab prefab) {

        Segment segment = segments.get(prefab.getName());
        if (segment != null)
            return segment;

        if (prefab.hasComponent(CurvedPathComponent.class)) {
            CurvedPathComponent pathComponent = prefab.getComponent(CurvedPathComponent.class);
            if (pathComponent == null)
                return null;

            CurvedPathComponent.CubicBezier[] c = new CurvedPathComponent.CubicBezier[pathComponent.path.size()];
            pathComponent.path.toArray(c);
            segment = new CurvedSegment(c, pathComponent.startingBinormal);
            segments.put(prefab.getName(), segment);
        } else if (prefab.hasComponent(LinearPathComponent.class)) {
            LinearPathComponent pathComponent = prefab.getComponent(LinearPathComponent.class);
            if (pathComponent == null)
                return null;
            LinearPathComponent.Linear[] c = new LinearPathComponent.Linear[pathComponent.path.size()];
            pathComponent.path.toArray(c);
            segment = new LinearSegment(c);
            segments.put(prefab.getName(), segment);

        }

        return segment;
    }

}
