package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

public class WorldTest {
    public static void main(String[] args)
    {
        World newWorld = World.generateWorld(50,50,867);
        TETile[][] world = newWorld.getWorld();

        TERenderer ter = new TERenderer(newWorld.getWIDTH(), newWorld.getHEIGHT());
        ter.initialize(newWorld.getWIDTH(),  newWorld.getHEIGHT());
        ter.renderFrame(world);
    }
}
