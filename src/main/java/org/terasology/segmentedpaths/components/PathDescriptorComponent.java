// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.segmentedpaths.components;

import com.google.common.collect.Lists;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.List;


/**
 * Created by michaelpollind on 8/15/16.
 */
public class PathDescriptorComponent implements Component<PathDescriptorComponent> {
    public List<Prefab> descriptors;

    @Override
    public void copy(PathDescriptorComponent other) {
        this.descriptors = Lists.newArrayList(other.descriptors);
    }
}
