package edu.ncsu.csc.ase.icon.crawler.paypal;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;

import edu.ncsu.csc.ase.dristi.logging.MyLoggerFactory;


/**
 * Class for crawling Paypal <a href="https://developer.paypal.com/docs/api/">REST API</a>
 * @author Rahul Pandita
 * @date October 21 2014
 *
 */
public class PaypalCrawler {
	
	private static Logger log = MyLoggerFactory.getLogger(PaypalCrawler.class);
	
	private static final String ASIDE_DIV_CLASS = "aside span6";
	
	private static final String SECTION_DIV_CLASS = "section row";
	
	private static final String URL = "https://developer.paypal.com/docs/api/";
	public static void main(String[] args) {
		PaypalCrawler pc = new PaypalCrawler();
		pc.crawl();
	}

	public void crawl() {
		Document doc ;
		try 
		{
			doc = Jsoup.connect(URL).get();
		} catch (IOException e) {
			log.error("Cannot Connect to API doc", e);
			return;
		} 
		removeAsideSpan(doc);
	}

	private void removeAsideSpan(Document doc) {
		// TODO Auto-generated method stub
		
	}
}
