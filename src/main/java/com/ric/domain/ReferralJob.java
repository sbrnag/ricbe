package com.ric.domain;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@XmlRootElement(name = "job")
@Document(collection = "referraljob")
public class ReferralJob {

	@Id
	private String jobId;
	
	private String position;
	
	private String company;
	
	private String skills;
	
	private String expTo;
	
	private String expFrom;
	
	private String location;
	
	private String expirayDate;
	
	private String refferedBy;
	
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getExpTo() {
		return expTo;
	}

	public void setExpTo(String expTo) {
		this.expTo = expTo;
	}

	public String getExpFrom() {
		return expFrom;
	}

	public void setExpFrom(String expFrom) {
		this.expFrom = expFrom;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getExpirayDate() {
		return expirayDate;
	}

	public void setExpirayDate(String expirayDate) {
		this.expirayDate = expirayDate;
	}

	public String getRefferedBy() {
		return refferedBy;
	}

	public void setRefferedBy(String refferedBy) {
		this.refferedBy = refferedBy;
	}
	
}
