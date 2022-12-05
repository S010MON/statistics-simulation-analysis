package org.ssa;

public class ErlangMachine_2 extends Machine{
    public ErlangMachine_2(Queue q, ProductAcceptor s, CEventList e, String n, double[] st) {
        super(q, s, e, n, st);
    }

    public ErlangMachine_2(Queue q, ProductAcceptor s, CEventList e, String n, double m) {
        super(q, s, e, n, m);
    }

    public ErlangMachine_2(Queue q, ProductAcceptor s, CEventList e, String n) {
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

	/**
	 * 
	 * @param mean a double, executed line "146" with var meanProcTime.
	 * Generate a random variate from an erlang-3 distribution with lambda = 1.
	 * Use of inverse-transformation algorithm to draw the random variate with an erlang distribution.
	 * @return random variate
	 */
	public static double drawRandom(){
		// 2nd Way inverse-transformation algorithm for generating random variates :
		// Erlang distribution increases from 0 to lambda thus from 0 to 1, we can use the inverse-transformation algorithm :
		
		// draw a [0,1] uniform distributed number
		double u = Math.random();

		// Find the CDF of the erlang-3 distribution :
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

