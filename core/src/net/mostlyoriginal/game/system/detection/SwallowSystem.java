package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.Telegulp;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class SwallowSystem extends FluidIteratingSystem {

    private static final int GROWL_RANGE = 200;
    private Vector3 vtmp = new Vector3();

    public SwallowSystem() {
        super(Aspect.all(Pos.class, Telegulp.class));
    }

    @Override
    protected void process(E e) {

        E player = entityWithTag("player");
        e.telegulpRoarCooldown(e.telegulpRoarCooldown()-world.delta);

        if ( !player.hasDead() && overlaps(player, e)) {
            E destination = entityWithTag(e.telegulpDestination());
            E.E().playSound(e.telegulpSfx());
            player.pos(destination.getPos());
        } else if ( !player.hasShrunk()) {
            float distance = vtmp.set(player.posXy()).sub(e.posXy()).len();
            if ( distance < GROWL_RANGE) {
                float factor = GROWL_RANGE - distance;
                float range = factor * 0.05f;
                if ( e.telegulpRoarCooldown() < 0 ) {
                    e.telegulpRoarCooldown(1f + Interpolation.pow2In.apply(distance / GROWL_RANGE) * 5f);
                    E.E().playSound("snail_roar");
                }
                e.angleRotation(MathUtils.random(-range, range));
            }
        }

    }

}
