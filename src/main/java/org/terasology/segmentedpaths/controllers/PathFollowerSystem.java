// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.segmentedpaths.controllers;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.engine.registry.Share;
import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;
import org.terasology.segmentedpaths.SegmentMeta;
import org.terasology.segmentedpaths.components.PathFollowerComponent;
import org.terasology.segmentedpaths.events.OnExitSegment;
import org.terasology.segmentedpaths.events.OnVisitSegment;
import org.terasology.segmentedpaths.segments.Segment;

/**
 * A class for working with entities following a path.
 */
@RegisterSystem(RegisterMode.AUTHORITY)
@Share(value = PathFollowerSystem.class)
public class PathFollowerSystem extends BaseComponentSystem {
    @In
    SegmentSystem segmentSystem;

    @In
    SegmentCacheSystem segmentCacheSystem;

    /**
     * Returns a tangent to the path at the point the vehicle is currently at.
     *
     * @param vehicleEntity Vehicle entity marking a point to which we want a tangent
     * @return Vector representation of the tangent
     */
    public Vector3f vehicleTangent(EntityRef vehicleEntity) {
        return vehicleTangent(vehicleEntity, 0, null);
    }

    /**
     * Returns a tangent to the path at the point the vehicle will be after delta distance.
     *
     * @param vehicleEntity Vehicle entity to which we want a tangent
     * @param delta Distance to move the point by
     * @param mapping A mapping to be used for figuring out chaining of segments
     * @return Vector representation of the tangent
     */
    public Vector3f vehicleTangent(EntityRef vehicleEntity, float delta, SegmentMapping mapping) {
        PathFollowerComponent vehicle = vehicleEntity.getComponent(PathFollowerComponent.class);
        if (delta == 0 || mapping == null) {
            Segment segment = segmentCacheSystem.getSegment(vehicle.segmentMeta.prefab);
            int index = segment.index(vehicle.segmentMeta.position);
            Quat4f rotation = segmentSystem.segmentRotation(vehicle.segmentMeta);
            return segment.tangent(index, segment.getSegmentPosition(index, vehicle.segmentMeta.position), rotation);
        }
        SegmentMeta meta = new SegmentMeta(vehicle.segmentMeta);
        if (this.segmentSystem.updateSegmentMeta(meta, delta, mapping)) {
            Segment segment = segmentCacheSystem.getSegment(meta.prefab);
            int index = segment.index(meta.position);
            Quat4f rotation = segmentSystem.segmentRotation(meta);
            return segment.tangent(index, segment.getSegmentPosition(index, meta.position), rotation);
        }
        return null;
    }

    /**
     * Returns point representing current vehicle position on path.
     *
     * @param vehicleEntity Vehicle whose position we are measuring
     * @return Vector representation of the point
     */
    public Vector3f vehiclePoint(EntityRef vehicleEntity) {
        return vehiclePoint(vehicleEntity, 0, null);
    }

    /**
     * Returns point representing vehicle position on path the vehicle will be after delta distance.
     *
     * @param vehicleEntity Vehicle whose position we are measuring
     * @param delta Distance to move the point by
     * @param mapping A mapping to be used for figuring out chaining of segments
     * @return Vector representation of the point
     */
    public Vector3f vehiclePoint(EntityRef vehicleEntity, float delta, SegmentMapping mapping) {
        PathFollowerComponent vehicle = vehicleEntity.getComponent(PathFollowerComponent.class);
        if (delta == 0 || mapping == null) {
            Segment segment = segmentCacheSystem.getSegment(vehicle.segmentMeta.prefab);
            int index = segment.index(vehicle.segmentMeta.position);
            Quat4f rotation = segmentSystem.segmentRotation(vehicle.segmentMeta);
            Vector3f position = segmentSystem.segmentPosition(vehicle.segmentMeta);
            return segment.point(index, segment.getSegmentPosition(index, vehicle.segmentMeta.position), position,
                    rotation);
        }
        SegmentMeta meta = new SegmentMeta(vehicle.segmentMeta);
        if (this.segmentSystem.updateSegmentMeta(meta, vehicle.segmentMeta.sign * delta, mapping)) {
            Segment segment = segmentCacheSystem.getSegment(meta.prefab);
            int index = segment.index(meta.position);
            Quat4f rotation = segmentSystem.segmentRotation(meta);
            Vector3f position = segmentSystem.segmentPosition(meta);
            return segment.point(index, segment.getSegmentPosition(index, meta.position), position, rotation);
        }
        return null;
    }

    /**
     * Returns a normal to the path at the point the vehicle is currently at.
     *
     * @param vehicleEntity Vehicle entity to which we want a normal
     * @return Vector representation of the normal
     */
    public Vector3f vehicleNormal(EntityRef vehicleEntity) {
        return this.vehicleNormal(vehicleEntity, 0, null);
    }

    /**
     * Returns a normal to the path at the point the vehicle will be at after delta distance.
     *
     * @param vehicleEntity Vehicle entity to which we want a normal
     * @param delta Distance to move the point by
     * @param mapping A mapping to be used for figuring out chaining of segments
     * @return Vector representation of the normal
     */
    public Vector3f vehicleNormal(EntityRef vehicleEntity, float delta, SegmentMapping mapping) {
        PathFollowerComponent vehicle = vehicleEntity.getComponent(PathFollowerComponent.class);
        if (delta == 0 || mapping == null) {
            Segment segment = segmentCacheSystem.getSegment(vehicle.segmentMeta.prefab);
            int index = segment.index(vehicle.segmentMeta.position);
            Quat4f rotation = segmentSystem.segmentRotation(vehicle.segmentMeta);
            return segment.normal(index, segment.getSegmentPosition(index, vehicle.segmentMeta.position), rotation);
        }
        SegmentMeta meta = new SegmentMeta(vehicle.segmentMeta);
        if (this.segmentSystem.updateSegmentMeta(meta, vehicle.segmentMeta.sign * delta, mapping)) {
            Segment segment = segmentCacheSystem.getSegment(meta.prefab);
            int index = segment.index(meta.position);
            Quat4f rotation = segmentSystem.segmentRotation(meta);
            return segment.normal(index, segment.getSegmentPosition(index, meta.position), rotation);
        }
        return null;
    }

    /**
     * Returns whether the vehicle is valid for working with.
     *
     * @param vehicleEntity Vehicle that we want to check
     * @return True if vehicle is valid, false otherwise
     */
    public boolean isVehicleValid(EntityRef vehicleEntity) {
        PathFollowerComponent vehicle = vehicleEntity.getComponent(PathFollowerComponent.class);
        if (vehicle == null)
            return false;
        if (vehicle.segmentMeta == null)
            return false;
        if (vehicle.segmentMeta.association == null)
            return false;
        return vehicle.segmentMeta.association.exists();
    }

    /**
     * Moves the vehicle for delta distance along the path
     *
     * @param vehicleEntity Vehicle to be moved
     * @param delta Distance the vehicle is to be moved by
     * @param mapping Mapping to be used for figuring out segment chaining
     * @return Returns false if end of path has been reached, true otherwise
     */
    public boolean move(EntityRef vehicleEntity, float delta, SegmentMapping mapping) {
        if (delta == 0)
            return true;
        PathFollowerComponent vehicle = vehicleEntity.getComponent(PathFollowerComponent.class);
        EntityRef previous = vehicle.segmentMeta.association;
        vehicle.heading = this.vehicleTangent(vehicleEntity).mul(vehicle.segmentMeta.sign);
        boolean result = segmentSystem.updateSegmentMeta(vehicle.segmentMeta, vehicle.segmentMeta.sign * delta,
                mapping);
        if (previous != vehicle.segmentMeta.association) {
            previous.send(new OnExitSegment(vehicleEntity));
            vehicle.segmentMeta.association.send(new OnVisitSegment(vehicleEntity));
        }
        return result;
    }
}
