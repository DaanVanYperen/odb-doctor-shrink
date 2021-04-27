package net.mostlyoriginal.game.system.view;

import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.render.TransitionSystem;
import net.mostlyoriginal.game.util.Anims;

import static com.artemis.E.E;
import static net.mostlyoriginal.api.operation.JamOperationFactory.moveBetween;
import static net.mostlyoriginal.api.operation.JamOperationFactory.scaleBetween;
import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * @author Daan van Yperen
 */
@Wire
public class FeatureScreenSetupSystem extends PassiveSystem {

    FeatureScreenAssetSystem assetSystem;

    @Override
    protected void initialize() {
        super.initialize();
        addLogo();
    }

    public void addLogo() {
        E()
                .renderLayer(1000)
                .pos(10, 10)
                .bounds(0,0,100,100)
                .anim("logo");
    }
}
