package net.mostlyoriginal.game.system.render;
/**
 * @author Daan van Yperen
 */

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Origin;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.basic.Scale;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.component.graphics.Invisible;
import net.mostlyoriginal.api.component.graphics.Render;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.api.manager.FontManager;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.delegate.DeferredEntityProcessingSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;
import net.mostlyoriginal.game.component.AnimSize;
import net.mostlyoriginal.game.component.G;

/**
 * Render and progress animations.
 *
 * @author Daan van Yperen
 * @see Anim
 */
@Wire
public class MyAnimRenderSystem2 extends DeferredEntityProcessingSystem {

    protected M<Pos> mPos;
    protected M<Anim> mAnim;
    protected M<Tint> mTint;
    protected M<Angle> mAngle;
    protected M<Scale> mScale;
    protected M<Origin> mOrigin;
    protected M<AnimSize> mAnimSize;
    protected M<Render> mRender;

    protected CameraSystem cameraSystem;
    protected AbstractAssetSystem abstractAssetSystem;

    protected SpriteBatch batch;
    private Origin DEFAULT_ORIGIN = new Origin(0.5f, 0.5f);

    public MyAnimRenderSystem2(EntityProcessPrincipal principal) {
        super(Aspect.all(Pos.class, Anim.class, Render.class).exclude(Invisible.class), principal);
    }

    @Override
    protected void initialize() {
        super.initialize();
        for (Controller controller : Controllers.getControllers()) {
            Gdx.app.log("bla", controller.getName());
        }
        batch = new SpriteBatch(2000);
    }

    @Override
    protected void processSystem() {
        super.processSystem();
    }

    boolean isRenderbuffering = true;

    @Override
    protected void begin() {

        isRenderbuffering = false;

        startBatch();
    }

    private void startBatch() {
        batch.setProjectionMatrix(cameraSystem.camera.combined);
        batch.begin();
    }

    @Override
    protected void end() {
        stopRenderBuffering();
        endBatch();
    }

    private void endBatch() {
        batch.end();
    }

    private GlyphLayout glyphLayout = new GlyphLayout();

    protected void process(final int e) {

        final Anim anim = mAnim.get(e);
        final Pos pos = mPos.get(e);
        final Angle angle = mAngle.getSafe(e, Angle.NONE);
        final float scale = mScale.getSafe(e, Scale.DEFAULT).scale;
        final Origin origin = mOrigin.getSafe(e, DEFAULT_ORIGIN);
        AnimSize animSize = mAnimSize.getSafe(e, null);

        if ( mRender.get(e).layer == G.LAYER_GREMLIN ) {
            startRenderBuffering();
        } else {
            stopRenderBuffering();
        }

        batch.setColor(mTint.getSafe(e, Tint.WHITE).color);

        if (anim.id != null) drawAnimation(anim, angle, origin, pos, anim.id, scale,animSize);
        if (anim.id2 != null) drawAnimation(anim, angle, origin, pos, anim.id2, scale,animSize);

//        if ( E.E(e).isBeamed() ) {
//            drawAnimation(anim, angle, origin, pos, anim.id+"_outline", scale, animSize);
//        }

        anim.age += world.delta * anim.speed;
    }

    private void stopRenderBuffering() {
        if ( isRenderbuffering ) {
            isRenderbuffering=false;
            endBatch();
            startBatch();
        }
    }

    private void startRenderBuffering() {
        // make worms and nodules pulse.
        if ( !isRenderbuffering ) {
            isRenderbuffering=true;
            endBatch();
            startBatch();
        }
    }

    Tint FONT_TINT = new Tint(1f,1f,1f,0.8f);
    Tint FONT_SHADOW_TINT = new Tint(0f,0f,0f,0.6f);

    /**
     * Pixel perfect aligning.
     */
    public float roundToPixels(final float val) {
        // since we use camera zoom rounding to integers doesn't work properly.
        return ((int) (val * cameraSystem.zoom)) / (float) cameraSystem.zoom;
    }

    public void forceAnim(E e, String id) {
        Animation<TextureRegion> anim = abstractAssetSystem.get(id);
        e.priorityAnim(id, anim.getFrameDuration(), anim.getPlayMode() == Animation.PlayMode.LOOP);
        e.priorityAnimCooldown(anim.getFrameDuration() * anim.getKeyFrames().length);
        e.priorityAnimAge(0);
    }

    private void drawAnimation(final Anim animation, final Angle angle, final Origin origin, final Pos position, String id, float scale, AnimSize animSize) {

        // don't support backwards yet.
        if (animation.age < 0) return;

        final Animation<TextureRegion> gdxanim = (Animation<TextureRegion>) abstractAssetSystem.get(id);
        if (gdxanim == null) return;

        final TextureRegion frame = gdxanim.getKeyFrame(animation.age, gdxanim.getPlayMode() != Animation.PlayMode.NORMAL && animation.loop);

        float ox =(animSize != null ? animSize.width : frame.getRegionWidth())* scale * (origin.xy.x);
        float oy = (animSize != null ? animSize.height : frame.getRegionHeight()) * scale * origin.xy.y;
        float y = roundToPixels(position.xy.y);
        float x = roundToPixels(position.xy.x);

        if (animation.flippedX && angle.rotation == 0) {
            // mirror
            batch.draw(frame.getTexture(),
                    x,
                    y,
                    ox,
                    oy,
                    animSize != null ? animSize.width : frame.getRegionWidth() * scale,
                    animSize != null ? animSize.height : frame.getRegionHeight() * scale,
                    1f,
                    1f,
                    angle.rotation,
                    frame.getRegionX(),
                    frame.getRegionY(),
                    frame.getRegionWidth(),
                    frame.getRegionHeight(),
                    true,
                    false);

        } else if (angle.rotation != 0) {
            batch.draw(frame,
                    x,
                    y,
                    ox,
                    oy,
                    animSize != null ? animSize.width : frame.getRegionWidth() * scale,
                    animSize != null ? animSize.height : frame.getRegionHeight() * scale, 1, 1,
                    angle.rotation);
        } else {
            batch.draw(frame,
                    x,
                    y,
                    animSize != null ? animSize.width :frame.getRegionWidth() * scale,
                    animSize != null ? animSize.height :frame.getRegionHeight() * scale);
        }
    }
}