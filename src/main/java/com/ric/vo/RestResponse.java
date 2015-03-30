package com.ric.vo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class RestResponse {
	
	private String statusCode;
	
	private String response;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
	
    
}
