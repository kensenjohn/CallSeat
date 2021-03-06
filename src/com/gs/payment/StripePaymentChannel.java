package com.gs.payment;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.gs.bean.PaymentChannelRequest;
import com.gs.common.Utility;
import com.stripe.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.exception.ExceptionHandler;
import com.gs.common.ParseUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Token;

public class StripePaymentChannel extends PaymentChannel {

	Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
	private static Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    private static BigDecimal CENTS_PER_DOLLAR = new BigDecimal("100");

	public String getPublicKey(PaymentChannelRequest paymentChannelRequest) {
		String sPublicKey = ParseUtil.checkNull(applicationConfig.get(Constants.PROP_STRIPE_TEST_PUBLISHABLE_KEY));
		if (Constants.STRIPE_ENVIRONMENT.LIVE.getStripeEnv().equalsIgnoreCase( getStripeEnvironment())
                && (paymentChannelRequest!=null && Constants.API_KEY_TYPE.LIVE_KEY.equals(paymentChannelRequest.getApiKeyType()))) {
			sPublicKey = ParseUtil.checkNull(applicationConfig.get(Constants.PROP_STRIPE_LIVE_PUBLISHABLE_KEY));
		}
		return sPublicKey;
	}

	private String getSecretKey(PaymentChannelRequest paymentChannelRequest) {
		String sSecretKey = ParseUtil.checkNull(applicationConfig.get(Constants.PROP_STRIPE_TEST_SECRET_KEY));
		if (Constants.STRIPE_ENVIRONMENT.LIVE.getStripeEnv().equalsIgnoreCase(getStripeEnvironment())
                && (paymentChannelRequest!=null && Constants.API_KEY_TYPE.LIVE_KEY.equals(paymentChannelRequest.getApiKeyType())) ) {
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
		if (billingMetaData.isStripeTokenUsed() && !Utility.isNullOrEmpty(billingMetaData.getStripeToken())) {
            PaymentChannelRequest paymentChannelRequest = new PaymentChannelRequest();
            paymentChannelRequest.setApiKeyType( billingMetaData.getApiKeyType()!=null?billingMetaData.getApiKeyType(): Constants.API_KEY_TYPE.LIVE_KEY );
            appLogging.error("paymentChannelRequest : "  + paymentChannelRequest.getApiKeyType().name() );
			Stripe.apiKey = getPublicKey( paymentChannelRequest );
			com.stripe.model.Token token = null;
			try {
				token = Token.retrieve(billingMetaData.getStripeToken());
			} catch (StripeException e) {
				appLogging.error("There was an error retrieving token from Stripe. "
								+ " Token : " + billingMetaData.getStripeToken() + " - " + ExceptionHandler.getStackTrace(e));
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
						.error("There was an error retrieving token from Stripe Application. "
								+ " Token : "
								+ billingMetaData.getStripeToken() + e.getMessage()
								+ ExceptionHandler.getStackTrace(e));
				billingResponse.setBillingResponseCode(Constants.BILLING_RESPONSE_CODES.GENERAL_ERROR);
				billingResponse.setMessage("Payment authentication error.");
			}
			if (token != null && token.getId().equalsIgnoreCase(billingMetaData.getStripeToken()))
            {

                PaymentChannelRequest paymentChannelRequest = new PaymentChannelRequest();
                paymentChannelRequest.setApiKeyType( billingMetaData.getApiKeyType()!=null?billingMetaData.getApiKeyType(): Constants.API_KEY_TYPE.LIVE_KEY );
				Stripe.apiKey = getSecretKey(paymentChannelRequest);

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
                        if(dPrice>0.0) {
                            BigDecimal bdPrice = new BigDecimal(ParseUtil.checkNull(billingMetaData.getPrice()));
                            BigDecimal bdPriceInCents = bdPrice.multiply(CENTS_PER_DOLLAR);

                            // Stripe.js
                            chargeParams.put("amount", bdPriceInCents.intValue());

                            Charge charge = Charge.create(chargeParams);

                            billingResponse.setBillingResponseCode(Constants.BILLING_RESPONSE_CODES.SUCCESS);
                            billingResponse.setMessage("Successfully charged.");
                            billingResponse.setPaymentChannelCustomerId(customer.getId());
                            appLogging.info("Successfully charged. " + " Token : "
                                    + billingMetaData.getStripeToken() + "_"
                                    + billingMetaData.getEventId() + "_"
                                    + billingMetaData.getZip() + "_"
                                    + billingMetaData.getEmail() + "_"
                                    + bdPriceInCents.intValue()+"_cents");
                        }  else {
                            appLogging.info("Invalid Price to charge " + " Customer : " + customer.getId() + " Token : "
                                    + billingMetaData.getStripeToken() + " Event id : "
                                    + billingMetaData.getEventId() + " xip :"
                                    + billingMetaData.getZip() + " Email :"
                                    + billingMetaData.getEmail() + " Price :"
                                    + ParseUtil.checkNull(billingMetaData.getPrice())+"_dollars");
                        }
                    }
				} catch (StripeException e) {
					appLogging
							.error("Error charging customer with a price. "
									+ " Token : "
									+ billingMetaData.getStripeToken()
									+ " eventid : "
									+ billingMetaData.getEventId()
									+ "\n zip:"
									+ billingMetaData.getZip()
									+ " email : "
									+ billingMetaData.getEmail()
									+  "\nEException Message"
                                    + e.getMessage()
                                    + "\n"
									+ ExceptionHandler.getStackTrace(e));
					billingResponse.setBillingResponseCode(Constants.BILLING_RESPONSE_CODES.GENERAL_ERROR);
					billingResponse.setMessage( e.getMessage() );
				}
			} else {

			}
		}

		return billingResponse;
	}

    private String getErrorMessage( String sCode ) {
        String sMessage = "We were unable to process you request. Please try again.";
        if( !Utility.isNullOrEmpty(sCode) ) {
            if( "Your card number is incorrect.".equalsIgnoreCase(sCode) || "Your card number is not a valid credit card number.".equalsIgnoreCase(sCode)
                    || "Your card has expired.".equalsIgnoreCase(sCode)  || "Your card was declined.".equalsIgnoreCase(sCode) )  {
                sMessage = "Please use a valid credit card and try again.";
            } else if( "Your card's expiration month is invalid.".equalsIgnoreCase(sCode) || "The card's expiration year is invalid.".equalsIgnoreCase(sCode) ) {
                sMessage = "Please use a valid expiration date and try again.";
            } else if( "Your card's security code is invalid.".equalsIgnoreCase(sCode) || "The card's security code is incorrect.".equalsIgnoreCase(sCode) ) {
                sMessage = "Please use a valid security code (CVC/CVV) and try again.";
            } else if( "There is no card on a customer that is being charged.".equalsIgnoreCase(sCode)  ) {
                sMessage = "Please use a valid security code (CVC/CVV) and try again.";
            } else if ( "An error occurred while processing the card.".equalsIgnoreCase(sCode) ) {
                sMessage = "We were unable to process you request. Please try again. (1)";
            } else {
                sMessage = "We were unable to process you request. Please try again. (2)";
            }
        }
        return sMessage;
    }

}
