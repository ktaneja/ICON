package com.accenture.labs.sa.REST.NLP.dataStructure;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Message implements Serializable
{
	
	private String url;
	
	private String headers;
	
	private String elements;
	
	private List<Header> headerList;
	
	private List<Element> elementList;
	
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
	 * @return the headers
	 */
	public String getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(String headers) {
		this.headers = headers;
	}

	/**
	 * @return the elements
	 */
	public String getElements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(String elements) {
		this.elements = elements;
	}

	/**
	 * @return the headerList
	 */
	public List<Header> getHeaderList() {
		return headerList;
	}

	/**
	 * @param headerList the headerList to set
	 */
	public void setHeaderList(List<Header> headerList) {
		this.headerList = headerList;
	}

	/**
	 * @return the elementList
	 */
	public List<Element> getElementList() {
		return elementList;
	}

	/**
	 * @param elementList the elementList to set
	 */
	public void setElementList(List<Element> elementList) {
		this.elementList = elementList;
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
