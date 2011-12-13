package com.gs.call.twilio.rest;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.common.Configuration;
import com.gs.common.Constants;

public class RestClient {

	public String resourcePath = "";

	public RestClient(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	Logger appLogging = LoggerFactory.getLogger("AppLogging");
	Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);

	public String invokeTwilioRest() {

		String sTwilioResponse = "";
		String ACCOUNT_SID = applicationConfig
				.get(Constants.PROP_TWILIO_ACCOUNT_SID);
		String ACCOUNT_TOKEN = applicationConfig
				.get(Constants.PROP_TWILIO_ACCOUNT_TOKEN);
		String API_DOMAIN = applicationConfig
				.get(Constants.PROP_TWILIO_API_DOMAIN);
		String API_VERSION = applicationConfig
				.get(Constants.PROP_TWILIO_REST_API_VERSION);
		// https://{AccountSid}:{AuthToken}@api.twilio.com/2010-04-01/Accounts
		String sUrl = "https://" + ACCOUNT_SID + ":" + ACCOUNT_TOKEN + "@"
				+ API_DOMAIN + "/" + API_VERSION + "/Accounts/" + resourcePath;

		try {
			// Create an instance of HttpClient.
			HttpClient httpclient = new DefaultHttpClient();

			HttpGet httpget = new HttpGet(sUrl);
			HttpResponse response = httpclient.execute(httpget);

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				try {
					sTwilioResponse = IOUtils.toString(instream);
				} finally {
					instream.close();
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sTwilioResponse;

	}
}
