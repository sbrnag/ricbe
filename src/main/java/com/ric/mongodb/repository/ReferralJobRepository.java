package com.ric.mongodb.repository;

import java.util.List;

import org.springframework.dao.DataAccessResourceFailureException;

import com.ric.domain.ReferralJob;



public interface ReferralJobRepository {
	
   public void save(ReferralJob job) throws DataAccessResourceFailureException;
   
   public List<ReferralJob> listJobs() throws DataAccessResourceFailureException;
	
   public ReferralJob findByReferredBy(String username) throws DataAccessResourceFailureException;
   
   public void delete(ReferralJob job);
   
   public void update(ReferralJob job);
   
}
