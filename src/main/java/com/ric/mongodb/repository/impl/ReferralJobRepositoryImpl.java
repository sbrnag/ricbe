package com.ric.mongodb.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.ric.domain.ReferralJob;
import com.ric.mongodb.repository.ReferralJobRepository;

@Repository("referralJobRepository")
public class ReferralJobRepositoryImpl implements ReferralJobRepository {

	private static final Class<ReferralJob> clazz = ReferralJob.class;

	@Autowired
	private MongoTemplate mongo;

	public MongoTemplate getMongo() {
		return mongo;
	}

	public void setMongo(MongoTemplate mongo) {
		this.mongo = mongo;
	}

	@Override
	public void save(final ReferralJob job) throws DataAccessResourceFailureException, DuplicateKeyException {
		mongo.insert(job);
	}

	@Override
	public List<ReferralJob> listJobs() throws DataAccessResourceFailureException {
		
		return mongo.findAll(clazz);
	}

	@Override
	public ReferralJob findByReferredBy(String username)
			throws DataAccessResourceFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ReferralJob job) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ReferralJob job) {
		// TODO Auto-generated method stub
		
	}



}
