package com.gs.phone.account;

import com.gs.common.Configuration;
import com.gs.common.Constants;

public class TwilioAccount {

	private static Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);

	public static String getAccountSid() {
		String sAccountSid = applicationConfig
				.get(Constants.PROP_TWILIO_ACCOUNT_SID);
		return sAccountSid;
	}

	public static String getAccountToken() {
		String sAccountToken = applicationConfig
				.get(Constants.PROP_TWILIO_ACCOUNT_TOKEN);
		return sAccountToken;
	}

}
