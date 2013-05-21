package at.ac.tuwien.dse.core.message;

public class NotificationMessage extends Message {
	private static final long serialVersionUID = 257368406584227756L;
	
	private boolean successful;
	private int timeSlotId;
	private boolean delete;

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public int getTimeSlotId() {
		return timeSlotId;
	}

	public void setTimeSlotId(int timeSlotId) {
		this.timeSlotId = timeSlotId;
	}
	
	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
}
