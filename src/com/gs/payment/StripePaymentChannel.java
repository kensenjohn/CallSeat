package com.gs.payment;

import java.util.HashMap;
import java.util.Map;

import com.stripe.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ExceptionHandler;
import com.gs.common.ParseUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Token;

public class StripePaymentChannel extends PaymentChannel {

	Logger appLogging = LoggerFactory.getLogger("AppLogging");
	private static Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);

	private String getPublicKey() {
		String sPublicKey = ParseUtil.checkNull(applicationConfig
				.get(Constants.PROP_STRIPE_TEST_PUBLISHABLE_KEY));
		if (Constants.STRIPE_ENVIRONMENT.LIVE.getStripeEnv().equalsIgnoreCase(
				getStripeEnvironment())) {
			sPublicKey = ParseUtil.checkNull(applicationConfig
					.get(Constants.PROP_STRIPE_LIVE_PUBLISHABLE_KEY));
		}
		return sPublicKey;
	}

	private String getSecretKey() {
		String sSecretKey = ParseUtil.checkNull(applicationConfig
				.get(Constants.PROP_STRIPE_TEST_SECRET_KEY));
		if (Constants.STRIPE_ENVIRONMENT.LIVE.getStripeEnv().equalsIgnoreCase(
				getStripeEnvironment())) {
			sSecretKey = ParseUtil.checkNull(applicationConfig
					.get(Constants.PROP_STRIPE_LIVE_SECRET_KEY));
		}
		return sSecretKey;
	}

	private String getStripeEnvironment() {
		String sStripeEnv = ParseUtil.checkNull(applicationConfig
				.get(Constants.PROP_STRIPE_ENV));
		if (sStripeEnv == null || "".equalsIgnoreCase(sStripeEnv)) {
			sStripeEnv = Constants.STRIPE_ENVIRONMENT.DEFAULT.getStripeEnv();
			appLogging.error("Stripe environment not set, "
					+ "reverting to default environment : "
					+ Constants.STRIPE_ENVIRONMENT.DEFAULT.getStripeEnv());
		}
		return sStripeEnv;
	}

	@Override
	public BillingResponse validateUserInput(BillingMetaData billingMetaData) {
		BillingResponse billingResponse = new BillingResponse();
		if (billingMetaData.isStripeTokenUsed()
				&& billingMetaData.getStripeToken() != null
				&& !"".equalsIgnoreCase(billingMetaData.getStripeToken())) {
			Stripe.apiKey = getPublicKey();
			com.stripe.model.Token token = null;
			try {
				token = Token.retrieve(billingMetaData.getStripeToken());
			} catch (StripeException e) {
				appLogging
						.error("There was an error retrieving token from Stripe. "
								+ " Token : "
								+ billingMetaData.getStripeToken());
			}
			if (token != null
					&& token.getId().equalsIgnoreCase(
							billingMetaData.getStripeToken())) {
				billingResponse
						.setBillingResponseCode(Constants.BILLING_RESPONSE_CODES.SUCCESS);
				billingResponse
						.setMessage("Credit card input was successfully validated");
			} else {
				billingResponse
						.setBillingResponseCode(Constants.BILLING_RESPONSE_CODES.INPUT_VALIDATION_ERROR);
				billingResponse
						.setMessage("There was an error validating Credit Card input.");
			}
		} else {
			billingResponse
					.setBillingResponseCode(Constants.BILLING_RESPONSE_CODES.INPUT_VALIDATION_ERROR);
			billingResponse
					.setMessage("There was an error validating Credit Card input.");
		}

		return billingResponse;
	}

	@Override
	public BillingResponse chargeUser(BillingMetaData billingMetaData) {
		BillingResponse billingResponse = new BillingResponse();

		if (billingMetaData != null) {
			com.stripe.model.Token token = null;
			try {
				token = Token.retrieve(billingMetaData.getStripeToken());
			} catch (StripeException e) {
				appLogging
						.error("There was an error retrieving token from Stripe. "
								+ " Token : "
								+ billingMetaData.getStripeToken()
								+ ExceptionHandler.getStackTrace(e));
				billingResponse.setBillingResponseCode(Constants.BILLING_RESPONSE_CODES.GENERAL_ERROR);
				billingResponse.setMessage("Payment authentication error.");
			}
			if (token != null && token.getId().equalsIgnoreCase(billingMetaData.getStripeToken()))
            {
				Stripe.apiKey = getSecretKey();

                Map<String, Object> customerParams = new HashMap<String, Object>();
                customerParams.put("card", billingMetaData.getStripeToken());
                customerParams.put("description","Event ID " + billingMetaData.getEventId()+ " Customer for email = " + billingMetaData.getEmail());





				try {
                    Customer customer = Customer.create(customerParams);

                    if(customer!=null)
                    {
                        appLogging.info("Customer object.  Customer ID :"
                                + customer.getId() );

                        Map<String, Object> chargeParams = new HashMap<String, Object>();
                        chargeParams.put("currency", "usd");
                        chargeParams.put("description",	"Ch:" + billingMetaData.getEventId() + "_" + billingMetaData.getZip() + "_"
                                + billingMetaData.getEmail());
                        chargeParams.put("customer", customer.getId()); // obtained


                        Double dPrice = ParseUtil.sToD(billingMetaData.getPrice());
                        dPrice = dPrice * 100;  //converting to cents
                        int iTmpPrice = dPrice.intValue();
                        Integer iPrice = new Integer(iTmpPrice);
                        // Stripe.js
                        chargeParams.put("amount", iPrice);

                        Charge charge = Charge.create(chargeParams);


                        billingResponse.setBillingResponseCode(Constants.BILLING_RESPONSE_CODES.SUCCESS);
                        billingResponse.setMessage("Successfully charged.");
                        billingResponse.setPaymentChannelCustomerId(customer.getId());
                        appLogging.info("Successfully charged. " + " Token : "
                                + billingMetaData.getStripeToken() + "_"
                                + billingMetaData.getEventId() + "_"
                                + billingMetaData.getZip() + "_"
                                + billingMetaData.getEmail() + "_"
                                + iPrice+"_cents");


                    }




				} catch (StripeException e) {
					appLogging
							.error("There was an charging customer with a price. "
									+ " Token : "
									+ billingMetaData.getStripeToken()
									+ "_"
									+ billingMetaData.getEventId()
									+ "_"
									+ billingMetaData.getZip()
									+ "_"
									+ billingMetaData.getEmail()
									+ "_"
									+ ExceptionHandler.getStackTrace(e));
					billingResponse
							.setBillingResponseCode(Constants.BILLING_RESPONSE_CODES.GENERAL_ERROR);
					billingResponse.setMessage("Payment authentication error.");
				}
			} else {

			}
		}

		return billingResponse;
	}

}
