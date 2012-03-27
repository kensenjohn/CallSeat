package com.gs.utilities;

import java.util.HashMap;
import java.util.Map;

import com.gs.phone.account.TwilioAccount;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.instance.Account;

public class ManagePhoneAccounts {

	public boolean closePhoneAccount(String sPhoneAccountNumber)
			throws TwilioRestException {

		boolean isSuccess = false;
		TwilioRestClient client = new TwilioRestClient(sPhoneAccountNumber,
				TwilioAccount.getAccountToken());

		Map<String, String> vars = new HashMap<String, String>();
		vars.put("Status", "closed");
		Account subAccount = client.getAccounts().create(vars);
		if (subAccount != null
				&& "closed".equalsIgnoreCase(subAccount.getStatus())) {
			isSuccess = true;
		}

		return isSuccess;

	}
}
