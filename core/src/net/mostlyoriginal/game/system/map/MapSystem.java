package net.mostlyoriginal.game.system.map;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.utils.Array;
import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class MapSystem extends BaseSystem {

    public TiledMap map;
    public int width;
    public int height;
    private Array<TiledMapTileLayer> layers;
    private boolean isSetup;
    public int mapVersion=1;


    private EntitySpawnerSystem entitySpawnerSystem;
    private GameScreenAssetSystem assetSystem;
     private MapLayer entityLayer;

    @Override
    protected void initialize() {
        map = new TmxMapLoader().load("map" + G.level + ".tmx");

        layers = new Array<TiledMapTileLayer>();
        for (MapLayer rawLayer : map.getLayers()) {
            if (rawLayer.getName().equals("entities")) {
                this.entityLayer = rawLayer;
                continue;
            }
            layers.add((TiledMapTileLayer) rawLayer);
        }
        width = layers.get(0).getWidth();
        height = layers.get(0).getHeight();

        // need to do this before we purge the indicators from the map.

        for (TiledMapTileSet tileSet : map.getTileSets()) {
            for (TiledMapTile tile : tileSet) {
                final MapProperties props = tile.getProperties();
                if (props.containsKey("entity")) {
                    Animation<TextureRegion> anim = new Animation<>(10, tile.getTextureRegion());
                    String id = (String) props.get("entity");
                    if (props.containsKey("cable-type")) {
                        id = cableIdentifier(tile);
                    } else if (props.containsKey("powered")) {
                        id = props.get("entity") + "_" + (((Boolean) props.get("powered")) ? "on" : "off");
                        if (props.containsKey("accept")) {
                            id = id + "_" + props.get("accept");
                        }
                    }
                    assetSystem.sprites.put(id, anim);
                }
            }
        }
    }

    public static String cableIdentifier(TiledMapTile tile) {
        return tile.getProperties().get("entity")
                + "_"
                + tile.getProperties().get("cable-type")
                + "_"
                + (((Boolean) tile.getProperties().get("cable-state")) ? "on" : "off");
    }

    public MapMask getMask(String property) {
        return new MapMask(height, width, G.CELL_SIZE, G.CELL_SIZE, layers, property);
    }

    /**
     * Spawn map entities.
     */
    protected void setup() {
        for (TiledMapTileMapObject o : entityLayer.getObjects().getByType(TiledMapTileMapObject.class)) {
            final MapProperties extendedProps = o.getProperties();
            if (o.getTile().getProperties().containsKey("entity")) {
                MapProperties tileProps = o.getTile().getProperties();
                entitySpawnerSystem.spawnEntity(o.getX(), o.getY(), tileProps, extendedProps);
            }
        }

        for (TiledMapTileLayer layer : layers) {
            for (int ty = 0; ty < height; ty++) {
                for (int tx = 0; tx < width; tx++) {
                    final TiledMapTileLayer.Cell cell = layer.getCell(tx, ty);
                    if (cell != null) {
                        final MapProperties properties = cell.getTile().getProperties();
                        if (properties.containsKey("entity")) {
                            if (entitySpawnerSystem.spawnEntity(tx * G.CELL_SIZE, ty * G.CELL_SIZE, properties, null)) {
                                layer.setCell(tx, ty, null);
                            }
                        }
                        if (properties.containsKey("invisible")) {
                            layer.setCell(tx, ty, null);
                        }
                    }
                }
            }
        }
    }

    public void removeAt(float x, float y) {
        for (TiledMapTileLayer layer : layers) {
            if  (layer.getProperties().containsKey("unlockable")) {
                layer.setCell((int)x/32,(int)y/32, null);
            }
        }
        mapVersion++;
    }

    @Override
    protected void processSystem() {
        if (!isSetup) {
            isSetup = true;
            setup();
        }
    }

}