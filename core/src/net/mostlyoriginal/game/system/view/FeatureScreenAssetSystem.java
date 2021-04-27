package net.mostlyoriginal.game.system.view;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.game.component.SpriteData;
import net.mostlyoriginal.game.system.render.SpriteLibrary;

/**
 * @author Daan van Yperen
 */
@Wire
public class FeatureScreenAssetSystem extends AbstractAssetSystem {

    public static final int LOGO_WIDTH = 276;
    private SpriteLibrary spriteLibrary;
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
        loadSprites();
    }

    @Override
    public Animation get(String identifier) {
        return super.get(identifier);
    }


    private void loadSprites() {
        final Json json = new Json();
        spriteLibrary = json.fromJson(SpriteLibrary.class, Gdx.files.internal("sprites.json"));
        for (SpriteData sprite : spriteLibrary.sprites) {
            Animation animation = add(sprite.id, sprite.x, sprite.y, sprite.width, sprite.height, sprite.countX, sprite.countY, this.tileset, sprite.milliseconds * 0.001f);
            if (!sprite.repeat) {
                animation.setPlayMode(Animation.PlayMode.NORMAL);
            } else animation.setPlayMode(Animation.PlayMode.LOOP);
        }
    }

}
