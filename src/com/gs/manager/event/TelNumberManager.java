package com.gs.manager.event;

import java.util.ArrayList;

import com.gs.bean.EventGuestBean;
import com.gs.bean.GuestBean;
import com.gs.bean.TelNumberBean;
import com.gs.data.GuestData;

public class TelNumberManager
{
	public TelNumberResponse getTelNumberDetails(TelNumberMetaData telNumberMetaData)
	{
		TelNumberResponse telNumberResponse = new TelNumberResponse();
		if (telNumberMetaData != null)
		{
			TelNumberData telNumData = new TelNumberData();
			TelNumberBean telNumberBean = telNumData.getTelNumber(telNumberMetaData);

			telNumberResponse.setTelNumberBean(telNumberBean);
		}

		return telNumberResponse;
	}

	private void getTelNumEventDetails(TelNumberMetaData telNumberMetaData)
	{

	}

	public EventGuestBean getTelNumGuestDetails(TelNumberMetaData telNumberMetaData)
	{
		EventGuestBean eventGuestBean = new EventGuestBean();
		if (telNumberMetaData != null)
		{
			GuestData guestData = new GuestData();

			ArrayList<GuestBean> arrGuestBean = guestData.getGuestsByTelNumber(telNumberMetaData);

			if (arrGuestBean != null && !arrGuestBean.isEmpty())
			{
				ArrayList<String> arrGuestId = new ArrayList<String>();
				for (GuestBean guestBean : arrGuestBean)
				{
					arrGuestId.add(guestBean.getGuestId());
				}

				EventGuestMetaData eventGuestMetaData = new EventGuestMetaData();
				eventGuestMetaData.setEventId(telNumberMetaData.getEventId());
				eventGuestMetaData.setArrGuestId(arrGuestId);

				EventGuestManager eventGuestManager = new EventGuestManager();
				eventGuestBean = eventGuestManager.getGuest(eventGuestMetaData);

			}
		}
		return eventGuestBean;
	}
}
