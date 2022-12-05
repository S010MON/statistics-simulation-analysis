package org.ssa;

public class ErlangMachine_1 extends Machine{

	public ErlangMachine_1(Queue q, ProductAcceptor s, CEventList e, String n, double[] st) {
        super(q, s, e, n, st);
    }

    public ErlangMachine_1(Queue q, ProductAcceptor s, CEventList e, String n, double m) {
        super(q, s, e, n, m);
    }

    public ErlangMachine_1(Queue q, ProductAcceptor s, CEventList e, String n) {
        super(q, s, e, n);
    }


    /**
	 * 
	 * @param n integer
	 * Uses recursion
	 * @return factorial of n
	 */
	public static int factorial(int n){    
		if (n == 0)    
		  return 1;    
		else    
		  return(n * factorial(n-1));    
	   }

	// To generate a random variate with an erlang distribution, either use convolution algorithm or inverse-transformation algorithm or a formula, these three versions are written down here.
	
	/**
	 * 
	 * Generate a random variate from an erlang-3 distribution with lambda = 1
	 * Use of convolution algorithm to draw the random variate with an erlang distribution
	 * @return random variate
	 */
	public static double drawRandom(){
		// 1st Way Convolution algorithm for generating random variates :
		int convolution = 3; // shape parameter
		double erlang_variate = 0;
		double lambda = 1; // scale parameter
		
		for (int l = 0; l < convolution; l++){
			// draw a [0,1] uniform distributed number
			double u = Math.random();

			// Convert it into a exponentially distributed random variate with mean lambda
			double exp = -lambda*Math.log(u);

			// Sum all of these exponentially distributed random variates (R.D.V) to have an erlang R.D.V with(3,1);
			erlang_variate += exp;
		}
		
		return erlang_variate;
	}
}