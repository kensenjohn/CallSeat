package com.gs.bean;

public class UserInfoBean
{
	/*
	 * USERINFOID | varchar(45) | NO | PRI | NULL | | | FIRST_NAME |
	 * varchar(256) | NO | | NULL | | | LAST_NAME | varchar(256) | YES | | NULL
	 * | | | ADDRESS_1 | varchar(1024) | YES | | NULL | | | ADDRESS_2 |
	 * varchar(1024) | YES | | NULL | | | CITY | varchar(1024) | YES | | NULL |
	 * | | STATE | varchar(30) | YES | | NULL | | | COUNTRY | varchar(45) | YES
	 * | | NULL | | | IP_ADDRESS | varchar(1024) | YES | | NULL
	 */

	private String userInfoId = "";
	private String firstName = "";
	private String lastName = "";
	private String address1 = "";
	private String address2 = "";
	private String city = "";
	private String state = "";
	private String country = "";
	private String ipAddress = "";
	private String isTemporary = "1";
	private String deleteRow = "0";
	private Long createDate = 0L;
	private String email = "";
	private String cellPhone = "";
	private String phoneNum = "";
	private String humanCreateDate = "";

	public String getUserInfoId()
	{
		return userInfoId;
	}

	public void setUserInfoId(String userInfoId)
	{
		this.userInfoId = userInfoId;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getAddress1()
	{
		return address1;
	}

	public void setAddress1(String address1)
	{
		this.address1 = address1;
	}

	public String getAddress2()
	{
		return address2;
	}

	public void setAddress2(String address2)
	{
		this.address2 = address2;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getIpAddress()
	{
		return ipAddress;
	}

	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	public String getIsTemporary()
	{
		return isTemporary;
	}

	public void setIsTemporary(String isTemporary)
	{
		this.isTemporary = isTemporary;
	}

	public String getDeleteRow()
	{
		return deleteRow;
	}

	public void setDeleteRow(String deleteRow)
	{
		this.deleteRow = deleteRow;
	}

	public Long getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Long createDate)
	{
		this.createDate = createDate;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getCellPhone()
	{
		return cellPhone;
	}

	public void setCellPhone(String cellPhone)
	{
		this.cellPhone = cellPhone;
	}

	public String getPhoneNum()
	{
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum)
	{
		this.phoneNum = phoneNum;
	}

	public String getHumanCreateDate()
	{
		return humanCreateDate;
	}

	public void setHumanCreateDate(String humanCreateDate)
	{
		this.humanCreateDate = humanCreateDate;
	}

}
