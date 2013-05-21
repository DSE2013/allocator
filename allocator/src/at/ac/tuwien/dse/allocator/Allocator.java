package at.ac.tuwien.dse.allocator;

import java.io.IOException;
import java.net.UnknownHostException;

import at.ac.tuwien.dse.core.util.Config;

import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.Mongo;

public class Allocator {
	public static void main(String []args) {
		DB db;
		try {
			db = Mongo.connect(new DBAddress(Config.DB_HOST));
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			return;
		}

		Thread t = null;
		
		// restart if worker thread stops for some reason
		while(true) {
			if(t == null || !t.isAlive()) {
				try {
					t = new Thread(new Worker(db));
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
