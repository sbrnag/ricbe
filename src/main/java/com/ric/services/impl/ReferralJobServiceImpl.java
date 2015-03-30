package com.ric.services.impl;

import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ric.domain.ReferralJob;
import com.ric.mongodb.repository.ReferralJobRepository;
import com.ric.services.ReferralJobService;
import com.ric.util.AppConstants;

@Service("referralJobService")
public class ReferralJobServiceImpl implements ReferralJobService {

	static final Logger log = LoggerFactory.getLogger(ReferralJobServiceImpl.class);
	@Autowired
	private ReferralJobRepository referralJobRepository;

	public ReferralJobRepository getReferralJobRepository() {
		return referralJobRepository;
	}

	public void setReferralJobRepository(ReferralJobRepository referralJobRepository) {
		this.referralJobRepository = referralJobRepository;
	}

	@Override
	public Response save(List<ReferralJob> jobs, String referredBy) {

		Response response;

		try {
			if(jobs != null && !jobs.isEmpty()) {
				for(ReferralJob job : jobs) {
					if(job != null) {
						job.setRefferedBy(referredBy);
						referralJobRepository.save(job);
					}
				}
			}
			response = getResponse(201, MediaType.APPLICATION_JSON, AppConstants.JOB_POSTED_SUCCESSFULL);
		} catch (DataAccessResourceFailureException c) {
			response = getResponse(500, MediaType.APPLICATION_JSON, AppConstants.INTERNAL_SERVER_ERROR);
		} catch (DuplicateKeyException de) {
			//ignore the duplicate
			//response = getResponse(409, MediaType.APPLICATION_JSON, AppConstants.MAIL_ALREADY_REGISTERED);
			response = getResponse(201, MediaType.APPLICATION_JSON, AppConstants.JOB_POSTED_SUCCESSFULL);
		}
		return response;
	}
	
	private Response getResponse(int statusCode, String mediaType, String obj) {
		String response = null;
		System.out.println("ReferralJobServiceImpl statusCode : " + statusCode);
		if(statusCode == 200 || statusCode == 201) {
	        response = "{\"secretKey\"" +":\""+ obj + "\"" + "}";
        } else {
        	response = "{\"message\"" +":\""+ obj + "\"" + "}";
		}
		return Response.status(statusCode).entity(response).type(mediaType).build(); 
	}

	@Override
	public List<ReferralJob> listJobs() {
		/*Response response;
		try {
			List<ReferralJob> jobs = referralJobRepository.listJobs();
			response = Response.status(200).entity("{\"referralJobs\"" +":\""+ jobs + "\"" + "}").type(MediaType.APPLICATION_JSON).build(); 
			
		} catch(DataAccessResourceFailureException DataAccessResourceFailureException) {
			response = getResponse(500, MediaType.APPLICATION_JSON, AppConstants.INTERNAL_SERVER_ERROR);
		}
		return response;*/
		
		List<ReferralJob> jobs = referralJobRepository.listJobs();
		return jobs;
	}


}
