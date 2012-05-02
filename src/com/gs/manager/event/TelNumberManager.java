package com.gs.manager.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.AdminTelephonyAccountBean;
import com.gs.bean.DemoTelNumber;
import com.gs.bean.EventGuestBean;
import com.gs.bean.GuestBean;
import com.gs.bean.TelNumberBean;
import com.gs.bean.TelNumberTypeBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ExceptionHandler;
import com.gs.common.ParseUtil;
import com.gs.common.Utility;
import com.gs.data.GuestData;
import com.gs.phone.account.AdminTelephonyAccountManager;
import com.gs.phone.account.AdminTelephonyAccountMeta;
import com.gs.phone.account.TwilioAccount;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.AvailablePhoneNumber;
import com.twilio.sdk.resource.instance.IncomingPhoneNumber;
import com.twilio.sdk.resource.list.AvailablePhoneNumberList;

public class TelNumberManager {
	Logger appLogging = LoggerFactory.getLogger("AppLogging");
	private static Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);

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
							telNumBer.getTelNumberType())
							|| Constants.EVENT_TASK.DEMO_RSVP.getTask()
									.equalsIgnoreCase(
											telNumBer.getTelNumberType())) {
						isRsvpNumberExists = true;
					} else if (Constants.EVENT_TASK.SEATING.getTask()
							.equalsIgnoreCase(telNumBer.getTelNumberType())
							|| Constants.EVENT_TASK.DEMO_SEATING.getTask()
									.equalsIgnoreCase(
											telNumBer.getTelNumberType())) {
						isSeatingNumberExists = true;
					}
				}

			}
		}

		boolean isSeatingNumSet = false;
		boolean isRsvpNumSet = false;
		if (!isSeatingNumberExists || !isRsvpNumberExists) {

			arrTelNumberBean = getGeneratedTelNumbers(isSeatingNumberExists,
					isRsvpNumberExists, telNumberMetaData, arrTelNumberBean);
			/*
			 * List<AvailablePhoneNumber> listAvailablePhone =
			 * generateTelephoneNumber();
			 * 
			 * if (listAvailablePhone != null && !listAvailablePhone.isEmpty())
			 * {
			 * 
			 * for (AvailablePhoneNumber availTelNum : listAvailablePhone) { if
			 * (!isSeatingNumberExists && !isSeatingNumSet) { TelNumberBean
			 * seatingBean = createTelNumberBean( telNumberMetaData,
			 * availTelNum.getPhoneNumber(), Constants.EVENT_TASK.SEATING);
			 * arrTelNumberBean.add(seatingBean); isSeatingNumSet = true;
			 * continue; } else { isSeatingNumSet = true; }
			 * 
			 * if (!isRsvpNumberExists && !isRsvpNumSet) { TelNumberBean
			 * rsvpBean = createTelNumberBean( telNumberMetaData,
			 * availTelNum.getPhoneNumber(), Constants.EVENT_TASK.RSVP);
			 * arrTelNumberBean.add(rsvpBean); isRsvpNumSet = true; continue; }
			 * else { isRsvpNumSet = true; }
			 * 
			 * if (isRsvpNumSet && isSeatingNumSet) { break; } }
			 * 
			 * }
			 */
		}

		return arrTelNumberBean;

	}

	public ArrayList<TelNumberBean> getGeneratedTelNumbers(
			boolean isSeatingNumberExists, boolean isRsvpNumberExists,
			TelNumberMetaData telNumberMetaData,
			ArrayList<TelNumberBean> arrTelNumberBean) {
		boolean isSeatingNumSet = false;
		boolean isRsvpNumSet = false;

		List<AvailablePhoneNumber> listAvailablePhone = generateTelephoneNumber();

		if (listAvailablePhone != null && !listAvailablePhone.isEmpty()) {

			for (AvailablePhoneNumber availTelNum : listAvailablePhone) {
				if (!isSeatingNumberExists && !isSeatingNumSet) {
					TelNumberBean seatingBean = createTelNumberBean(
							telNumberMetaData, availTelNum.getPhoneNumber(),
							Constants.EVENT_TASK.SEATING);
					arrTelNumberBean.add(seatingBean);
					isSeatingNumSet = true;
					continue;
				} else {
					isSeatingNumSet = true;
				}

				if (!isRsvpNumberExists && !isRsvpNumSet) {
					TelNumberBean rsvpBean = createTelNumberBean(
							telNumberMetaData, availTelNum.getPhoneNumber(),
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

	public boolean prePurchaseCheck(AdminTelephonyAccountMeta adminAccountMeta,
			String sTelephoneNum) {

		boolean isNumStillAvailable = false;
		if (sTelephoneNum != null && !"".equalsIgnoreCase(sTelephoneNum)) {
			HashMap<String, String> hmFilter = new HashMap<String, String>();

			hmFilter.put("__TMP_NUM_CHANGE_THIS__", sTelephoneNum);

			List<AvailablePhoneNumber> listAvailableNum = generateTelephoneNumber(hmFilter);

			if (listAvailableNum != null && !listAvailableNum.isEmpty()) {
				for (AvailablePhoneNumber availableNum : listAvailableNum) {
					if (availableNum != null
							&& availableNum.getPhoneNumber().equalsIgnoreCase(
									sTelephoneNum)) {
						isNumStillAvailable = true;
						break;
					}
				}
			}
		}
		return isNumStillAvailable;

	}

	public String purchaseTelephoneNumber(
			AdminTelephonyAccountMeta adminAccountMeta, String sTelephoneNum)
			throws TwilioRestException {

		String sPurchasedPhoneNum = "";
		if (adminAccountMeta != null
				&& !"".equalsIgnoreCase(adminAccountMeta.getAdminId())) {

			AdminTelephonyAccountManager adminTelAcMan = new AdminTelephonyAccountManager();
			AdminTelephonyAccountBean adminTelephonyAccBean = adminTelAcMan
					.getAdminAccount(adminAccountMeta);

			if (adminTelephonyAccBean != null
					&& !"".equalsIgnoreCase(adminTelephonyAccBean
							.getAccountSid())
					&& !"".equalsIgnoreCase(adminTelephonyAccBean
							.getAuthToken())) {

				TwilioRestClient client = new TwilioRestClient(
						adminTelephonyAccBean.getAccountSid(),
						adminTelephonyAccBean.getAuthToken());

				Account mainAccount = client.getAccount();

				String sEnvironment = applicationConfig
						.get(Constants.PROP_ENVIRONMENT);
				if (Constants.ENVIRONMENT.VIRTUAL_MACHINE.getEnv()
						.equalsIgnoreCase(sEnvironment)
						|| Constants.ENVIRONMENT.SANDBOX.getEnv()
								.equalsIgnoreCase(sEnvironment)
						|| Constants.ENVIRONMENT.ALPHA.getEnv()
								.equalsIgnoreCase(sEnvironment)) {

					sPurchasedPhoneNum = "777-888-9999";

				} else if (Constants.ENVIRONMENT.BETA.getEnv()
						.equalsIgnoreCase(sEnvironment)) {

					sPurchasedPhoneNum = "777-888-9999";

				} else if (Constants.ENVIRONMENT.PROD.getEnv()
						.equalsIgnoreCase(sEnvironment)) {
					// Buy the first number returned
					Map<String, String> params = new HashMap<String, String>();
					params.put("PhoneNumber", sTelephoneNum);
					IncomingPhoneNumber purchasedNumber = mainAccount
							.getIncomingPhoneNumberFactory().create(params);

					sPurchasedPhoneNum = purchasedNumber.getPhoneNumber();
				}

			}
		}
		return sPurchasedPhoneNum;
	}

	private TelNumberBean createTelNumberBean(
			TelNumberMetaData telNumberMetaData, String sTelephoneNum,
			Constants.EVENT_TASK telNumType) {
		TelNumberBean telNumberBean = new TelNumberBean();
		telNumberBean.setTelNumber(sTelephoneNum.substring(1));
		telNumberBean.setAdminId(telNumberMetaData.getAdminId());
		telNumberBean.setEventId(telNumberMetaData.getEventId());
		telNumberBean.setTelNumberType(telNumType.getTask());
		telNumberBean.setHumanTelNumber(getHumanFormTelNum(sTelephoneNum));
		return telNumberBean;
	}

	private String getHumanFormTelNum(String sTelephoneNum) {
		String sTmpTelNum = "";
		if (sTelephoneNum != null && !"".equalsIgnoreCase(sTelephoneNum)) {
			if (sTelephoneNum.length() == 12) {
				String sAreaCode = sTelephoneNum.substring(2, 5);
				String sFirstSetNumber = sTelephoneNum.substring(5, 8);
				String sLastSetNumber = sTelephoneNum.substring(9);

				sTmpTelNum = "(" + sAreaCode + ")" + " " + sFirstSetNumber
						+ " " + sLastSetNumber;
			}
		}
		return sTmpTelNum;
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
			TelNumberMetaData telNumMetaData, String sNumType) {

		HashMap<String, String> hmFilter = new HashMap<String, String>();

		hmFilter.put("AreaCode", telNumMetaData.getAreaCodeSearch());
		hmFilter.put("Contains", telNumMetaData.getTextPatternSearch());

		List<AvailablePhoneNumber> listAvailableNum = generateTelephoneNumber(hmFilter);
		ArrayList<TelNumberBean> arrTelNumBean = new ArrayList<TelNumberBean>();
		if (listAvailableNum != null && !listAvailableNum.isEmpty()) {
			for (AvailablePhoneNumber availableTelNum : listAvailableNum) {
				TelNumberBean telNumberBean = new TelNumberBean();
				telNumberBean.setTelNumber(availableTelNum.getPhoneNumber());
				telNumberBean
						.setHumanTelNumber(getHumanFormTelNum(availableTelNum
								.getPhoneNumber()));

				telNumberBean.setTelNumberType(sNumType);
				arrTelNumBean.add(telNumberBean);

			}
		}

		return arrTelNumBean;
	}

	public void saveTelNumbers(TelNumberMetaData telNumMetaData) {

		ArrayList<TelNumberTypeBean> arrTelNumTypeBean = getTelNumberTypeBeans(Constants.EVENT_TASK.ALL
				.getTask());

		if (arrTelNumTypeBean != null && !arrTelNumTypeBean.isEmpty()) {
			TelNumberData telNumData = new TelNumberData();
			for (TelNumberTypeBean telNumType : arrTelNumTypeBean) {
				if (Constants.EVENT_TASK.RSVP.getTask().equalsIgnoreCase(
						telNumType.getTelNumType())) {
					telNumMetaData.setDigits(telNumMetaData
							.getRsvpTelNumDigit());
				} else if (Constants.EVENT_TASK.SEATING.getTask()
						.equalsIgnoreCase(telNumType.getTelNumType())) {
					telNumMetaData.setDigits(telNumMetaData
							.getSeatingTelNumDigit());
				}
				telNumMetaData.setTelNumberTypeId(telNumType
						.getTelNumberTypeId());

				telNumData.createTelNumber(telNumMetaData);
			}
		}
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

	public ArrayList<TelNumberTypeBean> getTelNumberTypeBeans(String telNumType) {

		if (telNumType == null) {
			telNumType = ParseUtil.checkNull(telNumType);
		}
		TelNumberData telNumData = new TelNumberData();
		ArrayList<TelNumberTypeBean> arrTelNumTypeBean = telNumData
				.getTelNumberTypes(telNumType);
		return arrTelNumTypeBean;
	}

	public ArrayList<DemoTelNumber> getDemoTelNumber() {
		TelNumberData telNumData = new TelNumberData();
		ArrayList<DemoTelNumber> arrDemoTelNumBean = telNumData
				.getDemoTelNumberList();
		return arrDemoTelNumBean;
	}

	public DemoTelNumber getRandomDemoNumber(
			ArrayList<DemoTelNumber> arrDemoTelNumBean,
			Constants.EVENT_TASK eventTask) {
		DemoTelNumber demoTelNumber = new DemoTelNumber();
		if (arrDemoTelNumBean != null && !arrDemoTelNumBean.isEmpty()
				&& eventTask != null
				&& !"".equalsIgnoreCase(eventTask.getTask())) {
			TelNumberData telNumData = new TelNumberData();
			ArrayList<TelNumberTypeBean> arrTelNumTypeBean = telNumData
					.getTelNumberTypes(eventTask.getTask());

			String sDemoTelNumTypeId = "";
			if (arrTelNumTypeBean != null && !arrTelNumTypeBean.isEmpty()) {
				for (TelNumberTypeBean telNumTypeBean : arrTelNumTypeBean) {
					sDemoTelNumTypeId = telNumTypeBean.getTelNumberTypeId();
					break;
				}
			}

			ArrayList<DemoTelNumber> arrTmpDemoTelNumBean = new ArrayList<DemoTelNumber>();
			if (sDemoTelNumTypeId != null
					&& !"".equalsIgnoreCase(sDemoTelNumTypeId)) {

				for (DemoTelNumber tmpDemoTelNumber : arrDemoTelNumBean) {
					if (tmpDemoTelNumber != null
							&& sDemoTelNumTypeId
									.equalsIgnoreCase(tmpDemoTelNumber
											.getDemoTelNumberTypeId())) {
						arrTmpDemoTelNumBean.add(tmpDemoTelNumber);
					}
				}

			}

			if (arrTmpDemoTelNumBean != null && !arrTmpDemoTelNumBean.isEmpty()) {
				Integer iRandomIndex = Utility
						.getRandomInteger(arrTmpDemoTelNumBean.size());
				demoTelNumber = arrTmpDemoTelNumBean.get(iRandomIndex);
			}

		}
		return demoTelNumber;
	}

	public void setEventDemoNumber(String sEventId, String sAdmin) {
		if (sEventId != null && !"".equalsIgnoreCase(sEventId)) {
			ArrayList<DemoTelNumber> arrDemoTelNumBean = getDemoTelNumber();

			DemoTelNumber rsvpDemoNumber = getRandomDemoNumber(
					arrDemoTelNumBean, Constants.EVENT_TASK.DEMO_RSVP);
			DemoTelNumber seatingDemoNumber = getRandomDemoNumber(
					arrDemoTelNumBean, Constants.EVENT_TASK.DEMO_SEATING);

			String sEventIdentifier = Utility.generateSecretKey(5);
			String sRsvpEventSecretKey = Utility.generateSecretKey(5);
			String sSeatingEventSecretKey = Utility.generateSecretKey(5);

			TelNumberMetaData telNumberMetaData = new TelNumberMetaData();
			telNumberMetaData.setActive(true);
			telNumberMetaData.setAdminId(sAdmin);
			telNumberMetaData.setEventId(sEventId);
			telNumberMetaData.setDelRow(false);
			telNumberMetaData.setPurchased(true);
			telNumberMetaData.setSecretEventIdentifier(sEventIdentifier);

			TelNumberData telNumData = new TelNumberData();
			telNumberMetaData.setTelNumberTypeId(seatingDemoNumber
					.getDemoTelNumberTypeId());
			telNumberMetaData.setSecretEventSecretKey(sSeatingEventSecretKey);
			telNumberMetaData.setDigits(seatingDemoNumber.getDemoTelNumber());
			telNumberMetaData.setHumanTelNumber(seatingDemoNumber
					.getDemoHumanTelNumber());
			telNumData.createTelNumber(telNumberMetaData);

			telNumberMetaData.setTelNumberTypeId(rsvpDemoNumber
					.getDemoTelNumberTypeId());
			telNumberMetaData.setSecretEventSecretKey(sRsvpEventSecretKey);
			telNumberMetaData.setDigits(rsvpDemoNumber.getDemoTelNumber());
			telNumberMetaData.setHumanTelNumber(rsvpDemoNumber
					.getDemoHumanTelNumber());
			telNumData.createTelNumber(telNumberMetaData);

		}
	}

}
