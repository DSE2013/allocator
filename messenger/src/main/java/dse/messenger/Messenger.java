package dse.messenger;

import java.io.IOException;

public class Messenger {
	public static void main(String args[]) {
		Thread t = null;
		
		// restart if worker thread stops for some reason
		while(true) {
			if(t == null || !t.isAlive()) {
				try {
					t = new Thread(new Worker());
					t.start();
				} catch (IOException e) {
					System.out.println("thread creation failed: " + e.getMessage());
					System.out.println("retry");
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
}
