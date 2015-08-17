package com.accenture.labs.sa.REST.NLP.dataStructure;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Example implements Serializable
{
	
	private String url;
	
	private String Description;
	
	private String request;
	
	private String response;
	
	private List<String> linkURL;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return Description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		Description = description;
	}

	/**
	 * @return the request
	 */
	public String getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(String request) {
		this.request = request;
	}

	/**
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * @return the linkURL
	 */
	public List<String> getLinkURL() {
		return linkURL;
	}

	/**
	 * @param linkURL the linkURL to set
	 */
	public void setLinkURL(List<String> linkURL) {
		if(this.linkURL == null)
			this.linkURL = linkURL;
		else
		{
			for(String url: linkURL)
			{
				if(!this.linkURL.contains(url))
					this.linkURL.add(url);
			}
		}
	}
}
