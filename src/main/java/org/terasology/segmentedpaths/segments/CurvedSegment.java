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
import org.terasology.math.TeraMath;
import org.terasology.segmentedpaths.components.CurvedPathComponent;

/**
 * An implementation of {@code Segment} representing segment composed of curves.
 */
public class CurvedSegment implements Segment {

    public static final int ARC_SEGMENT_ITERATIONS = 100;

    private CurvedPathComponent.CubicBezier[] curves;
    private float[] arcLengths;
    private float[][] arcSamples;

    private Vector3f startingBinormal;
    private Vector3f startingNormal;

    public CurvedSegment(CurvedPathComponent.CubicBezier[] curves, Vector3f startingBinormal) {
        this.curves = curves;
        this.startingBinormal = startingBinormal;
        this.arcLengths = new float[this.curves.length];
        Vector3f normal = new Vector3f();
        normal.cross(tangent(0, 0), startingBinormal);
        this.startingNormal = normal;

        calculateLength();

    }

    /**
     * Calculates length of all the curves segment is composed of.
     */
    public void calculateLength() {
        if (this.curves.length == 0) {
            return;
        }

        arcSamples = new float[this.curves.length][ARC_SEGMENT_ITERATIONS + 1];

        float distance = 0f;

        Vector3f previous = point(0, 0);// curves[0].getPoint(0);

        Vector3f normal = new Vector3f();
        normal.cross(tangent(0, 0), startingBinormal);

        for (int x = 0; x < curves.length; x++) {

            for (int y = 0; y <= ARC_SEGMENT_ITERATIONS; y++) {
                Vector3f current = point(x, y / (float) ARC_SEGMENT_ITERATIONS);
                distance += current.distance(previous);
                arcSamples[x][y] = distance;
                previous = current;

            }
            this.arcLengths[x] = distance;
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
        for (int x = 0; x < arcSamples[index].length; x++) {
            if (segmentPosition < arcSamples[index][x]) {
                return x / ((float) ARC_SEGMENT_ITERATIONS);
            }
        }
        return 1.0f;
    }

    @Override
    public float nearestSegmentPosition(Vector3f pos, Vector3f segmentPosition, Quaternionf segmentRotation) {
        if (this.curves.length == 0) {
            return 0f;
        }

        float result = 0;
        float closest = Float.MAX_VALUE;

        float tvalue = 0f;
        Vector3f previous = point(0, 0, segmentPosition, segmentRotation);
        for (int x = 0; x < curves.length; x++) {
            for (int y = 0; y <= ARC_SEGMENT_ITERATIONS; y++) {
                Vector3f current = point(x, y / (float) ARC_SEGMENT_ITERATIONS, segmentPosition, segmentRotation);
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
        CurvedPathComponent.CubicBezier curve = curves[index];

        float result = TeraMath.clamp(t, 0, 1f);
        float num = 1f - result;
        Vector3f vf1 = new Vector3f(curve.f2).sub(curve.f1).mul(3f * num * num);
        Vector3f vf2 = new Vector3f(curve.f3).sub(curve.f2).mul(6f * num * t);
        Vector3f vf3 = new Vector3f(curve.f4).sub(curve.f3).mul(3f * t * t);

        return vf1.add(vf2).add(vf3).normalize();
    }

    @Override
    public Vector3f tangent(int index, float t, Quaternionf rotation) {
        return rotation.transform(this.tangent(index, t));
    }

    @Override
    public Vector3f point(int index, float t) {
        CurvedPathComponent.CubicBezier curve = curves[index];

        float v = Math.clamp(t, 0, 1f);
        float num = 1f - v;
        Vector3f vf1 = new Vector3f(curve.f1).mul(num * num * num);
        Vector3f vf2 = new Vector3f(curve.f2).mul(3f * num * num * v);
        Vector3f vf3 = new Vector3f(curve.f3).mul(3f * num * v * v);
        Vector3f vf4 = new Vector3f(curve.f4).mul(v * v * v);
        return vf1.add(vf2).add(vf3).add(vf4);
    }

    @Override
    public Vector3f point(int index, float t, Vector3f position, Quaternionf rotation) {
        return rotation.transform(point(index, t)).add(position);
    }

    @Override
    public Vector3f normal(int index, float t) {
        Vector3f startingTangent = tangent(0, 0);
        Vector3f tangent = tangent(index, t);
        Quaternionf arcCurve = new Quaternionf().rotateTo(startingTangent, tangent);
        return arcCurve.transform(startingNormal);
    }

    @Override
    public Vector3f normal(int index, float t, Quaternionf rotation) {
        return rotation.transform(normal(index, t));
    }
}
