package message;

public class AllocationMessage extends Message {
	private static final long serialVersionUID = 5115203031923646206L;
	
	private int operationTypeId;
	private int doctorId;
	private int patientId;
	private double maxDistance;

	public int getOperationTypeId() {
		return operationTypeId;
	}

	public void setOperationTypeId(int operationTypeId) {
		this.operationTypeId = operationTypeId;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}
}
