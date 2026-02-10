package byow.Core;


import java.util.Random;

import static java.lang.Math.sqrt;

public class Room {
    private final int width;
    private final int height;
    private final int size;
    private boolean connected;
    private int id;
    private final Position BLposition;
    private int hwNum;

    Room(int width, int height, Position position)
    {
        this.width = width;
        this.height = height;
        this.size = width * height;
        this.connected = false;
        this.id = -1;
        this.BLposition = position;
        this.hwNum = 0;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setConnected()
    {
        this.connected = true;
    }

    public boolean isConnected() {
        return connected;
    }

    public Position randomPosition(Random random)
    {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        return new Position(BLposition.x + x, BLposition.y + y);
    }

    public Position getBLposition()
    {
        return BLposition;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public int getHwNum() {
        return hwNum;
    }

    public void setHwNum(int hwNum)
    {
        this.hwNum = hwNum;
    }

    public void addHwNum()
    {
        this.hwNum++;
    }

    public int centerX()
    {
        return this.BLposition.x + width / 2;
    }


    public int centerY()
    {
        return this.BLposition.y + height / 2;
    }

    public static double distance(Room a, Room b)
    {
        int xa = a.centerX();
        int xb = b.centerX();
        int ya = a.centerY();
        int yb = b.centerY();
        return sqrt((xa - xb) * (xa - xb) + (ya - yb) * (ya - yb));
    }

    public Position center()
    {
        int x = this.centerX();
        int y = this.centerY();

        return new Position(x, y);
    }

    public boolean contains(int x, int y) {
        Position p = getBLposition();
        return x > p.x && x < p.x + width - 1
                && y > p.y && y < p.y + height - 1;
    }


}
