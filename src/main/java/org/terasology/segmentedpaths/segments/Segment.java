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

import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Represents a segment traversable by an entity that has the {@link PathFollowerComponent}. Contiguous segments are put together to
 * form paths.
 */
public interface Segment {
    /**
     * Returns the index of the subsegment at the given position on the segment.
     *
     * @param segmentPosition The position on the segment.
     * @return The subsegment index.
     */
    int index(float segmentPosition);

    /**
     * Returns the highest possible subsegment index.
     *
     * @return The highest possible subsegment index.
     */
    int maxIndex();

    /**
     * Returns the position on the subsegment with the given index for a given segment position.
     *
     * @param index The index of the subsegment.
     * @param segmentPosition The position on the segment.
     * @return The subsegment index.
     */
    float getSegmentPosition(int index, float segmentPosition);

    /**
     * Returns the segment position of the point on the segment closest to the given point when the segment is at the given position
     * and rotation.
     *
     * @param pos The point to which the returned segment point is closest.
     * @param segmentPosition The position of the segment.
     * @param segmentRotation The rotation of the segment.
     * @return The segment position of the closest point.
     */
    float nearestSegmentPosition(Vector3f pos, Vector3f segmentPosition, Quaternionf segmentRotation);

    /**
     * Returns the length of this segment.
     *
     * @return The segment length.
     */
    float maxDistance();

    /**
     * Returns the tangent to the segment at the given subsegment index and position.
     *
     * @param index The index of the subsegment.
     * @param t The subsegment position.
     * @return The tangent to the segment.
     */
    Vector3f tangent(int index, float t);

    /**
     * Returns the tangent to the rotated segment at the given subsegment index and position.
     *
     * @param index The index of the subsegment.
     * @param t The subsegment position.
     * @param rotation The rotation of the segment.
     * @return The tangent to the segment.
     */
    Vector3f tangent(int index, float t, Quaternionf rotation);

    /**
     * Returns the position of a point on the segment at the given subsegment index and position.
     *
     * @param index The index of the subsegment.
     * @param t The subsegment position.
     * @return The position of the point on the segment.
     */
    Vector3f point(int index, float t);

    /**
     * Returns the position of a point on the translated and rotated segment at the given subsegment index and position.
     *
     * @param index The index of the subsegment.
     * @param t The subsegment position.
     * @param position The position of the segment.
     * @param rotation The rotation of the segment.
     * @return The position of the point on the segment.
     */
    Vector3f point(int index, float t, Vector3f position, Quaternionf rotation);

    /**
     * Returns the normal to the segment at the given subsegment index and point.
     *
     * @param index The index of the subsegment.
     * @param t The subsegment position.
     * @return The normal to the segment.
     */
    Vector3f normal(int index, float t);

    /**
     * Returns the normal to the rotated segment at the given subsegment index and point.
     *
     * @param index The index of the subsegment.
     * @param t The subsegment position.
     * @param rotation The rotation of the segment.
     * @return The normal to the segment.
     */
    Vector3f normal(int index, float t, Quaternionf rotation);

}
