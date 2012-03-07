package com.gs.bean;

import java.util.HashMap;

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

	public PricingGroupBean() {

	}

	public PricingGroupBean(HashMap<String, String> hmResult) {
		setPricegroupid(ParseUtil.checkNull(hmResult.get("PRICEGROUPID")));
		setPricegroupname(ParseUtil.checkNull(hmResult.get("PRICEGROUPNAME")));
		setMinGuestNum(ParseUtil.sToI(hmResult.get("MIN_GUEST_NUM")));
		setMaxGuestNum(ParseUtil.sToI(hmResult.get("MAX_GUEST_NUM")));
		setPrice(ParseUtil.sToD(hmResult.get("PRICE")));
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PricingGroupBean [pricegroupid=").append(pricegroupid)
				.append(", pricegroupname=").append(pricegroupname)
				.append(", minGuestNum=").append(minGuestNum)
				.append(", maxGuestNum=").append(maxGuestNum).append("]");
		return builder.toString();
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}
