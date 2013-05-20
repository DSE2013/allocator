package message;

public class AllocationMessage extends Message {
	private static final long serialVersionUID = 5115203031923646206L;

	private double maxDistance;
	private int operationTypeId;
	private int lengthInMin;
	
	public int getOperationTypeId() {
		return operationTypeId;
	}

	public void setOperationTypeId(int operationTypeId) {
		this.operationTypeId = operationTypeId;
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}

	public int getLengthInMin() {
		return lengthInMin;
	}

	public void setLengthInMin(int lengthInMin) {
		this.lengthInMin = lengthInMin;
	}
}
