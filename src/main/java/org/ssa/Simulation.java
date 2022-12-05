/**
 *	Example program for using eventlists
 *	@author Joel Karel
 *	@version %I%, %G%
 */

package org.ssa;

public class Simulation {

    public CEventList list;
    public Queue queue;
    public Source source;
    public Sink sink;
    public Machine mach;
	

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
    	// Create an eventlist
        CEventList l = new CEventList();
        // A queue for the machine
        Queue q1 = new Queue();
        Queue q2 = new Queue();
        Queue q3 = new Queue();
        Queue q4 = new Queue();
        Queue q5 = new Queue();
        Queue q6 = new Queue();
        Queue q7 = new Queue();

        // A source
        Source s1 = new Source(q1,l,"Source 1");
        Source s2 = new Source(q2,l,"Source 2");
        Source s3 = new Source(q3,l,"Source 3");
        Source s4 = new Source(q4,l,"Source 4");
        Source s5 = new Source(q5,l,"Source 5");
        Source s6 = new Source(q6,l,"Source 6");
        Source s7 = new Source(q7,l,"Source 7");

        // A sink
        Sink si = new Sink("Sink 1");
        // A machine
        Machine ambulance1 = new Machine(q1,si,l,"Ambulance 1");
        Machine ambulance2 = new Machine(q1,si,l,"Ambulance 2");
        Machine ambulance3 = new Machine(q1,si,l,"Ambulance 3");
        Machine ambulance4 = new Machine(q1,si,l,"Ambulance 4");
        Machine ambulance5 = new Machine(q1,si,l,"Ambulance 5");

        // start the eventlist
        l.start(2000); // 2000 is maximum time
    }
    
}
