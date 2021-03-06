package com.gs.payment;

import com.gs.common.Constants;

public class BillingMetaData {

	private String adminId = "";
	private String eventId = "";
	private String firstName = "";
	private String lastName = "";
	private String middletName = "";
	private String price = "";
	private String address1 = "";
	private String address2 = "";
	private String zip = "";
	private String city = "";
	private String state = "";
	private String country = "";
	private String creditCardNum = "";
	private String secureNum = "";
	private String expiryMonth = "";
	private String expiryYear = "";

	private String stripeToken = "";
	private boolean isStripeTokenUsed = false;
	private String cardLast4 = "";

	private String email = "";
    private String paymentChannelCustomerId = "";

    private Constants.API_KEY_TYPE apiKeyType = Constants.API_KEY_TYPE.LIVE_KEY;

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCreditCardNum() {
		return creditCardNum;
	}

	public void setCreditCardNum(String creditCardNum) {
		this.creditCardNum = creditCardNum;
	}

	public String getSecureNum() {
		return secureNum;
	}

	public void setSecureNum(String secureNum) {
		this.secureNum = secureNum;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddletName() {
		return middletName;
	}

	public void setMiddletName(String middletName) {
		this.middletName = middletName;
	}

	public String getExpiryMonth() {
		return expiryMonth;
	}

	public void setExpiryMonth(String expiryMonth) {
		this.expiryMonth = expiryMonth;
	}

	public String getExpiryYear() {
		return expiryYear;
	}

	public void setExpiryYear(String expiryYear) {
		this.expiryYear = expiryYear;
	}

	public String getStripeToken() {
		return stripeToken;
	}

	public void setStripeToken(String stripeToken) {
		this.stripeToken = stripeToken;
	}

	public boolean isStripeTokenUsed() {
		return isStripeTokenUsed;
	}

	public void setStripeTokenUsed(boolean isStripeTokenUsed) {
		this.isStripeTokenUsed = isStripeTokenUsed;
	}

	public String getCardLast4() {
		return cardLast4;
	}

	public void setCardLast4(String cardLast4) {
		this.cardLast4 = cardLast4;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    public String getPaymentChannelCustomerId() {
        return paymentChannelCustomerId;
    }

    public void setPaymentChannelCustomerId(String paymentChannelCustomerId) {
        this.paymentChannelCustomerId = paymentChannelCustomerId;
    }

    public Constants.API_KEY_TYPE getApiKeyType() {
        return apiKeyType;
    }

    public void setApiKeyType(Constants.API_KEY_TYPE apiKeyType) {
        this.apiKeyType = apiKeyType;
    }
}
