package com.gs.bean.email;

public class SimpleEmailBean extends EmailObject {

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
	public String getFromAddressName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFromAddressName(String fromAddressName) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getToAddressName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setToAddressName(String toAddressName) {
		// TODO Auto-generated method stub

	}

    @Override
    public String getCcAddress() {
        return this.ccAddress;
    }

    @Override
    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    @Override
    public String getCcAddressName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCcAddressName(String ccAddressName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getBccAddress() {
        return this.bccAddress;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBccAddress(String bccAddress) {
        this.bccAddress = bccAddress;
    }

    @Override
    public String getBccAddressName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBccAddressName(String bccAddressName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
