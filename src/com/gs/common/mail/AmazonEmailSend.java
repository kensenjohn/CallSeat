package com.gs.common.mail;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.gs.bean.email.EmailObject;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ExceptionHandler;

public class AmazonEmailSend implements MailSender {

	public AmazonEmailSend() {
		emailerLogging.info("Amazon email send");
	}

	private Logger emailerLogging = LoggerFactory
			.getLogger(Constants.EMAILER_LOGS);

	private Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);

	private String AMAZON_ACCESS_KEY = applicationConfig
			.get(Constants.PROP_AMAZON_ACCESS_KEY);
	private String AMAZON_ACCESS_SECRET = applicationConfig
			.get(Constants.PROP_AMAZON_ACCESS_SECRET);

	@Override
	public boolean send(EmailObject emailObject) {
		boolean isSuccess = true;
		if (emailObject != null) {
            emailerLogging.info("Send invoked ");
            emailerLogging.debug("The email Object to send : " + emailObject);
			SendEmailRequest request = new SendEmailRequest()
					.withSource(emailObject.getFromAddress());

			List<String> toAddresses = new ArrayList<String>();
			toAddresses.add(emailObject.getToAddress());
			Destination dest = new Destination().withToAddresses(toAddresses);
			request.setDestination(dest);

			Content subjContent = new Content().withData(emailObject
					.getEmailSubject());
			Message msg = new Message().withSubject(subjContent);

			// Include a body in both text and HTML formats
			Content textContent = new Content().withData(emailObject
					.getTextBody());
			Content htmlContent = new Content().withData(emailObject
					.getHtmlBody());
			Body body = new Body().withHtml(htmlContent).withText(textContent);
			msg.setBody(body);

			request.setMessage(msg);

			// Set AWS access credentials
			AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(
					new BasicAWSCredentials(AMAZON_ACCESS_KEY,
							AMAZON_ACCESS_SECRET));

			emailerLogging.info("AWS Amazon Client : " + client);

			// Call Amazon SES to send the message
			try {
				SendEmailResult sendEmailResult = client.sendEmail(request);
				emailerLogging.info("After send result : "
						+ sendEmailResult.getMessageId() + " - "
						+ sendEmailResult);
			} catch (AmazonClientException e) {
				isSuccess = false;
				emailerLogging.error("AmazonClientException "
						+ emailObject.getToAddress() + " "
						+ emailObject.getFromAddress() + " "
						+ emailObject.getEmailSubject() + "\n"
						+ ExceptionHandler.getStackTrace(e));
			} catch (Exception e) {
				isSuccess = false;
				emailerLogging.error("Exception " + emailObject.getToAddress()
						+ " " + emailObject.getEmailSubject() + "\n"
						+ ExceptionHandler.getStackTrace(e));
			}
		}  else {
            emailerLogging.error("Email Object was null ");
        }
		return isSuccess;
	}
}
