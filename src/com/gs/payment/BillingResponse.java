package com.gs.payment;

import com.gs.common.Constants;

public class BillingResponse {

	private Constants.BILLING_RESPONSE_CODES billingResponseCode;
	private String message = "";
	private String tokenId = "";

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BillingResponse [Code=").append(billingResponseCode)
				.append(", message=").append(message).append("]");
		return builder.toString();
	}

}
