package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.game.component.PlayerControlled;
import net.mostlyoriginal.game.component.map.WallSensor;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class PlayerAnimSystem extends FluidIteratingSystem {

    public PlayerAnimSystem() {
        super(Aspect.all(PlayerControlled.class, Anim.class));
    }

    @Override
    protected void process(E e) {

        if ( e.hasShrunk()) {
            e.bounds(0, 0, 16,16);
            e.footstepsStepSize(10);
        } else {
            e.bounds(16, 0, 48,48);
            e.footstepsStepSize(30);
        }
    }
}
