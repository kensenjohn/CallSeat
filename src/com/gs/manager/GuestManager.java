package com.gs.manager;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.GuestBean;
import com.gs.common.ExceptionHandler;
import com.gs.common.ParseUtil;
import com.gs.data.GuestData;

public class GuestManager
{
	private static final Logger appLogging = LoggerFactory.getLogger("AppLogging");

	public GuestBean createGuest(GuestBean guestBean)
	{
		if (guestBean == null || guestBean.getGuestId() == null || guestBean.getAdminId() == null
				|| "".equalsIgnoreCase(guestBean.getAdminId())
				|| "".equalsIgnoreCase(guestBean.getGuestId())
				|| "".equalsIgnoreCase(guestBean.getUserInfoId()))
		{
			appLogging.error("There is no guest Info to create.");
		} else
		{
			GuestData guestData = new GuestData();

			int iNumOfRecs = guestData.insertGuest(guestBean);

			if (iNumOfRecs > 0)
			{
				appLogging.info("Guest creation successful for  : " + guestBean.getAdminId());
			} else
			{
				appLogging.error("Error creating Guest " + guestBean.getAdminId());
				guestBean = null;
			}

		}

		return guestBean;
	}

	public ArrayList<GuestBean> getGuestsByAdmin(String sAdminId)
	{
		ArrayList<GuestBean> arrGuestBean = new ArrayList<GuestBean>();
		if (sAdminId != null && !"".equalsIgnoreCase(sAdminId))
		{
			GuestData guestData = new GuestData();
			arrGuestBean = guestData.getGuestByAdmin(sAdminId);
		}
		return arrGuestBean;
	}

	public JSONObject getGuestsJson(ArrayList<GuestBean> arrGuestBean)
	{
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonGuestArray = new JSONArray();
		try
		{
			int numOfRows = 0;
			if (arrGuestBean != null && !arrGuestBean.isEmpty())
			{
				int iIndex = 0;
				for (GuestBean guestBean : arrGuestBean)
				{
					jsonGuestArray.put(iIndex, guestBean.toJson());
					iIndex++;
				}
				numOfRows = arrGuestBean.size();
			} else
			{

			}
			jsonObject.put("num_of_rows", ParseUtil.iToI(numOfRows));
			jsonObject.put("guests", jsonGuestArray);
		} catch (JSONException e)
		{
			appLogging.error(ExceptionHandler.getStackTrace(e));
		}
		return jsonObject;
	}
}
