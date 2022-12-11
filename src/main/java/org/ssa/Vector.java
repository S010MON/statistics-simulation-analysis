package org.ssa;

public class Vector
{
    private double x;
    private double y;

    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector copy()
    {
        return new Vector(x, y);
    }

    @Override
    public String toString()
    {
        return "[" + x + "," + y + "]";
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
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

    public double manhattan_distance(Vector other)
    {
        double abs_x = Math.abs(this.x - other.getX());
        double abs_y = Math.abs(this.y - other.getY());
        return abs_x + abs_y;
    }
}
