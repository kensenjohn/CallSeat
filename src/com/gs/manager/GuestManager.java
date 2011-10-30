package com.gs.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.GuestBean;
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
}
