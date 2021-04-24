package net.mostlyoriginal.game.system;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.G;

/**
 * Borrowed from
 * https://gamedev.stackexchange.com/questions/163941/2d-water-animation-is-jagged-and-not-smooth
 *
 * @author Daan van Yperen
 */
public class ShadedWaterRenderSystem extends BaseSystem {

    public ShaderProgram waterProgram;
    final SpriteBatch batch = new SpriteBatch(500);
    private CameraSystem cameraSystem;
    private boolean compiled;
    private FrameBuffer frameBuffer;

    @Override
    protected void initialize() {
        super.initialize();
    }

    float time = 0f;

    float[] velocity = new float[] {.006f, .007f};

    public void framebufferBegin() {
        if ( this.frameBuffer == null ) {
            this.frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, G.SCREEN_WIDTH/2, G.SCREEN_HEIGHT/2, false);
        }

        //frameBuffer.getColorBufferTexture().setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        frameBuffer.begin();
        Gdx.gl.glClearColor(0.01f,0.11f,0.11f,1f);
        Gdx.gl.glClear(16384);
    }

    public void framebufferEnd() {
        frameBuffer.end();
    }

    @Override
    protected void processSystem() {

        framebufferEnd();

        // TODO: disable img dependency.
        if ( waterProgram == null ) {
            waterProgram = new ShaderProgram(Gdx.files.internal("shader/water.vertex"), Gdx.files.internal("shader/water.fragment"));
            compiled = waterProgram.isCompiled();
            if ( !compiled) Gdx.app.error("Shader", waterProgram.getLog());
        }


        time += world.delta;

        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
        batch.setColor(new Color(1f,1f,1f,1f));
        batch.begin();
        if ( compiled ) {
            waterProgram.setUniformf("u_time", time*2f);
            waterProgram.setUniformf("u_scrollx", cameraSystem.camera.position.x / (G.SCREEN_WIDTH/2));
            waterProgram.setUniformf("u_scrolly", cameraSystem.camera.position.y / (G.SCREEN_HEIGHT/2));
            batch.setShader(waterProgram);
        }
        batch.draw(frameBuffer.getColorBufferTexture(), 0,G.SCREEN_HEIGHT/2, G.SCREEN_WIDTH/2, -G.SCREEN_HEIGHT/2);
        batch.end();
    }
}
