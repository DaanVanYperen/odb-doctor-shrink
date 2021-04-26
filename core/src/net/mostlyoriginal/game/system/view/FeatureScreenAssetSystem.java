package net.mostlyoriginal.game.system.view;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;

/**
 * @author Daan van Yperen
 */
@Wire
public class FeatureScreenAssetSystem extends AbstractAssetSystem {

    public static final int LOGO_WIDTH = 276;
    public static final int LOGO_HEIGHT = 109;
    public static final int FEATURE_WIDTH = 21;
    public static final int FEATURE_HEIGHT = 21;
    public static final int BACKGROUND_WIDTH = 125;
    public static final int BACKGROUND_HEIGHT = 58;

    public FeatureScreenAssetSystem() {
        super("tileset.png");
    }

    @Override
    protected void initialize() {
        super.initialize();

        add("background", 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, 1);

        add("logo", 748, 916, 276,109, 1);
        add("feature-packed", 160, 114, FEATURE_WIDTH, FEATURE_HEIGHT, 1);
        add("feature-pooled", 182, 114, FEATURE_WIDTH, FEATURE_HEIGHT, 1);
        add("feature-hotspot", 204, 114, FEATURE_WIDTH, FEATURE_HEIGHT, 1);
        add("feature-factory", 226, 114, FEATURE_WIDTH, FEATURE_HEIGHT, 1);

    }

    @Override
    public Animation get(String identifier) {
        return super.get(identifier);
    }

}
