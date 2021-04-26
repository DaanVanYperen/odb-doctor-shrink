package net.mostlyoriginal.game.system;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * Borrowed from
 * https://gamedev.stackexchange.com/questions/163941/2d-water-animation-is-jagged-and-not-smooth
 *
 * @author Daan van Yperen
 */
public class CircleTransitionSystem extends BaseSystem {

    public ShaderProgram shader;
    final SpriteBatch batch = new SpriteBatch(500);
    private CameraSystem cameraSystem;
    private boolean compiled;
    private GameScreenAssetSystem assetSystem;

    @Override
    protected void initialize() {
        super.initialize();
    }

    float time = 0f;
    float age = 0;
    boolean active=false;
    boolean closing=false;

    public void close() {
        age=0;
        active=true;
        closing=true;
    }

    public void open() {
        age=0;
        active=true;
        closing=false;
    }

    @Override
    protected void processSystem(){

        age += world.delta*2f;
        if ( age > 1f ) {
            if ( !closing ) active=false;
        }
        if ( age > 1.4f ) {
            if ( closing ) open();
        }

        // TODO: disable img dependency.
        if ( shader == null ) {
            shader = new ShaderProgram(Gdx.files.internal("shader/circle.vertex"), Gdx.files.internal("shader/circle.fragment"));
            compiled = shader.isCompiled();
            if ( !compiled) Gdx.app.error("Shader", shader.getLog());
        }

        if ( active ) {
            batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
            batch.setColor(new Color(0f, 0f, 0f, 1f));
            batch.begin();
            if (compiled) {
                float ageCapped = this.age < 1 ? this.age : 1;
                float radius = closing ? 1f - Interpolation.pow2.apply(ageCapped) : Interpolation.pow2.apply(ageCapped);

                shader.setUniformf("u_resolution", G.SCREEN_WIDTH, G.SCREEN_HEIGHT);
                shader.setUniformf("u_radius", radius);
                batch.setShader(shader);
            }
            TextureRegion particle = (TextureRegion) assetSystem.get("particle").getKeyFrame(0);
            batch.draw(particle, 0, 0, G.SCREEN_WIDTH / 2, G.SCREEN_HEIGHT / 2);
            batch.end();
        }
    }

    public boolean isFullyCovering() {
        return closing && age >= 1f;
    }
}
