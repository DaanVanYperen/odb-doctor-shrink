package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class PlayerGazeTrackerSystem extends FluidIteratingSystem {

    private static final float SPEED = 10;
    private static final int MOVEMENT_LOOK_AHEAD = 64;

    public PlayerGazeTrackerSystem() {
        super(Aspect.all(PlayerGazeTracker.class));
    }

    Vector3 vTmp = new Vector3();

    @Override
    protected void process(E e) {
        E player = entityWithTag("player");

        PlayerGazeTracker tracker = e.getPlayerGazeTracker();

        tracker.target.set(player.getPos().xy);
        tracker.target.x += player.animFlippedX() ? -100 : 100;
//        tracker.target.x += player.physicsVx() > 0.1f ? MOVEMENT_LOOK_AHEAD : player.physicsVx() < -0.1f ? -MOVEMENT_LOOK_AHEAD : 0;
        tracker.target.y += player.physicsVy() > 0.1f ? MOVEMENT_LOOK_AHEAD : player.physicsVy() < -0.1f ? -MOVEMENT_LOOK_AHEAD : 0;

        e.getPos().xy.lerp(tracker.target,world.delta*4f);
        e.bounds(player.getBounds());
    }
}
