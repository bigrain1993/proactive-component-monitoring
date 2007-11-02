package active;

import java.io.Serializable;
import java.util.Vector;
import org.objectweb.proactive.api.ProFuture;
import org.objectweb.proactive.core.util.wrapper.BooleanWrapper;
import org.objectweb.proactive.core.util.wrapper.LongWrapper;

public class PrimeManager implements Serializable{
	private Vector<PrimeWorker> workers = new Vector<PrimeWorker>();
	public PrimeManager () {} ////empty no-arg constructor needed by ProActive
	//1. send number to all workers and if all of them say the
	//number is prime then it is
	//2. send the number randomly to one worker if prime
	//3. try the next number
	public void startComputation(LongWrapper maxNumber){
		boolean prime;//true after checking if a number is prime 
		int futureIndex;//updated future index;
		long primeCheck = 2; //start number
		long primeCounter = 1;
		int k=0;
		Vector<BooleanWrapper> answers = new Vector<BooleanWrapper>();
		while (primeCounter < maxNumber.longValue()){
			//1. send request to all workers
			for (PrimeWorker worker : workers)
				// Non blocking (asynchronous method call)
				// adds the futures to the vector
				answers.add(worker.isPrime(new LongWrapper(primeCheck))); 	
			//2. wait for all the answers, or an answer that says NO 
			prime = true;
			while (!answers.isEmpty() && prime)  {//repeat until a worker says no or all the workers responded (i.e. vector is emptied)
			    // Will block until a new response is available
				futureIndex=ProFuture.waitForAny(answers); //blocks until a future is actualized 
				prime = answers.get(futureIndex).booleanValue(); //check the answer
				answers.remove(futureIndex); //remove the actualized future
			}// end while check for primes
			if (prime) { //print if prime
				sendPrime(new LongWrapper(primeCheck));	
				System.out.print(primeCheck + ", ");
				primeCounter++;//prime number found 
				//flush print buffer every 20 numbers
				if (k % 20 == 0) System.out.println("\n");
				k++;
			}
			primeCheck++;
		}//end while number loop
	}// end StartComputation
	
	//add a workers to the worker Vector
	public void addWorker(PrimeWorker worker){
		workers.add(worker);
	}
	
	//sends the prime numbers found  to one worker randomly
	public void sendPrime(LongWrapper number){
		int destination = (int)Math.round(Math.random()* (workers.size()-1));
		workers.get(destination).addPrime(number);
	}

}