package com.ric.mongodb.repository;

import org.springframework.dao.DataAccessResourceFailureException;

import com.ric.domain.User;



public interface UserRepository {
	
   public void save(User user) throws DataAccessResourceFailureException;
	
   public boolean authenticate(String userName, String password) throws DataAccessResourceFailureException;
   
   public User findByUserName(String userName) throws DataAccessResourceFailureException;
   
   public User findByEmailId(final String emailId) throws DataAccessResourceFailureException;
   
   public User findByUsernameAndPassword(final String userName, final String password) throws DataAccessResourceFailureException;
   
   public void delete(User user);
   
   public void update(User user);
   
}
