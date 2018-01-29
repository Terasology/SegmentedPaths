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
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.Rotation;
import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;
import org.terasology.registry.In;
import org.terasology.registry.Share;
import org.terasology.segmentedpaths.segments.CurvedSegment;
import org.terasology.segmentedpaths.SegmentMeta;
import org.terasology.segmentedpaths.blocks.PathFamily;
import org.terasology.segmentedpaths.segments.Segment;
import org.terasology.world.block.BlockComponent;
import org.terasology.world.block.family.BlockFamily;

@RegisterSystem(RegisterMode.AUTHORITY)
@Share(value = SegmentSystem.class)
public class SegmentSystem extends BaseComponentSystem {
    /**
     * Enum representing possible combinations of ends segments may be connected by.
     */
    public enum JointMatch {
        Start_End,
        Start_Start,
        End_End,
        End_Start,
        None
    }

    /**
     * The maximum deviation of two points to be from each other for them to be considered matching.
     */
    public static final float MATCH_EPSILON = .09f * .09f;

    @In
    private SegmentCacheSystem segmentCacheSystem;

    /**
     * Returns by which type of connection from {@link JointMatch} are two segments connected.
     *
     * @param current Segment to be used as the base for comparing
     * @param p1 Where is the {@code current} segment to be placed
     * @param r1 How is the {@code current} segment to be rotated
     * @param next The other segment used in comparing
     * @param p2 Where is the {@code next} segment to be placed
     * @param r2 How is the {@code next} segment to be rotated
     * @return Type of match
     */
    public JointMatch segmentMatch(Segment current, Vector3f p1, Quat4f r1, Segment next, Vector3f p2, Quat4f r2) {
        Vector3f s1 = current.point(0, 0, p1, r1);
        Vector3f e1 = current.point(current.maxIndex(), 1, p1, r1);

        Vector3f s2 = next.point(0, 0, p2, r2);
        Vector3f e2 = next.point(next.maxIndex(), 1, p2, r2);

        if (s1.distanceSquared(s2) < MATCH_EPSILON)
            return JointMatch.Start_Start;
        if (s1.distanceSquared(e2) < MATCH_EPSILON)
            return JointMatch.Start_End;
        if (e1.distanceSquared(s2) < MATCH_EPSILON)
            return JointMatch.End_Start;
        if (e1.distanceSquared(e2) < MATCH_EPSILON)
            return JointMatch.End_End;
        return JointMatch.None;
    }

    /**
     * Updates {@link SegmentMeta} to contain the correct data based on distance delta.
     *
     * @param segmentMeta SegmentMeta to update
     * @param delta Distance by which to update
     * @param mapping A mapping to be used for figuring out chaining of segments
     * @return Returns false if end of path has been reached, true otherwise
     */
    public boolean updateSegmentMeta(SegmentMeta segmentMeta, float delta, SegmentMapping mapping) {

        Segment segment = segmentCacheSystem.getSegment(segmentMeta.prefab);

        while (true) {
            if(Math.abs(delta) < Float.MIN_VALUE)
                return true;

            if (delta + segmentMeta.position > 0 && delta + segmentMeta.position < segment.maxDistance()) {
                segmentMeta.position = delta + segmentMeta.position;
                return true;
            }
            SegmentMapping.MappingResult mappingResult = mapping.nextSegment(segmentMeta, delta < 0 ? SegmentMapping.SegmentEnd.START : SegmentMapping.SegmentEnd.END);
            if (delta < 0) {
                delta -= segmentMeta.position * Math.signum(delta);
            } else {
                delta -= (segment.maxDistance() - segmentMeta.position) * Math.signum(delta);
            }

            if (mappingResult == null)
                return false;
            Segment nextSegment = segmentCacheSystem.getSegment(mappingResult.prefab);

            Vector3f p1 = this.segmentPosition(segmentMeta);
            Quat4f q1 = this.segmentRotation(segmentMeta);
            Vector3f p2 = this.segmentPosition(mappingResult.entity);
            Quat4f q2 = this.segmentRotation(mappingResult.entity);

            JointMatch match = this.segmentMatch(segment, p1, q1, nextSegment, p2, q2);
            switch (match) {
                case Start_End:
                    segmentMeta.position = nextSegment.maxDistance();
                    break;
                case Start_Start:
                    segmentMeta.position = 0;
                    delta *= -1;
                    segmentMeta.sign *= -1;
                    break;
                case End_End:
                    delta *= -1;
                    segmentMeta.position = nextSegment.maxDistance();
                    segmentMeta.sign *= -1;
                    break;
                case End_Start:
                    segmentMeta.position = 0;
                    break;
                default:
                    return false;
            }
            segmentMeta.prefab = mappingResult.prefab;
            segmentMeta.association = mappingResult.entity;
            segment = nextSegment;
        }
    }

    /**
     * Returns a position of segment referenced by given {@code SegmentMeta}.
     *
     * @param meta Segment we want to get position of
     * @return Position of the segment
     */
    public Vector3f segmentPosition(SegmentMeta meta) {
        return segmentPosition(meta.association);
    }

    /**
     * Returns a position of segment referenced by given {@code EntityRef}.
     *
     * @param entity Segment we want to get position of
     * @return Position of the segment
     */
    public Vector3f segmentPosition(EntityRef entity) {
        if (entity.hasComponent(BlockComponent.class)) {
            BlockComponent blockComponent = entity.getComponent(BlockComponent.class);
            return blockComponent.getPosition().toVector3f();
        }
        if (entity.hasComponent(LocationComponent.class)) {
            return entity.getComponent(LocationComponent.class).getWorldPosition();
        }
        return Vector3f.zero();
    }

    /**
     * Returns a rotation of segment referenced by given {@code EntityRef}.
     *
     * @param meta Segment we want to get rotation of
     * @return Rotation of the segment
     */
    public Quat4f segmentRotation(SegmentMeta meta) {
        return segmentRotation(meta.association);
    }

    /**
     * Returns a rotation of segment referenced by given {@code EntityRef}.
     *
     * @param entity Segment we want to get rotation of
     * @return Rotation of the segment
     */
    public Quat4f segmentRotation(EntityRef entity) {
        if (entity.hasComponent(BlockComponent.class)) {
            BlockComponent blockComponent = entity.getComponent(BlockComponent.class);
            BlockFamily blockFamily = blockComponent.getBlock().getBlockFamily();
            if (blockFamily instanceof PathFamily) {
                Rotation rotation = ((PathFamily) blockFamily).getRotationFor(blockComponent.getBlock().getURI());
                return rotation.getQuat4f();
            }
        }
        if (entity.hasComponent(LocationComponent.class)) {
            return entity.getComponent(LocationComponent.class).getWorldRotation();
        }
        return new Quat4f(Quat4f.IDENTITY);
    }
}
