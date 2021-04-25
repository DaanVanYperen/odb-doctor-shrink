package net.mostlyoriginal.game.api;

/**
 * @author Daan van Yperen
 */
public class SolidDirections {
    private final int width;
    private final int height;
    private final int tileWidth;
    private final int tileHeight;
    private byte[][] mask;

    public void toggle(int x, int y, BlockType dir, boolean value) {
        if (value)
            mask[y][x] |= dir.bit;
        else
            mask[y][x] &= ~dir.bit;
    }

    public boolean atGrid(int x, int y, BlockType blockType, boolean outOfBoundsResult) {
        if (x >= width || x < 0 || y < 0 || y >= height) return outOfBoundsResult;
//        return true;
        return (mask[y][x] & blockType.bit) != 0;
    }

    public enum BlockType {
        EAST_WALL((byte) 1),
        WEST_WALL((byte) 2),
        CEILING((byte) 4),
        FLOOR((byte) 8);

        final byte bit;

        BlockType(byte bit) {
            this.bit = bit;
        }
    }

    public boolean atScreenRect(float startX, float startY, float endX, float endY, BlockType blockType, boolean outOfBoundsResult) {
        for (int x = ((int) startX / tileWidth); x <= ((int) endX / tileWidth); x++) {
            for (int y = ((int) startY / tileHeight); y <= ((int) endY / tileHeight); y++) {
                if(atGrid(x, y, blockType, outOfBoundsResult)) return true;
            }
        }
        return false;
    }

    public boolean atScreen(final float x, final float y, BlockType blockType, boolean outOfBoundsResult) {
        return atGrid(((int) x / tileWidth), ((int) y / tileHeight), blockType, outOfBoundsResult);
    }


    public void clear() {
        for (int ty = 0; ty < height; ty++) {
            for (int tx = 0; tx < width; tx++) {
                mask[ty][tx] = 0;
            }
        }
    }

    public SolidDirections(int width, int height, int tileWidth, int tileHeight) {
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.mask = new byte[height][width];
    }
}
