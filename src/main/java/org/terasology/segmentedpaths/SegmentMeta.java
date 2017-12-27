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
package org.terasology.segmentedpaths;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.network.Replicate;
import org.terasology.reflection.MappedContainer;

@MappedContainer
public class SegmentMeta {
    @Replicate
    public float position;
    @Replicate
    public EntityRef association;
    @Replicate
    public Prefab prefab;

    @Replicate
    public int sign = 1;

    public SegmentMeta(){

    }
    public SegmentMeta(SegmentMeta meta){
        this.position = meta.position;
        this.association = meta.association;
        this.prefab = meta.prefab;
    }

    public SegmentMeta(float position, EntityRef association, Prefab prefab){
        this.prefab= prefab;
        this.association = association;
        this.position = position;
    }
}
