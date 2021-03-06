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
package org.terasology.segmentedpaths.components;

import org.joml.Vector3f;
import org.terasology.engine.entitySystem.Component;
import org.terasology.reflection.MappedContainer;

import java.util.List;

/**
 * Created by michaelpollind on 4/3/17.
 */
public class CurvedPathComponent implements Component {
    public List<CurvedPathComponent.CubicBezier> path;
    public Vector3f binormal;
    public float rotation;

    @MappedContainer
    public static class CubicBezier {
        public Vector3f f1;
        public Vector3f f2;
        public Vector3f f3;
        public Vector3f f4;
    }
}
