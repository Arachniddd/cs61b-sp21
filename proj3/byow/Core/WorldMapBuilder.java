package byow.Core;

import java.util.*;

import static java.lang.Math.*;

public class WorldMapBuilder {

    /**
     * Make a MST logic net of the given Room list.
     * @param rooms
     * @param world
     * @return
     */
    public static List<Hallway> roomMST(List<Room> rooms, World world)
    {
        PriorityQueue<Edge> pq  = new PriorityQueue<Edge>();
        Set<Room> connected =  new HashSet<>();
        List<Hallway> hallways = new ArrayList<>();

        Room start = rooms.get(0);
        connected.add(start);

        for(Room r : rooms)
        {
            if(r != start)
            {
                pq.add(new Edge(start,r));
            }
        }

        while (connected.size() < rooms.size())
        {
            Edge e = pq.poll();

            if (e != null && connected.contains(e.to)) continue;

            if (e != null) {
                connected.add(e.to);
            }

            Position a = null;
            if (e != null) {
                a = e.from.center();
            }
            Position b = null;
            if (e != null) {
                b = e.to.center();
            }
//            int width = RandomUtils.uniform(random, 1, 3);
            List<Position> path= null;
            if (a != null) {
                path = findPath(a, b, world);
            }
            if (path != null && path.isEmpty()) {
                path = makePath(a, b, world.getRANDOM());
            }


            Hallway hw = null;
            if (e != null) {
                hw = new Hallway(e.from.getBLposition(), e.to.getBLposition(),1, path);
            }
            hallways.add(hw);

            for(Room r : rooms)
            {
                if (e != null && !connected.contains(r)){
                    pq.add(new Edge(e.to, r));
                }
            }
        }

        return hallways;
    }

    /**
     * Find nearest rooms of the given room.
     * @param room
     * @param rooms
     * @return
     */
    public static List<Room> findNearestRooms(Room room, List<Room> rooms) {
        List<Room> result = new ArrayList<>();

        for (Room r : rooms) {
            if (r != room) result.add(r);
        }

        result.sort(
                Comparator.comparingDouble(r -> Room.distance(room, r))
        );

        return result;
    }


    private static class Edge implements Comparable<Edge>
    {
        Room from, to;
        double weight;

        public Edge(Room a, Room b)
        {
            from = a;
            to = b;
            weight = Room.distance(a,b);
        }
        @Override
        public int compareTo(Edge e) {
            return Double.compare(this.weight, e.weight);
        }
    }


    /**
     * Create a list of Positions along the hallway.
     * Randomly choose which way(x or y) it should go first.
     * @param start
     * @param end
     * @return
     */
    private static ArrayList<Position> makePath(Position start, Position end, Random RANDOM) {
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

    static class Node {
        Position p;
        Node prev;

        public Node(Position start, Node prev) {
            this.p = start;
            this.prev = prev;
        }
    }

    private static final int[][] DIRS = {
            {1, 0},   // right
            {-1, 0},  // left
            {0, 1},   // up
            {0, -1}   // down
    };


    private static List<Position> findPath(Position start, Position end, World world) {
        Queue<Node> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[world.getWIDTH()][world.getHEIGHT()];

        q.add(new Node(start, null));
        visited[start.x][start.y] = true;

        while (!q.isEmpty()) {
            Node cur = q.poll();

            if (cur.p.equals(end)) {
                return reconstructPath(cur);
            }

            for (int[] d : DIRS) {
                int nx = cur.p.x + d[0];
                int ny = cur.p.y + d[1];

                if (!inBounds(nx, ny, world)) continue;
                if (visited[nx][ny]) continue;
                if (isRoomInterior(nx, ny, world.getRooms()) && !new Position(nx, ny).equals(end)) continue;

                visited[nx][ny] = true;
                q.add(new Node(new Position(nx, ny), cur));
            }
        }
        return List.of(); // 找不到路
    }

    private static boolean inBounds(int x, int y, World world) {
        return x >= 0 && x < world.getWIDTH() && y >= 0 && y < world.getHEIGHT();
    }

    private static boolean isRoomInterior(int x, int y, List<Room> Rooms) {
        for (Room r : Rooms) {
            if (r.contains(x, y)) return true;
        }
        return false;
    }

    private static List<Position> reconstructPath(Node end) {
        List<Position> path = new ArrayList<>();
        Node cur = end;

        while (cur != null) {
            path.add(cur.p);
            cur = cur.prev;
        }

        Collections.reverse(path);
        return path;
    }

}
