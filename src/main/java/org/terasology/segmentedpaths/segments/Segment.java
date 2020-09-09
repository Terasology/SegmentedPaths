// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.segmentedpaths.segments;

import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;

/**
 * Represents a segment traversable by an entity that has the {@link PathFollowerComponent}. Contiguous segments are put
 * together to form paths.
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
     * Returns the segment position of the point on the segment closest to the given point when the segment is at the
     * given position and rotation.
     *
     * @param pos The point to which the returned segment point is closest.
     * @param segmentPosition The position of the segment.
     * @param segmentRotation The rotation of the segment.
     * @return The segment position of the closest point.
     */
    float nearestSegmentPosition(Vector3f pos, Vector3f segmentPosition, Quat4f segmentRotation);

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
    Vector3f tangent(int index, float t, Quat4f rotation);

    /**
     * Returns the position of a point on the segment at the given subsegment index and position.
     *
     * @param index The index of the subsegment.
     * @param t The subsegment position.
     * @return The position of the point on the segment.
     */
    Vector3f point(int index, float t);

    /**
     * Returns the position of a point on the translated and rotated segment at the given subsegment index and
     * position.
     *
     * @param index The index of the subsegment.
     * @param t The subsegment position.
     * @param position The position of the segment.
     * @param rotation The rotation of the segment.
     * @return The position of the point on the segment.
     */
    Vector3f point(int index, float t, Vector3f position, Quat4f rotation);

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
    Vector3f normal(int index, float t, Quat4f rotation);

}
