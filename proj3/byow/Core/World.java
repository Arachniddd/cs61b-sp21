package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    private final TETile[][] WorldMap;
    private final int WIDTH;
    private final int HEIGHT;
    private final int SEED;
    private final Random RANDOM;
    private final int roomNum;
    private final List<Room> Rooms;

    private static final int MAX_ATTEMPT = 50;
    private static final int MIN_WIDTH = 50;
    private static final int MAX_WIDTH = 100;
    private static final int MIN_HEIGHT = 50;
    private static final int MAX_HEIGHT = 100;
    private static final int MIN_roomWIDTH = 2;
    private static final int MAX_roomWIDTH = 6;
    private static final int MIN_roomHEIGHT = 2;
    private static final int MAX_roomHEIGHT = 6;
    private static final int MAX_HWNUM = 3;

    /**
     * Init a new world with its width and height.
     * @param width
     * @param height
     */
    World(int width, int height, int seed)
    {
        WIDTH = width;
        HEIGHT = height;
        SEED = seed;
        RANDOM = new Random(seed);
        roomNum = RandomUtils.uniform(RANDOM, WIDTH * HEIGHT / 24 / 8, WIDTH * HEIGHT / 24 / 4);
        Rooms = new ArrayList<Room>();
        WorldMap = new TETile[WIDTH][HEIGHT];
        fillWithBlankTiles();
    }

    /**
     * Generate a World with given width, height, and seed.
     */
    public static World generateWorld(int width, int height, int seed)
    {
        World newWorld =  new World(width, height, seed);
        newWorld.createRooms();
        newWorld.connectRooms();
        newWorld.placeRooms();
        newWorld.addWalls();

        return newWorld;
    }

    /**
     * Pull all the created rooms into the given Room List.
     */
    private void createRooms()
    {
        int cnt = 0;
        int attempts = 0;
        while (cnt < roomNum && attempts < MAX_ATTEMPT)
        {
            boolean flag = true;
            Room tmp =  createRoom();
            if (!isConflict(tmp, Rooms)) {
                tmp.setId(cnt);
                Rooms.add(tmp);
                cnt++;
            } else {
                attempts++;
            }
        }
    }

    /**
     * (Place) Draw a list of rooms.
     */
    private void placeRooms()
    {
        for(Room room : Rooms)
        {
            drawRoom(room);
        }
    }

    /**
     * Connect rooms in the Rooms list.
     */
    private void connectRooms()
    {
        int length = Rooms.size();
//
//        for (int i = 1; i < length; i++)
//        {
//            Room nowRoom =  Rooms.get(i);
//            Room oldRoom = getRoom(i);
////            Room oldRoom = Rooms.get(i - 1);
//            while(oldRoom.getHwNum() > MAX_HWNUM)
//            {
//                oldRoom = getRoom(i);
//            }
//            Hallway hw = createHallway(nowRoom, oldRoom);
//            drawHallway(hw);
//            nowRoom.setConnected();
//            oldRoom.setConnected();
//            nowRoom.addHwNum();
//            oldRoom.addHwNum();
//        }

        List<Hallway> hws = WorldMapBuilder.roomMST(Rooms,this);
        for (Hallway hw : hws)
        {
            drawHallway(hw);
        }
    }

    private Room getRoom(int i)
    {
        int n = RandomUtils.uniform(RANDOM, 0, i);
        return Rooms.get(n);
    }

    /**
     * Add walls to all the hallways and rooms.
     */
    private void addWalls()
    {
        for  (int i = 0; i < WIDTH; i++)
        {
            for (int j = 0; j < HEIGHT; j++)
            {
                if(hasNeighbourFloor(i,j) && WorldMap[i][j] == Tileset.NOTHING)
                    WorldMap[i][j] = Tileset.WALL;
            }
        }
    }


    private boolean hasNeighbourFloor(int x, int y) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < WIDTH && ny >= 0 && ny < HEIGHT) {
                    if (WorldMap[nx][ny] == Tileset.FLOOR) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Fill the world with blanks.
     */
    private void fillWithBlankTiles() {
        int height = WorldMap[0].length;
        int width = WorldMap.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                WorldMap[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Create a hallway between two given room.
     * @param a
     * @param b
     */
    private Hallway createHallway(Room a, Room b)
    {
        Position start = a.randomPosition(RANDOM);
        Position end = b.randomPosition(RANDOM);
        int width = RandomUtils.uniform(RANDOM, 1, 3);
        ArrayList<Position> path= makePath(start, end);
        return new Hallway(start, end, width, path);
    }

    /**
     * Create a list of Positions along the hallway.
     * Randomly choose which way(x or y) it should go first.
     * @param start
     * @param end
     * @return
     */
    private ArrayList<Position> makePath(Position start, Position end) {
        ArrayList<Position> path = new ArrayList<>();

        int x = start.x;
        int y = start.y;
        path.add(new Position(x, y));

        boolean horizontalFirst = RANDOM.nextBoolean();

        if (horizontalFirst) {
            while (x != end.x) {
                x += Integer.signum(end.x - x);
                path.add(new Position(x, y));
            }
            while (y != end.y) {
                y += Integer.signum(end.y - y);
                path.add(new Position(x, y));
            }
        } else {
            while (y != end.y) {
                y += Integer.signum(end.y - y);
                path.add(new Position(x, y));
            }
            while (x != end.x) {
                x += Integer.signum(end.x - x);
                path.add(new Position(x, y));
            }
        }

        return path;
    }

    /**
     * Place/Draw the given hallway.
     * @param hallway
     */
    private void drawHallway(Hallway hallway)
    {
        List<Position> path = hallway.getPath();

        for (int i = 0; i < path.size(); i++) {
            Position p = path.get(i);
            safeDraw(p.x, p.y);

//            if (hallway.getWidth() == 2 && i > 0) {
//                Position prev = path.get(i - 1);
//
//                if (p.x != prev.x) { // horizontal move
//                    safeDraw(p.x, p.y + 1);
//                } else { // vertical move
//                    safeDraw(p.x + 1, p.y);
//                }
//            }
        }

    }

    private void safeDraw(int x, int y) {
        if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            if(WorldMap[x][y] == Tileset.NOTHING)
                WorldMap[x][y] = Tileset.FLOOR;
        }
    }


    /**
     * Draw a single room on the WorldMap.
     * @param room
     */
    private void drawRoom(Room room)
    {
        Position p = room.getBLposition();
        for (int y = p.y; y < p.y + room.getHeight(); y++) {
            drawRow(new Position(p.x, y), room.getWidth());
        }
    }

    /**
     * Draw a row with given length.
     * @param start
     * @param length
     */
    private void drawRow(Position start, int length)
    {
        for (int dx = 0; dx < length; dx ++)
        {
            int x = start.x + dx;
            int y = start.y;
            safeDraw(x,y);
        }
    }

    /**
     * Create a distinct room within the limitation of the MAX_SIZE.
     * @return A random room.
     */
    private Room createRoom()
    {
        int width = RandomUtils.uniform(RANDOM, MIN_roomWIDTH, MAX_roomWIDTH + 1);
        int height = RandomUtils.uniform(RANDOM, MIN_roomHEIGHT, MAX_roomHEIGHT + 1);
        Position p = createPosition(width, height);

        Room room = new Room(width, height, p);
        return room;

    }

    /**
     * Create a random position using RANDOM.
     * Given the thick of the wall, position should be placed from (1,1) to (width - 2, height - 2).
     * @return A good position where we can place different things.
     */
    private Position createPosition(int w, int h)
    {
        int maxX = WIDTH - w - 2;
        int maxY = HEIGHT - h - 2;

        int x = RandomUtils.uniform(RANDOM, 1, maxX + 1);
        int y = RandomUtils.uniform(RANDOM, 1, maxY + 1);
        return new Position(x, y);
    }

    /**
     * Check whether the given room is conflicted with the existing rooms.
     * @param newRoom
     * @return
     */
    private static boolean isConflict(Room newRoom, List<Room> rooms)
    {
        for (Room room : rooms)
        {
            if (isConflicted(room, newRoom))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the room can be placed.
     * @param  a
     * @param  b
     */
    private static boolean isConflicted(Room a, Room b) {
        Position pa = a.getBLposition();
        Position pb = b.getBLposition();

        int aLeft = pa.x;
        int aRight = pa.x + a.getWidth();
        int aBottom = pa.y;
        int aTop = pa.y + a.getHeight();

        int bLeft = pb.x;
        int bRight = pb.x + b.getWidth();
        int bBottom = pb.y;
        int bTop = pb.y + b.getHeight();

        // non-conflicted
        boolean noOverlap =
                aRight < bLeft ||
                        bRight < aLeft ||
                        aTop < bBottom ||
                        bTop < aBottom;

        // 冲突 = 非 noOverlap
        return !noOverlap;
    }

    public int getSEED() {
        return SEED;
    }

    public Random getRANDOM() {
        return RANDOM;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public int  getWIDTH() {
        return WIDTH;
    }

    public int  getHEIGHT()
    {
        return HEIGHT;
    }

    public TETile[][] getWorld()
    {
        return WorldMap;
    }

    public List<Room> getRooms()
    {
        return Rooms;
    }
}
