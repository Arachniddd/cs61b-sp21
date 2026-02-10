package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    /**
     * Fills the given 2D array of tiles with BLANK tiles.
     * @param tiles
     */
    public static void fillWithBlankTiles(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    private static void drawRow(Position start, int length, TETile tile, TETile[][] world)
    {
        for (int dx = 0; dx < length; dx ++)
        {
            world[start.x + dx][start.y] = tile;
        }
    }

    private static class Position
    {
        private final int x;
        private final int y;

        Position(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        public Position shift(int dx, int dy)
        {
            return new Position(x + dx, y + dy);
        }
    }

    private static void addHexagonHelper(Position start, TETile[][] world, TETile tile, int b, int t)
    {
        Position point =  new Position(start.x + b, start.y);
        drawRow(point, t, tile, world);

        if (b>0)
        {
            Position nextPoint = start.shift(0, -1);
            addHexagonHelper(nextPoint, world, tile, b-1, t+2);
        }

        Position startOfReflectRow = point.shift(0, -(2*b + 1));
        drawRow(startOfReflectRow, t, tile, world);
    }

    public static void addHexagon(TETile[][] world, int size, TETile tile, Position p)
    {
        if (size < 2) return;
        addHexagonHelper(p, world, tile, size - 1, size);
    }

    public static void addHexColumn(TETile[][] world, int size, Position p, int num)
    {
        if (num < 1) return;

        addHexagon(world, size, randomBio(), p);

        if (num > 1)
        {
            Position nextP = getBottomNeighbour(p, size);
            addHexColumn(world, size, nextP, num-1);
        }
    }

    private static Position getBottomNeighbour(Position p, int size)
    {
        return p.shift(0, - 2*size);
    }

    private static Position getTopRightNeighbour(Position p, int size)
    {
        return p.shift(2*size - 1, size);
    }

    private static Position getBottomRightNeighbour(Position p, int size)
    {
        return p.shift(2*size - 1, -size);
    }

    private static TETile randomBio() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.TREE;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.SAND;
            case 3: return Tileset.GRASS;
            case 4: return Tileset.MOUNTAIN;
            default: return Tileset.NOTHING;
        }
    }


    public static void drawWorld(TETile[][] world, int hexSize, int tessSize, Position p)
    {
        addHexColumn(world, hexSize, p,  tessSize);

        for (int i = 1; i < tessSize; i++)
        {
            p = getTopRightNeighbour(p, hexSize);
            addHexColumn(world, hexSize, p, tessSize + i);
        }

        for (int i = tessSize - 2; i >= 0; i--)
        {
            p =  getBottomRightNeighbour(p, hexSize);
            addHexColumn(world, hexSize, p, tessSize + i);
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer(WIDTH, HEIGHT);
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        fillWithBlankTiles(world);
        Position p = new Position(1, 35);
        drawWorld(world, 3, 3, p);

        ter.renderFrame(world);
    }
}
