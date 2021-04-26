package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.PlayerCanTouch;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.detection.ParticleSystem;

/**
 * @author Daan van Yperen
 */
public class CheckpointSystem extends FluidIteratingSystem {
    private static final String RESPAWN_LOCATION_TAG = "respawn-location";
    private static final Aspect.Builder CHECKPOINTS = Aspect.all(PlayerCanTouch.class, Pos.class, Bounds.class);
    private ParticleSystem particleSystem;

    public CheckpointSystem() {
        super(CHECKPOINTS);
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected void process(E e) {
        E player = entityWithTag("player");
        if ( player == null ) return;

        if ( entityWithTag(RESPAWN_LOCATION_TAG) == null ) {
            // initial checkpoint.
            setRespawnAt(E.E().tag(RESPAWN_LOCATION_TAG), player.posX(), player.posY());
        }

        if ( !e.checkpointActive() && e.isPlayerTouching() ) {
            disableAllCheckpoints();
            e.checkpointActive(true);
            e.anim("waypoint-on");
            setRespawnAt(entityWithTag(RESPAWN_LOCATION_TAG), e.posX()+16, e.posY());
            particleSystem.bloodSpray(e.posX()+16,e.posY()+16,24);
            E.E().playSound("checkpoint_active");
        }
    }

    private void disableAllCheckpoints() {
        for (E e : allEntitiesMatching(CHECKPOINTS)) {
            if ( e.checkpointActive()) {
                e.anim("waypoint-off");
                e.checkpointActive(false);
            }
        }
    }

    public void respawn() {
        E player = entityWithTag("player");
        player.removeDead();
        player.removeInvisible();
        E respawnPoint = entityWithTag(RESPAWN_LOCATION_TAG);
        player.posX(respawnPoint.posX() - player.boundsCx());
        player.posY(respawnPoint.posY());
    }

    private void setRespawnAt(E checkpoint, float x, float y) {
        checkpoint.posX(x);
        checkpoint.posY(y);
    }
}
