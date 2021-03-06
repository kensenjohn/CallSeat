package com.gs.manager;

import com.gs.bean.*;
import com.gs.bean.email.EmailScheduleBean;
import com.gs.bean.response.WebRespRequest;
import com.gs.common.*;
import com.gs.manager.event.EventManager;
import com.gs.manager.event.TelNumberManager;
import com.gs.manager.event.TelNumberMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.email.EmailQueueBean;
import com.gs.bean.email.EmailTemplateBean;
import com.gs.common.mail.MailCreator;
import com.gs.common.mail.MailingServiceData;
import com.gs.common.mail.QuickMailSendThread;
import com.gs.common.mail.SingleEmailCreator;
import com.gs.data.AdminData;

import java.util.ArrayList;

public class AdminManager {
	private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
    Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);

	/**
	 * This will create an Admin record. If admin is null or does not have a
	 * matching User Info it will throw an error.
	 * 
	 * @param adminBean
	 */
	public void createAdmin(AdminBean adminBean) {
		if (adminBean == null || adminBean.getAdminId() == null
				|| "".equalsIgnoreCase(adminBean.getAdminId())
				|| adminBean.getUserInfoId() == null
				|| "".equalsIgnoreCase(adminBean.getUserInfoId())) {
			appLogging.error("There is no user info to create.");
		} else {
			AdminData adminData = new AdminData();

			int iNumOfRecs = adminData.insertAdmin(adminBean);

			if (iNumOfRecs > 0) {
				appLogging.info("Admin creation successful for  : "
						+ adminBean.getAdminId());
			} else {
				appLogging.error("Error creating Admin "
						+ adminBean.getAdminId());
			}
		}

	}

	public AdminBean createAdmin() {
		AdminBean adminBean = getTemporaryAdmin();
		createAdmin(adminBean);

		return adminBean;
	}

	private AdminBean getTemporaryAdmin() {
		AdminBean adminBean = new AdminBean();

		UserInfoManager userInfoManager = new UserInfoManager();
		UserInfoBean userInfoBean = userInfoManager.createUserInfoBean();

		if (userInfoBean != null
				&& !"".equalsIgnoreCase(userInfoBean.getUserInfoId())) {
			adminBean.setUserInfoId(userInfoBean.getUserInfoId());

			adminBean.setAdminId(Utility.getNewGuid());
			adminBean.setIsTemporary("1");
			adminBean.setDeleteRow("0");
			adminBean.setCreateDate(DateSupport.getEpochMillis()); // change
			// this to a
			// valid date
			adminBean.setHumanCreateDate(DateSupport.getUTCDateTime());
		}

		// adminBean.

		return adminBean;
	}

	public UserInfoBean getAminUserInfo(String sAdminId) {
		UserInfoBean adminUserInfo = new UserInfoBean();
		if (sAdminId != null && !"".equals(sAdminId)) {
			AdminData adminData = new AdminData();
			adminUserInfo = adminData.getAdminUserInfo(sAdminId);

		}
		return adminUserInfo;
	}

	public AdminBean getAdmin(String sAdminId) {
		AdminBean adminBean = new AdminBean();
		if (sAdminId != null && !"".equals(sAdminId)) {
			AdminData adminData = new AdminData();
			adminBean = adminData.getAdmin(sAdminId);
		}
		return adminBean;
	}

	public boolean isUserExists(RegisterAdminBean regAdminBean) {

		boolean isAdminExists = false;
		if (regAdminBean != null) {
			AdminData adminData = new AdminData();
			AdminBean adminBean = adminData.isAdminEmailExists(regAdminBean);

			String sAdminId = adminBean.getAdminId();

			if (sAdminId != null && !"".equalsIgnoreCase(sAdminId)) {
				isAdminExists = true;
			}
		}

		return isAdminExists;
	}

	public Integer updateUser(AdminBean adminBean) {
		int numOfRecords = 0;
		if (adminBean != null) {

			AdminData adminData = new AdminData();

			numOfRecords = adminData.updateUser(adminBean);
		}
		return numOfRecords;
	}

	public AdminBean registerUser(RegisterAdminBean regAdminBean) {
		AdminBean adminBean = new AdminBean();
		int numOfAdminRegs = 0;
		if (regAdminBean != null) {

			String sEmail = regAdminBean.getEmail();
			String sPassword = regAdminBean.getPassword();

			String sPasswordHash = BCrypt.hashpw(sPassword, BCrypt.gensalt(5));

			regAdminBean.setPasswordHash(sPasswordHash);
			regAdminBean.setCreateDate(DateSupport.getEpochMillis());
			regAdminBean.setHumanCreateDate(DateSupport.getUTCDateTime());

			AdminData adminData = new AdminData();

			adminBean = adminData.getAdmin(regAdminBean.getAdminId());

			if (adminBean == null
					|| (adminBean != null && !adminBean.isAdminExists())) {
				adminBean = createAdmin();

				regAdminBean.setAdminId(adminBean.getAdminId());
			}

			if (regAdminBean != null && regAdminBean.getAdminId() != null
					&& !"".equalsIgnoreCase(regAdminBean.getAdminId())) {
				adminData.registerUser(regAdminBean, adminBean);

				if (adminBean != null && adminBean.getAdminId() != null
						&& !"".equalsIgnoreCase(adminBean.getAdminId())) {
					numOfAdminRegs = adminData.toggleAdminTemp(false,
							adminBean.getAdminId());
				} else {
					appLogging.error("Error  registering user.");
				}
			} else {
				appLogging
						.error("Error registering because the admin id was null or empty."
								+ regAdminBean + " - " + adminBean);
				adminBean = null;
			}

		}
		return adminBean;
	}

	public void sendNewRegUserEmail(RegisterAdminBean regAdminBean) {
		if (regAdminBean != null) {
			MailingServiceData mailingServiceData = new MailingServiceData();
			EmailTemplateBean emailTemplate = mailingServiceData.getEmailTemplate(Constants.EMAIL_TEMPLATE.REGISTRATION);

			String sHtmlBody = emailTemplate.getHtmlBody();
			String sTxtBody = emailTemplate.getTextBody();

            String sRegisteredUserGivenName = ( !Utility.isNullOrEmpty(regAdminBean.getFirstName())? regAdminBean.getFirstName():"" ) + " " +
                    ( !Utility.isNullOrEmpty(regAdminBean.getLastName())? regAdminBean.getLastName():"");
            sHtmlBody = sHtmlBody.replaceAll("__GIVENNAME__",sRegisteredUserGivenName );
			sHtmlBody = sHtmlBody.replaceAll("__USERNAME__",
					ParseUtil.checkNull(regAdminBean.getEmail()));
			sTxtBody = sTxtBody.replaceAll("__USERNAME__",
					ParseUtil.checkNull(regAdminBean.getEmail()));
            sTxtBody = sTxtBody.replaceAll("__GIVENNAME__",sRegisteredUserGivenName );


			EmailQueueBean emailQueueBean = new EmailQueueBean();
			emailQueueBean.setEmailSubject(emailTemplate.getEmailSubject());
			emailQueueBean.setFromAddress(emailTemplate.getFromAddress());
			emailQueueBean.setFromAddressName(emailTemplate
					.getFromAddressName());
			emailQueueBean.setToAddress(regAdminBean.getEmail());
			emailQueueBean.setToAddressName(regAdminBean.getFirstName() + " "
					+ regAdminBean.getLastName());
			emailQueueBean.setHtmlBody(sHtmlBody);
			emailQueueBean.setTextBody(sTxtBody);
			emailQueueBean.setStatus(Constants.EMAIL_STATUS.NEW.getStatus());

            //Bcc to keep track of who registered to event. This can be put in a property file
            if( ParseUtil.sTob(applicationConfig.get(Constants.PROP_EMAIL_ADMIN_NEW_REGISTRATION , Constants.FALSE ))  ) {
                final String emailAdmin =  applicationConfig.get(Constants.PROP_EMAIL_ADMIN , "kjohn@smarasoft.com" );
                emailQueueBean.setBccAddress( emailAdmin );
                emailQueueBean.setBccAddressName( emailAdmin );
            }

			MailCreator mailCreator = new SingleEmailCreator();
			mailCreator.create(emailQueueBean, new EmailScheduleBean());
		}

	}

    public EmailTemplateBean getFormattedSeatingInformationEmail(WebRespRequest webRespRequest ) {
        EmailTemplateBean guestResponseEmailTemplate = getFormattedSeatingInformationEmail(webRespRequest, new EmailTemplateBean());

        return guestResponseEmailTemplate;
    }

    public EmailTemplateBean getFormattedRSVPResponseEmail(WebRespRequest webRespRequest ) {
        EmailTemplateBean guestResponseEmailTemplate = getFormattedRSVPResponseEmail(webRespRequest, new EmailTemplateBean());

        return guestResponseEmailTemplate;
    }

    public EmailTemplateBean getFormattedSeatingInformationEmail( WebRespRequest webRespRequest , EmailTemplateBean seatingInfoEmailTemplate ) {
        if(webRespRequest!=null && !"".equalsIgnoreCase(webRespRequest.getAdminId())  && !"".equalsIgnoreCase(webRespRequest.getEventId()) && seatingInfoEmailTemplate!=null ) {
            Constants.EMAIL_TEMPLATE emailTemplateType = Constants.EMAIL_TEMPLATE.SEATING_CONFIRMATION_EMAIL;
            if( Utility.isNullOrEmpty(seatingInfoEmailTemplate.getEmailTemplateId())) {
                MailingServiceData mailingServiceData = new MailingServiceData();
                seatingInfoEmailTemplate = mailingServiceData.getEmailTemplate(emailTemplateType);
            }

            if(seatingInfoEmailTemplate!=null && !"".equalsIgnoreCase(seatingInfoEmailTemplate.getEmailTemplateId())) {
                String sSubject = seatingInfoEmailTemplate.getEmailSubject();
                sSubject = replaceTemplateWithEventData(sSubject, webRespRequest.getEventId());
                sSubject = replaceTemplateWithAdminData(sSubject, webRespRequest.getAdminId());
                seatingInfoEmailTemplate.setEmailSubject(sSubject);

                String sHtmlBody = seatingInfoEmailTemplate.getHtmlBody();
                sHtmlBody = replaceTemplateWithEventData(sHtmlBody, webRespRequest.getEventId());
                sHtmlBody = replaceTemplateWithAdminData(sHtmlBody, webRespRequest.getAdminId());
                seatingInfoEmailTemplate.setHtmlBody( sHtmlBody );

                String sTextBody = seatingInfoEmailTemplate.getTextBody();
                sTextBody = replaceTemplateWithEventData(sTextBody, webRespRequest.getEventId());
                sTextBody = replaceTemplateWithAdminData(sTextBody, webRespRequest.getAdminId());
                seatingInfoEmailTemplate.setTextBody( sTextBody );
            }
        }
        return seatingInfoEmailTemplate;
    }

    public EmailTemplateBean getFormattedRSVPResponseEmail( WebRespRequest webRespRequest , EmailTemplateBean guestResponseEmailTemplate ) {

        if(webRespRequest!=null && !"".equalsIgnoreCase(webRespRequest.getAdminId())  && !"".equalsIgnoreCase(webRespRequest.getEventId()) && guestResponseEmailTemplate!=null ) {
            TelNumberMetaData telNumberMetaData = new TelNumberMetaData();

            telNumberMetaData.setAdminId( webRespRequest.getAdminId() );
            telNumberMetaData.setEventId( webRespRequest.getEventId() );
            TelNumberManager telNumManager = new TelNumberManager();
            ArrayList<TelNumberBean> arrTelNumberBean = telNumManager.getTelNumEventDetails(telNumberMetaData);

            Constants.EMAIL_TEMPLATE emailTemplateType = Constants.EMAIL_TEMPLATE.RSVPRESPONSEDEMO;
            TelNumberBean rsvpTelNumberBean = new TelNumberBean();
            if(arrTelNumberBean!=null && !arrTelNumberBean.isEmpty()) {
                for(TelNumberBean telNumberBean : arrTelNumberBean ){
                    if( Constants.EVENT_TASK.PREMIUM_TELEPHONE_NUMBER.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType()))  {
                        emailTemplateType =  Constants.EMAIL_TEMPLATE.RSVPRESPONSE ;
                        rsvpTelNumberBean = telNumberBean;
                    } else if ( Constants.EVENT_TASK.DEMO_TELEPHONE_NUMBER.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType()) ) {
                        emailTemplateType =  Constants.EMAIL_TEMPLATE.RSVPRESPONSEDEMO ;
                        rsvpTelNumberBean = telNumberBean;
                    }
                }
            }

            if( Utility.isNullOrEmpty(guestResponseEmailTemplate.getEmailTemplateId())) {
                MailingServiceData mailingServiceData = new MailingServiceData();
                guestResponseEmailTemplate = mailingServiceData.getEmailTemplate(emailTemplateType);
            }

            if(guestResponseEmailTemplate!=null && !"".equalsIgnoreCase(guestResponseEmailTemplate.getEmailTemplateId())) {
                String sSubject = guestResponseEmailTemplate.getEmailSubject();
                sSubject = replaceTemplateWithEventData(sSubject, telNumberMetaData.getEventId());
                sSubject = replaceRSVPResponseTemplateWithPhoneData(sSubject, rsvpTelNumberBean);
                sSubject = replaceRSVPResponseTemplateWithHostData(sSubject, telNumberMetaData.getAdminId());
                guestResponseEmailTemplate.setEmailSubject(sSubject);

                String sHtmlBody = guestResponseEmailTemplate.getHtmlBody();
                sHtmlBody = replaceTemplateWithEventData(sHtmlBody, telNumberMetaData.getEventId());
                sHtmlBody = replaceRSVPResponseTemplateWithPhoneData(sHtmlBody, rsvpTelNumberBean );
                sHtmlBody = replaceRSVPResponseTemplateWithHostData(sHtmlBody,  telNumberMetaData.getAdminId() );
                guestResponseEmailTemplate.setHtmlBody(sHtmlBody);

                String sTextBody = guestResponseEmailTemplate.getTextBody();
                sTextBody = replaceTemplateWithEventData(sTextBody, telNumberMetaData.getEventId());
                sTextBody = replaceRSVPResponseTemplateWithPhoneData(sTextBody, rsvpTelNumberBean );
                sTextBody = replaceRSVPResponseTemplateWithHostData(sTextBody,  telNumberMetaData.getAdminId() );
                guestResponseEmailTemplate.setTextBody(sTextBody);
            }
        }
        return guestResponseEmailTemplate;
    }

    public String  replaceTemplateWithAdminData(String sText , String sAdminId ) {
        String srcText = ParseUtil.checkNull(sText);
        String  sAdminName = Constants.EMPTY;
        if( !Utility.isNullOrEmpty(sText)  && !Utility.isNullOrEmpty(sAdminId)  ) {
            AdminManager adminManager = new AdminManager();
            AdminBean adminBean = adminManager.getAdmin(sAdminId);
            if(adminBean!=null && adminBean.getAdminId()!=null && !"".equalsIgnoreCase( adminBean.getAdminId() ) ) {

                sAdminName =  ParseUtil.checkNull(adminBean.getAdminUserInfoBean().getFirstName()) + " " +   ParseUtil.checkNull(adminBean.getAdminUserInfoBean().getLastName());
            }
        }
        srcText = srcText.replaceAll("__HOSTNAME__", sAdminName );
        return srcText;
    }

    public String  replaceTemplateWithEventData(String sText , String sEventId ) {
        String srcText = ParseUtil.checkNull(sText);
        if(sText!=null && !"".equalsIgnoreCase(sText) && sEventId!=null && !"".equalsIgnoreCase(sEventId)) {
            EventManager eventManager = new EventManager();
            EventBean eventBean = eventManager.getEvent(sEventId);

            if(eventBean!=null && eventBean.getEventId()!=null && !"".equalsIgnoreCase( eventBean.getEventId() ) ) {
                srcText = srcText.replaceAll("__SEATINGPLANNAME__",ParseUtil.checkNull(eventBean.getEventName()));
                String sRSVPDeadline = DateSupport.getTimeByZone(eventBean.getRsvpDeadlineDate(), DateSupport.getTimeZone(eventBean.getEventTimeZone()).getID(), Constants.PRETTY_DATE_PATTERN_2) ;
                srcText = srcText.replaceAll("__RSVPDEADLINE__",ParseUtil.checkNull(sRSVPDeadline));
            }

        }
        return srcText;
    }
    private String replaceRSVPResponseTemplateWithPhoneData(String sText, TelNumberBean rsvpTelNumberBean ) {
        String srcText = ParseUtil.checkNull(sText);
        if(sText!=null && !"".equalsIgnoreCase(sText) && rsvpTelNumberBean!=null && !"".equalsIgnoreCase(rsvpTelNumberBean.getTelNumberId())) {

            String sTelephoneNumber = ParseUtil.checkNull(rsvpTelNumberBean.getHumanTelNumber());
            if ( Constants.EVENT_TASK.DEMO_TELEPHONE_NUMBER.getTask().equalsIgnoreCase(rsvpTelNumberBean.getTelNumberType()) ) {
                sTelephoneNumber = sTelephoneNumber + " Plan Id : " + ParseUtil.checkNull( rsvpTelNumberBean.getSecretEventIdentity() );
            }
            srcText = srcText.replaceAll("__RSVPPHONENUM__",ParseUtil.checkNull( sTelephoneNumber ));

        }
        return srcText;
    }
    private String replaceRSVPResponseTemplateWithHostData(String sText , String sAdminId  ) {
        String srcText = ParseUtil.checkNull(sText);
        if(sText!=null && !"".equalsIgnoreCase(sText) && sAdminId!=null && !"".equalsIgnoreCase(sAdminId)) {
            AdminManager adminManager = new AdminManager();
            AdminBean adminBean = adminManager.getAdmin(sAdminId);

            if(adminBean!=null && adminBean.getAdminId()!=null && !"".equalsIgnoreCase( adminBean.getAdminId() ) ) {
                srcText = srcText.replaceAll("__HOSTNAME__",ParseUtil.checkNull(adminBean.getAdminUserInfoBean().getFirstName()) +  " " + ParseUtil.checkNull(adminBean.getAdminUserInfoBean().getLastName()));
            }

        }
        return srcText;
    }

    private String replaceRSVPResponseTemplateWithRsvpLinkData(String sText , String sGuestId , String sEventId ) {
        String srcText = ParseUtil.checkNull(sText);
        if(sText!=null && !"".equalsIgnoreCase(sText) && sGuestId!=null && !"".equalsIgnoreCase(sGuestId)&& sEventId!=null && !"".equalsIgnoreCase(sEventId)) {

        }
        return srcText;
    }

	public AdminBean getAdminBeanFromEmail(RegisterAdminBean loginAdminBean) {
		AdminData adminData = new AdminData();
		AdminBean adminBean = adminData.isAdminEmailExists(loginAdminBean);

		return adminBean;
	}

	public AdminBean authenticateUser(RegisterAdminBean loginAdminBean) {

		AdminBean adminBean = new AdminBean();
		if (loginAdminBean != null && loginAdminBean.getEmail() != null
				&& !"".equalsIgnoreCase(loginAdminBean.getEmail())
				&& loginAdminBean.getPassword() != null
				&& !"".equalsIgnoreCase(loginAdminBean.getPassword())) {
			String sEmail = loginAdminBean.getEmail();
			String sPasswordInput = loginAdminBean.getPassword();

			AdminData adminData = new AdminData();
			adminBean = adminData.isAdminEmailExists(loginAdminBean);

			if (adminBean != null) {
				loginAdminBean.setAdminId(adminBean.getAdminId());

				String sPasswordHashFromDB = adminData.getPasswordHash(
						loginAdminBean, Constants.PASSWORD_STATUS.ACTIVE);
				if (sPasswordHashFromDB != null
						&& !"".equalsIgnoreCase(sPasswordHashFromDB)) {
					if (BCrypt.checkpw(sPasswordInput, sPasswordHashFromDB)) {
						// logged in.
						UserInfoBean userinfoBean = adminData
								.getAdminUserInfo(adminBean.getAdminId());
						adminBean.setAdminUserInfoBean(userinfoBean);
					} else {
						adminBean = new AdminBean();
						appLogging.info("User was not authenticated - Email : "
								+ sEmail);
					}
				} else {
					appLogging.error("Password Hash does not exist - Email : "
							+ sEmail);
				}
			} else {
				appLogging.error("Admin Info does not exist for email."
						+ sEmail);
			}

		} else {
			appLogging.error("Invalid data for credentials. ");
		}
		return adminBean;
	}

	public void assignTmpToPermAdmin(String sTmpAdminId, String sPermAdminId) {

		if (sTmpAdminId != null && !"".equalsIgnoreCase(sTmpAdminId)
				&& sPermAdminId != null && !"".equalsIgnoreCase(sPermAdminId)) {

			AdminData adminData = new AdminData();
			adminData.assignEventToPermAdmin(sTmpAdminId, sPermAdminId);
			adminData.assignGuestToPermAdmin(sTmpAdminId, sPermAdminId);
			adminData.assignTableToPermAdmin(sTmpAdminId, sPermAdminId);
			adminData.assignTelNumberToPermAdmin(sTmpAdminId, sPermAdminId);

		}

	}

	public RestPasswordResponseBean resetPassword(String sEmailId) {

		RestPasswordResponseBean resetPasswordRespBean = new RestPasswordResponseBean();
		if (sEmailId != null && !"".equalsIgnoreCase(sEmailId)) {
			RegisterAdminBean regAdminBean = new RegisterAdminBean();
			regAdminBean.setEmail(sEmailId);

			AdminData adminData = new AdminData();
			AdminBean adminBean = adminData.isAdminEmailExists(regAdminBean);

			if (adminBean != null) {

				String sNewPassword = ParseUtil.checkNull(Utility.getNewGuid()
						.substring(0, 8));

				if (sNewPassword != null && !"".equalsIgnoreCase(sNewPassword)) {

					resetPasswordRespBean = resetPassword(adminBean,
							sNewPassword);
				}
			}
		}
		return resetPasswordRespBean;
	}

	public boolean resetPasswordAndEmail(String sEmailId) {
		boolean isSuccess = false;
		if (sEmailId != null && !"".equalsIgnoreCase(sEmailId)) {
			RestPasswordResponseBean resetPasswordRespBean = resetPassword(sEmailId);
			if (resetPasswordRespBean != null
					&& resetPasswordRespBean.getAdminBean() != null
					&& resetPasswordRespBean.getNewPassword() != null) {
				isSuccess = true;
				sendPasswordResetEmail(resetPasswordRespBean);
			}
		}
		return isSuccess;
	}

	private void sendPasswordResetEmail(
			RestPasswordResponseBean resetPasswordRespBean) {
		if (resetPasswordRespBean != null
				&& resetPasswordRespBean.getAdminBean() != null
				&& resetPasswordRespBean.getAdminBean().getAdminUserInfoBean() != null
				&& resetPasswordRespBean.getNewPassword() != null
				&& !"".equalsIgnoreCase(resetPasswordRespBean.getNewPassword())) {

			AdminBean adminBean = resetPasswordRespBean.getAdminBean();
			String sNewPassword = resetPasswordRespBean.getNewPassword();
			MailingServiceData mailingServiceData = new MailingServiceData();
			EmailTemplateBean emailTemplate = mailingServiceData
					.getEmailTemplate(Constants.EMAIL_TEMPLATE.NEWPASSWORD);

			String sHtmlTemplate = emailTemplate.getHtmlBody();
			String sTxtTemplate = emailTemplate.getTextBody();

			EmailQueueBean emailQueueBean = new EmailQueueBean();
			emailQueueBean.setEmailSubject(emailTemplate.getEmailSubject());
			emailQueueBean.setFromAddress(emailTemplate.getFromAddress());
			emailQueueBean.setFromAddressName(emailTemplate
					.getFromAddressName());
			emailQueueBean.setToAddress(adminBean.getAdminUserInfoBean()
					.getEmail());
			emailQueueBean.setToAddressName(adminBean.getAdminUserInfoBean()
					.getFirstName()
					+ " "
					+ adminBean.getAdminUserInfoBean().getLastName());
			emailQueueBean.setHtmlBody(sHtmlTemplate);
			emailQueueBean.setTextBody(sTxtTemplate);
			// mark it as sent so that it wont get picked up by email service.
			emailQueueBean.setStatus(Constants.EMAIL_STATUS.SENT.getStatus());

			// We are just creating a record in the database with this action.
			// The new password will be sent separately.
			// This must be changed so that user will have to click link to
			// generate the new password.
			MailCreator dummeEailCreator = new SingleEmailCreator();
			dummeEailCreator.create(emailQueueBean, new EmailScheduleBean());

			// Now here we will be putting the correct password in the email
			// text and
			// send it out directly.
			// This needs to be changed. Warning bells are rining.
			// Lots of potential to fail.
			sTxtTemplate = sTxtTemplate.replaceAll("__NEW__PASSWORD__",
					sNewPassword);
			sHtmlTemplate = sHtmlTemplate.replaceAll("__NEW__PASSWORD__",
					sNewPassword);
			emailQueueBean.setHtmlBody(sHtmlTemplate);
			emailQueueBean.setTextBody(sTxtTemplate);

			emailQueueBean.setStatus(Constants.EMAIL_STATUS.NEW.getStatus());

			// This will actually send the email. Spawning a thread and continue
			// execution.
			Thread quickEmail = new Thread(new QuickMailSendThread(
					emailQueueBean), "Quick Email Password Reset");
			quickEmail.start();
		}

	}

	public RestPasswordResponseBean resetPassword(AdminBean adminBean,
			String sNewPassword) {
		RestPasswordResponseBean resetPasswordRespBean = new RestPasswordResponseBean();
		if (adminBean != null && sNewPassword != null
				&& !"".equalsIgnoreCase(sNewPassword)) {
			AdminData adminData = new AdminData();

			adminData.deactivateOldPassword(adminBean);
			String sPasswordHash = BCrypt.hashpw(sNewPassword,
					BCrypt.gensalt(5));

			RegisterAdminBean regAdminBean = new RegisterAdminBean();
			regAdminBean.setPasswordHash(sPasswordHash);
			regAdminBean.setAdminId(adminBean.getAdminId());
			regAdminBean.setCreateDate(DateSupport.getEpochMillis());
			regAdminBean.setHumanCreateDate(DateSupport.getUTCDateTime());

			Integer iNumOfRecs = adminData.createPassword(regAdminBean);
			if (iNumOfRecs > 0) {
				resetPasswordRespBean.setAdminBean(adminBean);
				resetPasswordRespBean.setNewPassword(sNewPassword);
			}
		}
		return resetPasswordRespBean;
	}
	
	public void createTemporaryContact(AdminBean adminBean, String sTmpEmail)
	{
		if (adminBean != null && !"".equalsIgnoreCase(adminBean.getAdminId())
				&& sTmpEmail != null && !"".equalsIgnoreCase(sTmpEmail)) {
			AdminData adminData = new AdminData();
			adminData.insertTmpContact(adminBean, sTmpEmail);
		}
	}
}
