package net.mostlyoriginal.game.system.map;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.graphics.Color;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.game.api.SolidDirections;
import net.mostlyoriginal.game.component.Ethereal;
import net.mostlyoriginal.game.component.Flying;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class MapCollisionSystem2 extends FluidIteratingSystem {

    public static boolean DEBUG = false;

    private MapSystem mapSystem;
    private CameraSystem cameraSystem;

    private boolean initialized;
    private MapMask solidMask;
    private SolidDirections dirMask;

    private Color RED = Color.valueOf("FF0000FF");

    private int mapVersion = -1;

    public MapCollisionSystem2() {
        super(Aspect.all(Physics.class, Pos.class, Bounds.class).exclude(Flying.class, Ethereal.class));
    }

    private void init() {
        if (!initialized) {
            initialized = true;
            solidMask = mapSystem.getMask("solid");
            dirMask = new SolidDirections(solidMask.width, solidMask.height, G.CELL_SIZE, G.CELL_SIZE);
            refresh();
        }
    }

    @Override
    protected void begin() {
        init();
        if (mapVersion < mapSystem.mapVersion) {
            mapVersion = mapSystem.mapVersion;
            refresh();
        }
    }

    private void refresh() {
        solidMask.refresh();
        dirMask.clear();
        for (int x = 0; x < solidMask.width; x++) {
            for (int y = 0; y < solidMask.height; y++) {
                if (solidMask.atGrid(x, y, true)) {
                    dirMask.toggle(x, y, SolidDirections.BlockType.EAST_WALL, x > 0 && !solidMask.atGrid(x - 1, y, true));
                    dirMask.toggle(x, y, SolidDirections.BlockType.WEST_WALL, x < solidMask.width - 1 && !solidMask.atGrid(x + 1, y, true));
                    dirMask.toggle(x, y, SolidDirections.BlockType.FLOOR, y < solidMask.height - 1 && !solidMask.atGrid(x, y + 1, true));
                    dirMask.toggle(x, y, SolidDirections.BlockType.CEILING, y > 0 && !solidMask.atGrid(x, y - 1, true));
                }
            }
        }
    }

    @Override
    protected void end() {
    }

    @Override
    protected void process(E e) {
        final Physics physics = e.getPhysics();
        final Pos pos = e.getPos();
        final Bounds bounds = e.getBounds();

        //  no math required here.
        if (physics.vx != 0 || physics.vy != 0) {
            if ( physics.vy < 0 ) {
                physics.vy = collideWithY(physics, pos, bounds, physics.vy, SolidDirections.BlockType.FLOOR);
            }
            if ( physics.vy > 0 ) {
                physics.vy = collideWithY(physics, pos, bounds, physics.vy, SolidDirections.BlockType.CEILING);
            }
            if ( physics.vx < 0 ) {
                physics.vx = collideWithX(pos, bounds, physics.vy, SolidDirections.BlockType.WEST_WALL, physics.vx);
            }
            if ( physics.vx > 0 ) {
                physics.vx = collideWithX(pos, bounds, physics.vy, SolidDirections.BlockType.EAST_WALL, physics.vx);
            }
        }
    }

    private float collideWithY(Physics physics, Pos pos, Bounds bounds, float vy, SolidDirections.BlockType type) {

        while (true) {
            float framePixelsX = physics.vx * world.delta;
            float framePixelsY = vy * world.delta;

            // Y Do we hit a FLOOR?
            float startX = pos.xy.x + bounds.minx;
            float startY = pos.xy.y + bounds.miny + framePixelsY;
            float endX = pos.xy.x + bounds.maxx;
            float endY = pos.xy.y + bounds.maxy + framePixelsY;

            // not blocked? Succes! allow this.
            if (!dirMask.atScreenRect(startX, startY, endX, endY, type, true)) {
                return vy;
            }

            if (vy > 0) {
                // slow down by 1 pixel approx.
                vy -= 1f / world.delta;
                if (vy <= 0) {
                    vy = 0;
                    return vy;
                }
            } else {
                // slow down by 1 pixel approx.
                vy = 1f / world.delta;
                if (vy >= 0) {
                    vy = 0;
                    return vy;
                }
            }
        }
    }

    private float collideWithX(Pos pos, Bounds bounds, float vy, SolidDirections.BlockType type, float vx) {

        while (true) {
            float framePixelsX = vx * world.delta;
            float framePixelsY = vy * world.delta;

            // Y Do we hit a FLOOR?
            float startX = pos.xy.x + bounds.minx +framePixelsX;
            float startY = pos.xy.y + bounds.miny + framePixelsY;
            float endX = pos.xy.x + bounds.maxx + framePixelsX;
            float endY = pos.xy.y + bounds.maxy + framePixelsY;

            // not blocked? Succes! allow this.
            if (!dirMask.atScreenRect(startX, startY, endX, endY, type, true)) {
                return vx;
            }

            if (vx > 0) {
                // slow down by 1 pixel approx.
                vx -= 1f / world.delta;
                if (vx <= 0) {
                    vx = 0;
                    return vx;
                }
            } else {
                // slow down by 1 pixel approx.
                vx = 1f / world.delta;
                if (vx >= 0) {
                    vx = 0;
                    return vx;
                }
            }
        }
    }

    public boolean touching(E e, SolidDirections.BlockType type, float x1, float y1, float x2, float y2) {
        init();
        if ( dirMask == null ) return false;
        final Pos pos = e.getPos();
        float startX = pos.xy.x + x1;
        float startY = pos.xy.y + y1;
        float endX = pos.xy.x + x2;
        float endY = pos.xy.y + y2;

        // not blocked? Succes! allow this.
        return dirMask.atScreenRect(startX, startY, endX, endY, type, true);
    }
}
