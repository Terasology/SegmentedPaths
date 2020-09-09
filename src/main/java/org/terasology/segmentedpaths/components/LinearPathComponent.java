// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.segmentedpaths.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.math.geom.Vector3f;
import org.terasology.reflection.MappedContainer;

import java.util.List;

public class LinearPathComponent implements Component {
    public List<Linear> path;

    @MappedContainer
    public static class Linear {
        public Vector3f point;
        public Vector3f binormal;
    }
}
