package at.ac.tuwien.dse.core.util;

public abstract class Config {
	// MQ
	public static final String MQ_HOST = "10.0.0.144";
	public static final int MQ_PORT = 5672;
	public static final String MQ_USER = "guest";
	public static final String MQ_PASS = "guest";
	
	// MQ Names
	public static final String MQ_NAME_UI_ALLOCATOR = "UIAllocator";
	public static final String MQ_NAME_ALLOCATOR_MESSENGER = "AllocatorMessenger";
	
	// DB
	public static final String DB_HOST = "localhost";
	public static final String DB_NAME = "dse";
	
	// DB Collection names
	public static final String DB_COLLECTION_SLOT = "slot";
	public static final String DB_COLLECTION_OPERATION = "operation";
	public static final String DB_COLLECTION_OPERATION_TYPE = "operationtype";
	public static final String DB_COLLECTION_HOSPITAL = "hospital";
	public static final String DB_COLLECTION_PATIENT = "patient";
	public static final String DB_COLLECTION_USER = "user";
	public static final String DB_COLLECTION_DOCTOR = "doctor";
	public static final String DB_COLLECTION_NOTIFICATION = "notification";
	public static final String DB_COLLECTION_HOSPITAL_EMPLOYEE = "hospitalemployee";
}
