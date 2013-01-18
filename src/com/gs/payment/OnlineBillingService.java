package com.gs.payment;

import com.gs.common.Constants;

public class OnlineBillingService implements BillingService {

	PaymentChannel paymentChannel;

	public OnlineBillingService(PaymentChannel paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	@Override
	public BillingResponse chargePrice(BillingMetaData billingMetaData) {

		BillingResponse billingResponse = this.paymentChannel.validateUserInput(billingMetaData);
		if (billingResponse != null
				&& Constants.BILLING_RESPONSE_CODES.SUCCESS
						.equals(billingResponse.getBillingResponseCode())) {
			billingResponse = new BillingResponse();
			billingResponse = this.paymentChannel.chargeUser(billingMetaData);
		}

		return billingResponse;
	}

}
