package org.ssa;

public class ErlangMachine_3 extends Machine {
    public ErlangMachine_3(Queue q, ProductAcceptor s, CEventList e, String n, double[] st) {
        super(q, s, e, n, st);
    }

    public ErlangMachine_3(Queue q, ProductAcceptor s, CEventList e, String n, double m) {
        super(q, s, e, n, m);
    }

    public ErlangMachine_3(Queue q, ProductAcceptor s, CEventList e, String n) {
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
	 * Generate a random variate from an erlang-3 distribution with lambda = 1.
	 * Use of a formula to draw the random variate with an erlang distribution.
	 * @return random variate
	 */
	public static double drawRandom(){
		//3rd Way formula to generate erlang distributed random variates :
		 int k = 3; // shape param
		 int lambda = 1; // scale param

		 double uniform_nb = 1;

		 for(int multiply = 1; multiply <= k; multiply++){
			// draw a [0,1] uniform distributed number
			double u = Math.random();
			// multiply all of them, process repeated k times
			uniform_nb = uniform_nb * u;
		 }

		 double erlang_variate = ((-1)/lambda) * Math.log(uniform_nb);

		 return erlang_variate; // erlang R.D.V with (3,1);
	}
}



