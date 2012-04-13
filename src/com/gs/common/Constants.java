package com.gs.common;

public class Constants {

	public static String PROP_FILE_PATH = "/etc/guests/props/";

	public static String DBCONN_PROP = PROP_FILE_PATH + "dbconnections.prop";
	public static String APPLICATION_PROP = PROP_FILE_PATH + "application.prop";
	public static String PROCESS_SCHEDULER_PROP = PROP_FILE_PATH
			+ "process_scheduler.prop";
	public static String EMAILER_PROP = PROP_FILE_PATH + "emailer.prop";

	public static String ADMIN_DB = "admin_db";

	public static String LOG_PATH = "/var/log/guests";
	public static String DB_LOGS = "DBLogging";
	public static String SCHEDULER_LOGS = "SchedulerLogging";
	public static String EMAILER_LOGS = "EmailerLogging";

	public static String DB_ERROR_LOGS = "DataBaseErrors";
	public static String JSP_LOGS = "JspLogging";
	public static String APP_LOGS = "AppLogging";
	public static String CONFIG_LOGS = "ConfiguLogging";

	public static String DATE_PATTERN = "yyyy-MM-dd hh:mm:ss";
	public static String PRETTY_DATE_PATTERN_1 = "MM/dd/yyyy hh:mm z";
	public static String PRETTY_DATE_PATTERN_2 = "MM/dd/yyyy";
	public static String TIME_PATTERN = "hh mm";

	public static String ROOT_FOLDER = "5ec93f8053404960b7936e8699fc49cc";
	public static String DEFAULT_EVENT_NAME = "Our Day";

	public static String J_RESP_SUCCESS = "success";
	public static String J_RESP_ERR_MSSG = "error_message";
	public static String J_RESP_RESPONSE = "response";

	public static String DEFAULT_TIMEZONE = "UTC";

	public static String COOKIE_APP_USERID = "c_a_ui";
	public static String USER_SESSION = "user_session";

	public enum TWILIO_CALL_STATUS {
		QUEUED("queued"), RINGING("ringing"), IN_PROGRESS("in-progress"), COMPLETED(
				"completed"), BUSY("busy"), FAILED("failed"), NO_ANSWER(
				"no-answer"), CANCELED("canceled");

		private String callStatus = "";

		TWILIO_CALL_STATUS(String callStatus) {
			this.callStatus = callStatus;
		}

		public String getCallStatus() {
			return this.callStatus;
		}
	}

	public static String EMPTY = "";

	public static String PROP_CALL_SERVICE = "call_service";
	public static String PROP_SMS_SERVICE = "sms_service";

	public static String PROP_TWILIO_VOICE = "twilio_voice";

	public enum CALL_SERVICE {
		TWILIO("twilio");

		private String callService = "";

		CALL_SERVICE(String callService) {
			this.callService = callService;
		}

		public String getCallService() {
			return this.callService;
		}

	}

	public enum SMS_SERVICE {
		TWILIO("twilio");

		private String smsService = "";

		SMS_SERVICE(String smsService) {
			this.smsService = smsService;
		}

		public String getSmsService() {
			return this.smsService;
		}

	}

	public enum EVENT_TASK {
		RSVP("RSVP_TEL"), SEATING("SEATING_TEL"), ALL("ALL");

		private String sTask = "";

		EVENT_TASK(String sTask) {
			this.sTask = sTask;
		}

		public String getTask() {
			return this.sTask;
		}
	}

	public enum CALL_TYPE {
		FIRST_REQUEST, RSVP_DIGIT_RESP;

	}

	public enum RSVP_STATUS {
		RSVP_UPDATE_SUCCESS, RSVP_UPDATE_FAIL, RSVP_EXCEED_TOTAL_SEATS;
	}

	public enum PASSWORD_STATUS {
		ACTIVE("act"), OLD("old"), DELETED("del");

		private String passwordStatus = "";

		PASSWORD_STATUS(String passwordStatus) {
			this.passwordStatus = passwordStatus;
		}

		public String getStatus() {
			return this.passwordStatus;
		}
	}

	public static String PROP_TWILIO_ACCOUNT_SID = "twilio_account_sid";
	public static String PROP_TWILIO_ACCOUNT_TOKEN = "twilio_auth_token";
	public static String PROP_TWILIO_REST_API_VERSION = "twilio_rest_api_version";
	public static String PROP_TWILIO_API_DOMAIN = "twilio_api_domain";

	public static String PROP_STANNDARD_MAIL_INIT_DELAY = "standard_mail.initial_delay";
	public static String PROP_STANNDARD_MAIL_PROC_DELAY = "standard_mail.delay_between_call";

	public static String PROP_AMAZON_ACCESS_KEY = "amazon_access_key";
	public static String PROP_AMAZON_ACCESS_SECRET = "amazon_secret_key";

	public static String PROP_ENABLE_SINGLE_EMAIL_SEND = "enable_single_email_send";
	public static String PROP_ENABLE_BULK_EMAIL_SEND = "enable_bulk_email_send";

	public static String OWSAPI_POLICY_FILE = "owsapi_policy";

	public enum EMAIL_STATUS {
		NEW("NEW"), PICKED_TO_SEND("PICKED"), ERROR("ERROR"), SENT("SENT");

		private String emailStatus = "";

		EMAIL_STATUS(String emailStatus) {
			this.emailStatus = emailStatus;
		}

		public String getStatus() {
			return this.emailStatus;
		}
	}

	public enum EMAIL_TEMPLATE {
		REGISTRATION("REGISTRATION"), NEWPASSWORD("NEWPASSWORD");

		private String emailTemplate = "";

		EMAIL_TEMPLATE(String emailTemplate) {
			this.emailTemplate = emailTemplate;
		}

		public String getEmailTemplate() {
			return this.emailTemplate;
		}
	}

}
