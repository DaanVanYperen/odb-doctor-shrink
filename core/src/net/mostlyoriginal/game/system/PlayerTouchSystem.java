package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.system.physics.CollisionSystem;
import net.mostlyoriginal.game.component.Checkpoint;
import net.mostlyoriginal.game.component.PlayerCanTouch;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.detection.ParticleSystem;

/**
 * @author Daan van Yperen
 */
public class PlayerTouchSystem extends FluidIteratingSystem {
    public PlayerTouchSystem() {
        super(Aspect.all(PlayerCanTouch.class, Pos.class, Bounds.class));
    }

    CollisionSystem collisionSystem;

    @Override
    protected void process(E e) {
        E player = entityWithTag("player");
        if ( overlaps(player,e)  ) {
            e.playerTouching(true);
        } else {
            e.playerTouching(false);
        }
    }
}
