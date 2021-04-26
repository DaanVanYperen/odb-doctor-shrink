package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.CheckpointSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.map.EntitySpawnerSystem;
import net.mostlyoriginal.game.system.render.MyAnimRenderSystem;
import net.mostlyoriginal.game.system.render.TransitionSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static net.mostlyoriginal.api.utils.Duration.milliseconds;
import static net.mostlyoriginal.api.utils.Duration.seconds;

/**
 * Going overboard with the systems now but i'm out of tiiiime!
 * @author Daan van Yperen
 */
public class EatenSystem extends FluidIteratingSystem {

    public EatenSystem() {
        super(Aspect.one(Eaten.class,Swallowed.class));
    }

    @Override
    protected void process(E e) {
        if ( e.hasEaten() ) {
            e.eatenAge(e.eatenAge()+world.delta);
            if ( e.eatenAge() > 2f) {
                e.removeEaten();
            }
        }
        if ( e.hasSwallowed() ) {
            e.swallowedAge(e.swallowedAge()+world.delta);
            if ( e.swallowedAge() > 2f) {
                e.removeSwallowed();
            }
        }
    }
}
