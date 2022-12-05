package org.ssa;



/**
 *	Machine in a factory
 *	@author Joel Karel
 *	@version %I%, %G%
 */
public class Machine implements CProcess,ProductAcceptor
{
	static double SPEED = 1;
	// The edges of a hexagon are equal to the radius of the tightest circel around it.
	static double EDGE_LENGTH = 5;
	/* Since a hexagon exists of 6 equal triangles with the same edgeLength x as the hexagon,
	 * the distance from the the center of the hexagon to the edge is x*(0.5*x)*0.5 = x*x/4.
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
	static double D_TO_EDGE = EDGE_LENGTH*EDGE_LENGTH/4;
	
	/** Product that is being handled  */
	private Product product;
	/** Eventlist that will manage events */
	private final CEventList eventlist;
	/** Queue from which the machine has to take products */
	private Queue queue;
	/** Sink to dump products */
	private ProductAcceptor sink;
	/** Status of the machine (b=busy, i=idle) */
	private char status;
	/** Machine name */
	private final String name;
	/** Mean processing time */
	private double meanProcTime;
	/** Processing times (in case pre-specified) */
	private double[] processingTimes;
	/** Processing time iterator */
	private int procCnt;
	

	/**
	*	Constructor
	*        Service times are exponentially distributed with mean 30
	*	@param q	Queue from which the machine has to take products
	*	@param s	Where to send the completed products
	*	@param e	Eventlist that will manage events
	*	@param n	The name of the machine
	*/
	public Machine(Queue q, ProductAcceptor s, CEventList e, String n)
	{
		status='i';
		queue=q;
		sink=s;
		eventlist=e;
		name=n;
		meanProcTime=30;
		queue.askProduct(this);
	}

	/**
	*	Constructor
	*        Service times are exponentially distributed with specified mean
	*	@param q	Queue from which the machine has to take products
	*	@param s	Where to send the completed products
	*	@param e	Eventlist that will manage events
	*	@param n	The name of the machine
	*        @param m	Mean processing time
	*/
	public Machine(Queue q, ProductAcceptor s, CEventList e, String n, double m)
	{
		status='i';
		queue=q;
		sink=s;
		eventlist=e;
		name=n;
		meanProcTime=m;
		queue.askProduct(this);
	}
	
	/**
	*	Constructor
	*        Service times are pre-specified
	*	@param q	Queue from which the machine has to take products
	*	@param s	Where to send the completed products
	*	@param e	Eventlist that will manage events
	*	@param n	The name of the machine
	*        @param st	service times
	*/
	public Machine(Queue q, ProductAcceptor s, CEventList e, String n, double[] st)
	{
		status='i';
		queue=q;
		sink=s;
		eventlist=e;
		name=n;
		meanProcTime=-1;
		processingTimes=st;
		procCnt=0;
		queue.askProduct(this);
	}

	/**
	*	Method to have this object execute an event
	*	@param type	The type of the event that has to be executed
	*	@param tme	The current time
	*/
	public void execute(int type, double tme)
	{
		// show arrival
		System.out.println("Product finished at time = " + tme);
		// Remove product from system
		product.stamp(tme,"Production complete",name);
		sink.giveProduct(product);
		product=null;
		// set machine status to idle
		status='i';
		// Ask the queue for products
		if (queue.askProduct(this)) {
			status='h';
		}
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
		if(status=='i')
		{
			// accept the product
			product=p;
			// mark starting time
			product.stamp(eventlist.getTime(),"Production started",name);
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
			double duration = drawRandomExponential(meanProcTime);
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

	public static double drawPickupAndDeliveryTime()
	{
		// draw a [0,1] uniform distributed number
		double u = Math.random();
		// Convert it into a exponentially distributed random variate with mean 33
		double res = -mean*Math.log(u);
		return res;
	}
	
	public static double drawPickupAndDeliveryTime(){
		String hubType = name.substring(0,3).toLowerCase();
		if ((hubType.equals("hos")) || (status == 'h')){
			return SPEED*pickupFromHospital();
		}
		else {
			return SPEED*pickupFromHub();
		}
	}
	
	public static double pickupFromHospital() {
		Vector v = generatePointInHexagon();
		double x = v.getX();
		double y = v.getY();
		double pickupDistance = Math.abs(x) + Math.abs(y);
		double bringToHospitalDistance = pickupDistance;return pickupDistance + bringToHospitalDistance;
	}
	
	/* To shift the coordinates from the perspective where to hub is (0,0)
	 * to the perspective where the hospital is (0,0),
	 * we just ad 2*D_TO_EDGE to the x-coordinate.
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

	public static double pickupFromHub() {
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
	public static Vector generatePointInHexagon() {
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
}
