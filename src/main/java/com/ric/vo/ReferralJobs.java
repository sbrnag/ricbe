package com.ric.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.ric.domain.ReferralJob;

@XmlRootElement(name = "jobs")
public class ReferralJobs {

	private List<ReferralJob> jobs;

	public List<ReferralJob> getJobs() {
		return jobs;
	}

	public void setJobs(List<ReferralJob> jobs) {
		this.jobs = jobs;
	}
	
}
