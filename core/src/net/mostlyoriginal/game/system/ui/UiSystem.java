package net.mostlyoriginal.game.system.ui;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Inventory;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class UiSystem extends BaseSystem {

    private static final float TOP_MARGIN = 48f;
    GameScreenAssetSystem assetSystem;
    private CameraSystem cameraSystem;
    private SpriteBatch batch;
    private TagManager tagManager;

    @Override
    protected void initialize() {
        super.initialize();
        batch = new SpriteBatch(1000);
    }



    @Override
    protected void begin() {
        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
        batch.setColor(new Color(1f,1f,1f,1f));
        batch.begin();
    }

    public int previousSyringes=1;
    public float syringeAnimAge=0;
    public float syringeConsumeAnimAge=0;
    public int consumedCount = 0;


    @Override
    protected void end() {
        batch.end();
    }

    @Override
    protected void processSystem() {
        E player = E.E(tagManager.getEntity("player"));

        Inventory inventory = player.getInventory();

        if ( previousSyringes < inventory.syringes) {
            previousSyringes = inventory.syringes;
            syringeAnimAge = 0;
        }
        if ( previousSyringes > inventory.syringes) {
            consumedCount=previousSyringes - inventory.syringes;
            previousSyringes = inventory.syringes;
            syringeConsumeAnimAge = 0;
        }

        int offset = 0;
        syringeAnimAge += world.delta;
        syringeConsumeAnimAge += world.delta;
        {
            TextureRegion syringe = (TextureRegion) assetSystem.get("syringe-ui-gain").getKeyFrame(syringeAnimAge);
            for (int i = 0; i < inventory.syringes; i++) {
                batch.draw(syringe, 8 + offset++ * 40, G.SCREEN_HEIGHT / 2 - TOP_MARGIN);
            }
        }

        {
            TextureRegion syringe = (TextureRegion) assetSystem.get("syringe-ui-use").getKeyFrame(syringeConsumeAnimAge);
            for (int i = 0; i < consumedCount; i++) {
                batch.draw(syringe, 8 + offset++ * 40, G.SCREEN_HEIGHT / 2 - TOP_MARGIN);
            }
            if ( syringeConsumeAnimAge > 40*11f*0.001f ) consumedCount=0;
        }


//        batch.draw(((TextureRegion)assetSystem.get("icon_reset").getKeyFrame(0)),GameRules.SCREEN_WIDTH / 2 - 20,GameRules.SCREEN_HEIGHT / 2 - 20, 16f, 16f);
//        batch.draw(((TextureRegion)assetSystem.get(GameRules.musicOn ?"icon_music":"icon_music_off").getKeyFrame(0)),GameRules.SCREEN_WIDTH / 2 - 40,GameRules.SCREEN_HEIGHT / 2 - 20, 16f, 16f);
//        batch.draw(((TextureRegion)assetSystem.get(GameRules.sfxOn?"icon_sound":"icon_sound_off").getKeyFrame(0)),GameRules.SCREEN_WIDTH / 2 - 60,GameRules.SCREEN_HEIGHT / 2 - 20, 16f, 16f);
//
//            if (Gdx.input.isTouched()) {
//                float posX = Gdx.input.getX()/2;
//                float posY = GameRules.SCREEN_HEIGHT/2 - Gdx.input.getY()/2;
//                if (released && posY > GameRules.SCREEN_HEIGHT / GameRules.CAMERA_ZOOM - 20) {
//                    if (posX > GameRules.SCREEN_WIDTH / GameRules.CAMERA_ZOOM - 20) {
//                        if ( !E.E().withTag("player").isDead()) {
//                            released = false;
//                            leakSystem.forceDeath();
//                            E.E().playSound("astronaut-pops");
//                        }
//                        //sfxButton.anim(GameRules.sfxOn ? "icon_sound" : "icon_sound_off");
//
//                    } else if (posX > GameRules.SCREEN_WIDTH / GameRules.CAMERA_ZOOM - 40) {
//                        released = false;
//                        GameRules.musicOn = !GameRules.musicOn;
//                        if (GameRules.musicOn) {
//                            GameRules.music.play();
//                        } else {
//                            GameRules.music.pause();
//                        }
//                        //musicButton.anim(GameRules.musicOn ? "icon_music" : "icon_music_off");
//                    } else if (posX > GameRules.SCREEN_WIDTH / GameRules.CAMERA_ZOOM - 60) {
//                        released = false;
//                        GameRules.sfxOn = !GameRules.sfxOn;
//                    }
//                }
//            } else {
//                released=true;
//            }

    }
}
