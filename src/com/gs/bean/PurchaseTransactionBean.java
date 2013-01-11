package com.gs.bean;

import com.gs.common.ParseUtil;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 1/2/13
 * Time: 11:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseTransactionBean {
    private String purchaseTransactionId = "";
    private String adminId = "";
    private String eventId = "";
    private boolean isPurchaseComplete = false;
    private String rsvpTelNumber = "";
    private String seatingTelNumber = "";
    private String priceGroupId = "";
    private String firstName = "";
    private String lastName = "";
    private String state = "";
    private String zipcode = "";
    private String country = "";
    private String stripeCustomerId = "";
    private String stripeToken = "";
    private String creditCardLast4Digits = "";
    private Long createDate = 0L;
    private String uniquePurchaseToken = "";

    public PurchaseTransactionBean()
    {

    }

    public PurchaseTransactionBean(HashMap<String,String> hmResult)
    {
        setPurchaseTransactionId(ParseUtil.checkNull(hmResult.get("PURCHASETRANSACTIONID")));
        setAdminId(ParseUtil.checkNull(hmResult.get("FK_ADMINID")));
        setEventId(ParseUtil.checkNull(hmResult.get("FK_EVENTID")));
        setPurchaseComplete(ParseUtil.sTob(hmResult.get("IS_PURCHASE_COMPLETE")));
        setRsvpTelNumber(ParseUtil.checkNull(hmResult.get("RSVP_TELNUMBER")));
        setSeatingTelNumber(ParseUtil.checkNull(hmResult.get("SEATING_TELNUMBER")));
        setPriceGroupId(ParseUtil.checkNull(hmResult.get("FK_PRICEGROUPID")));
        setFirstName(ParseUtil.checkNull(hmResult.get("FIRSTNAME")));
        setLastName(ParseUtil.checkNull(hmResult.get("LASTNAME")));
        setState(ParseUtil.checkNull(hmResult.get("STATE")));
        setZipcode(ParseUtil.checkNull(hmResult.get("ZIPCODE")));
        setCountry(ParseUtil.checkNull(hmResult.get("COUNTRY")));
        setStripeCustomerId(ParseUtil.checkNull(hmResult.get("STRIPE_CUSTOMER_ID")));
        setCreateDate(ParseUtil.sToL(hmResult.get("CREATEDATE")));
        setStripeToken(ParseUtil.checkNull(hmResult.get("STRIPE_TOKEN")));
        setCreditCardLast4Digits(ParseUtil.checkNull(hmResult.get("CREDIT_CARD_LAST4_DIGITS")));
        setUniquePurchaseToken( ParseUtil.checkNull(hmResult.get("UNIQUE_PURCHASE_TOKEN")) );
    }

    public String getPurchaseTransactionId() {
        return purchaseTransactionId;
    }

    public void setPurchaseTransactionId(String purchaseTransactionId) {
        this.purchaseTransactionId = purchaseTransactionId;
    }

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

    public boolean isPurchaseComplete() {
        return isPurchaseComplete;
    }

    public void setPurchaseComplete(boolean purchaseComplete) {
        isPurchaseComplete = purchaseComplete;
    }

    public String getRsvpTelNumber() {
        return rsvpTelNumber;
    }

    public void setRsvpTelNumber(String rsvpTelNumber) {
        this.rsvpTelNumber = rsvpTelNumber;
    }

    public String getSeatingTelNumber() {
        return seatingTelNumber;
    }

    public void setSeatingTelNumber(String seatingTelNumber) {
        this.seatingTelNumber = seatingTelNumber;
    }

    public String getPriceGroupId() {
        return priceGroupId;
    }

    public void setPriceGroupId(String priceGroupId) {
        this.priceGroupId = priceGroupId;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public String getStripeToken() {
        return stripeToken;
    }

    public void setStripeToken(String stripeToken) {
        this.stripeToken = stripeToken;
    }

    public String getCreditCardLast4Digits() {
        return creditCardLast4Digits;
    }

    public void setCreditCardLast4Digits(String creditCardLast4Digits) {
        this.creditCardLast4Digits = creditCardLast4Digits;
    }

    public String getUniquePurchaseToken() {
        return uniquePurchaseToken;
    }

    public void setUniquePurchaseToken(String uniquePurchaseToken) {
        this.uniquePurchaseToken = uniquePurchaseToken;
    }

    @Override
    public String toString() {
        return "PurchaseTransactionBean{" +
                "purchaseTransactionId='" + purchaseTransactionId + '\'' +
                ", adminId='" + adminId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", isPurchaseComplete=" + isPurchaseComplete +
                ", rsvpTelNumber='" + rsvpTelNumber + '\'' +
                ", seatingTelNumber='" + seatingTelNumber + '\'' +
                ", priceGroupId='" + priceGroupId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", state='" + state + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", country='" + country + '\'' +
                ", stripeCustomerId='" + stripeCustomerId + '\'' +
                ", stripeToken='" + stripeToken + '\'' +
                ", creditCardLast4Digits='" + creditCardLast4Digits + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
