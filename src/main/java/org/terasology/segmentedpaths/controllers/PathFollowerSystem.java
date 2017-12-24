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
import org.terasology.segmentedpaths.Segment;
import org.terasology.registry.In;
import org.terasology.registry.Share;
import org.terasology.segmentedpaths.SegmentMeta;
import org.terasology.segmentedpaths.blocks.PathFamily;
import org.terasology.segmentedpaths.components.PathFollowerComponent;
import org.terasology.segmentedpaths.events.OnExitSegment;
import org.terasology.segmentedpaths.events.OnVisitSegment;
import org.terasology.world.block.BlockComponent;
import org.terasology.world.block.family.BlockFamily;

/**
 * Created by michaelpollind on 4/1/17.
 */
@RegisterSystem(RegisterMode.AUTHORITY)
@Share(value = PathFollowerSystem.class)
public class PathFollowerSystem extends BaseComponentSystem {
    @In
    SegmentSystem segmentSystem;

    @In
    SegmentCacheSystem segmentCacheSystem;

    public float findDeltaT(EntityRef vehicleEntity, Vector3f vector) {
        PathFollowerComponent segmentVehicleComponent = vehicleEntity.getComponent(PathFollowerComponent.class);
        return segmentVehicleComponent.heading.dot(vector);
    }

    private float calculateDeltaT(EntityRef vehicleEntity, float deltaT, boolean updateHeading) {
        PathFollowerComponent segmentVehicleComponent = vehicleEntity.getComponent(PathFollowerComponent.class);
        Vector3f tangent = vehicleTangent(vehicleEntity);
        if (tangent.dot(segmentVehicleComponent.heading) < 0) {
            deltaT *= -1;
            tangent.invert();
        }
        if (updateHeading)
            segmentVehicleComponent.heading = tangent;
        return deltaT;
    }

    public Vector3f vehicleTangent(EntityRef vehicleEntity) {
        return vehicleTangent(vehicleEntity, 0, null);
    }

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

    public Vector3f vehiclePoint(EntityRef vehicleEntity) {
        return vehiclePoint(vehicleEntity, 0, null);
    }

    public Vector3f vehiclePoint(EntityRef vehicleEntity, float delta, SegmentMapping mapping) {
        PathFollowerComponent vehicle = vehicleEntity.getComponent(PathFollowerComponent.class);
        if (delta == 0 || mapping == null) {
            Segment segment = segmentCacheSystem.getSegment(vehicle.segmentMeta.prefab);
            int index = segment.index(vehicle.segmentMeta.position);
            Quat4f rotation = segmentSystem.segmentRotation(vehicle.segmentMeta);
            Vector3f position = segmentSystem.segmentPosition(vehicle.segmentMeta);
            return segment.point(index, segment.getSegmentPosition(index, vehicle.segmentMeta.position), position, rotation);
        }
        SegmentMeta meta = new SegmentMeta(vehicle.segmentMeta);
        if (this.segmentSystem.updateSegmentMeta(meta, delta, mapping)) {
            Segment segment = segmentCacheSystem.getSegment(meta.prefab);
            int index = segment.index(meta.position);
            Quat4f rotation = segmentSystem.segmentRotation(meta);
            Vector3f position = segmentSystem.segmentPosition(meta);
            return segment.point(index, segment.getSegmentPosition(index, meta.position), position, rotation);
        }
        return null;
    }


    public Vector3f vehicleNormal(EntityRef vehicleEntity) {
        return this.vehicleNormal(vehicleEntity, 0, null);
    }

    public Vector3f vehicleNormal(EntityRef vehicleEntity, float delta, SegmentMapping mapping) {
        PathFollowerComponent vehicle = vehicleEntity.getComponent(PathFollowerComponent.class);
        if (delta == 0 || mapping == null) {
            Segment segment = segmentCacheSystem.getSegment(vehicle.segmentMeta.prefab);
            int index = segment.index(vehicle.segmentMeta.position);
            Quat4f rotation = segmentSystem.segmentRotation(vehicle.segmentMeta);
            return segment.normal(index, segment.getSegmentPosition(index, vehicle.segmentMeta.position), rotation);
        }
        SegmentMeta meta = new SegmentMeta(vehicle.segmentMeta);
        if (this.segmentSystem.updateSegmentMeta(meta, delta, mapping)) {
            Segment segment = segmentCacheSystem.getSegment(meta.prefab);
            int index = segment.index(meta.position);
            Quat4f rotation = segmentSystem.segmentRotation(meta);
            return segment.normal(index, segment.getSegmentPosition(index, meta.position), rotation);
        }
        return null;
    }


    public boolean isVehicleValid(EntityRef vehicleEntity) {
        PathFollowerComponent vehicle = vehicleEntity.getComponent(PathFollowerComponent.class);
        if (vehicle == null)
            return false;
        if (vehicle.segmentMeta == null)
            return false;
        if (vehicle.segmentMeta.association == null)
            return false;
        if (!vehicle.segmentMeta.association.exists())
            return false;
        return true;
    }

    public boolean move(EntityRef vehicleEntity, float tDelta, SegmentMapping mapping) {
        if (tDelta == 0)
            return true;
        float deltaT = calculateDeltaT(vehicleEntity, tDelta, true);
        PathFollowerComponent vehicle = vehicleEntity.getComponent(PathFollowerComponent.class);
        EntityRef previous = vehicle.segmentMeta.association;

        boolean result = segmentSystem.updateSegmentMeta(vehicle.segmentMeta, deltaT, mapping);
        if (previous != vehicle.segmentMeta.association) {
            vehicleEntity.send(new OnExitSegment(previous));
            vehicleEntity.send(new OnVisitSegment(vehicle.segmentMeta.association));
        }
        return result;
    }
}
