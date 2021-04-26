package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.PlayerCanTouch;
import net.mostlyoriginal.game.component.Powerup;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.detection.ParticleSystem;

/**
 * @author Daan van Yperen
 */
public class PowerupSystem extends FluidIteratingSystem {
    private static final String RESPAWN_LOCATION_TAG = "respawn-location";
    private static final Aspect.Builder POWERUPS = Aspect.all(Powerup.class, Pos.class, Bounds.class);
    private ParticleSystem particleSystem;

    public PowerupSystem() {
        super(POWERUPS);
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected void process(E e) {
        E player = entityWithTag("player");
        if ( player == null ) return;

        if ( e.isPlayerTouching() ) {
            switch (e.powerupType()) {
                case SYRINGE:
                    player.inventorySyringes(player.inventorySyringes()+1);
            }
            e.deleteFromWorld();
            E.E().playSound("syringe_grab");
        }
    }
}
