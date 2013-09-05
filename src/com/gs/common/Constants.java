package com.gs.common;

import java.util.SortedMap;
import java.util.TreeMap;

public class Constants {

	public static String PROP_FILE_PATH = "/etc/guests/props/";

	public static String DBCONN_PROP = PROP_FILE_PATH + "dbconnections.prop";
	public static String APPLICATION_PROP = PROP_FILE_PATH + "application.prop";
    public static String UNSAFE_WORD_FILTER_PROP = PROP_FILE_PATH + "unsafe_word_filter.prop";
	public static String PROCESS_SCHEDULER_PROP = PROP_FILE_PATH
			+ "process_scheduler.prop";
	public static String EMAILER_PROP = PROP_FILE_PATH + "emailer.prop";
    public static String SMS_PROP = PROP_FILE_PATH + "sms.prop";
	public static String ANALYTICS_PROP = PROP_FILE_PATH + "analytics.prop";

	public static String ADMIN_DB = "admin_db";

	public static String LOG_PATH = "/var/log/guests";
	public static String DB_LOGS = "DBLogging";
	public static String SCHEDULER_LOGS = "SchedulerLogging";
	public static String EMAILER_LOGS = "EmailerLogging";
    public static String SMS_LOGS = "SmsLogging";

	public static String DB_ERROR_LOGS = "DataBaseErrors";
	public static String JSP_LOGS = "JspLogging";
	public static String APP_LOGS = "AppLogging";
	public static String CONFIG_LOGS = "ConfiguLogging";

	public static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_PATTERN_TZ = "yyyy-MM-dd HH:mm:ss z";
	public static String PRETTY_DATE_PATTERN_1 = "MM/dd/yyyy hh:mm a z";
	public static String PRETTY_DATE_PATTERN_2 = "MM/dd/yyyy";
	public static String TIME_PATTERN = "hh mm";

	public static String ROOT_FOLDER = "5ec93f8053404960b7936e8699fc49cc";
	public static String DEFAULT_EVENT_NAME = "New Seating";

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
    public static String PROP_VOICE_RECORDING_DOMAIN = "voice_recording_domain";
    public static String PROP_VOICE_RECORDING_FOLDER = "voice_recording_folder";

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
		RSVP("RSVP_TEL"), SEATING("SEATING_TEL"), DEMO_RSVP("DEMO_RSVP_TEL"), DEMO_SEATING(
				"DEMO_SEATING_TEL"), ALL("ALL");

		private String sTask = "";

		EVENT_TASK(String sTask) {
			this.sTask = sTask;
		}

		public String getTask() {
			return this.sTask;
		}
	}

	public enum CALL_TYPE {
		FIRST_REQUEST, RSVP_DIGIT_RESP, DEMO_FIRST_REQUEST, DEMO_GATHER_EVENT_NUM, DEMO_GATHER_SECRET_KEY, DEMO_GATHER_RSVP_NUM,
        DEMO_GATHER_SEATING_NUM, DEMO_ERROR_HANGUP;

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

    public static String PROP_SMS_CREATORL_INIT_DELAY = "sms_creator.initial_delay";
    public static String PROP_SMS_CREATOR_PROC_DELAY = "sms_creator.delay_between_call";

    public static String PROP_SMS_SENDER_INIT_DELAY = "sms_sender.initial_delay";
    public static String PROP_SMS_SENDER_PROC_DELAY = "sms_sender.delay_between_call";

    public static String PROP_EMAIL_CREATORL_INIT_DELAY = "email_creator.initial_delay";
    public static String PROP_EMAIL_CREATOR_PROC_DELAY = "email_creator.delay_between_call";

    public static String PROP_AMAZON_ACCESS_KEY = "amazon_access_key";
	public static String PROP_AMAZON_ACCESS_SECRET = "amazon_secret_key";

    public static String PROP_APPLICATION_DOMAIN = "application_domain";

	public static String PROP_ENABLE_SINGLE_EMAIL_SEND = "enable_single_email_send";
	public static String PROP_ENABLE_BULK_EMAIL_SEND = "enable_bulk_email_send";
    public static String PROP_ENABLE_SMS_CREATOR = "enable_sms_creator";
    public static String PROP_ENABLE_SMS_SENDER = "enable_sms_sender";


    public static String PROP_SMS_SCHEDULE_PICKUPTIME_PADDING = "schedule_sms_pickuptime_padding";
    public static String PROP_SMS_SCHEDULE_SMS_DELAY = "schedule_sms_delay";
    public static String PROP_SMS_SENDER_DELAY = "sms_sender_delay";

    public static String PROP_EMAIL_SCHEDULE_PICKUPTIME_PADDING = "schedule_email_pickuptime_padding";
    public static String PROP_EMAIL_SCHEDULE_EMAIL_DELAY = "schedule_email_delay";
    public static String PROP_EMAIL_SENDER_DELAY = "email_sender_delay";

    public static String PROP_ENABLE_EMAIL_CREATOR = "enable_email_creator";




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
		REGISTRATION("REGISTRATION"),
        NEWPASSWORD("NEWPASSWORD"),
        NEWTELNUMBERPURCHASE("NEWTELNUMBERPURCHASE"),
        RSVP_CONFIRMATION_EMAIL("RSVP_CONFIRMATION"),
        SEATING_CONFIRMATION_EMAIL("SEATING_CONFIRMATION"),
        RSVPRESPONSEDEMO("RSVPRESPONSEDEMO"),
        RSVPRESPONSE("RSVPRESPONSE");

		private String emailTemplate = "";

		EMAIL_TEMPLATE(String emailTemplate) {
			this.emailTemplate = emailTemplate;
		}

		public String getEmailTemplate() {
			return this.emailTemplate;
		}
	}

    public enum SMS_TEMPLATE {
        SMS_RSVP_CONFIRMATION("SMS_RSVP_CONFIRMATION"), SMS_SEATING_CONFIRMATION("SMS_SEATING_CONFIRMATION");

        private String smsTemplate = "";

        SMS_TEMPLATE(String smsTemplate) {
            this.smsTemplate = smsTemplate;
        }

        public String getSmsTemplate() {
            return this.smsTemplate;
        }
    }

	public final static String PROP_ENVIRONMENT = "environment";

	public enum ENVIRONMENT {
		VIRTUAL_MACHINE("vm"), SANDBOX("sandbox"), ALPHA("alpha"), BETA("beta"), PROD(
				"prod");

		private String environment = "";

		ENVIRONMENT(String environment) {
			this.environment = environment;
		}

		public String getEnv() {
			return this.environment;
		}
	}

	public enum BILLING_RESPONSE_CODES {
		GENERAL_ERROR("GEN_ERROR"), INPUT_VALIDATION_ERROR("INP_VALID_ERROR"), SUCCESS(
				"SUCCESS");

		private String billingRespCodes = "";

		BILLING_RESPONSE_CODES(String billingRespCodes) {
			this.billingRespCodes = billingRespCodes;
		}

		public String getCode() {
			return this.billingRespCodes;
		}
	}

	public enum STRIPE_ENVIRONMENT {
		TEST("TEST"), LIVE("LIVE"), DEFAULT("TEST");

		private String sStripeEnv = "";

		STRIPE_ENVIRONMENT(String sStripeEnv) {
			this.sStripeEnv = sStripeEnv;
		}

		public String getStripeEnv() {
			return sStripeEnv;
		}
	}

	public final static String PROP_PAYMENT_CHANNEL = "payment_channel";
	public final static String PROP_STRIPE_ENV = "stripe_env";
	public final static String PROP_STRIPE_TEST_PUBLISHABLE_KEY = "stripe_test_publishable_key";
	public final static String PROP_STRIPE_LIVE_PUBLISHABLE_KEY = "stripe_live_publishable_key";
	public final static String PROP_STRIPE_TEST_SECRET_KEY = "stripe_test_secret_key";
	public final static String PROP_STRIPE_LIVE_SECRET_KEY = "stripe_live_secret_key";

    public enum PROP_UNSAFE_WORD_FILTER {
        POTENTIAL_UNSAFE_WORD("potential_unsafe_words"), DEFINITE_UNSAFE_WORD("definite_unsafe_words"), DELIMITER("delimiter"),
        IS_FILTER_ENABLED("is_filter_enabled","true"),RELOAD_FILTER_PARAM_INTERVAL_IN_MINS("reload_filter_param_interval_in_minutes","15");
        private String unsafeWordFilter = "";
        private String defaultValue = "";

        PROP_UNSAFE_WORD_FILTER(String sUnsafeWordFilter) {
            this.unsafeWordFilter =sUnsafeWordFilter;
        }
        PROP_UNSAFE_WORD_FILTER(String sUnsafeWordFilter, String sDefaultValue) {
            this.unsafeWordFilter = sUnsafeWordFilter;
            this.defaultValue = sDefaultValue;
        }
        public String getUnsafeWordFilterPropKey() {
            return unsafeWordFilter;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }
	
	public enum ANALYTICS_KEYS {
		GOOGLE_TRACKING_ID("google_tracking_id");
		
		private String sAnalyticKey = "";
		ANALYTICS_KEYS(String sAnalyticKey){
			this.sAnalyticKey = sAnalyticKey;
		}
		
		public String getKey()
		{
			return this.sAnalyticKey;
		}
	}
	
	public enum FORGOT_INFO_ACTION
	{
		PASSWORD("PASSWORD"),
		USERNAME("USERNAME");
		
		private String forgotInfoAction = "";
		FORGOT_INFO_ACTION(String forgotInfoAction)
		{
			this.forgotInfoAction = forgotInfoAction;
		}
		
		public String getAction()
		{
			return this.forgotInfoAction;
		}
	}
	
	public static String PRODUCT_NAME="product_name";
	public static String DOMAIN="domain";

    public static String PROP_PRODUCT_PHONE = "product_phone";
    public static String PROP_PRODUCT_ADDRESS = "product_address";

    public enum US_STATES
    {
        Alabama ("Alabama","AL"),
        Alaska ("Alaska","AK"),
        Arizona ("Arizona","AZ"),
        Arkansas ("Arkansas","AR"),
        California ("California","CA"),
        Colorado ("Colorado","CO"),
        Connecticut ("Connecticut","CT"),
        Delaware ("Delaware","DE"),
        District_of_Columbia ("District of Columbia","DC"),
        Florida ("Florida","FL"),
        Georgia ("Georgia","GA"),
        Hawaii ("Hawaii","HI"),
        Idaho ("Idaho","ID"),
        Illinois ("Illinois","IL"),
        Indiana ("Indiana","IN"),
        Iowa ("Iowa","IA"),
        Kansas ("Kansas","KS"),
        Kentucky ("Kentucky","KY"),
        Louisiana ("Louisiana","LA"),
        Maine ("Maine","ME"),
        Maryland ("Maryland","MD"),
        Massachusetts ("Massachusetts","MA"),
        Michigan ("Michigan","MI"),
        Minnesota ("Minnesota","MN"),
        Mississippi ("Mississippi","MS"),
        Missouri ("Missouri","MO"),
        Montana ("Montana","MT"),
        Nebraska ("Nebraska","NE"),
        Nevada ("Nevada","NV"),
        New_Hampshire ("New Hampshire","NH"),
        New_Jersey ("New Jersey","NJ"),
        New_Mexico ("New Mexico","NM"),
        New_York ("New York","NY"),
        North_Carolina ("North Carolina","NC"),
        North_Dakota ("North Dakota","ND"),
        Ohio ("Ohio","OH"),
        Oklahoma ("Oklahoma","OK"),
        Oregon ("Oregon","OR"),
        Pennsylvania ("Pennsylvania","PA"),
        Rhode_Island ("Rhode Island","RI"),
        South_Carolina ("South Carolina","SC"),
        South_Dakota ("South Dakota","SD"),
        Tennessee ("Tennessee","TN"),
        Texas ("Texas","TX"),
        Utah ("Utah","UT"),
        Vermont ("Vermont","VT"),
        Virginia ("Virginia","VA"),
        Washington ("Washington","WA"),
        West_Virginia ("West Virginia","WV"),
        Wisconsin ("Wisconsin","WI"),
        Wyoming ("Wyoming","WY");

        private String fullName = "";
        private String shortForm = "";
        US_STATES(String fullName, String shortForm)
        {
            this.fullName = fullName;
            this.shortForm = shortForm;
        }

        public String getFullName()
        {
            return this.fullName;
        }

        public String getShortForm()
        {
            return this.shortForm;
        }

    }

    public static SortedMap<String, US_STATES> SORTED_US_STATES()
    {
        SortedMap<String, US_STATES> mapStates = new TreeMap<String,US_STATES>();
        for (US_STATES usState : US_STATES.values()) {
            mapStates.put(usState.getFullName(), usState);
        }

        return mapStates;
    }

    public enum TAX_TYPE
    {
        SALES_TAX("SALES_TAX");

        private String taxType = "";
        TAX_TYPE(String taxType)
        {
            this.taxType = taxType;
        }


        public String getTaxType()
        {
            return this.taxType;
        }

    }

    public enum EVENT_FEATURES
    {
        SEATING_CALL_FORWARD_NUMBER("SEATING_CALL_FORWARD_NUMBER"),
        RSVP_CALL_FORWARD_NUMBER("RSVP_CALL_FORWARD_NUMBER"),
        SEATING_SMS_GUEST_AFTER_CALL("SEATING_SMS_GUEST_AFTER_CALL"),
        SEATING_EMAIL_GUEST_AFTER_CALL("SEATING_EMAIL_GUEST_AFTER_CALL"),
        RSVP_SMS_CONFIRMATION("RSVP_SMS_CONFIRMATION"),
        RSVP_EMAIL_CONFIRMATION("RSVP_EMAIL_CONFIRMATION"),
        DEMO_TOTAL_CALL_MINUTES("DEMO_TOTAL_CALL_MINUTES"),
        DEMO_TOTAL_TEXT_MESSAGES("DEMO_TOTAL_TEXT_MESSAGES"),
        PREMIUM_TOTAL_CALL_MINUTES("PREMIUM_TOTAL_CALL_MINUTES"),
        PREMIUM_TOTAL_TEXT_MESSAGES("PREMIUM_TOTAL_TEXT_MESSAGES"),
        DEMO_FINAL_CALL_MINUTES_USED("DEMO_FINAL_CALL_MINUTES_USED"),
        DEMO_FINAL_TEXT_MESSAGES_SENT("DEMO_FINAL_TEXT_MESSAGES_SENT"),
        PREMIUM_FINAL_CALL_MINUTES_USED("PREMIUM_FINAL_CALL_MINUTES_USED"),
        PREMIUM_FINAL_TEXT_MESSAGES_SENT("PREMIUM_FINAL_TEXT_MESSAGES_SENT"),
        USAGE_LIMIT_REACHED_ACTION("USAGE_LIMIT_REACHED_ACTION"),
        SEATINGPLAN_TELNUMBER_TYPE("SEATINGPLAN_TELNUMBER_TYPE");

        private String eventFeature = "";
        EVENT_FEATURES(String eventFeature)
        {
            this.eventFeature = eventFeature;
        }

        public String getEventFeature()
        {
            return this.eventFeature;
        }
    }

    public enum TIME_UNIT
    {
        SECONDS,
        MINUTES,
        HOURS,
        DAYS,
        MONTHS,
        YEARS;
    }

    public enum SCHEDULER_STATUS
    {
        NEW_SCHEDULE("NEW_SCHEDULE"),
        PICKED_TO_PROCESS("PICKED_TO_PROCESS"),
        COMPLETE("COMPLETE"),
        ERROR("ERROR");

        private String schedulerStatus = "";
        SCHEDULER_STATUS( String schedulerStatus )
        {
            this.schedulerStatus = schedulerStatus;
        }

        public String getSchedulerStatus()
        {
            return this.schedulerStatus;
        }
    }

    public enum SCHEDULE_PICKUP_TYPE
    {
        NEW_RECORDS("NEW_RECORDS"),
        OLD_RECORDS("OLD_RECORDS"),
        CURRENT_RECORD("CURRENT_RECORDS");

        private String scedhuledPickupType = "";
        SCHEDULE_PICKUP_TYPE(String scedhuledPickupType)
        {
            this.scedhuledPickupType = scedhuledPickupType;
        }

        public String getScheduledPickupType()
        {
            return this.scedhuledPickupType;
        }

    }

    public enum USAGE_LIMIT_REACHED_ACTION
    {
        AUTO_EXTEND("AUTO_EXTEND"),
        STOP_USAGE("STOP_USAGE");

        private String action = "";
        USAGE_LIMIT_REACHED_ACTION(String action)
        {
            this.action =action;
        }

        public String getAction()
        {
            return this.action;
        }

    }

    public enum TELNUMBER_TYPE
    {
        DEMO("DEMO"),
        PREMIUM("PREMIUM");

        private String type = "";
        TELNUMBER_TYPE(String type)
        {
            this.type = type;
        }

        public String getType()
        {
            return this.type;
        }

    }

    public static Integer MAX_SMS_LENGTH = 160;

    public enum RSVP_WEB_PARAM {
        LINK_ID("l"), RESPONSE("response"), RSVP_NUM("rsvp_num"), COMMENTS("comments");

        private String param = Constants.EMPTY;
        RSVP_WEB_PARAM(String param) {
            this.param = param;
        }
        public String getParam() {
            return this.param;
        }
    }

    public enum GUEST_WEB_RESPONSE_TYPE {
        NONE,RSVP;
    }

    public enum GUEST_WEB_RESPONSE_STATUS {
        NEW,COMPLETED_RESPONSE, ERROR;
    }

    public static final int SIZE_OF_RSVP_LINK_ID = 10;

    public enum GROUP_NAME{
        SUPERUSER,HOST,EVENT_MANAGER,GUEST;
    }

    public enum API_KEY_TYPE{
        LIVE_KEY,TEST_KEY;
    }
}
