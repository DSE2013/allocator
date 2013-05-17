package message;

public class NotificationMessage extends Message {
	private static final long serialVersionUID = 257368406584227756L;
	
	private boolean successful;
	private int timeSlotId;

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
}
