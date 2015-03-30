package com.ric.rest.resources;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ric.domain.ReferralJob;
import com.ric.rest.providers.Secure;
import com.ric.services.ReferralJobService;
import com.ric.util.AppConstants;
import com.ric.vo.ReferralJobs;

/**
 * @author siva
 *
 */

@Path("/")
public class ReferralJobResource {
	static final Logger log = LoggerFactory.getLogger(ReferralJobResource.class);	
	
	@Autowired
	private ReferralJobService referralJobService;
	
	public ReferralJobService getReferralJobService() {
		return referralJobService;
	}

	public void setReferralJobService(ReferralJobService referralJobService) {
		this.referralJobService = referralJobService;
	}

	@POST
	@Secure
	@Path("/postJobs")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createJob(@Context SecurityContext sc, final List<ReferralJob> jobs) {
		Response response = null;
		try {
			if (sc.isUserInRole(AppConstants.ROLE_USER)) {

				return referralJobService.save(jobs, sc.getUserPrincipal()
						.getName());
			}
		} catch (WebApplicationException e) {
			response = e.getResponse();
		}
		return response;
	}
	
	@GET
	@Secure
	@Path("/listJobs")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listJobs(@Context SecurityContext sc) {
		Response response = null;
		try {
			if(sc.isSecure()) {
			ReferralJobs jobs = new ReferralJobs();	
			jobs.setJobs(referralJobService.listJobs());	
			response = Response.status(200).entity(jobs).build();  
			}
		} catch(WebApplicationException e) {
			response = e.getResponse();
		}
		return response;
	}

	@DELETE
	@Secure
	@Path("/deletejob")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteJob(@Context SecurityContext sc) {

		if (sc.isSecure())
			return Response.status(200).entity("deletejob executed").build();
		return Response.status(200).entity("deletejob  not executed").build();
	}

}
