package com.gs.common;

public class Constants
{

	public static String PROP_FILE_PATH = "/etc/guests/props/";

	public static String DBCONN_PROP = PROP_FILE_PATH + "dbconnections.prop";
	public static String APPLICATION_PROP = PROP_FILE_PATH + "application.prop";

	public static String ADMIN_DB = "admin_db";

	public static String LOG_PATH = "/var/log/guests";
	public static String DB_LOGS = "DBLogging";
	public static String DB_ERROR_LOGS = "DataBaseErrors";
	public static String JSP_LOGS = "JspLogging";
	public static String APP_LOGS = "AppLogging";
	public static String CONFIG_LOGS = "ConfiguLogging";

	public static String DATE_PATTERN = "yyyy-MM-dd hh:mm:ss";
	public static String TIME_PATTERN = "hh mm";

	public static String ROOT_FOLDER = "5ec93f8053404960b7936e8699fc49cc";
	public static String DEFAULT_EVENT_NAME = "Our Day";

	public static String J_RESP_SUCCESS = "success";
	public static String J_RESP_ERR_MSSG = "error_message";
	public static String J_RESP_RESPONSE = "response";

	public enum TWILIO_CALL_STATUS
	{
		QUEUED("queued"), RINGING("ringing"), IN_PROGRESS("in-progress"), COMPLETED("completed"), BUSY(
				"busy"), FAILED("failed"), NO_ANSWER("no-answer"), CANCELED("canceled");

		private String callStatus = "";

		TWILIO_CALL_STATUS(String callStatus)
		{
			this.callStatus = callStatus;
		}

		public String getCallStatus()
		{
			return this.callStatus;
		}
	}

	public static String EMPTY = "";
}
