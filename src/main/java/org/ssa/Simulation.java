/**
 *	Example program for using eventlists
 *	@author Joel Karel
 *	@version %I%, %G%
 */

package org.ssa;

import adaptedEngine.Ambulance;
import adaptedEngine.Location;
import adaptedEngine.TriageQueue;
import engine.CEventList;
import engine.Queue;
import engine.Sink;
import engine.Source;
import jogging.Logger;

public class Simulation {

    public CEventList list;
    public Queue queue;
    public Source source;
    public Sink sink;
    public Ambulance mach;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
    	// Create an eventlist
        CEventList l = new CEventList();
        // A queue for each of the regions that triages patients by highest priority
        TriageQueue q1 = new TriageQueue();
        TriageQueue q2 = new TriageQueue();
        TriageQueue q3 = new TriageQueue();
        TriageQueue q4 = new TriageQueue();
        TriageQueue q5 = new TriageQueue();
        TriageQueue q6 = new TriageQueue();
        TriageQueue q7 = new TriageQueue();

        // A source for each region that generates random patients within each region
        Source s1 = new Source(q1,l,"Region 1");
        Source s2 = new Source(q2,l,"Region 2");
        Source s3 = new Source(q3,l,"Region 3");
        Source s4 = new Source(q4,l,"Region 4");
        Source s5 = new Source(q5,l,"Region 5");
        Source s6 = new Source(q6,l,"Region 6");
        Source s7 = new Source(q7,l,"Region 7");

        // The hospital is the Sink, once a patient arrives there, they are no
        // longer our problem!
        Sink si = new Sink("Hospital");

        /*
         * Each region has 5 ambulances that are modeled as machines which take two
         * uniformly distributed variables (x, y) as a coordinate and calculate the
         * time taken to dispatch and transport a patient to the hospital as the
         * manhattan distance from their location, to the patient, and the distance
         * to
         */
        Ambulance ambulance11 = new Ambulance(q1,si,l,"Ambulance 1.1", Location.HOSPITAL);
        Ambulance ambulance12 = new Ambulance(q1,si,l,"Ambulance 1.2", Location.HOSPITAL);
        Ambulance ambulance13 = new Ambulance(q1,si,l,"Ambulance 1.3", Location.HOSPITAL);
        Ambulance ambulance14 = new Ambulance(q1,si,l,"Ambulance 1.4", Location.HOSPITAL);
        Ambulance ambulance15 = new Ambulance(q1,si,l,"Ambulance 1.5", Location.HOSPITAL);

        Ambulance ambulance21 = new Ambulance(q2,si,l,"Ambulance 2.1", Location.HUB);
        Ambulance ambulance22 = new Ambulance(q2,si,l,"Ambulance 2.2", Location.HUB);
        Ambulance ambulance23 = new Ambulance(q2,si,l,"Ambulance 2.3", Location.HUB);
        Ambulance ambulance24 = new Ambulance(q2,si,l,"Ambulance 2.4", Location.HUB);
        Ambulance ambulance25 = new Ambulance(q2,si,l,"Ambulance 2.5", Location.HUB);

        Ambulance ambulance31 = new Ambulance(q3,si,l,"Ambulance 3.1", Location.HUB);
        Ambulance ambulance32 = new Ambulance(q3,si,l,"Ambulance 3.2", Location.HUB);
        Ambulance ambulance33 = new Ambulance(q3,si,l,"Ambulance 3.3", Location.HUB);
        Ambulance ambulance34 = new Ambulance(q3,si,l,"Ambulance 3.4", Location.HUB);
        Ambulance ambulance35 = new Ambulance(q3,si,l,"Ambulance 3.5", Location.HUB);

        Ambulance ambulance41 = new Ambulance(q4,si,l,"Ambulance 4.1", Location.HUB);
        Ambulance ambulance42 = new Ambulance(q4,si,l,"Ambulance 4.2", Location.HUB);
        Ambulance ambulance43 = new Ambulance(q4,si,l,"Ambulance 4.3", Location.HUB);
        Ambulance ambulance44 = new Ambulance(q4,si,l,"Ambulance 4.4", Location.HUB);
        Ambulance ambulance45 = new Ambulance(q4,si,l,"Ambulance 4.5", Location.HUB);

        Ambulance ambulance51 = new Ambulance(q5,si,l,"Ambulance 5.1", Location.HUB);
        Ambulance ambulance52 = new Ambulance(q5,si,l,"Ambulance 5.2", Location.HUB);
        Ambulance ambulance53 = new Ambulance(q5,si,l,"Ambulance 5.3", Location.HUB);
        Ambulance ambulance54 = new Ambulance(q5,si,l,"Ambulance 5.4", Location.HUB);
        Ambulance ambulance55 = new Ambulance(q5,si,l,"Ambulance 5.5", Location.HUB);

        Ambulance ambulance61 = new Ambulance(q6,si,l,"Ambulance 6.1", Location.HUB);
        Ambulance ambulance62 = new Ambulance(q6,si,l,"Ambulance 6.2", Location.HUB);
        Ambulance ambulance63 = new Ambulance(q6,si,l,"Ambulance 6.3", Location.HUB);
        Ambulance ambulance64 = new Ambulance(q6,si,l,"Ambulance 6.4", Location.HUB);
        Ambulance ambulance65 = new Ambulance(q6,si,l,"Ambulance 6.5", Location.HUB);

        Ambulance ambulance71 = new Ambulance(q7,si,l,"Ambulance 7.1", Location.HUB);
        Ambulance ambulance72 = new Ambulance(q7,si,l,"Ambulance 7.2", Location.HUB);
        Ambulance ambulance73 = new Ambulance(q7,si,l,"Ambulance 7.3", Location.HUB);
        Ambulance ambulance74 = new Ambulance(q7,si,l,"Ambulance 7.4", Location.HUB);
        Ambulance ambulance75 = new Ambulance(q7,si,l,"Ambulance 7.5", Location.HUB);

        // start the eventlist
        l.start(2000); // 2000 is maximum time

        int[] numbers = si.getNumbers();
        double[] times = si.getTimes();
        String[] events = si.getEvents();
        String[] stations = si.getStations();

        Logger logger = new Logger();
        System.out.println("Numbers = " + numbers.length);
        System.out.println("Times = " + times.length);
        System.out.println("Events = " + events.length);
        System.out.println("Stations = " + stations.length);

        logger.log("number,time,event,station");
        for( int i = 0; i < numbers.length; i++)
        {
            logger.log(numbers[i] + "," + times[i] + "," + events[i] + "," + stations[i]);
        }

    }
    
}
