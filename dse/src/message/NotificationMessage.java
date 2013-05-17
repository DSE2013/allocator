package message;

public class NotificationMessage extends Message {
	private static final long serialVersionUID = 257368406584227756L;
	
	protected int userId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
