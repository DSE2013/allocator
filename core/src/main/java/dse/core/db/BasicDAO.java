package dse.core.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public abstract class BasicDAO {
	protected DBCollection collection;
	
	protected Integer getNewIndex() {
		DBCursor cur = collection.find().sort(new BasicDBObject("id", -1)).limit(1);
		if(!cur.hasNext())
			return 1;
		DBObject highestIndex = cur.next();
		Integer index = 1;
		if(highestIndex != null)
			index = ((Integer)highestIndex.get("id")) + 1;
		return index;
	}
}
