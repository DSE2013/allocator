package at.ac.tuwien.dse.messenger.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationStrings {
	private String successTitleDoctor = "Successfully reserved surgery slot";
	private String successMsgDoctor = "Successfully reserved /operationtype/ surgery slot (/start/ - /end/) for patient /patient/ in hospital /hospital/.";
	
	private String failedTitleDoctor = "Surgery slot reservation failed";
	private String failedMsgDoctor = "Reservation for patient /patient/ was not successful.";
	
	private String successTitlePatient = "Surgery slot reserved";
	private String successMsgPatient = "/doctor/ reserved a /operationtype/ surgery slot in hospital /hospital/ at /start/ - /end/";
	
	private String deletedTitleDoctor = "Surgery slot reservation deleted";
	private String deletedMsgDoctor = "Successfully deleted /operationtype/ surgery slot reservation (/start/ - /end/) for patient /patient/ in hospital /hospital/.";
	
	private String deletedTitlePatient = "Surgery slot reservation deleted";
	private String deletedMsgPatient = "/doctor/ deleted your /operationtype/ surgery slot reservation in hospital /hospital/ at /start/ - /end/";
	
	private String successTitleHospital = "Surgery slot reserved";
	private String successMsgHospital = "/doctor/ reserved a /operationtype/ surgery slot at /start/ - /end/ for patient /patient/";
	
	private String deletedTitleHospital = "Surgery slot reservation deleted";
	private String deletedMsgHospital = "/doctor/ deleted a /operationtype/ surgery slot reservation at /start/ - /end/ for patient /patient/";
	
	public NotificationStrings(String doctor, String patient, String hospital, String operationType, Date start, Date end) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		successMsgDoctor = successMsgDoctor.replaceAll("/doctor/", doctor).replaceAll("/patient/", patient).replaceAll("/hospital/", hospital).replaceAll("/operationtype/", operationType).replaceAll("/start/", sdf.format(start)).replaceAll("/end/", sdf.format(end));
		successMsgPatient = successMsgPatient.replaceAll("/doctor/", doctor).replaceAll("/patient/", patient).replaceAll("/hospital/", hospital).replaceAll("/operationtype/", operationType).replaceAll("/start/", sdf.format(start)).replaceAll("/end/", sdf.format(end));
		successMsgHospital = successMsgHospital.replaceAll("/doctor/", doctor).replaceAll("/patient/", patient).replaceAll("/hospital/", hospital).replaceAll("/operationtype/", operationType).replaceAll("/start/", sdf.format(start)).replaceAll("/end/", sdf.format(end));
		deletedMsgDoctor = deletedMsgDoctor.replaceAll("/doctor/", doctor).replaceAll("/patient/", patient).replaceAll("/hospital/", hospital).replaceAll("/operationtype/", operationType).replaceAll("/start/", sdf.format(start)).replaceAll("/end/", sdf.format(end));
		deletedMsgPatient = deletedMsgPatient.replaceAll("/doctor/", doctor).replaceAll("/patient/", patient).replaceAll("/hospital/", hospital).replaceAll("/operationtype/", operationType).replaceAll("/start/", sdf.format(start)).replaceAll("/end/", sdf.format(end));
		deletedMsgHospital = deletedMsgHospital.replaceAll("/doctor/", doctor).replaceAll("/patient/", patient).replaceAll("/hospital/", hospital).replaceAll("/operationtype/", operationType).replaceAll("/start/", sdf.format(start)).replaceAll("/end/", sdf.format(end));
		failedMsgDoctor = failedMsgDoctor.replaceAll("/doctor/", doctor).replaceAll("/patient/", patient).replaceAll("/hospital/", hospital).replaceAll("/operationtype/", operationType).replaceAll("/start/", sdf.format(start)).replaceAll("/end/", sdf.format(end));
	}

	public String getSuccessTitleDoctor() {
		return successTitleDoctor;
	}

	public void setSuccessTitleDoctor(String successTitleDoctor) {
		this.successTitleDoctor = successTitleDoctor;
	}

	public String getSuccessMsgDoctor() {
		return successMsgDoctor;
	}

	public void setSuccessMsgDoctor(String successMsgDoctor) {
		this.successMsgDoctor = successMsgDoctor;
	}

	public String getFailedTitleDoctor() {
		return failedTitleDoctor;
	}

	public void setFailedTitleDoctor(String failedTitleDoctor) {
		this.failedTitleDoctor = failedTitleDoctor;
	}

	public String getFailedMsgDoctor() {
		return failedMsgDoctor;
	}

	public void setFailedMsgDoctor(String failedMsgDoctor) {
		this.failedMsgDoctor = failedMsgDoctor;
	}

	public String getSuccessTitlePatient() {
		return successTitlePatient;
	}

	public void setSuccessTitlePatient(String successTitlePatient) {
		this.successTitlePatient = successTitlePatient;
	}

	public String getSuccessMsgPatient() {
		return successMsgPatient;
	}

	public void setSuccessMsgPatient(String successMsgPatient) {
		this.successMsgPatient = successMsgPatient;
	}

	public String getDeletedTitleDoctor() {
		return deletedTitleDoctor;
	}

	public void setDeletedTitleDoctor(String deletedTitleDoctor) {
		this.deletedTitleDoctor = deletedTitleDoctor;
	}

	public String getDeletedMsgDoctor() {
		return deletedMsgDoctor;
	}

	public void setDeletedMsgDoctor(String deletedMsgDoctor) {
		this.deletedMsgDoctor = deletedMsgDoctor;
	}

	public String getDeletedTitlePatient() {
		return deletedTitlePatient;
	}

	public void setDeletedTitlePatient(String deletedTitlePatient) {
		this.deletedTitlePatient = deletedTitlePatient;
	}

	public String getDeletedMsgPatient() {
		return deletedMsgPatient;
	}

	public void setDeletedMsgPatient(String deletedMsgPatient) {
		this.deletedMsgPatient = deletedMsgPatient;
	}

	public String getSuccessTitleHospital() {
		return successTitleHospital;
	}

	public void setSuccessTitleHospital(String successTitleHospital) {
		this.successTitleHospital = successTitleHospital;
	}

	public String getSuccessMsgHospital() {
		return successMsgHospital;
	}

	public void setSuccessMsgHospital(String successMsgHospital) {
		this.successMsgHospital = successMsgHospital;
	}

	public String getDeletedTitleHospital() {
		return deletedTitleHospital;
	}

	public void setDeletedTitleHospital(String deletedTitleHospital) {
		this.deletedTitleHospital = deletedTitleHospital;
	}

	public String getDeletedMsgHospital() {
		return deletedMsgHospital;
	}

	public void setDeletedMsgHospital(String deletedMsgHospital) {
		this.deletedMsgHospital = deletedMsgHospital;
	}
}
