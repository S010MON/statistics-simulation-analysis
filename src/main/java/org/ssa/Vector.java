package org.ssa;
public class Vector {

    private int x, y;
    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector copy()
    {
        return new Vector(x, y);
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector add(Vector other)
    {
        return new Vector((x + other.getX()), (y + other.getY()));
    }

    public Vector sub(Vector other)
    {
        return new Vector((x - other.getX()), (y - other.getY()));
    }

    public int manhattan_distance(Vector other)
    {
        int abs_x = Math.abs(this.x - other.getX());
        int abs_y = Math.abs(this.y - other.getY());
        return abs_x + abs_y;
    }
}
