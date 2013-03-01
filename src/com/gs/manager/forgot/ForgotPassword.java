package com.gs.manager.forgot;

import com.gs.bean.email.EmailScheduleBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.AdminBean;
import com.gs.bean.RegisterAdminBean;
import com.gs.bean.SecurityForgotInfoBean;
import com.gs.bean.email.EmailQueueBean;
import com.gs.bean.email.EmailTemplateBean;
import com.gs.common.BCrypt;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.DateSupport;
import com.gs.common.ParseUtil;
import com.gs.common.Utility;
import com.gs.common.mail.MailCreator;
import com.gs.common.mail.MailingServiceData;
import com.gs.common.mail.QuickMailSendThread;
import com.gs.common.mail.SingleEmailCreator;
import com.gs.data.ForgotInfoData;
import com.gs.manager.AdminManager;

public class ForgotPassword implements ForgotInfoManager {

	Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
	private static final Logger appLogging = LoggerFactory.getLogger("AppLogging");
	private String sEmailId = "";
	
	public ForgotPassword()
	{
		
	}
	public ForgotPassword(String sEmailId)
	{
		this.sEmailId = sEmailId;
	}
	@Override
	public boolean createUserRequest() {
		
		boolean isRequestSuccess = false;
		appLogging.info("Password Reset request for "+ ParseUtil.checkNull(this.sEmailId) + " recieved.");
		if(this.sEmailId!=null && !"".equalsIgnoreCase(this.sEmailId))
		{
			RegisterAdminBean regAdminBean = new RegisterAdminBean();
			regAdminBean.setEmail(this.sEmailId);
			
			AdminManager adminManager = new AdminManager();
			AdminBean adminBean = adminManager.getAdminBeanFromEmail(regAdminBean); 
			
			if(adminBean!=null && adminBean.getAdminId()!=null && !"".equalsIgnoreCase(adminBean.getAdminId()))
			{
				SecurityForgotInfoBean secForgInfoBean = new SecurityForgotInfoBean();
				secForgInfoBean.setSecurityForgotInfoId(Utility.getNewGuid());
				secForgInfoBean.setAdminId(adminBean.getAdminId());
				
				String sSecureTokenId = Utility.getNewGuid();
				String sTokenAdminCombo = sSecureTokenId + adminBean.getAdminId();
				
				String sTokenAdminComboHash =  BCrypt.hashpw(sTokenAdminCombo,
						BCrypt.gensalt(4));
				
				//secForgInfoBean.setSecureTokenIdHash(sSecureTokenId);
				secForgInfoBean.setTokenAdminIdHash(sTokenAdminComboHash);
				
				secForgInfoBean.setSecureTokenId(sSecureTokenId);
				secForgInfoBean.setTokenAdminId(sTokenAdminCombo);
				
				secForgInfoBean.setCreateDate(DateSupport.getEpochMillis());
				secForgInfoBean.setHumanCreateDate(DateSupport.getUTCDateTime());
				
				secForgInfoBean.setUsable(true);
				secForgInfoBean.setActionType(Constants.FORGOT_INFO_ACTION.PASSWORD);
				
				boolean isSuccess = createChangePasswordRecord(secForgInfoBean);
				
				if(isSuccess)
				{
					appLogging.info("Password Reset request : The request was created successfully "  + this.sEmailId);
					boolean isSendEmailSuccess = sendPasswordResetEmail(secForgInfoBean, adminBean);
					
					if(isSendEmailSuccess)
					{
						appLogging.info("Password Reset request : Success sending email : " + this.sEmailId);
						isRequestSuccess = true;
					}
					else
					{
						appLogging.info("Password Reset request : Sending email failed : " + this.sEmailId);
					}
				}
				else
				{
					appLogging.info("Password Reset request : The request failed for " + this.sEmailId);
				}
			}
			else
			{
				appLogging.info("Password Reset request : Admin account does not exist for "  + this.sEmailId);
			}
		}
		else
		{
			appLogging.info("Password Reset request : Invalid email address ");
		}
		return isRequestSuccess;
	}

	@Override
	public boolean processUserResponse(SecurityForgotInfoBean securityForgotInfoBean) {
		
		boolean isSuccess = false;
		if(securityForgotInfoBean!=null)
		{
			ForgotInfoData forgotInfoData = new ForgotInfoData();
			forgotInfoData.deactivateForgotInfoRecord(securityForgotInfoBean);
			appLogging.info("Password Reset request : deactivated success : "  + securityForgotInfoBean.getSecurityForgotInfoId());
			isSuccess = true;
		}
		else
		{
			appLogging.info("Password Reset request : Deactivate has failed : "  + securityForgotInfoBean);
			
		}
		return isSuccess;
	}
	
	
	private boolean createChangePasswordRecord(SecurityForgotInfoBean newSecurityForgotInfoBean)
	{
		boolean isSuccess = false;
		if(newSecurityForgotInfoBean!=null && newSecurityForgotInfoBean.getActionType()!=null)
		{
			ForgotInfoData forgotInfoData = new ForgotInfoData();
			SecurityForgotInfoBean dbSecurityForgotInfoBean = forgotInfoData.getForgotInfoRecord(newSecurityForgotInfoBean.getAdminId(), true);
			
			if(dbSecurityForgotInfoBean!=null && dbSecurityForgotInfoBean.getActionType()!=null)
			{
				// deactivate old request.
				forgotInfoData.deactivateForgotInfoRecord(dbSecurityForgotInfoBean);
				appLogging.info("Password Reset request : An old active request exists and it is deactivated"  + this.sEmailId);
			}
			
			Integer iNumOfRecords = forgotInfoData.createForgotInfoRecord(newSecurityForgotInfoBean);
			if(iNumOfRecords > 0 )
			{
				isSuccess = true;
			}
		}
		return isSuccess;
	}
	
	private boolean sendPasswordResetEmail(SecurityForgotInfoBean secForgInfoBean, AdminBean adminBean)
	{
		boolean isSuccess = false;
		if(secForgInfoBean!=null)
		{
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
			dummeEailCreator.create(emailQueueBean , new EmailScheduleBean());

			// Now here we will be putting the correct password in the email
			// text and
			// send it out directly.
			// This needs to be changed. Warning bells are rining.
			// Lots of potential to fail.
			if(adminBean.getAdminUserInfoBean()!=null)
			{
				sTxtTemplate = sTxtTemplate.replaceAll("__FIRST_NAME__",
						adminBean.getAdminUserInfoBean().getFirstName());
				sHtmlTemplate = sHtmlTemplate.replaceAll("__FIRST_NAME__",
						adminBean.getAdminUserInfoBean().getFirstName());
			}
			else
			{
				sTxtTemplate = sTxtTemplate.replaceAll("__FIRST_NAME__",
						"");
				sHtmlTemplate = sHtmlTemplate.replaceAll("__FIRST_NAME__",
						"");
			}
			String sResetDomain = ParseUtil.checkNull(applicationConfig.get(Constants.DOMAIN));
			if(sResetDomain!=null && !"".equalsIgnoreCase(sResetDomain))
			{
				String sResetLink = "https://" + sResetDomain +
					"/web/com/gs/common/forgot.jsp?lotophagi="+secForgInfoBean.getSecureTokenId();
				sTxtTemplate = sTxtTemplate.replaceAll("__NEW_PASSWORD_RESET_LINK__",
						ParseUtil.checkNull(sResetLink));
				sHtmlTemplate = sHtmlTemplate.replaceAll("__NEW_PASSWORD_RESET_LINK__",
						ParseUtil.checkNull(sResetLink));
				
				String sProductName = ParseUtil.checkNull(applicationConfig.get(Constants.PRODUCT_NAME));
				sTxtTemplate = sTxtTemplate.replaceAll("__PRODUCT_NAME__",
						ParseUtil.checkNull(sProductName));
				sHtmlTemplate = sHtmlTemplate.replaceAll("__PRODUCT_NAME__",
						ParseUtil.checkNull(sProductName));
				
				
				
				emailQueueBean.setHtmlBody(sHtmlTemplate);
				emailQueueBean.setTextBody(sTxtTemplate);

				emailQueueBean.setStatus(Constants.EMAIL_STATUS.NEW.getStatus());

				// This will actually send the email. Spawning a thread and continue
				// execution.
				Thread quickEmail = new Thread(new QuickMailSendThread(
						emailQueueBean), "Quick Email Password Reset");
				quickEmail.start();
				isSuccess = true;
			}
		}
		return isSuccess;
	}
	@Override
	public SecurityForgotInfoBean identifyUserResponse(String sSecurityTokenId) {
		SecurityForgotInfoBean securityForgInfoBean = new SecurityForgotInfoBean();
		if(sSecurityTokenId!=null && !"".equalsIgnoreCase(sSecurityTokenId))
		{
			Long currentTime = DateSupport.getEpochMillis();
			Long pwdValidityTime = DateSupport.subtractHours(currentTime, 24);
			ForgotInfoData forgotInfoData = new ForgotInfoData();
			securityForgInfoBean = forgotInfoData.getForgotRecordFromToken(sSecurityTokenId, pwdValidityTime);
		}
		return securityForgInfoBean;
	}

}
