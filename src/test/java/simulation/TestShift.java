package simulation;

import adaptedEngine.Shift;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestShift
{
    @Test void shift_available_T()
    {
        Shift shift = new Shift(10, 100);
        assertTrue(shift.available(50));
    }


    @Test void shift_available_F()
    {
        Shift shift = new Shift(10, 100);
        assertFalse(shift.available(1));
        assertFalse(shift.available(101));
    }

    @Test void shift_available_mod_T()
    {
        Shift shift = new Shift(10, 100);
        assertTrue(shift.available(1490));
    }


    @Test void shift_available_mod_F()
    {
        Shift shift = new Shift(10, 100);
        assertFalse(shift.available(1439));
        assertFalse(shift.available(1541));
    }

    @Test void shift_available_midnight_before_T()
    {
        Shift shift = new Shift(1380, 1460);
        assertTrue(shift.available(1439));
    }

    @Test void shift_available_midnight_T()
    {
        Shift shift = new Shift(1380, 1460);
        assertTrue(shift.available(1440));
    }

    @Test void shift_available_midnight_after_T()
    {
        Shift shift = new Shift(1380, 1460);
        assertTrue(shift.available(1441));
    }

    @Test void shift_available_midnight_rollover_T()
    {
        Shift shift = new Shift(1380, 420);
        assertTrue(shift.available(1500));
    }

    @Test void shift_zero_not_available()
    {
        Shift shift = new Shift(0, 0);
        assertFalse(shift.available(100));
    }
}
