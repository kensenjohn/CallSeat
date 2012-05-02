package com.gs.manager.event;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

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

	public JSONArray getPricingGroupJsonArray(
			ArrayList<PricingGroupBean> arrPricingGroupBean) {
		JSONArray jsonPricingGroupArray = new JSONArray();
		try {

			int numOfRows = 0;
			if (arrPricingGroupBean != null && !arrPricingGroupBean.isEmpty()) {
				int iIndex = 0;
				for (PricingGroupBean pricingGroupBean : arrPricingGroupBean) {
					jsonPricingGroupArray
							.put(iIndex, pricingGroupBean.toJson());
					iIndex++;
				}
				numOfRows++;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonPricingGroupArray;
	}
}
