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

    public static final Tint COLOR_FEATURE_FADED = new Tint(0.8f, 1.0f, 1.0f, 0.3f);
    private final Tint TINT_FEATURE_FADED = new Tint(COLOR_FEATURE_FADED);
    public static final Tint COLOR_FEATURE_OFF = new Tint(0.8f, 1.0f, 1.0f, 0.0f);
    private final Tint TINT_FEATURE_OFF = new Tint(COLOR_FEATURE_OFF);
    public static final Tint COLOR_FEATURE_ON_OFF_COLOR = new Tint(0.8f, 1.0f, 1.0f, 1.0f);
    private final Tint TINE_FEATURE_ON_OFF = new Tint(COLOR_FEATURE_ON_OFF_COLOR);
    public static final Tint COLOR_FEATURE_ON = new Tint(1.0f, 1.0f, 1.0f, 1.0f);
    private final Tint TINT_FEATURE_ON = new Tint(COLOR_FEATURE_ON);
    public static final Tint COLOR_LOGO_FADED = new Tint(1.0f, 1.0f, 1.0f, 0.0f);
    public static final Tint COLOR_LOGO_FULL = new Tint(1.0f, 1.0f, 1.0f, 1.0f);
    FeatureScreenAssetSystem assetSystem;
    TagManager tagManager;

    private int iconIndex;


    @Override
    protected void initialize() {
        super.initialize();

        addBackground();
        addLogo();

        //scheduleTransitionToGameScreen();
    }

    private void addBackground() {

        // scale to fit.
        final float widthScale = Gdx.graphics.getWidth() / FeatureScreenAssetSystem.FEATURE_WIDTH;
        final float heightScale = Gdx.graphics.getHeight() / FeatureScreenAssetSystem.FEATURE_HEIGHT;

        Anims.createAnimAt(
                0,
                0,
                "background",
                Math.max(heightScale, widthScale));
    }

    public void addLogo() {

        // approximate percentage of screen size with logo. Use rounded numbers to keep the logo crisp.

        float zoom = Anims.scaleToScreenRounded(0.8f, FeatureScreenAssetSystem.LOGO_WIDTH);

        Anims.createCenteredAt(
                FeatureScreenAssetSystem.LOGO_WIDTH,
                FeatureScreenAssetSystem.LOGO_HEIGHT,
                "logo",
                zoom)
                .tint(COLOR_LOGO_FADED)
                .script(
                        scaleBetween(zoom * 2, zoom, 2f, Interpolation.bounceOut),
                        tween(new Tint(COLOR_LOGO_FADED), new Tint(COLOR_LOGO_FULL), 2f, Interpolation.fade)
                );

    }

    public static final int DISPLAY_SECONDS = 2;


    private void scheduleTransitionToGameScreen() {
    }

}
