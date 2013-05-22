package at.ac.tuwien.dse.core.model;

import java.util.Date;

public class TimeSlot {
	private Integer id;
	private Date start;
	private Date end;
	private Integer hospitalId;
	private Integer operationTypeId;
	private Integer operationId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public Integer getOperationTypeId() {
		return operationTypeId;
	}
	public void setOperationTypeId(Integer operationTypeId) {
		this.operationTypeId = operationTypeId;
	}
	public Integer getOperationId() {
		return operationId;
	}
	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}
}
