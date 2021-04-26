package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.CheckpointSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.map.EntitySpawnerSystem;
import net.mostlyoriginal.game.system.map.MapCollisionSystem;
import net.mostlyoriginal.game.system.render.MyAnimRenderSystem;
import net.mostlyoriginal.game.system.render.TransitionSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static net.mostlyoriginal.api.utils.Duration.milliseconds;
import static net.mostlyoriginal.api.utils.Duration.seconds;

/**
 * @author Daan van Yperen
 */
public class DeathSystem extends FluidIteratingSystem {

    public TransitionSystem transitionSystem;
    private EntitySpawnerSystem entitySpawnerSystem;
    private ParticleSystem particleSystem;
    private MyAnimRenderSystem animSystem;
    private GameScreenAssetSystem assetSystem;
    private DialogSystem dialogSystem;
    private CameraSystem cameraSystem;
    private CheckpointSystem checkpointSystem;

    public DeathSystem() {
        super(Aspect.all(Pos.class).one(Mortal.class, Robot.class));
    }

    @Override
    protected void process(E e) {
        if (!e.hasDead()) {
            if (touchingDeadlyStuffs(e, false) != null) {
                e.dead();
            }

            float halfScreenWidth = (Gdx.graphics.getWidth() / G.CAMERA_ZOOM) * 0.5f + 16;
            if (e.hasRunning() && e.posX() + e.boundsMaxx() < cameraSystem.camera.position.x - halfScreenWidth) {
                e.dead();
            }
        }

        if (e.hasDead()) {
            if (e.deadCooldown() <= 0) {
                E.E().playSound("deathsound");
                e.invisible();
                e.deadCooldown(1);
                e.physicsVx(0);
                e.physicsVy(0);

                E.E().physics().gravity().pos(e.getPos()).bounds(0,0,32,32).anim("doctor-big-crushed").renderLayer(G.LAYER_PLAYER-10).script(OperationFactory.sequence(
                        OperationFactory.delay(milliseconds(500)),
                        JamOperationFactory.tintBetween(Tint.WHITE, Tint.TRANSPARENT, seconds(1f),Interpolation.fade),
                        OperationFactory.deleteFromWorld()
                ));;
                particleSystem.bloodExplosion(e.posX() + e.boundsCx(), e.posY() + e.boundsCy());
            } else {
                e.deadCooldown(e.deadCooldown() - world.delta);
                if (e.deadCooldown() <= 0) {
                    if ((e.isRobot() || e.isPlayerControlled())) {
                        checkpointSystem.respawn();
                    } else e.deleteFromWorld();
                }
            }
        }
    }

    private E touchingDeadlyStuffs(E e, boolean onlyMortals) {
        for (E o : allEntitiesWith(Deadly.class)) {
            if (o == e) continue;
            if (overlaps(o, e) && o.teamTeam() != e.teamTeam() && (!onlyMortals || o.hasMortal())) return o;
        }

        return null;
    }

    private void doExit() {
        transitionSystem.transition(GameScreen.class, 0.1f);
    }
}
