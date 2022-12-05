package adaptedEngine;

import engine.Queue;

import java.util.PriorityQueue;

public class TriageQueue extends Queue
{
    private PriorityQueue<Patient> row;
    private PriorityQueue<Patient> requests;

    public TriageQueue()
    {
        row = new PriorityQueue<>();
        requests = new PriorityQueue<>();
    }
}
