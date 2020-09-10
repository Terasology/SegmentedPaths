// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.segmentedpaths.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.math.geom.Vector3f;
import org.terasology.nui.reflection.MappedContainer;

import java.util.List;

/**
 * Created by michaelpollind on 4/3/17.
 */
public class CurvedPathComponent implements Component {
    public List<CurvedPathComponent.CubicBezier> path;
    public Vector3f startingBinormal;
    public float rotation;

    @MappedContainer
    public static class CubicBezier {
        public Vector3f f1;
        public Vector3f f2;
        public Vector3f f3;
        public Vector3f f4;
    }
}
