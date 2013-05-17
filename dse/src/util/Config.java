package util;

public abstract class Config {
	public static final String MQ_HOST = "localhost";
	public static final String UI_ALLOCATOR = "UIAllocator";
	public static final String ALLOCATOR_MESSENGER = "AllocatorMessenger";
	public static final String DB_HOST = "localhost";
	public static final String DB_NAME = "dse";
	
	//DB Collection names
	public static final String DB_COLLECTION_SLOT = "slot";
	public static final String DB_COLLECTION_OPERATION = "operation";
	public static final String DB_COLLECTION_OPERATION_TYPE = "operationtype";
	public static final String DB_COLLECTION_HOSPITAL = "hospital";
	public static final String DB_COLLECTION_PATIENT = "patient";
	public static final String DB_COLLECTION_USER = "user";
}
