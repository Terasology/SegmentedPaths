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

import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.terasology.segmentedpaths.components.LinearPathComponent;

/**
 * An implementation of {@code Segment} representing segment composed of straight subsegments.
 */
public class LinearSegment implements Segment {
    private float[] arcLengths;
    private Vector3f[] tangents;
    private Vector3f[] arc;
    private LinearPathComponent.Linear[] linearPoints;

    public LinearSegment(LinearPathComponent.Linear[] points) {
        if (points.length < 2) {
            return;
        }
        this.linearPoints = points;
        calculateLength();
    }

    /**
     * Calculates lengths and tagets for all the subsegments in this segment.
     */
    public void calculateLength() {

        this.tangents = new Vector3f[linearPoints.length - 1];
        this.arcLengths = new float[linearPoints.length - 1];
        this.arc = new Vector3f[linearPoints.length - 1];
        LinearPathComponent.Linear previous = linearPoints[0];
        float distance = 0;
        for (int x = 1; x < linearPoints.length; x++) {
            distance += previous.point.distance(linearPoints[x].point);
            arcLengths[x - 1] = distance;
            tangents[x - 1] = new Vector3f(linearPoints[x].point).sub(previous.point).normalize();
            arc[x - 1] = new Vector3f(linearPoints[x].point).sub(previous.point);
            previous = linearPoints[x];
        }
    }

    @Override
    public int index(float segmentPosition) {
        if (segmentPosition < 0) {
            return 0;
        }
        for (int x = 0; x < arcLengths.length; x++) {
            if (segmentPosition < arcLengths[x]) {
                return x;
            }
        }
        return arcLengths.length - 1;
    }

    @Override
    public int maxIndex() {
        return arcLengths.length - 1;
    }

    @Override
    public float getSegmentPosition(int index, float segmentPosition) {
        if (index - 1 < 0) {
            return (segmentPosition / arcLengths[0]);
        }
        return ((segmentPosition - arcLengths[index - 1]) / (arcLengths[index] - arcLengths[index - 1]));
    }

    @Override
    public float nearestSegmentPosition(Vector3f pos, Vector3f segmentPosition, Quaternionf segmentRotation) {
        if (this.linearPoints.length == 0) {
            return 0f;
        }

        float result = 0;
        float closest = Float.MAX_VALUE;

        float tvalue = 0f;
        Vector3f previous = point(0, 0, segmentPosition, segmentRotation);
        for (int x = 0; x < arcLengths.length; x++) {
            for (int y = 0; y <= 100; y++) {
                Vector3f current = point(x, y / (float) 100, segmentPosition, segmentRotation);
                tvalue += current.distance(previous);
                previous = current;

                float distance = current.distance(pos);
                if (distance < closest) {
                    closest = distance;
                    result = tvalue;
                }
            }
        }
        return result;
    }

    @Override
    public float maxDistance() {
        return arcLengths[arcLengths.length - 1];
    }

    @Override
    public Vector3f tangent(int index, float t) {
        return new Vector3f(tangents[index]);
    }

    @Override
    public Vector3f tangent(int index, float t, Quaternionf rotation) {
        return rotation.transform(this.tangent(index, t));
    }

    @Override
    public Vector3f point(int index, float t) {
        float value = Math.clamp(t, 0, 1f);
        return new Vector3f(linearPoints[index].point).add(new Vector3f(this.arc[index]).mul(value));
    }

    @Override
    public Vector3f point(int index, float t, Vector3f position, Quaternionf rotation) {
        return rotation.transform(point(index, t)).add(position);
    }

    @Override
    public Vector3f normal(int index, float t) {
        Vector3f b1 = linearPoints[index].binormal;
        Vector3f b2 = linearPoints[index + 1].binormal;

        Vector3f n1 = new Vector3f().cross(tangents[index], b1);
        Vector3f n2 = new Vector3f().cross(tangents[index], b2);

        return n1.lerp(n2, t); //Vector3f.lerp(n1, n2, t);
    }

    @Override
    public Vector3f normal(int index, float t, Quaternionf rotation) {
        return rotation.transform(normal(index, t));
    }
}
