package com.accenture.labs.sa.REST.NLP.dataStructure;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Operation implements Serializable
{
	private String url;

	private String name;
	
	private String description;
	
	private Request request;
	
	private Response response;
	
	private List<Operation> pre;
	
	private List<Operation> post;
	
	private List<String> linkURL;
	
	private Example example;
	
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param name the name to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the request
	 */
	public Request getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(Request request) {
		this.request = request;
	}

	/**
	 * @return the response
	 */
	public Response getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(Response response) {
		this.response = response;
	}

	/**
	 * @return the pre
	 */
	public List<Operation> getPre() {
		return pre;
	}

	/**
	 * @param pre the pre to set
	 */
	public void setPre(List<Operation> pre) {
		this.pre = pre;
	}

	/**
	 * @return the post
	 */
	public List<Operation> getPost() {
		return post;
	}

	/**
	 * @param post the post to set
	 */
	public void setPost(List<Operation> post) {
		this.post = post;
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

	/**
	 * @return the example
	 */
	public Example getExample() {
		return example;
	}

	/**
	 * @param example the example to set
	 */
	public void setExample(Example example) {
		this.example = example;
	}
}
