package net.mostlyoriginal.game.system.render;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.InterpolationStrategy;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.render.CameraFocus;
import net.mostlyoriginal.game.system.CircleTransitionSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import com.badlogic.gdx.math.Interpolation;

/**
 * @author Daan van Yperen
 */
public class CameraFollowSystem extends FluidIteratingSystem {

    CameraSystem cameraSystem;
    private MyAnimRenderSystem myAnimRenderSystem;
    private boolean lockCamera;
    private CircleTransitionSystem circleTransitionSystem;

    public CameraFollowSystem() {
        super(Aspect.all(Pos.class, CameraFocus.class));
    }

    private int targetY = 0;
    private int sourceY = 0;
    private float cooldown = 0f;

    @Override
    protected void process(E e) {
        if ( Gdx.input.isKeyJustPressed(Input.Keys.F9) ) lockCamera = !lockCamera;

        if ( lockCamera) return;

        float focusX = e.posX() + e.boundsCx();
        float focusY = e.posY() + e.boundsCy();

        if ( circleTransitionSystem.isFullyCovering() ) {
            cameraSystem.camera.position.x = myAnimRenderSystem.roundToPixels(focusX);
            cameraSystem.camera.position.y = myAnimRenderSystem.roundToPixels(focusY);
            cameraSystem.camera.update();
            return;
        }

        if (true) {
            float newTargetY = myAnimRenderSystem.roundToPixels(focusY);
            if (targetY != newTargetY) {
                sourceY = (int) cameraSystem.camera.position.y;
                targetY = (int) newTargetY;
                cooldown = 0f;
            }
        }
        if (cooldown <= 1F) {
            cooldown += world.delta*2f;
            if (cooldown > 1f) cooldown = 1f;
            cameraSystem.camera.position.y = myAnimRenderSystem.roundToPixels(Interpolation.pow2Out.apply(sourceY,targetY, cooldown));        }
        cameraSystem.camera.position.x = myAnimRenderSystem.roundToPixels(focusX);
        cameraSystem.camera.update();

        float maxDistance = (Gdx.graphics.getHeight() / G.CAMERA_ZOOM) * 0.5F * 0.6f;
        if (  e.posY() < cameraSystem.camera.position.y - maxDistance) {
            cameraSystem.camera.position.y = myAnimRenderSystem.roundToPixels(e.posY() + maxDistance);
            cameraSystem.camera.update();
        }
    }
}

