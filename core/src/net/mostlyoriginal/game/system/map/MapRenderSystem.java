package net.mostlyoriginal.game.system.map;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.system.PulsatingFramebufferManager;

/**
 * @author Daan van Yperen
 */
public class MapRenderSystem extends BaseSystem {

    private Color BACKGROUND_COLOR= Color.valueOf("031D1E");
    private MapSystem mapSystem;
    private CameraSystem cameraSystem;
    private PulsatingFramebufferManager pulsatingFramebufferManager;

    public MyMapRendererImpl renderer;


    @Override
    protected void initialize() {
        renderer = new MyMapRendererImpl(mapSystem.map);
    }

    @Override
    protected void processSystem() {
        pulsatingFramebufferManager.fbBegin();
        for (MapLayer layer : mapSystem.map.getLayers()) {
            if ( layer.getName().equals("entities"))
                continue;

            if (layer.isVisible()) {
                if (layer.getName().equals("background")) {
                    renderLayer((TiledMapTileLayer) layer);
                }
            }
        }
        pulsatingFramebufferManager.fbEnd(5);
        pulsatingFramebufferManager.fbBegin();
        for (MapLayer layer : mapSystem.map.getLayers()) {
            if ( layer.getName().equals("entities"))
                continue;

            if (layer.isVisible()) {
                if (!layer.getName().equals("infront") && !layer.getName().equals("background")) {
                    renderLayer((TiledMapTileLayer) layer);
                }
            }
        }
        pulsatingFramebufferManager.fbEnd(0);
    }

    private void renderLayer(final TiledMapTileLayer layer) {
        renderer.setView(cameraSystem.camera);
        renderer.renderLayer(layer);
    }
}