package com.gs.payment;

import com.gs.common.Constants;

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

	public BillingResponse chargePriceToUser(BillingMetaData billingMetaData) {

		PaymentChannel paymentChannel = PaymentChannel.getPaymentChannel();
		BillingService billingService = new OnlineBillingService(paymentChannel);

		BillingResponse billingResponse = billingService
				.chargePrice(billingMetaData);

		if (Constants.BILLING_RESPONSE_CODES.SUCCESS.equals(billingResponse
				.getBillingResponseCode())) {
			saveBillingInfo(billingMetaData);
		}

		return billingResponse;

	}

}
