package net.mostlyoriginal.game.system.map;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.api.SolidDirections;
import net.mostlyoriginal.game.component.EvictFromWall;
import net.mostlyoriginal.game.component.map.WallSensor;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class WallEvictionSystem extends FluidIteratingSystem {

    private MapCollisionSystem2 mapCollisionSystem2;

    private boolean initialized;

    public WallEvictionSystem() {
        super(Aspect.all(EvictFromWall.class, Pos.class, Bounds.class));
    }

    @Override
    protected void process(E e) {

        Bounds bounds = e.getBounds();
        while ( mapCollisionSystem2.touching(e, SolidDirections.BlockType.WEST_WALL, bounds.minx, bounds.miny, bounds.minx, bounds.maxy) ) {
            e.posX(e.posX()+1);
        }
        while ( mapCollisionSystem2.touching(e, SolidDirections.BlockType.EAST_WALL,bounds.maxx, bounds.miny, bounds.maxx, bounds.maxy) ) {
            e.posX(e.posX()-1);
        }

        if ( !e.hasShrunk() && mapCollisionSystem2.touching(e, SolidDirections.BlockType.CEILING, bounds.minx, 32, bounds.maxx, 38) ) {
            e.dead();
            E.E().playSound("neck_break");
            return;
        }

    }
}