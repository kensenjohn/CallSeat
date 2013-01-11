package com.gs.bean;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 1/9/13
 * Time: 12:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class CheckoutBean {
    private String adminId = "";
    private String eventId = "";
    private String itemName = "";
    private Double itemPrice = 0.00;
    private Double taxPercentage = 0.00;
    private Double taxAmount = 0.00;
    private Double beforeTaxTotal = 0.00;
    private Double grandTotal = 0.00;
    private Double discountAmount = 0.00;
    private Double discountPercentage = 0.00;

    private String formattedItemPrice = "";
    private String formattedDiscountAmount = "";
    private String formattedDiscountPercentage = "";
    private String formattedBeforeTaxTotal = "";
    private String formattedTaxPercentage = "";
    private String formattedTaxAmount = "";
    private String formattedGrandTotal = "";

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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Double getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(Double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getBeforeTaxTotal() {
        return beforeTaxTotal;
    }

    public void setBeforeTaxTotal(Double beforeTaxTotal) {
        this.beforeTaxTotal = beforeTaxTotal;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getFormattedItemPrice() {
        return formattedItemPrice;
    }

    public void setFormattedItemPrice(String formattedItemPrice) {
        this.formattedItemPrice = formattedItemPrice;
    }

    public String getFormattedDiscountAmount() {
        return formattedDiscountAmount;
    }

    public void setFormattedDiscountAmount(String formattedDiscountAmount) {
        this.formattedDiscountAmount = formattedDiscountAmount;
    }

    public String getFormattedDiscountPercentage() {
        return formattedDiscountPercentage;
    }

    public void setFormattedDiscountPercentage(String formattedDiscountPercentage) {
        this.formattedDiscountPercentage = formattedDiscountPercentage;
    }

    public String getFormattedBeforeTaxTotal() {
        return formattedBeforeTaxTotal;
    }

    public void setFormattedBeforeTaxTotal(String formattedBeforeTaxTotal) {
        this.formattedBeforeTaxTotal = formattedBeforeTaxTotal;
    }

    public String getFormattedTaxPercentage() {
        return formattedTaxPercentage;
    }

    public void setFormattedTaxPercentage(String formattedTaxPercentage) {
        this.formattedTaxPercentage = formattedTaxPercentage;
    }

    public String getFormattedTaxAmount() {
        return formattedTaxAmount;
    }

    public void setFormattedTaxAmount(String formattedTaxAmount) {
        this.formattedTaxAmount = formattedTaxAmount;
    }

    public String getFormattedGrandTotal() {
        return formattedGrandTotal;
    }

    public void setFormattedGrandTotal(String formattedGrandTotal) {
        this.formattedGrandTotal = formattedGrandTotal;
    }

    @Override
    public String toString() {
        return "CheckoutBean{" +
                "adminId='" + adminId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", taxPercentage=" + taxPercentage +
                ", taxAmount=" + taxAmount +
                ", beforeTaxTotal=" + beforeTaxTotal +
                ", grandTotal=" + grandTotal +
                ", discountAmount=" + discountAmount +
                ", discountPercentage=" + discountPercentage +
                ", formattedItemPrice='" + formattedItemPrice + '\'' +
                ", formattedDiscountAmount='" + formattedDiscountAmount + '\'' +
                ", formattedDiscountPercentage='" + formattedDiscountPercentage + '\'' +
                ", formattedBeforeTaxTotal='" + formattedBeforeTaxTotal + '\'' +
                ", formattedTaxPercentage='" + formattedTaxPercentage + '\'' +
                ", formattedTaxAmount='" + formattedTaxAmount + '\'' +
                ", formattedGrandTotal='" + formattedGrandTotal + '\'' +
                '}';
    }
}
