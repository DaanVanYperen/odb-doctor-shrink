package net.mostlyoriginal.game.system.map;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.api.SolidDirections;
import net.mostlyoriginal.game.component.map.WallSensor;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class WallSensorSystem extends FluidIteratingSystem {

    private MapCollisionSystem2 mapCollisionSystem2;

    private boolean initialized;

    public WallSensorSystem() {
        super(Aspect.all(WallSensor.class, Pos.class, Bounds.class));
    }

    @Override
    protected void process(E e) {

        Bounds bounds = e.getBounds();
        boolean onFloor = mapCollisionSystem2.touching(e, SolidDirections.BlockType.FLOOR, bounds.cx(), bounds.miny-1, bounds.cx(), bounds.miny+1);
        boolean nearFloor = mapCollisionSystem2.touching(e, SolidDirections.BlockType.FLOOR, bounds.cx(), bounds.miny-3, bounds.cx(), bounds.miny+1);
        boolean onCeiling = mapCollisionSystem2.touching(e, SolidDirections.BlockType.CEILING,bounds.minx-1, bounds.maxy+1, bounds.maxx+1, bounds.maxy+1);
        boolean onWestWall = mapCollisionSystem2.touching(e, SolidDirections.BlockType.WEST_WALL, bounds.minx-1, bounds.miny-1, bounds.minx -1, bounds.maxy+1);
        boolean onEastWall = mapCollisionSystem2.touching(e, SolidDirections.BlockType.EAST_WALL,bounds.maxx+1, bounds.miny-1, bounds.maxx+1, bounds.maxy+1);

        e
                .wallSensorOnVerticalSurface(onEastWall || onWestWall)
                .wallSensorOnFloor(onFloor)
                .wallSensorNearFloor(nearFloor)
                .wallSensorOnHorizontalSurface(onCeiling || onFloor)
                .mapWallSensorWallAngle(onFloor ? 90 :
                        onCeiling ? -90 :
                                onEastWall ? 0 :
                                        onWestWall ? 180 : 90);
    }
}