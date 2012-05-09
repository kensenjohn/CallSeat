package com.gs.phone.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.gs.bean.AdminTelephonyAccountBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.instance.Account;

public class AdminTelephonyAccountManager {
	private static Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);

	public boolean createAccount(AdminTelephonyAccountMeta adminAccountMeta)
			throws TwilioRestException {
		ArrayList<AdminTelephonyAccountBean> arrAdminTelAccount = getAdminTelephonyAccount(adminAccountMeta);

		boolean isSuccess = true;
		if (arrAdminTelAccount == null
				|| (arrAdminTelAccount != null && arrAdminTelAccount.isEmpty())) {

			String sEnvironment = applicationConfig
					.get(Constants.PROP_ENVIRONMENT);
			if (Constants.ENVIRONMENT.VIRTUAL_MACHINE.getEnv()
					.equalsIgnoreCase(sEnvironment)
					|| Constants.ENVIRONMENT.SANDBOX.getEnv().equalsIgnoreCase(
							sEnvironment)
					|| Constants.ENVIRONMENT.ALPHA.getEnv().equalsIgnoreCase(
							sEnvironment)) {

			} else if (Constants.ENVIRONMENT.BETA.getEnv().equalsIgnoreCase(
					sEnvironment)) {

			} else if (Constants.ENVIRONMENT.PROD.getEnv().equalsIgnoreCase(
					sEnvironment)) {

				TwilioRestClient client = new TwilioRestClient(
						TwilioAccount.getAccountSid(),
						TwilioAccount.getAccountToken());

				Map<String, String> vars = new HashMap<String, String>();
				vars.put("FriendlyName", adminAccountMeta.getFriendlyName());
				Account subAccount = client.getAccounts().create(vars);
				/*
				 * client.safeRequest("/" + TwilioRestClient.DEFAULT_VERSION +
				 * "/Account", "POST", vars);
				 */
				if (subAccount != null) {
					adminAccountMeta.setAccountSid(subAccount.getSid());
					adminAccountMeta.setAuthToken(subAccount.getAuthToken());
					AdminTelephonyAccountData adminTelAccountData = new AdminTelephonyAccountData();
					adminTelAccountData.insertAdminAccount(adminAccountMeta);
				}
			}

		}
		return isSuccess;
	}

	public ArrayList<AdminTelephonyAccountBean> getAdminTelephonyAccount(
			AdminTelephonyAccountMeta adminAccountMeta) {
		AdminTelephonyAccountData adminTelAccountData = new AdminTelephonyAccountData();
		ArrayList<AdminTelephonyAccountBean> arrAdminTelAccount = adminTelAccountData
				.getAdminAccount(adminAccountMeta);

		return arrAdminTelAccount;
	}

	public AdminTelephonyAccountBean getAdminAccount(
			AdminTelephonyAccountMeta adminAccountMeta) {

		AdminTelephonyAccountBean adminTelephonyAccBean = new AdminTelephonyAccountBean();
		if (adminAccountMeta != null
				&& !"".equalsIgnoreCase(adminAccountMeta.getAdminId())) {

			AdminTelephonyAccountManager adminTelAcMan = new AdminTelephonyAccountManager();
			ArrayList<AdminTelephonyAccountBean> arrAdminTelAccount = adminTelAcMan
					.getAdminTelephonyAccount(adminAccountMeta);

			if (arrAdminTelAccount != null && !arrAdminTelAccount.isEmpty()) {
				for (AdminTelephonyAccountBean tmpAdminTelephonyAccBean : arrAdminTelAccount) {
					adminTelephonyAccBean = tmpAdminTelephonyAccBean;
					break;
				}
			}
		}
		return adminTelephonyAccBean;
	}
}
