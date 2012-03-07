package com.gs.manager.event;

import java.util.ArrayList;

import com.gs.bean.PricingGroupBean;

public class EventPricingGroupManager {

	public ArrayList<PricingGroupBean> getPricingGroups() {

		PricingGroupData pricingGroupData = new PricingGroupData();

		ArrayList<PricingGroupBean> arrPricingGroupBean = pricingGroupData
				.getAllPricingGroups();

		return arrPricingGroupBean;
	}

	public PricingGroupBean getPricingGroups(String sPricingGroupId) {
		PricingGroupBean pricingGroupBean = new PricingGroupBean();
		if (sPricingGroupId != null && !"".equalsIgnoreCase(sPricingGroupId)) {
			PricingGroupData pricingGroupData = new PricingGroupData();

			pricingGroupBean = pricingGroupData
					.getPricingGroup(sPricingGroupId);
		}

		return pricingGroupBean;
	}
}
