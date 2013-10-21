package com.gs.manager.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gs.bean.*;
import com.gs.bean.email.EmailScheduleBean;
import com.gs.common.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.email.EmailQueueBean;
import com.gs.bean.email.EmailTemplateBean;
import com.gs.common.exception.ExceptionHandler;
import com.gs.common.mail.MailCreator;
import com.gs.common.mail.MailingServiceData;
import com.gs.common.mail.SingleEmailCreator;
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
	Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
	private static Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);

	public TelNumberResponse getTelNumberDetails( TelNumberMetaData telNumberMetaData) {
		TelNumberResponse telNumberResponse = new TelNumberResponse();
		if (telNumberMetaData != null) {
			TelNumberData telNumData = new TelNumberData();
			TelNumberBean telNumberBean = telNumData.getTelNumber(telNumberMetaData);

			telNumberResponse.setTelNumberBean(telNumberBean);
		}

		return telNumberResponse;
	}

	public ArrayList<TelNumberBean> getTelNumbersByEvent( TelNumberMetaData telNumberMetaData) {
		ArrayList<TelNumberBean> arrTelNumberBean = new ArrayList<TelNumberBean>();

		if (telNumberMetaData != null && !Utility.isNullOrEmpty(telNumberMetaData.getAdminId())
                && !Utility.isNullOrEmpty(telNumberMetaData.getEventId())) {
			TelNumberData telNumData = new TelNumberData();
			arrTelNumberBean = telNumData.getEventTelNumbers(telNumberMetaData);
		}
		return arrTelNumberBean;
	}

	public ArrayList<TelNumberBean> getTelNumEventDetails( TelNumberMetaData telNumberMetaData ) {
		ArrayList<TelNumberBean> arrTelNumberBean = new ArrayList<TelNumberBean>();

		if (telNumberMetaData != null && !Utility.isNullOrEmpty(telNumberMetaData.getAdminId())
                && !Utility.isNullOrEmpty(telNumberMetaData.getEventId()) ) {
			arrTelNumberBean = getTelNumbersByEvent(telNumberMetaData);
		}

        boolean isTelNumberExists = false;

        if (arrTelNumberBean != null && !arrTelNumberBean.isEmpty()) {
            for (TelNumberBean telNumBer : arrTelNumberBean) {
                if (telNumBer != null) {
                    if(  Constants.EVENT_TASK.DEMO_TELEPHONE_NUMBER.getTask().equalsIgnoreCase(telNumBer.getTelNumberType())
                            || Constants.EVENT_TASK.PREMIUM_TELEPHONE_NUMBER.getTask().equalsIgnoreCase(telNumBer.getTelNumberType())) {
                        isTelNumberExists = true;
                    }
                }
            }
        }

        if ( !isTelNumberExists ) {

            arrTelNumberBean = getGeneratedTelNumbers(isTelNumberExists, telNumberMetaData, arrTelNumberBean);
        }
		return arrTelNumberBean;

	}

    public ArrayList<TelNumberBean> getGeneratedTelNumbers(  boolean isTelNumberExists,
                                                             TelNumberMetaData telNumberMetaData,
                                                             ArrayList<TelNumberBean> arrTelNumberBean ) {
        if(!isTelNumberExists) {
            List<AvailablePhoneNumber> listAvailablePhone = generateTelephoneNumber();
            if (listAvailablePhone != null && !listAvailablePhone.isEmpty()) {
                for (AvailablePhoneNumber availTelNum : listAvailablePhone) {
                    TelNumberBean seatingBean = createTelNumberBean(
                            telNumberMetaData, availTelNum.getPhoneNumber(),
                            Constants.EVENT_TASK.PREMIUM_TELEPHONE_NUMBER);

                }
            }
        }

        return arrTelNumberBean;
    }

    public ArrayList<TelNumberBean> getGeneratedTelNumbers( TelNumberMetaData telNumberMetaData, ArrayList<TelNumberBean> arrTelNumberBean) {
        List<AvailablePhoneNumber> listAvailablePhone = generateTelephoneNumber();
        if (listAvailablePhone != null && !listAvailablePhone.isEmpty()) {
            for (AvailablePhoneNumber availTelNum : listAvailablePhone) {
                TelNumberBean telephonyBean = createTelNumberBean(telNumberMetaData, availTelNum.getPhoneNumber(),
                        Constants.EVENT_TASK.PREMIUM_TELEPHONE_NUMBER);
                arrTelNumberBean.add(telephonyBean);
                break;
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

        appLogging.info("Purchase the Telephone Num : " + sTelephoneNum + " adminid : " + adminAccountMeta.getAdminId());
		String sPurchasedPhoneNum = "777-888-9999";
		if (adminAccountMeta != null
				&& !"".equalsIgnoreCase(adminAccountMeta.getAdminId())) {

			AdminTelephonyAccountManager adminTelAcMan = new AdminTelephonyAccountManager();
			AdminTelephonyAccountBean adminTelephonyAccBean = adminTelAcMan.getAdminAccount(adminAccountMeta);

			if (adminTelephonyAccBean != null && !"".equalsIgnoreCase(adminTelephonyAccBean.getAccountSid())
					&& !"".equalsIgnoreCase(adminTelephonyAccBean.getAuthToken())) {

				TwilioRestClient client = new TwilioRestClient(
						adminTelephonyAccBean.getAccountSid(),
						adminTelephonyAccBean.getAuthToken());

				Account mainAccount = client.getAccount();

				String sEnvironment = applicationConfig.get(Constants.PROP_ENVIRONMENT);
				if (Constants.ENVIRONMENT.VIRTUAL_MACHINE.getEnv().equalsIgnoreCase(sEnvironment)
						|| Constants.ENVIRONMENT.SANDBOX.getEnv().equalsIgnoreCase(sEnvironment)
						|| Constants.ENVIRONMENT.ALPHA.getEnv().equalsIgnoreCase(sEnvironment)) {

					sPurchasedPhoneNum = "777-888-9999";

				} else if (Constants.ENVIRONMENT.BETA.getEnv().equalsIgnoreCase(sEnvironment)) {

					sPurchasedPhoneNum = "777-888-9999";

				} else if (Constants.ENVIRONMENT.PROD.getEnv().equalsIgnoreCase(sEnvironment)) {
					// Buy the first number returned
                    String sApplicationDomain = ParseUtil.checkNull(applicationConfig.get(Constants.PROP_APPLICATION_DOMAIN));
					Map<String, String> params = new HashMap<String, String>();
					params.put("PhoneNumber", sTelephoneNum);
                    params.put("VoiceUrl", sApplicationDomain+"/IncomingCall");
                    params.put("VoiceMethod", "POST");
                    params.put("VoiceFallbackUrl",  sApplicationDomain+"/IncomingCall?incoming_call_type=call_fail");
                    params.put("VoiceFallbackMethod", "POST");
                    params.put("StatusCallback",  sApplicationDomain+"/IncomingCall?incoming_call_type=end_call");
                    params.put("StatusCallbackMethod", "POST");
                    appLogging.info("Twilio Params at point of sale : " + params );
					IncomingPhoneNumber purchasedNumber = mainAccount.getIncomingPhoneNumberFactory().create(params);

					sPurchasedPhoneNum = purchasedNumber.getPhoneNumber();
				}

			}
		}
		return sPurchasedPhoneNum;
	}

	private TelNumberBean createTelNumberBean( TelNumberMetaData telNumberMetaData, String sTelephoneNum,
			Constants.EVENT_TASK telNumType) {
		TelNumberBean telNumberBean = new TelNumberBean();
		telNumberBean.setTelNumber(sTelephoneNum.substring(1));
		telNumberBean.setAdminId(telNumberMetaData.getAdminId());
		telNumberBean.setEventId(telNumberMetaData.getEventId());
		telNumberBean.setTelNumberType(telNumType.getTask());
		telNumberBean.setHumanTelNumber(Utility.convertInternationalToHumanTelNum(sTelephoneNum));
		return telNumberBean;
	}

	public EventGuestBean getTelNumGuestDetails( TelNumberMetaData telNumberMetaData) {
		EventGuestBean eventGuestBean = new EventGuestBean();
		if (telNumberMetaData != null) {
			GuestData guestData = new GuestData();

			ArrayList<GuestBean> arrGuestBean = guestData.getGuestsByTelNumber(telNumberMetaData);
            appLogging.info(" Get the guest bean from guest id and event id arrGuestBean : " + arrGuestBean );
			if (arrGuestBean != null && !arrGuestBean.isEmpty()) {
				ArrayList<String> arrGuestId = new ArrayList<String>();
				for (GuestBean guestBean : arrGuestBean) {
					arrGuestId.add(guestBean.getGuestId());
				}

				EventGuestMetaData eventGuestMetaData = new EventGuestMetaData();
				eventGuestMetaData.setEventId(telNumberMetaData.getEventId());
				eventGuestMetaData.setArrGuestId(arrGuestId);
                appLogging.info(" Requesting event guest data : " + eventGuestMetaData );
				EventGuestManager eventGuestManager = new EventGuestManager();
				eventGuestBean = eventGuestManager.getGuest(eventGuestMetaData);
                appLogging.info(" Event Guest Bean : " + eventGuestBean );

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
				telNumberBean.setHumanTelNumber(Utility
						.convertInternationalToHumanTelNum(availableTelNum
								.getPhoneNumber()));

				telNumberBean.setTelNumberType(sNumType);
				arrTelNumBean.add(telNumberBean);

			}
		}

		return arrTelNumBean;
	}

	public void saveConvertDemoToPremiumTelNumbers(TelNumberMetaData telNumMetaData) {

        if(telNumMetaData!=null) {
            TelNumberTypeBean demoTelNumTypeBean = getIndividualTelNumberTypeBean(Constants.EVENT_TASK.DEMO_TELEPHONE_NUMBER);
            TelNumberTypeBean premiumTelNumTypeBean = getIndividualTelNumberTypeBean(Constants.EVENT_TASK.PREMIUM_TELEPHONE_NUMBER);

            telNumMetaData.setDigits(Utility.convertHumanToInternationalTelNum(telNumMetaData.getTelephoneNumNumDigit()));
            telNumMetaData.setHumanTelNumber(Utility.convertInternationalToHumanTelNum(telNumMetaData.getDigits()));

            telNumMetaData.setTelNumberTypeId(premiumTelNumTypeBean.getTelNumberTypeId());

            TelNumberData telNumData = new TelNumberData();
            telNumData.updateTelNumber(telNumMetaData, demoTelNumTypeBean);
        }
	}

	public TelNumberTypeBean getDemoTelNumTypes(Constants.EVENT_TASK eventTask) {
		TelNumberTypeBean telNumTypeBean = new TelNumberTypeBean();
		if (eventTask != null) {
			ArrayList<TelNumberTypeBean> arrTelNumTypeBean = getTelNumberTypeBeans(eventTask);

			if (arrTelNumTypeBean != null && !arrTelNumTypeBean.isEmpty()) {
				for (TelNumberTypeBean tmpTelNumTypeBean : arrTelNumTypeBean) {
					telNumTypeBean = tmpTelNumTypeBean;
				}
			}
		}
		return telNumTypeBean;
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
    public  TelNumberTypeBean getIndividualTelNumberTypeBean(Constants.EVENT_TASK eventTask) {
        TelNumberTypeBean telNumberTypeBean = new TelNumberTypeBean();
        ArrayList<TelNumberTypeBean> arrTelNumTypeBean =  getTelNumberTypeBeans(eventTask);
        if(arrTelNumTypeBean!=null && !arrTelNumTypeBean.isEmpty() && !Constants.EVENT_TASK.ALL.getTask().equalsIgnoreCase(eventTask.getTask())) {
            for( TelNumberTypeBean tmpTelNumberTypeBean : arrTelNumTypeBean ) {
                telNumberTypeBean = tmpTelNumberTypeBean;
            }
        }
        return telNumberTypeBean;
    }
	public ArrayList<TelNumberTypeBean> getTelNumberTypeBeans(Constants.EVENT_TASK eventTask) {

        ArrayList<TelNumberTypeBean> arrTelNumTypeBean = new ArrayList<TelNumberTypeBean>();
        if (eventTask != null) {
            TelNumberData telNumData = new TelNumberData();
            arrTelNumTypeBean = telNumData.getTelNumberTypes(eventTask);
		}
		return arrTelNumTypeBean;
	}

	public ArrayList<DemoTelNumber> getDemoTelNumber() {
		TelNumberData telNumData = new TelNumberData();
		ArrayList<DemoTelNumber> arrDemoTelNumBean = telNumData.getDemoTelNumberList();
		return arrDemoTelNumBean;
	}

	public DemoTelNumber getRandomDemoNumber(ArrayList<DemoTelNumber> arrDemoTelNumBean,
			Constants.EVENT_TASK eventTask) {
		DemoTelNumber demoTelNumber = new DemoTelNumber();
		if (arrDemoTelNumBean != null && !arrDemoTelNumBean.isEmpty() && eventTask != null && !"".equalsIgnoreCase(eventTask.getTask())) {
			TelNumberData telNumData = new TelNumberData();
			ArrayList<TelNumberTypeBean> arrTelNumTypeBean = telNumData.getTelNumberTypes(eventTask);

			String sDemoTelNumTypeId = Constants.EMPTY;
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
							&& sDemoTelNumTypeId.equalsIgnoreCase(tmpDemoTelNumber.getDemoTelNumberTypeId())) {
						arrTmpDemoTelNumBean.add(tmpDemoTelNumber);
					}
				}

			}

			if (arrTmpDemoTelNumBean != null && !arrTmpDemoTelNumBean.isEmpty()) {
				Integer iRandomIndex = Utility.getRandomInteger(arrTmpDemoTelNumBean.size());
				demoTelNumber = arrTmpDemoTelNumBean.get(iRandomIndex);
			}

		}
		return demoTelNumber;
	}

	public void setEventDemoNumber(String sEventId, String sAdmin) {
		if (sEventId != null && !"".equalsIgnoreCase(sEventId)) {
			ArrayList<DemoTelNumber> arrDemoTelNumBean = getDemoTelNumber();

            DemoTelNumber demoTelephoneNumberNumber = getRandomDemoNumber(arrDemoTelNumBean, Constants.EVENT_TASK.DEMO_TELEPHONE_NUMBER);

            String sEventIdentifier = Utility.generateSecretKey(5);

			TelNumberMetaData telNumberMetaData = new TelNumberMetaData();
			telNumberMetaData.setActive(true);
			telNumberMetaData.setAdminId(sAdmin);
			telNumberMetaData.setEventId(sEventId);
			telNumberMetaData.setDelRow(false);
			telNumberMetaData.setPurchased(true);
			telNumberMetaData.setSecretEventIdentifier(sEventIdentifier);
            telNumberMetaData.setCurrentTime(DateSupport.getEpochMillis() );
            telNumberMetaData.setCurrentHumanTime( DateSupport.getUTCDateTime() );
            telNumberMetaData.setTelNumberTypeId(demoTelephoneNumberNumber.getDemoTelNumberTypeId());
            telNumberMetaData.setDigits(Utility.convertHumanToInternationalTelNum(demoTelephoneNumberNumber.getDemoTelNumber()));
            telNumberMetaData.setHumanTelNumber(demoTelephoneNumberNumber.getDemoHumanTelNumber());

            TelNumberData telNumData = new TelNumberData();
            telNumData.createTelNumber(telNumberMetaData);
		}
	}

	public void sendNewTelnumberPurchasedEmail(
			TelNumberMetaData telNumberMetaData, UserInfoBean adminUserInfoBean) {

		if (telNumberMetaData != null && adminUserInfoBean != null) {

			MailingServiceData mailingServiceData = new MailingServiceData();
			EmailTemplateBean emailTemplate = mailingServiceData
					.getEmailTemplate(Constants.EMAIL_TEMPLATE.NEWTELNUMBERPURCHASE);

            String sHtmlTemplate = emailTemplate.getHtmlBody();
            String sTxtTemplate = emailTemplate.getTextBody();

            String sRegisteredUserGivenName = ( !Utility.isNullOrEmpty(adminUserInfoBean.getFirstName())? adminUserInfoBean.getFirstName():"" ) + " " +
                    ( !Utility.isNullOrEmpty(adminUserInfoBean.getLastName())? adminUserInfoBean.getLastName():"");
            sTxtTemplate = sTxtTemplate.replaceAll("__GIVENNAME__",sRegisteredUserGivenName );
            sHtmlTemplate = sHtmlTemplate.replaceAll("__GIVENNAME__",sRegisteredUserGivenName );



			sTxtTemplate = sTxtTemplate.replaceAll("__NEW_TELEPHONE_TELNUM__", telNumberMetaData.getTelephoneNumNumDigit());
            sHtmlTemplate = sHtmlTemplate.replaceAll("__NEW_TELEPHONE_TELNUM__", telNumberMetaData.getTelephoneNumNumDigit());

            String sProductName = ParseUtil.checkNull(applicationConfig.get(Constants.PRODUCT_NAME));
            String sProductPhone = ParseUtil.checkNull(applicationConfig.get(Constants.PROP_PRODUCT_PHONE));
            String sProductAddress = ParseUtil.checkNull(applicationConfig.get(Constants.PROP_PRODUCT_ADDRESS));
            sTxtTemplate = sTxtTemplate.replaceAll("__PRODUCT_NAME__",ParseUtil.checkNull(sProductName));
            sHtmlTemplate = sHtmlTemplate.replaceAll("__PRODUCT_NAME__",ParseUtil.checkNull(sProductName));

            sTxtTemplate = sTxtTemplate.replaceAll("__PRODUCT_PHONE__",ParseUtil.checkNull(sProductPhone));
            sHtmlTemplate = sHtmlTemplate.replaceAll("__PRODUCT_PHONE__",ParseUtil.checkNull(sProductPhone));

            sTxtTemplate = sTxtTemplate.replaceAll("__PRODUCT_ADDRESS__",ParseUtil.checkNull(sProductAddress));
            sHtmlTemplate = sHtmlTemplate.replaceAll("__PRODUCT_ADDRESS__",ParseUtil.checkNull(sProductAddress));

            EventManager eventManager = new EventManager();
            EventBean eventBean = eventManager.getEvent( ParseUtil.checkNull(telNumberMetaData.getEventId()) );
            String sSeatingPlanName = Constants.EMPTY;

            if(eventBean!=null && !Utility.isNullOrEmpty(eventBean.getEventId())) {
                sSeatingPlanName = ParseUtil.checkNull(eventBean.getEventName());
            }
            String sEmailSubject = ParseUtil.checkNull(emailTemplate.getEmailSubject());
            sEmailSubject = sEmailSubject.replaceAll("__SEATINGPLANNAME__",sSeatingPlanName);

			EmailQueueBean emailQueueBean = new EmailQueueBean();
			emailQueueBean.setEmailSubject(sEmailSubject);
			emailQueueBean.setFromAddress(emailTemplate.getFromAddress());
			emailQueueBean.setFromAddressName(emailTemplate.getFromAddressName());
			emailQueueBean.setToAddress(adminUserInfoBean.getEmail());
			emailQueueBean.setToAddressName(adminUserInfoBean.getFirstName()
					+ " " + adminUserInfoBean.getLastName());
			emailQueueBean.setHtmlBody(sHtmlTemplate);
			emailQueueBean.setTextBody(sTxtTemplate);
            if( ParseUtil.sTob(applicationConfig.get(Constants.PROP_EMAIL_ADMIN_NEW_REGISTRATION , Constants.FALSE ))  ) {
                final String emailAdmin =  applicationConfig.get(Constants.PROP_EMAIL_ADMIN , "kjohn@smarasoft.com" );
                emailQueueBean.setBccAddress( emailAdmin );
                emailQueueBean.setBccAddressName( emailAdmin );
            }

			emailQueueBean.setStatus(Constants.EMAIL_STATUS.NEW.getStatus());

			MailCreator eailCreator = new SingleEmailCreator();
			eailCreator.create(emailQueueBean , new EmailScheduleBean());

		}

	}

	public ArrayList<TelNumberBean> getTelNumbersFromSecretEventNumAndKey( TelNumberMetaData telNumMetaData) {
		ArrayList<TelNumberBean> arrTelNumBean = new ArrayList<TelNumberBean>();
		if (telNumMetaData != null && !"".equalsIgnoreCase(telNumMetaData.getSecretEventIdentifier())
				&& !"".equalsIgnoreCase(telNumMetaData.getSecretEventSecretKey())) {
			TelNumberData telNumberData = new TelNumberData();
			arrTelNumBean = telNumberData.getTelNumbersFromSecretEventNumAndKey(telNumMetaData);
		}
		return arrTelNumBean;
	}

	public ArrayList<TelNumberBean> getTelNumbersFromSecretEventNum( TelNumberMetaData telNumMetaData) {
		ArrayList<TelNumberBean> arrTelNumBean = new ArrayList<TelNumberBean>();
		if (telNumMetaData != null && !"".equalsIgnoreCase(telNumMetaData.getSecretEventIdentifier())) {
			TelNumberData telNumberData = new TelNumberData();
			arrTelNumBean = telNumberData.getTelNumbersFromSecretEventNum(telNumMetaData);
		}
		return arrTelNumBean;
	}
}
