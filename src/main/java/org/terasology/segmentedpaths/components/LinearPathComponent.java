// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.segmentedpaths.components;

import org.joml.Vector3f;
import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.reflection.MappedContainer;

import java.util.List;
import java.util.stream.Collectors;

public class LinearPathComponent implements Component<LinearPathComponent> {
    public List<Linear> path;

    @Override
    public void copy(LinearPathComponent other) {
        this.path = other.path.stream()
                .map(Linear::copy)
                .collect(Collectors.toList());
    }

    @MappedContainer
    public static class Linear {
        public Vector3f point;
        public Vector3f binormal;

        private Linear copy() {
            Linear linear = new Linear();
            linear.point = new Vector3f(point);
            linear.binormal = new Vector3f(binormal);
            return linear;
        }
    }
}
