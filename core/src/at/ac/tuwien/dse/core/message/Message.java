package at.ac.tuwien.dse.core.message;

import java.io.Serializable;

public abstract class Message implements Serializable{
	private static final long serialVersionUID = 5580987960648403668L;
	
	private int doctorId;
	private int patientId;
	private boolean delete;
	
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

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
}
