package com.gs.bean;

import java.util.HashMap;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.gs.common.Utility;
import org.json.JSONException;
import org.json.JSONObject;

import com.gs.common.ParseUtil;

public class UserInfoBean {
	/*
	 * USERINFOID | varchar(45) | NO | PRI | NULL | | | FIRST_NAME |
	 * varchar(256) | NO | | NULL | | | LAST_NAME | varchar(256) | YES | | NULL
	 * | | | ADDRESS_1 | varchar(1024) | YES | | NULL | | | ADDRESS_2 |
	 * varchar(1024) | YES | | NULL | | | CITY | varchar(1024) | YES | | NULL |
	 * | | STATE | varchar(30) | YES | | NULL | | | COUNTRY | varchar(45) | YES
	 * | | NULL | | | IP_ADDRESS | varchar(1024) | YES | | NULL
	 */
	private boolean isUserInfoExists = false;
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
    private String humanCellPhone = "";
    private String humanPhoneNum = "";
	private String humanCreateDate = "";
	private String timezone = "";

	public UserInfoBean() {

	}

	public UserInfoBean(HashMap<String, String> hmUserInfo) {
		/*
		 * | USERINFOID | varchar(45) | NO | PRI | NULL | | | FIRST_NAME |
		 * varchar(256) | NO | | NULL | | | LAST_NAME | varchar(256) | YES | |
		 * NULL | | | ADDRESS_1 | varchar(1024) | YES | | NULL | | | ADDRESS_2 |
		 * varchar(1024) | YES | | NULL | | | CITY | varchar(1024) | YES | |
		 * NULL | | | STATE | varchar(30) | YES | | NULL | | | COUNTRY |
		 * varchar(45) | YES | | NULL | | | IP_ADDRESS | varchar(1024) | YES | |
		 * NULL | | | CELL_PHONE | varchar(15) | YES | | NULL | | | PHONE_NUM |
		 * varchar(15) | YES | | NULL | | | EMAIL | varchar(100) | YES | | NULL
		 * | | | IS_TMP | int(1) | NO | | 1 | | | DEL_ROW | int(1) | NO | | 0 |
		 * | | CREATEDATE | bigint(20) | NO | | 0 | | | HUMAN_CREATEDATE |
		 * varchar(45) | YES | | NULL | | | TIMEZONE | varchar(15) | NO | | NULL
		 * | |
		 */
		setUserInfoId(ParseUtil.checkNull(hmUserInfo.get("USERINFOID")));
		this.firstName = ParseUtil.checkNull(hmUserInfo.get("FIRST_NAME"));
		this.lastName = ParseUtil.checkNull(hmUserInfo.get("LAST_NAME"));
		this.address1 = ParseUtil.checkNull(hmUserInfo.get("ADDRESS_1"));
		this.address2 = ParseUtil.checkNull(hmUserInfo.get("ADDRESS_2"));
		this.city = ParseUtil.checkNull(hmUserInfo.get("CITY"));
		this.state = ParseUtil.checkNull(hmUserInfo.get("STATE"));
		this.country = ParseUtil.checkNull(hmUserInfo.get("COUNTRY"));
		this.ipAddress = ParseUtil.checkNull(hmUserInfo.get("IP_ADDRESS"));
		this.isTemporary = ParseUtil.checkNull(hmUserInfo.get("IS_TMP"));
		this.deleteRow = ParseUtil.checkNull(hmUserInfo.get("DEL_ROW"));
		this.createDate = ParseUtil.sToL(hmUserInfo.get("CREATEDATE"));
		this.email = ParseUtil.checkNull(hmUserInfo.get("EMAIL"));
		this.cellPhone = ParseUtil.checkNull(hmUserInfo.get("CELL_PHONE"));
		this.phoneNum = ParseUtil.checkNull(hmUserInfo.get("PHONE_NUM"));
		this.humanCreateDate = ParseUtil.checkNull(hmUserInfo
				.get("HUMAN_CREATEDATE"));
		this.timezone = ParseUtil.checkNull(hmUserInfo.get("TIMEZONE"));


        this.humanCellPhone = getHumanFormattedPhoneNumber(this.cellPhone);
        this.humanPhoneNum = getHumanFormattedPhoneNumber(this.phoneNum);
    }

    public String getHumanFormattedPhoneNumber(String sTmpPhoneNumber)
    {
        String sHumanFormattedPhones = "";
        if(sTmpPhoneNumber!=null && !"".equalsIgnoreCase(sTmpPhoneNumber))
        {
            sHumanFormattedPhones = Utility.getHumanFormattedNumber(sTmpPhoneNumber, "US");
        }
        return sHumanFormattedPhones;
    }

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getUserInfoId() {
		return userInfoId;
	}

	public void setUserInfoId(String userInfoId) {
		if (userInfoId != null && !"".equalsIgnoreCase(userInfoId)) {
			isUserInfoExists = true;
		}
		this.userInfoId = userInfoId;
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

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getIsTemporary() {
		return isTemporary;
	}

	public void setIsTemporary(String isTemporary) {
		this.isTemporary = isTemporary;
	}

	public String getDeleteRow() {
		return deleteRow;
	}

	public void setDeleteRow(String deleteRow) {
		this.deleteRow = deleteRow;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getHumanCreateDate() {
		return humanCreateDate;
	}

	public void setHumanCreateDate(String humanCreateDate) {
		this.humanCreateDate = humanCreateDate;
	}

	public boolean isUserInfoExists() {
		return isUserInfoExists;
	}

	public void setUserInfoExists(boolean isUserInfoExists) {
		this.isUserInfoExists = isUserInfoExists;
	}

    public String getHumanCellPhone() {
        return humanCellPhone;
    }

    public void setHumanCellPhone(String humanCellPhone) {
        this.humanCellPhone = humanCellPhone;
    }

    public String getHumanPhoneNum() {
        return humanPhoneNum;
    }

    public void setHumanPhoneNum(String humanPhoneNum) {
        this.humanPhoneNum = humanPhoneNum;
    }

    public JSONObject toJson() {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("first_name", this.firstName);
			jsonObject.put("last_name", this.lastName);
			jsonObject.put("address1", this.address1);
			jsonObject.put("address2", this.address2);
			jsonObject.put("city", this.city);
			jsonObject.put("state", this.state);
			jsonObject.put("country", this.country);
			jsonObject.put("ip_address", this.ipAddress);
			jsonObject.put("is_tmp", this.isTemporary);
			jsonObject.put("del_row", this.deleteRow);
			jsonObject.put("email", this.email);
			jsonObject.put("cell_phone", this.cellPhone);
			jsonObject.put("phone_num", this.phoneNum);
            jsonObject.put("human_formatted_cell_phone", this.humanCellPhone);
            jsonObject.put("human_formatted_phone_num", this.humanPhoneNum);
			jsonObject.put("time_zone", this.timezone);
			jsonObject.put("userinfo_id", this.userInfoId);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
}
