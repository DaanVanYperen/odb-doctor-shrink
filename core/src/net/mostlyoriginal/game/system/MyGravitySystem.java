package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import net.mostlyoriginal.api.component.physics.Frozen;
import net.mostlyoriginal.api.component.physics.Gravity;
import net.mostlyoriginal.api.component.physics.Physics;

/**
 * @author Daan van Yperen
 */
public class MyGravitySystem extends IteratingSystem {
    public static final int GRAVITY_FACTOR = 50;
    private static final int TERMINAL_VELOCITY = -1000;
    ComponentMapper<Physics> pm;
    ComponentMapper<Gravity> gm;

    public MyGravitySystem() {
        super(Aspect.all(new Class[]{Gravity.class, Physics.class}).exclude(Frozen.class));
    }

    protected void process(int e) {
        Physics physics = (Physics)this.pm.get(e);
        Gravity gravity = (Gravity)this.gm.get(e);
        physics.vy += gravity.y * 50.0F * this.world.delta;
        physics.vx += gravity.x * 50.0F * this.world.delta;

        if ( physics.vy < TERMINAL_VELOCITY) physics.vy = TERMINAL_VELOCITY;
    }
}
