package com.gs.payment;

import com.gs.common.Configuration;
import com.gs.common.Constants;

public abstract class PaymentChannel {
	private static Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);

	public abstract BillingResponse validateUserInput(
			BillingMetaData billingMetaData);

	public abstract BillingResponse chargeUser(BillingMetaData billingMetaData);

    public abstract String getPublicKey();

	public static PaymentChannel getPaymentChannel() {
		PaymentChannel paymentChannel = null;
		String sPaymentChannel = applicationConfig.get(Constants.PROP_PAYMENT_CHANNEL);
		if (sPaymentChannel != null	&& "stripe".equalsIgnoreCase(sPaymentChannel))
        {
			paymentChannel = new StripePaymentChannel();
		}
		return paymentChannel;
	}
}
