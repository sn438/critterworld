package distributed;

public class Mutex {

	 boolean releaseLock;
	
	 synchronized void notifyAllMethod() {
		 releaseLock = true;
		 notifyAll();
	 }
	 
	 synchronized void waitMethod() throws InterruptedException {
		 while (!releaseLock) {
			 System.out.println("Waiting");
			 wait();
		 }
	 }
}
