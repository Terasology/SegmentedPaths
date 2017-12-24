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
package org.terasology.segmentedpaths.segments;

import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;

public interface Segment {
    int index(float segmentPosition);

    int maxIndex();

    float getSegmentPosition(int index, float segmentPosition);

    float nearestSegmentPosition(Vector3f pos, Vector3f segmentPosition, Quat4f segmentRotation);

    float maxDistance();

    Vector3f tangent(int index, float t);
    Vector3f tangent(int index, float t, Quat4f rotation);

    Vector3f point(int index, float t);
    Vector3f point(int index, float t, Vector3f position, Quat4f rotation);

    Vector3f normal(int index, float t);
    Vector3f normal(int index, float t, Quat4f rotation);

}
