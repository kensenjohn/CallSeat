package com.gs.data;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.SecurityForgotInfoBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.db.DBDAO;

public class ForgotInfoData {

	private String sourceFile = "ForgotInfoData.java";
	private static final Logger appLogging = LoggerFactory
		.getLogger("AppLogging");

	Configuration applicationConfig = Configuration
		.getInstance(Constants.APPLICATION_PROP);
	
	private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

	public Integer createForgotInfoRecord(SecurityForgotInfoBean securityForgotInfoBean)
	{
		Integer iNumOfRows = 0;
		if(securityForgotInfoBean!=null && securityForgotInfoBean.getActionType()!=null)
		{
			String sQuery = "INSERT INTO GTSECURITYFORGOTINFO ( SECURITYFORGOTINFOID,  FK_ADMINID , " +
					" SECURE_TOKEN_ID , TOKEN_ADMIN_ID_COMBO ,  CREATEDATE , HUMANCREATEDATE , " +
					" IS_USABLE , ACTION_TYPE ) VALUES ( ?,? ,?,? ,?,? ,?,?)";
			ArrayList<Object> aParams = DBDAO.createConstraint(securityForgotInfoBean.getSecurityForgotInfoId(),
					securityForgotInfoBean.getAdminId(), securityForgotInfoBean.getSecureTokenId(),
					securityForgotInfoBean.getTokenAdminIdHash(), securityForgotInfoBean.getCreateDate(),
					securityForgotInfoBean.getHumanCreateDate(), securityForgotInfoBean.isUsable(),
					securityForgotInfoBean.getActionType().getAction() );
			
			iNumOfRows = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB, sourceFile, "createForgotInfoRecord()");
		}
		return iNumOfRows;
	}
	
	public SecurityForgotInfoBean getForgotRecordFromToken(String sSecurityTokenId, Long validDate)
	{
		SecurityForgotInfoBean securityForgotInfoBean = new SecurityForgotInfoBean();
		if(sSecurityTokenId!=null && !"".equalsIgnoreCase(sSecurityTokenId))
		{
			String sQuery = "SELECT * FROM GTSECURITYFORGOTINFO WHERE SECURE_TOKEN_ID = ? AND IS_USABLE = '1' " +
					" AND CREATEDATE >= ? ";
			ArrayList<Object> aParams = DBDAO.createConstraint(sSecurityTokenId ,validDate );
			
			ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(ADMIN_DB, sQuery, aParams, true,
					sourceFile, "getForgotRecordFromToken()");
			
			if(arrResult!=null && !arrResult.isEmpty())
			{
				for(HashMap<String,String> hmForgotInfoRes : arrResult)
				{
					securityForgotInfoBean = new SecurityForgotInfoBean(hmForgotInfoRes);
				}
			}
			
		}
		return securityForgotInfoBean;
	}
	
	public SecurityForgotInfoBean getForgotInfoRecord(String sAdminId, boolean isUsable)
	{
		SecurityForgotInfoBean securityForgotInfoBean = new SecurityForgotInfoBean();
		if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId))
		{
			String sQuery = "SELECT * FROM GTSECURITYFORGOTINFO WHERE FK_ADMINID = ? and IS_USABLE = ?";
			ArrayList<Object> aParams = DBDAO.createConstraint(sAdminId,isUsable?"1":"0");
			
			ArrayList<HashMap<String, String>> arrForgotInfoRes =  DBDAO.getDBData(ADMIN_DB, sQuery, aParams, true, sourceFile, "getForgotInfoRecord()" );
			
			if(arrForgotInfoRes!=null && !arrForgotInfoRes.isEmpty())
			{
				for(HashMap<String, String> hmForgotInfoRes : arrForgotInfoRes )
				{
					securityForgotInfoBean = new SecurityForgotInfoBean(hmForgotInfoRes);
				}
			}
		}
		appLogging.info("Get the security forgotinfo record for admin : " + sAdminId + " Bean : " + securityForgotInfoBean );
		return securityForgotInfoBean;
	}
	
	public Integer deactivateForgotInfoRecord(SecurityForgotInfoBean securityForgotInfoBean)
	{
		Integer iNumOfRecs = 0;
		if(securityForgotInfoBean!=null)
		{
			iNumOfRecs = deactivateForgotInfoRecord(securityForgotInfoBean.getSecurityForgotInfoId());
		}
		return iNumOfRecs;
	}
	
	public Integer deactivateForgotInfoRecord(String sSecurityForgotInfoRecordId)
	{
		appLogging.info("Deactivating security forgotinfo record : " + sSecurityForgotInfoRecordId );
		Integer iNumOfRecs = 0;
		if(sSecurityForgotInfoRecordId!=null && !"".equalsIgnoreCase(sSecurityForgotInfoRecordId))
		{
			String sQuery = "UPDATE GTSECURITYFORGOTINFO SET IS_USABLE = ? WHERE SECURITYFORGOTINFOID = ?";
			ArrayList<Object> aParams = DBDAO.createConstraint("0",sSecurityForgotInfoRecordId);
			
 			iNumOfRecs = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB, sourceFile, "deactivateForgotInfoRecord()");
		}
		return iNumOfRecs;
	}
}
