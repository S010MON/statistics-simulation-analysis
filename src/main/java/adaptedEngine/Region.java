package adaptedEngine;

import engine.CEventList;
import engine.Product;
import engine.ProductAcceptor;
import engine.Source;

public class Region extends Source
{

    public Region(ProductAcceptor q, CEventList l, String n)
    {
        super(q, l, n);
    }

    public Region(ProductAcceptor q, CEventList l, String n, double m)
    {
        super(q, l, n, m);
    }

    public Region(ProductAcceptor q, CEventList l, String n, double[] ia)
    {
        super(q, l, n, ia);
    }

    @Override
    public void execute(int type, double tme)
    {
        // show arrival
        System.out.println("Arrival at time = " + tme);
        // give arrived product to queue
        Patient p = new Patient();
        Priority priority = p.getPriority();
        p.stamp(tme,"New patient priority " + priority.toString(),name);
        queue.giveProduct(p);
        // generate duration
        if(meanArrTime>0)
        {
            double duration = drawRandomExponential(meanArrTime);
            // Create a new event in the eventlist
            list.add(this,0,tme+duration); //target,type,time
        }
        else
        {
            interArrCnt++;
            if(interarrivalTimes.length>interArrCnt)
            {
                list.add(this,0,tme+interarrivalTimes[interArrCnt]); //target,type,time
            }
            else
            {
                list.stop();
            }
        }
    }
}
