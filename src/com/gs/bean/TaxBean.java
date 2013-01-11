package com.gs.bean;

import com.gs.common.Constants;
import com.gs.common.ParseUtil;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 1/8/13
 * Time: 12:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaxBean {
    // STATETAXID,TAXTYPE,STATE,COUNTRY ,TAXPERCENTAGE

    private String stateTaxId = "";
    private Constants.TAX_TYPE taxType = null;
    private String state = "";
    private String country = "";
    private Double taxPercentage = 0.00;

    public TaxBean()
    {

    }

    public TaxBean(HashMap<String,String> hmResult)
    {
        this.stateTaxId = ParseUtil.checkNull(hmResult.get("STATETAXID"));
        this.taxType = Constants.TAX_TYPE.valueOf(hmResult.get("TAXTYPE"));
        this.state = ParseUtil.checkNull(hmResult.get("STATE"));
        this.country = ParseUtil.checkNull(hmResult.get("COUNTRY"));
        this.taxPercentage = ParseUtil.sToD( hmResult.get("TAXPERCENTAGE") ) ;
    }

    public String getStateTaxId() {
        return stateTaxId;
    }

    public void setStateTaxId(String stateTaxId) {
        this.stateTaxId = stateTaxId;
    }

    public Constants.TAX_TYPE getTaxType() {
        return taxType;
    }

    public void setTaxType(Constants.TAX_TYPE taxType) {
        this.taxType = taxType;
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

    public Double getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(Double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    @Override
    public String toString() {
        return "TaxBean{" +
                "stateTaxId='" + stateTaxId + '\'' +
                ", taxType=" + taxType!=null?taxType.getTaxType():"No Tax type set" +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", taxPercentage=" + taxPercentage +
                '}';
    }
}
