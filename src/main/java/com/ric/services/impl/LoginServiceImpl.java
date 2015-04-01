package com.ric.services.impl;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ric.domain.Session;
import com.ric.domain.User;
import com.ric.mongodb.repository.SessionRepository;
import com.ric.mongodb.repository.UserRepository;
import com.ric.services.LoginService;
import com.ric.services.MailService;
import com.ric.util.AppConstants;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

	static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SessionRepository sessionRepository;
	
	@Autowired
	private MailService mailService;

	/**
	 * @return the userRepository
	 */
	public UserRepository getUserRepository() {
		return userRepository;
	}

	/**
	 * @param userRepository
	 *            the userRepository to set
	 */
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * @return the sessionRepository
	 */
	public SessionRepository getSessionRepository() {
		return sessionRepository;
	}

	/**
	 * @param sessionRepository
	 *            the sessionRepository to set
	 */
	public void setSessionRepository(SessionRepository sessionRepository) {
		this.sessionRepository = sessionRepository;
	}
	
	/**
	 * @return the mailService
	 */
	public MailService getMailService() {
		return mailService;
	}

	/**
	 * @param mailService the mailService to set
	 */
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	@Override
	public Response save(User user) {

		Response response;

		try {
			String mailId = user.getPersonalEmail();
			
			User userFromDb = userRepository.findByEmailId(mailId);
			if(userFromDb  != null) {
				DuplicateKeyException exception = new DuplicateKeyException(AppConstants.MAIL_ALREADY_REGISTERED);
				throw exception;
			}
			
			/*String[] str = mailId.split("@");
			String userName = str[0];
			user.setUserName(userName);*/
			userRepository.save(user);

			Session session = new Session();
			session.setUserName(user.getUserName());
			sessionRepository.save(session);

			String sessionID = session.getSessionId();

			if (sessionID != null && !sessionID.isEmpty()) {
				response = getResponse(201, MediaType.APPLICATION_JSON, session.getSessionId());
			} else {
				userRepository.delete(user);
				response = getResponse(500, MediaType.APPLICATION_JSON, AppConstants.INTERNAL_SERVER_ERROR);
			}

		} catch (DataAccessResourceFailureException darfe) {
			response = getResponse(500, MediaType.APPLICATION_JSON, AppConstants.INTERNAL_SERVER_ERROR);
		} catch (DuplicateKeyException de) {
			String errorMessage = de.getMessage(); 
			if(errorMessage.equals(AppConstants.MAIL_ALREADY_REGISTERED) ) {
				response = getResponse(409, MediaType.APPLICATION_JSON, AppConstants.MAIL_ALREADY_REGISTERED);
			} else {
				response = getResponse(409, MediaType.APPLICATION_JSON, AppConstants.USERNAME_ALREADY_TAKEN);
			}
			
		}
		return response;
	}

	@Override
	public Response authenticate(String userName, String password) {	
		Response response;

		log.info("user is trying to login ");
		try {
			if(userRepository.authenticate(userName, password)) {
				Session session = sessionRepository.getSessionByUserName(userName);
				if(session != null) {
					response = getResponse(200, MediaType.APPLICATION_JSON, session.getSessionId());
				} else {
					session = new Session();
					session.setUserName(userName);
					sessionRepository.save(session);
					response = getResponse(200, MediaType.APPLICATION_JSON, session.getSessionId());
				}
			} else{
				response = getResponse(404, MediaType.APPLICATION_JSON, AppConstants.INVALID_USER_CREDENTIALS_ERROR);
			}
		} catch (DataAccessResourceFailureException darfe) {
			response = getResponse(500, MediaType.APPLICATION_JSON, AppConstants.INTERNAL_SERVER_ERROR);
		} 
		return response;
	}

	@Override
	public void deleteSession(String key) {
		sessionRepository.delete(key);

	}

	private Response getResponse(int statusCode, String mediaType, String obj) {
		String response = null;
		if(statusCode == 200 || statusCode == 201) {
	        response = "{\"secretKey\"" +":\""+ obj + "\"" + "}";
        } else {
        	response = "{\"message\"" +":\""+ obj + "\"" + "}";
		}
		return Response.status(statusCode).entity(response).type(mediaType).build(); 
	}

	@Override
	public Response forgetPassword(String mailId) {
		Response response;
		
		log.info("forgot password is Executing ");

		try {
			User user = userRepository.findByEmailId(mailId);
			if(user !=null) {
			/*String sessionId;
			String userName = user.getUserName();
			Session session = sessionRepository.getSessionByUserName(userName);
			if(session != null) {
				sessionId = session.getSessionId();
				// if session is already there in forgot password case we are deleting the session and creating a new one.
				sessionRepository.delete(sessionId);
				session = new Session();
				session.setUserName(userName);
				sessionRepository.save(session);
				sessionId = session.getSessionId();
				
			} else {
				session = new Session();
				session.setUserName(userName);
				sessionRepository.save(session);
				sessionId = session.getSessionId();
			}*/
				//hash the password
				String userName = user.getUserName();
				String token =user.getPassword() ;
				
				String from = "referralindiadomains@gmail.com";
				String to = mailId;
				String subject = AppConstants.RESET_PASSWORD_MAIL_SUBJECT;
				String body = "http://localhost:9090/ric/changepassword.html?user=" + userName + "&token=" + token;
				mailService.sendMail(from, to, subject, body);
				response = getResponse(200, MediaType.APPLICATION_JSON, AppConstants.FORGET_PASSWORD_RESPONSE_MESSAGE);
			} else{
				response = getResponse(404, MediaType.APPLICATION_JSON, AppConstants.MAILID_NOT_REGISTERED_ERROR);
			}
		} catch(Exception e) {
			response = getResponse(500, MediaType.APPLICATION_JSON, AppConstants.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}

	@Override
	public Response resetPassword(String userName, String currentPassword, String password) {
		Response response;
		try {
			
		/*Session session = sessionRepository.getSessionByID(sessionId);
		if(session != null) {
			String userName = session.getUsername();*/
			
			User user = userRepository.findByUsernameAndPassword(userName, currentPassword);
			if(user != null) {
			user.setPassword(password);
			userRepository.update(user);
			//sessionRepository.delete(sessionId);
			response = getResponse(201, MediaType.APPLICATION_JSON, AppConstants.PASSWORD_RESET_SUCCESSFULL);
		} else {
			response = getResponse(404, MediaType.APPLICATION_JSON, AppConstants.INVALID_REQUEST_FOR_PASSWORD_RESET);
		}
		} catch(Exception e) {
			
			response = getResponse(500, MediaType.APPLICATION_JSON, AppConstants.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	

}
