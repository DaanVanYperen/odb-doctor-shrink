package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Invisible;
import net.mostlyoriginal.api.component.physics.Frozen;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.game.component.Telegulp;
import net.mostlyoriginal.game.system.CircleTransitionSystem;
import net.mostlyoriginal.game.system.PlayerGazeTrackerSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static net.mostlyoriginal.api.utils.Duration.seconds;

/**
 * @author Daan van Yperen
 */
public class SwallowSystem extends FluidIteratingSystem {

    private static final int GROWL_RANGE = 200;
    private Vector3 vtmp = new Vector3();
    PlayerGazeTrackerSystem playerGazeTrackerSystem;
    CircleTransitionSystem circleTransitionSystem;

    public SwallowSystem() {
        super(Aspect.all(Pos.class, Telegulp.class));
    }

    @Override
    protected void begin() {
        super.begin();

        if (0==1&& Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
            E player = entityWithTag("player");
            E destination = entityWithTag("debug");
            if (destination != null) {
                //player.pos(destination.getPos());
                player.frozen().invisible().script(
                        OperationFactory.sequence(
                                OperationFactory.delay(seconds(0.5f)),
                                JamOperationFactory.moveTo(destination.posX(), destination.posY()),
                                OperationFactory.delay(seconds(0.5f)),
                                OperationFactory.remove(Frozen.class),
                                OperationFactory.remove(Invisible.class)));
                circleTransitionSystem.close();
            }
        }
    }

    GameScreenAssetSystem gameScreenAssetSystem;

    @Override
    protected void process(E e) {

        E player = entityWithTag("player");
        e.telegulpRoarCooldown(e.telegulpRoarCooldown()-world.delta);

        if ( !player.hasDead() && !player.isFrozen() && overlaps(player, e)) {
            e.swallowed();
            E destination = entityWithTag(e.telegulpDestination());
            if (destination != null) {
                if ( "w4".equals(e.telegulpDestination())) {
                    gameScreenAssetSystem.playMusicInGame("final.mp3");
                }
                E.E().playSound(e.telegulpSfx());
                //player.pos(destination.getPos());
                player.frozen().invisible().script(
                        OperationFactory.sequence(
                                OperationFactory.delay(seconds(0.5f)),
                                JamOperationFactory.moveTo(destination.posX(), destination.posY()),
                                OperationFactory.delay(seconds(0.5f)),
                                OperationFactory.remove(Frozen.class),
                                OperationFactory.remove(Invisible.class)));
                circleTransitionSystem.close();
            }
        } else {
            if ("start".equals(e.telegulpDestination())) {
                float distance = vtmp.set(player.posXy()).sub(e.posXy()).len();
                if (distance < GROWL_RANGE) {
                    if (e.telegulpRoarCooldown() < 0) {
                        e.telegulpRoarCooldown(30);
                        E.E().playSound("joke");
                    }
                    float factor = GROWL_RANGE - distance;
                    float range = factor * 0.05f;
                    e.angleRotation(MathUtils.random(-range, range));
                }
            }
            if (!player.hasShrunk()) {
                float distance = vtmp.set(player.posXy()).sub(e.posXy()).len();
                if (distance < GROWL_RANGE) {
                    float factor = GROWL_RANGE - distance;
                    float range = factor * 0.05f;
                    if (e.telegulpRoarCooldown() < 0) {
                        e.telegulpRoarCooldown(1f + Interpolation.pow2In.apply(distance / GROWL_RANGE) * 5f);
                        E.E().playSound("snail_roar");
                    }
                    e.angleRotation(MathUtils.random(-range, range));
                }
            }
        }

    }

}
