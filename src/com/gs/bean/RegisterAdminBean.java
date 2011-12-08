package com.gs.bean;

public class RegisterAdminBean {

	private String adminId = "";
	private String email = "";
	private String firstName = "";
	private String lastName = "";
	private String password = "";
	private String passwordConf = "";
	private String passwordHash = "";
	private Long createDate = 0L;
	private String humanCreateDate = "";

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConf() {
		return passwordConf;
	}

	public void setPasswordConf(String passwordConf) {
		this.passwordConf = passwordConf;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public String getHumanCreateDate() {
		return humanCreateDate;
	}

	public void setHumanCreateDate(String humanCreateDate) {
		this.humanCreateDate = humanCreateDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegisterAdminBean [adminId=").append(adminId)
				.append(", email=").append(email).append(", firstName=")
				.append(firstName).append(", lastName=").append(lastName)
				.append(", password=").append(password)
				.append(", passwordConf=").append(passwordConf)
				.append(", passwordHash=").append(passwordHash)
				.append(", createDate=").append(createDate)
				.append(", humanCreateDate=").append(humanCreateDate)
				.append("]");
		return builder.toString();
	}

}
