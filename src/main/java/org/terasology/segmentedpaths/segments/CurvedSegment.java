// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.segmentedpaths.segments;

import org.terasology.math.TeraMath;
import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;
import org.terasology.segmentedpaths.components.CurvedPathComponent;

/**
 * An implementation of {@code Segment} representing segment composed of curves.
 */
public class CurvedSegment implements Segment {

    public static final int ARC_SEGMENT_ITERATIONS = 100;

    private final CurvedPathComponent.CubicBezier[] curves;
    private final float[] arcLengths;
    private float[][] arcSamples;

    private final Vector3f startingBinormal;
    private final Vector3f startingNormal;

    public CurvedSegment(CurvedPathComponent.CubicBezier[] curves, Vector3f startingBinormal) {
        this.curves = curves;
        this.startingBinormal = startingBinormal;
        this.arcLengths = new float[this.curves.length];
        Vector3f normal = new Vector3f();
        normal.cross(tangent(0, 0), startingBinormal);
        this.startingNormal = normal;

        CalculateLength();

    }

    /**
     * Calculates length of all the curves segment is composed of.
     */
    public void CalculateLength() {
        if (this.curves.length == 0)
            return;

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
        if (segmentPosition < 0)
            return 0;
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
    public float nearestSegmentPosition(Vector3f pos, Vector3f segmentPosition, Quat4f segmentRotation) {
        if (this.curves.length == 0)
            return 0f;

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

        t = TeraMath.clamp(t, 0, 1f);
        float num = 1f - t;
        Vector3f vf1 = new Vector3f(curve.f2).sub(curve.f1).mul(3f * num * num);
        Vector3f vf2 = new Vector3f(curve.f3).sub(curve.f2).mul(6f * num * t);
        Vector3f vf3 = new Vector3f(curve.f4).sub(curve.f3).mul(3 * t * t);

        return vf1.add(vf2).add(vf3).normalize();
    }

    @Override
    public Vector3f tangent(int index, float t, Quat4f rotation) {
        return rotation.rotate(this.tangent(index, t));
    }

    @Override
    public Vector3f point(int index, float t) {
        CurvedPathComponent.CubicBezier curve = curves[index];

        t = TeraMath.clamp(t, 0, 1f);
        float num = 1f - t;
        Vector3f vf1 = new Vector3f(curve.f1).mul(num * num * num);
        Vector3f vf2 = new Vector3f(curve.f2).mul(3f * num * num * t);
        Vector3f vf3 = new Vector3f(curve.f3).mul(3f * num * t * t);
        Vector3f vf4 = new Vector3f(curve.f4).mul(t * t * t);
        return vf1.add(vf2).add(vf3).add(vf4);

    }

    @Override
    public Vector3f point(int index, float t, Vector3f position, Quat4f rotation) {
        return rotation.rotate(point(index, t)).add(position);
    }

    @Override
    public Vector3f normal(int index, float t) {
        Vector3f startingTangent = tangent(0, 0);
        Vector3f tangent = tangent(index, t);
        Quat4f arcCurve = Quat4f.shortestArcQuat(startingTangent, tangent);

        return arcCurve.rotate(startingNormal);
    }

    @Override
    public Vector3f normal(int index, float t, Quat4f rotation) {
        return rotation.rotate(normal(index, t));
    }


}
