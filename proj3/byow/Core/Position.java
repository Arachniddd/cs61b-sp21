package byow.Core;

public class Position {
    int x;
    int y;

    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position shift(int dx, int dy)
    {
        return new Position(x +  dx, y + dy);
    }
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return this.x == position.x && this.y == position.y;
    }
}
