/**
 *	Example program for using eventlists
 *	@author Joel Karel
 *	@version %I%, %G%
 */

package org.ssa;

import adaptedEngine.*;
import engine.CEventList;
import engine.Sink;
import jogging.Logger;

import java.util.ArrayList;
import java.util.Stack;

public class Simulation {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
    	// Create an eventlist
        CEventList l = new CEventList();


        int no_of_regions = 7;
        // A queue for each of the regions that triages patients by highest priority
        TriageQueue[] Q = new TriageQueue[7];
        for(int i = 0; i < no_of_regions; i++)
        {
            Q[i] = new TriageQueue();
        }

        // A source for each region that generates random patients within each region
        Region[] R = new Region[no_of_regions];
        for(int i = 0; i < no_of_regions; i++)
        {
            R[i] = new Region(Q[i],l,"Region " + i);
        }

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

        // TODO - Add your shift pattern here and it will be applied to all the regions!
        Shift[] shifts = {
                new Shift(1380, 420),
                new Shift(420,  900),
                new Shift(900, 1380),
        };

        Stack<Ambulance> ambulances = new Stack<>();

        for(int region = 0; region < no_of_regions; region++)
        {
            Location location;
            if(region == 0)
                location = Location.HOSPITAL;
            else
                location = Location.HUB;

            for(int i = 0; i < shifts.length; i++)
            {
                ambulances.add(new Ambulance(Q[region], si, l, "Ambulance " + region + "." + i, location, shifts[i]));
            }
        }

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

        logger.setAppend(false);
        logger.log("number,time,event,station");
        logger.setAppend(true);
        for( int i = 0; i < numbers.length; i++)
        {
            logger.log(numbers[i] + "," + times[i] + "," + events[i] + "," + stations[i]);
        }

    }

    public static void createSchedule1(TriageQueue[] queues, Sink si, CEventList l) {
        for (int i = 0; i < 7; i++) {
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".1", Location.HUB, new Shift(1380, 420));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".2", Location.HUB, new Shift(420, 900));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".3", Location.HUB, new Shift(900, 1380));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".4", Location.HUB, new Shift(0, 0));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".5", Location.HUB, new Shift(0, 0));
        }
    }


    public static void createSchedule2(TriageQueue[] queues, Sink si, CEventList l) {
        for (int i = 0; i < 7; i++) {
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".1", Location.HUB, new Shift(1380, 420));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".2", Location.HUB, new Shift(420, 900));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".3", Location.HUB, new Shift(900, 1380));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".4", Location.HUB, new Shift(780, 1020));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".5", Location.HUB, new Shift(1260, 60 ));
        }
    }

    public static void createSchedule3(TriageQueue[] queues, Sink si, CEventList l) {
        for (int i = 0; i < 7; i++) {
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".1", Location.HUB, new Shift(1380, 420));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".2", Location.HUB, new Shift(600, 1080));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".3", Location.HUB, new Shift(1080, 120));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".4", Location.HUB, new Shift(420, 660));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".5", Location.HUB, new Shift(960, 120));
        }
    }

    public static void createSchedule4(TriageQueue[] queues, Sink si, CEventList l) {
        for (int i = 0; i < 7; i++) {
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".1", Location.HUB, new Shift(1380, 420));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".2", Location.HUB, new Shift(960, 0));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".3", Location.HUB, new Shift(420, 660));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".4", Location.HUB, new Shift(600, 840));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".5", Location.HUB, new Shift(780, 120));
        }
    }

    public static void createSchedule5(TriageQueue[] queues, Sink si, CEventList l) {
        for (int i = 0; i < 7; i++) {
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".1", Location.HUB, new Shift(1380, 420));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".2", Location.HUB, new Shift(420, 900));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".3", Location.HUB, new Shift(900, 1380));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".4", Location.HUB, new Shift(660, 1140));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".5", Location.HUB, new Shift(420, 900));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".6", Location.HUB, new Shift(900, 1380));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".7", Location.HUB, new Shift(1320, 120));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".8", Location.HUB, new Shift(1200, 0));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".9", Location.HUB, new Shift(1020,60 ));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".10", Location.HUB, new Shift(780,1020));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".11", Location.HUB, new Shift(840, 1320));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".12", Location.HUB, new Shift(420, 660));
            new Ambulance(queues[i], si, l, "Ambulance " + i + ".13", Location.HUB, new Shift(600, 840));
        }
    }

}