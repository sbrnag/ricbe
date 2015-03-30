package com.ric.services;

import java.util.List;

import javax.ws.rs.core.Response;

import com.ric.domain.ReferralJob;

public interface ReferralJobService {

	public Response save(List<ReferralJob> jobs, String referredBy);

	public List<ReferralJob> listJobs();
	
	

}
