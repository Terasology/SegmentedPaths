// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.segmentedpaths.components;

import org.joml.Vector3f;
import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.reflection.MappedContainer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by michaelpollind on 4/3/17.
 */
public class CurvedPathComponent implements Component<CurvedPathComponent> {
    public List<CurvedPathComponent.CubicBezier> path;
    public Vector3f binormal;
    public float rotation;

    @Override
    public void copy(CurvedPathComponent other) {
        this.path = other.path.stream()
                .map(CubicBezier::copy)
                .collect(Collectors.toList());
        this.binormal = new Vector3f(other.binormal);
        this.rotation = other.rotation;
    }

    @MappedContainer
    public static class CubicBezier {
        public Vector3f f1;
        public Vector3f f2;
        public Vector3f f3;
        public Vector3f f4;

        private CubicBezier copy() {
            CubicBezier newCubicBezier = new CubicBezier();
            newCubicBezier.f1 = f1;
            newCubicBezier.f2 = f2;
            newCubicBezier.f3 = f3;
            newCubicBezier.f4 = f4;
            return newCubicBezier;
        }
    }
}
