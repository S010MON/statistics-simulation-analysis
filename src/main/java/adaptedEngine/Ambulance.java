package adaptedEngine;

import engine.*;
import org.ssa.Vector;

/**
 *	Machine in a factory
 *	@author Joel Karel
 *	@version %I%, %G%
 */
public class Ambulance extends Machine implements CProcess, ProductAcceptor
{
	static double SPEED = 1;
	// The edges of a hexagon are equal to the radius of the tightest circel around it.
	static double EDGE_LENGTH = 5;
	/* Since a hexagon exists of 6 equal triangles with the same edgeLength x as the hexagon,
	 * the distance from the center of the hexagon to the edge is x*(0.5*x)*0.5 = x*x/4.
	 *                ____________
	 *               /      |     \                 ^   __
	 *              /       |      \               /|\  |\
	 *             /        | x*x*4 \             / | \   \
	 *            /         |        \           /  |  \   \
	 *            \                  /          /   |   \   x
	 *             \                /          /    |    \   \
	 *              \              /          /     |x*x/4\   \
	 *               \____________/          /______|______\   \ |
	 *               <------------>         <--------------->  ---
	 *                    x=5                      x=5
	 */
	static double D_TO_EDGE = EDGE_LENGTH * EDGE_LENGTH/4;

	private Location base;
	private Location currentLocation;


	/**
	*	Constructor
	*        Service times are exponentially distributed with mean 30
	*	@param q	Queue from which the machine has to take products
	*	@param s	Where to send the completed products
	*	@param e	Eventlist that will manage events
	*	@param n	The name of the machine
	*/
	public Ambulance(Queue q, ProductAcceptor s, CEventList e, String n, Location b)
	{
		super(q, s, e, n);
		base = b;
		currentLocation = base;
	}

	/**
	*	Constructor
	*        Service times are exponentially distributed with specified mean
	*	@param q	Queue from which the machine has to take products
	*	@param s	Where to send the completed products
	*	@param e	Eventlist that will manage events
	*	@param n	The name of the machine
	*   @param m	Mean processing time
	*/
	public Ambulance(Queue q, ProductAcceptor s, CEventList e, String n, double m, Location b)
	{
		super(q, s, e, n, m);
		base = b;
		currentLocation = base;
	}
	
	/**
	*	Constructor
	*        Service times are pre-specified
	*	@param q	Queue from which the machine has to take products
	*	@param s	Where to send the completed products
	*	@param e	Eventlist that will manage events
	*	@param n	The name of the machine
	*   @param st	service times
	*/
	public Ambulance(Queue q, ProductAcceptor s, CEventList e, String n, double[] st, Location b)
	{
		super(q, s, e, n, st);
		base = b;
		currentLocation = base;
	}

	/**
	*	Method to have this object execute an event
	*	@param type	The type of the event that has to be executed
	*	@param tme	The current time
	*/
	public void execute(int type, double tme)
	{
		// show arrival
		System.out.println("Patient delivered at time = " + tme);
		// Remove product from system
		product.stamp(tme,"Task complete",name);
		sink.giveProduct(product);
		product=null;
		// set machine status to idle
		status='i';
		// Check the queue, if it returns false then there is nobody to pickup, so return to hub using TravelTime class
		if(!queue.askProduct(this) && base == Location.HUB)
			giveProduct(new TravelTime());
	}
	
	/**
	*	Let the machine accept a product and let it start handling it
	*	@param p	The product that is offered
	*	@return	true if the product is accepted and started, false in all other cases
	*/
        @Override
	public boolean giveProduct(Product p)
	{
		// Only accept something if the machine is idle
		if(status == 'i' && p instanceof TravelTime)
		{
			// Set status to traveling
			status = 't';
			// accept the product
			product = p;
			// mark starting time
			product.stamp(eventlist.getTime(), "Ambulance return to hub", name);
			// start production
			startProduction();
			// Flag that the product has arrived
			return true;
		}
		else if(status=='i')
		{
			// accept the product
			product=p;
			// mark starting time
			product.stamp(eventlist.getTime(),"Ambulance dispatched",name);
			// start production
			startProduction();
			// Flag that the product has arrived
			return true;
		}
		// Flag that the product has been rejected
		else return false;
	}

	/**
	*	Starting routine for the production
	*	Start the handling of the current product with an exponentionally distributed processingtime with average 30
	*	This time is placed in the eventlist
	*/
	private void startProduction()
	{
		// generate duration
		if(meanProcTime>0)
		{
			double duration = drawPickupAndDeliveryTime();
			// Create a new event in the eventlist
			double tme = eventlist.getTime();
			eventlist.add(this,0,tme+duration); //target,type,time
			// set status to busy
			status='b';
		}
		else
		{
			if(processingTimes.length>procCnt)
			{
				eventlist.add(this,0,eventlist.getTime()+processingTimes[procCnt]); //target,type,time
				// set status to busy
				status='b';
				procCnt++;
			}
			else
			{
				eventlist.stop();
			}
		}
	}

	public double drawPickupAndDeliveryTime()
	{
		// If the Ambulance is returning to hub (travel = t)
		if(status == 't')
			return 2 * D_TO_EDGE;

		// If the ambulance is currently at the hospital, but is usually based at a hub
		else if (currentLocation == Location.HOSPITAL  && base == Location.HUB)
			// Return distance travelled + processing time
			return SPEED * pickupFromHospital() + drawErlang();

		// If the ambulance is collecting a patient from within the region
		else
			// Return distance travelled + processing time
			return SPEED * pickupFromHub() + drawErlang();
	}
	
	public double pickupFromHospital() {
		Vector v = generatePointInHexagon();
		double x = v.getX();
		double y = v.getY();
		double pickupDistance = Math.abs(x) + Math.abs(y);
		double bringToHospitalDistance = pickupDistance;
		return pickupDistance + bringToHospitalDistance;
	}
	
	/* To shift the coordinates from the perspective where to hub is (0,0)
	 * to the perspective where the hospital is (0,0),
	 * we just add 2*D_TO_EDGE to the x-coordinate.
	 *          _________
	 *         /         \
	 *        /           \
	 *       <      *      > /|\            /|\
	 *        \     |     /   | D_TO_EDGE    |
	 *         \____|____/   \|/             | 2*D_TO_EDGE
	 *         /    |    \   /|\             |
	 *        /     |     \   | D_TO_EDGE    |
	 *       <      *      > \|/            \|/
	 *        \           /
	 *         \_________/
	 */

	public double pickupFromHub() {
		Vector v = generatePointInHexagon();
		double x = v.getX();
		double y = v.getY();
		double pickupDistance = Math.abs(x) + Math.abs(y);
		double bringToHospitalDistance = Math.abs(x+2*D_TO_EDGE) + Math.abs(y);
		return pickupDistance + bringToHospitalDistance;
	}

	/* The center of the hexagon is (0, 0).
	 * The method will first draw an (x, y) uniformly from the square around the hexagon.
	 * As can be seen from the drawing below x ~ U(-D_TO_EDGE, D_TO_EDGE)
	 *                                   and y ~ U(-EDGE_LENGTH, EDGE_LENGTH).
	 * If the point is in the hexagon it will be returned if not a new point will be drawn from the square.
	 *            ______________________  
	 *            |   /      |     \   | /|\
	 *            |  /       |      \  |  |
	 *            | /        | x*x*4 \ |  |
	 *            |/         |        \|  | 2*D_TO_EDGE
	 *            |\                  /|  |
	 *            | \                / |  |
	 *            |  \              /  |  |
	 *            |___\____________/___| \|/
	 *            <-------------------->
	 *                 2*EDGE_LENGTH
	 */
	public Vector generatePointInHexagon() {
		double x, y;
		boolean succesfull = false;
		do {
			x = 2*D_TO_EDGE*Math.random() - D_TO_EDGE;
			y = 2*EDGE_LENGTH*Math.random() - EDGE_LENGTH;
			// Checks if it is inside or on the hexagon.
			succesfull = x < EDGE_LENGTH - Math.sqrt(3)*y;
		} while(!succesfull);
		return new Vector(x, y);
	}

	/**
	 *
	 * @param n integer
	 * Uses recursion
	 * @return factorial of n
	 */
	public static int factorial(int n)
	{
		if (n == 0)
			return 1;
		else
			return(n * factorial(n-1));
	}

	/**
	 * Generate a random variate from an erlang-3 distribution with lambda = 1.
	 * Use of inverse-transformation algorithm to draw the random variate with an erlang distribution.
	 * @return random variate
	 */
	public static double drawErlang()
	{
		// 2nd Way inverse-transformation algorithm for generating random variates 
		// Erlang distribution increases from 0 to lambda thus from 0 to 1, we can use the inverse-transformation algorithm 

		// draw a [0,1] uniform distributed number
		double u = Math.random();

		// Find the CDF of the erlang-3 distribution 
		int k = 3;
		double lambda = 1;

		double sum = 0;

		for(int times = 0; times <= k - 1; times++){
			sum = sum + ( (1/factorial(times)) * Math.exp(-lambda*u) * Math.pow(lambda*u,times));
		}

		//CDF found
		double cdf = 1 - sum;

		// Take the inverse
		double inverse = 1/cdf;

		// Erlang random variate is successfully generated
		double erlang_variate = inverse;

		return erlang_variate; // erlang R.D.V with (3,1);
	}
}
