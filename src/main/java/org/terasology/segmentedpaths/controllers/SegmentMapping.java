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

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.segmentedpaths.Segment;
import org.terasology.segmentedpaths.SegmentMeta;

/**
 * Created by michaelpollind on 4/5/17.
 */
public interface SegmentMapping {
    class MappingResult{
        public  MappingResult(Prefab prefab,EntityRef entity){
            this.prefab = prefab;
            this.entity = entity;
        }
        Prefab prefab;
        EntityRef entity;
    }

    enum SegmentEnd{
        START, END
    }

    MappingResult nextSegment(SegmentMeta meta, SegmentEnd ends);

}
