package net.mostlyoriginal.game.screen.detection;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.map.MapCollisionSystem;
import net.mostlyoriginal.game.system.render.TransitionSystem;

/**
 * @author Daan van Yperen
 */
public class ButtonPressSystem extends BaseSystem {
    boolean transitioning=false;
    @Override
    protected void processSystem() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !transitioning) {
            transitioning=true;
            world.getSystem(TransitionSystem.class).transition(GameScreen.class, 0.1f);
        }
    }
}
