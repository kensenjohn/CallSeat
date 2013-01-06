package com.gs.bean;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.gs.common.ParseUtil;

public class PricingGroupBean {

	/*
	 * PRICEGROUPID, PRICEGROUPNAME, MIN_GUEST_NUM, MAX_GUEST_NUM
	 */

	private String pricegroupid = "";
	private String pricegroupname = "";
	private Integer minGuestNum = 0;
	private Integer maxGuestNum = 0;
	private Double price = 0.0;

	private Integer minMinutes = 0;
	private Integer maxMinutes = 0;
	private Integer smsCount = 0;

    private boolean isDefault = false;

	public PricingGroupBean() {

	}

	public PricingGroupBean(HashMap<String, String> hmResult) {
		setPricegroupid(ParseUtil.checkNull(hmResult.get("PRICEGROUPID")));
		setPricegroupname(ParseUtil.checkNull(hmResult.get("PRICEGROUPNAME")));
		setMinGuestNum(ParseUtil.sToI(hmResult.get("MIN_GUEST_NUM")));
		setMaxGuestNum(ParseUtil.sToI(hmResult.get("MAX_GUEST_NUM")));
		setPrice(ParseUtil.sToD(hmResult.get("PRICE")));
		setMinMinutes(ParseUtil.sToI(hmResult.get("MIN_MINUTES")));
		setMaxMinutes(ParseUtil.sToI(hmResult.get("MAX_MINUTES")));
		setSmsCount(ParseUtil.sToI(hmResult.get("SMS_COUNT")));
        setDefault(ParseUtil.sTob(hmResult.get("IS_DEFAULT")));
	}

	public Integer getMinMinutes() {
		return minMinutes;
	}

	public void setMinMinutes(Integer minMinutes) {
		this.minMinutes = minMinutes;
	}

	public Integer getMaxMinutes() {
		return maxMinutes;
	}

	public void setMaxMinutes(Integer maxMinutes) {
		this.maxMinutes = maxMinutes;
	}

	public Integer getSmsCount() {
		return smsCount;
	}

	public void setSmsCount(Integer smsCount) {
		this.smsCount = smsCount;
	}

	public String getPricegroupid() {
		return pricegroupid;
	}

	public void setPricegroupid(String pricegroupid) {
		this.pricegroupid = pricegroupid;
	}

	public String getPricegroupname() {
		return pricegroupname;
	}

	public void setPricegroupname(String pricegroupname) {
		this.pricegroupname = pricegroupname;
	}

	public Integer getMinGuestNum() {
		return minGuestNum;
	}

	public void setMinGuestNum(Integer minGuestNum) {
		this.minGuestNum = minGuestNum;
	}

	public Integer getMaxGuestNum() {
		return maxGuestNum;
	}

	public void setMaxGuestNum(Integer maxGuestNum) {
		this.maxGuestNum = maxGuestNum;
	}
    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public JSONObject toJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("pricing_group_id", pricegroupid);
			jsonObject.put("pricing_group_name", pricegroupname);
			jsonObject.put("min_guest_num", minGuestNum);
			jsonObject.put("max_guest_num", maxGuestNum);
			jsonObject.put("price", price);
			jsonObject.put("min_minutes", minMinutes);
			jsonObject.put("max_minutes", maxMinutes);
			jsonObject.put("sms_count", smsCount);
            jsonObject.put("is_default",isDefault);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

    @Override
    public String toString() {
        return "PricingGroupBean{" +
                "pricegroupid='" + pricegroupid + '\'' +
                ", pricegroupname='" + pricegroupname + '\'' +
                ", minGuestNum=" + minGuestNum +
                ", maxGuestNum=" + maxGuestNum +
                ", price=" + price +
                ", minMinutes=" + minMinutes +
                ", maxMinutes=" + maxMinutes +
                ", smsCount=" + smsCount +
                ", isDefault=" + isDefault +
                '}';
    }
}
