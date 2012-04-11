package com.gs.bean.email;

public abstract class EmailObject {

	protected String fromAddress = "";
	protected String fromAddressName = "";
	protected String toAddress = "";
	protected String toAddressName = "";
	protected String emailSubject = "";
	protected String htmlBody = "";
	protected String textBody = "";
	protected String status = "";

	public abstract String getFromAddress();

	public abstract void setFromAddress(String fromAddress);

	public abstract String getFromAddressName();

	public abstract void setFromAddressName(String fromAddressName);

	public abstract String getToAddress();

	public abstract void setToAddress(String toAddressName);

	public abstract String getToAddressName();

	public abstract void setToAddressName(String toAddressName);

	public abstract String getEmailSubject();

	public abstract void setEmailSubject(String emailSubject);

	public abstract String getHtmlBody();

	public abstract void setHtmlBody(String htmlBody);

	public abstract String getTextBody();

	public abstract void setTextBody(String textBody);

	public abstract String getStatus();

	public abstract void setStatus(String status);

}
