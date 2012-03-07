package com.gs.payment;

public class BillingManager {

	public boolean isCreditCardAccepted(BillingMetaData billingMetaData) {
		return true;
	}

	public void saveBillingInfo(BillingMetaData billingMetaData) {

		BillingData billingData = new BillingData();
		String sBillAddressId = billingData
				.insertBillingAddress(billingMetaData);
		if (sBillAddressId != null && !"".equalsIgnoreCase(sBillAddressId)) {
			Integer iNumOfRows = billingData.insertBillingCreditCard(
					billingMetaData, sBillAddressId);
		}
	}
}
