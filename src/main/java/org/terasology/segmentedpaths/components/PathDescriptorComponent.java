// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.segmentedpaths.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.entitySystem.prefab.Prefab;

import java.util.List;


/**
 * Created by michaelpollind on 8/15/16.
 */
public class PathDescriptorComponent implements Component {
    public List<Prefab> descriptors;
}
