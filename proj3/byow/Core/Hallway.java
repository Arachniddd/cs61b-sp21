package byow.Core;

import java.util.ArrayList;
import java.util.List;

public class Hallway {
    private final Position start;
    private final Position end;
    private final int width;
    private final List<Position> path;

    Hallway(Position start, Position end, int width, List<Position> path)
    {
        this.start = start;
        this.end = end;
        this.width = width;
        this.path = path;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }


    public List<Position> getPath() {
        return path;
    }

    public int getWidth() {
        return width;
    }
}
