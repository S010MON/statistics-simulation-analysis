package adaptedEngine;

public enum Priority
{
    A1(1),
    A2(2),
    B(3);

    private int value;

    Priority(int n)
    {
        this.value = n;
    }
}
