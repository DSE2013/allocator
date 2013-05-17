package allocator;

import java.io.IOException;
import java.net.UnknownHostException;

import util.Config;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class Allocator {
	public static void main(String []args) {
		Mongo mongo;
		try {
			mongo = new Mongo(Config.DB_HOST);
		} catch (UnknownHostException e1) {
			System.out.println("conn failed");
			return;
		}
		DB db = mongo.getDB(Config.DB_NAME);

		Thread t = null;
		
		// restart if worker thread stops for some reason
		while(true) {
			if(t == null || !t.isAlive()) {
				try {
					t = new Thread(new Worker(Config.MQ_HOST, db));
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
				t.start();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
