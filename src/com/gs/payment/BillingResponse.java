package com.gs.payment;

import com.gs.common.Constants;

public class BillingResponse {

	private Constants.BILLING_RESPONSE_CODES billingResponseCode;
	private String message = "";
	private String tokenId = "";
    private String paymentChannelCustomerId = "";

	public Constants.BILLING_RESPONSE_CODES getBillingResponseCode() {
		return billingResponseCode;
	}

	public void setBillingResponseCode(
			Constants.BILLING_RESPONSE_CODES billingResponseCode) {
		this.billingResponseCode = billingResponseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

    public String getPaymentChannelCustomerId() {
        return paymentChannelCustomerId;
    }

    public void setPaymentChannelCustomerId(String paymentChannelCustomerId) {
        this.paymentChannelCustomerId = paymentChannelCustomerId;
    }

    @Override
    public String toString() {
        return "BillingResponse{" +
                "billingResponseCode=" + billingResponseCode +
                ", message='" + message + '\'' +
                ", tokenId='" + tokenId + '\'' +
                ", paymentChannelCustomerId='" + paymentChannelCustomerId + '\'' +
                '}';
    }

}
