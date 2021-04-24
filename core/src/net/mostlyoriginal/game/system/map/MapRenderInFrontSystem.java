package net.mostlyoriginal.game.system.map;

import com.artemis.BaseSystem;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.mostlyoriginal.game.system.PulsatingFramebufferManager;

/**
 * @author Daan van Yperen
 */
public class MapRenderInFrontSystem extends BaseSystem {

    private MapSystem mapSystem;
    private CameraSystem cameraSystem;

    public MyMapRendererImpl renderer;
    private PulsatingFramebufferManager pulsatingFramebufferManager;

    @Override
    protected void initialize() {
        renderer = new MyMapRendererImpl(mapSystem.map);
    }

    @Override
    protected void processSystem() {
        pulsatingFramebufferManager.fbBegin();
        for (MapLayer layer : mapSystem.map.getLayers()) {
            if (layer.isVisible()) {
                if (layer.getName().equals("infront")) {
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