package adaptedEngine;

public class Shift
{
    private double start;
    private double end;
    private final double day_length = 1440;
    private boolean invert = false;

    public Shift(double start, double  end)
    {
        this.start = start;
        this.end = end;

        if(start > end)
            invert = true;
    }

    public boolean available(double time)
    {
        time = time % day_length;

        if(invert)
            return time <= start || end < time;

        if( start < day_length && day_length <= end)
            return time > start || time <= end - day_length;

        return start < time && time <= end;
    }
}
