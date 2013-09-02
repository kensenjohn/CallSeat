package com.gs.bean.email;

import java.util.HashMap;

import com.gs.common.ParseUtil;

public class EmailQueueBean extends EmailObject {
	private String emailQueueId = "";

	public EmailQueueBean(HashMap<String, String> hmEmailQueue) {
		this.emailQueueId = ParseUtil.checkNull(hmEmailQueue
				.get("EMAILQUEUEID"));
		this.fromAddress = ParseUtil
				.checkNull(hmEmailQueue.get("FROM_ADDRESS"));
		this.fromAddressName = ParseUtil.checkNull(hmEmailQueue
				.get("FROM_ADDRESS_NAME"));
		this.toAddress = ParseUtil.checkNull(hmEmailQueue.get("TO_ADDRESS"));
		this.toAddressName = ParseUtil.checkNull(hmEmailQueue
				.get("TO_ADDRESS_NAME"));
		this.emailSubject = ParseUtil.checkNull(hmEmailQueue
				.get("EMAIL_SUBJECT"));
		this.htmlBody = ParseUtil.checkNull(hmEmailQueue.get("HTML_BODY"));
		this.textBody = ParseUtil.checkNull(hmEmailQueue.get("TEXT_BODY"));
		this.status = ParseUtil.checkNull(hmEmailQueue.get("STATUS"));
	}

	public EmailQueueBean() {
		// TODO Auto-generated constructor stub
	}

	public String getEmailQueueId() {
		return emailQueueId;
	}

	public void setEmailQueueId(String emailQueueId) {
		this.emailQueueId = emailQueueId;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getHtmlBody() {
		return htmlBody;
	}

	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
	}

	public String getTextBody() {
		return textBody;
	}

	public void setTextBody(String textBody) {
		this.textBody = textBody;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EmailQueueBean [emailQueueId:").append(emailQueueId)
				.append(", To: ").append(toAddress).append(", Sub: ")
				.append(emailSubject).append(", Html Body: ")
                .append(htmlBody).append("]");
		return builder.toString();
	}

	@Override
	public String getFromAddressName() {
		return this.fromAddressName;
	}

	@Override
	public void setFromAddressName(String fromAddressName) {
		this.fromAddressName = fromAddressName;
	}

	@Override
	public String getToAddressName() {
		return this.toAddressName;
	}

	@Override
	public void setToAddressName(String toAddressName) {
		this.toAddressName = toAddressName;
	}
}
