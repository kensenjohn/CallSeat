package com.gs.manager.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.EventGuestBean;
import com.gs.bean.GuestBean;
import com.gs.bean.TelNumberBean;
import com.gs.common.Constants;
import com.gs.common.ExceptionHandler;
import com.gs.data.GuestData;
import com.gs.phone.account.TwilioAccount;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.AvailablePhoneNumber;
import com.twilio.sdk.resource.list.AvailablePhoneNumberList;

public class TelNumberManager {
	Logger appLogging = LoggerFactory.getLogger("AppLogging");

	public TelNumberResponse getTelNumberDetails(
			TelNumberMetaData telNumberMetaData) {
		TelNumberResponse telNumberResponse = new TelNumberResponse();
		if (telNumberMetaData != null) {
			TelNumberData telNumData = new TelNumberData();
			TelNumberBean telNumberBean = telNumData
					.getTelNumber(telNumberMetaData);

			telNumberResponse.setTelNumberBean(telNumberBean);
		}

		return telNumberResponse;
	}

	public ArrayList<TelNumberBean> getTelNumbersByEvent(
			TelNumberMetaData telNumberMetaData) {
		ArrayList<TelNumberBean> arrTelNumberBean = new ArrayList<TelNumberBean>();

		if (telNumberMetaData != null && telNumberMetaData.getAdminId() != null
				&& telNumberMetaData.getEventId() != null
				&& !"".equalsIgnoreCase(telNumberMetaData.getAdminId())
				&& !"".equalsIgnoreCase(telNumberMetaData.getEventId())) {
			TelNumberData telNumData = new TelNumberData();
			arrTelNumberBean = telNumData.getEventTelNumbers(telNumberMetaData);
		}
		return arrTelNumberBean;
	}

	public ArrayList<TelNumberBean> getTelNumEventDetails(
			TelNumberMetaData telNumberMetaData) {
		ArrayList<TelNumberBean> arrTelNumberBean = new ArrayList<TelNumberBean>();

		if (telNumberMetaData != null && telNumberMetaData.getAdminId() != null
				&& telNumberMetaData.getEventId() != null
				&& !"".equalsIgnoreCase(telNumberMetaData.getAdminId())
				&& !"".equalsIgnoreCase(telNumberMetaData.getEventId())) {
			arrTelNumberBean = getTelNumbersByEvent(telNumberMetaData);
		}

		boolean isSeatingNumberExists = false;
		boolean isRsvpNumberExists = false;
		if (arrTelNumberBean != null && !arrTelNumberBean.isEmpty()) {
			for (TelNumberBean telNumBer : arrTelNumberBean) {
				if (telNumBer != null) {
					if (Constants.EVENT_TASK.RSVP.getTask().equalsIgnoreCase(
							telNumBer.getTelNumberType())) {
						isRsvpNumberExists = true;
					} else if (Constants.EVENT_TASK.SEATING.getTask()
							.equalsIgnoreCase(telNumBer.getTelNumberType())) {
						isSeatingNumberExists = true;
					}
				}

			}
		}

		boolean isSeatingNumSet = false;
		boolean isRsvpNumSet = false;
		if (!isSeatingNumberExists || !isRsvpNumberExists) {

			List<AvailablePhoneNumber> listAvailablePhone = generateTelephoneNumber();

			if (listAvailablePhone != null && !listAvailablePhone.isEmpty()) {

				for (AvailablePhoneNumber availTelNum : listAvailablePhone) {
					if (!isSeatingNumberExists && !isSeatingNumSet) {
						TelNumberBean seatingBean = createTelNumberBean(
								telNumberMetaData,
								availTelNum.getPhoneNumber(),
								Constants.EVENT_TASK.SEATING);
						arrTelNumberBean.add(seatingBean);
						isSeatingNumSet = true;
						continue;
					} else {
						isSeatingNumSet = true;
					}

					if (!isRsvpNumberExists && !isRsvpNumSet) {
						TelNumberBean rsvpBean = createTelNumberBean(
								telNumberMetaData,
								availTelNum.getPhoneNumber(),
								Constants.EVENT_TASK.RSVP);
						arrTelNumberBean.add(rsvpBean);
						isRsvpNumSet = true;
						continue;
					} else {
						isRsvpNumSet = true;
					}

					if (isRsvpNumSet && isSeatingNumSet) {
						break;
					}
				}

			}
		}

		return arrTelNumberBean;

	}

	private List<AvailablePhoneNumber> generateTelephoneNumber() {
		return generateTelephoneNumber(new HashMap<String, String>());
	}

	private List<AvailablePhoneNumber> generateTelephoneNumber(
			HashMap<String, String> hmFilter) {

		TwilioRestClient client = new TwilioRestClient(
				TwilioAccount.getAccountSid(), TwilioAccount.getAccountToken());
		// Get the main account (The one we used to authenticate the client
		Account mainAccount = client.getAccount();

		// Search for available phone numbers
		AvailablePhoneNumberList phoneNumbers = null;
		if (hmFilter != null && !hmFilter.isEmpty()) {
			phoneNumbers = mainAccount.getAvailablePhoneNumbers(hmFilter);
		} else {
			phoneNumbers = mainAccount.getAvailablePhoneNumbers();
		}
		List<AvailablePhoneNumber> list = null;
		if (phoneNumbers != null) {
			list = phoneNumbers.getPageData();
		}

		return list;
	}

	private TelNumberBean createTelNumberBean(
			TelNumberMetaData telNumberMetaData, String sTelephoneNum,
			Constants.EVENT_TASK telNumType) {
		TelNumberBean telNumberBean = new TelNumberBean();
		telNumberBean.setTelNumber(sTelephoneNum);
		telNumberBean.setAdminId(telNumberMetaData.getAdminId());
		telNumberBean.setEventId(telNumberMetaData.getEventId());
		telNumberBean.setTelNumberType(telNumType.getTask());
		return telNumberBean;
	}

	public EventGuestBean getTelNumGuestDetails(
			TelNumberMetaData telNumberMetaData) {
		EventGuestBean eventGuestBean = new EventGuestBean();
		if (telNumberMetaData != null) {
			GuestData guestData = new GuestData();

			ArrayList<GuestBean> arrGuestBean = guestData
					.getGuestsByTelNumber(telNumberMetaData);

			if (arrGuestBean != null && !arrGuestBean.isEmpty()) {
				ArrayList<String> arrGuestId = new ArrayList<String>();
				for (GuestBean guestBean : arrGuestBean) {
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

	public ArrayList<TelNumberBean> searchTelNumber(
			TelNumberMetaData telNumMetaData) {

		HashMap<String, String> hmFilter = new HashMap<String, String>();

		hmFilter.put("AreaCode", telNumMetaData.getAreaCodeSearch());
		hmFilter.put("Contains", telNumMetaData.getTextPatternSearch());

		List<AvailablePhoneNumber> listAvailableNum = generateTelephoneNumber(hmFilter);
		ArrayList<TelNumberBean> arrTelNumBean = new ArrayList<TelNumberBean>();
		if (listAvailableNum != null && !listAvailableNum.isEmpty()) {
			for (AvailablePhoneNumber availableTelNum : listAvailableNum) {
				TelNumberBean telNumberBean = new TelNumberBean();
				telNumberBean.setTelNumber(availableTelNum.getPhoneNumber());

				arrTelNumBean.add(telNumberBean);

			}
		}

		return arrTelNumBean;
	}

	public void saveTelNumbers(TelNumberMetaData telNumMetaData) {

	}

	public JSONObject getTelNumberBeanJson(
			ArrayList<TelNumberBean> arrTelNumberBean) {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonTelNumArray = new JSONArray();
		try {
			if (arrTelNumberBean != null && !arrTelNumberBean.isEmpty()) {
				Integer iIndex = 0;
				for (TelNumberBean telNumBean : arrTelNumberBean) {
					jsonTelNumArray.put(iIndex, telNumBean.toJson());
					iIndex++;
				}
				jsonObject.put("telnum_array", jsonTelNumArray);
				jsonObject.put("num_of_rows", arrTelNumberBean.size());
			}
		} catch (JSONException e) {
			appLogging.error(ExceptionHandler.getStackTrace(e));
		}
		return jsonObject;
	}

}
