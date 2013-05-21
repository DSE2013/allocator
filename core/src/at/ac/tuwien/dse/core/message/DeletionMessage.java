package at.ac.tuwien.dse.core.message;

public class DeletionMessage extends Message {
	private static final long serialVersionUID = 6974338581864888070L;
	
	private int timeSlotId;

	public int getTimeSlotId() {
		return timeSlotId;
	}

	public void setTimeSlotId(int timeSlotId) {
		this.timeSlotId = timeSlotId;
	}
}
