package com.gs.bean;

public class FolderBean
{
	/*
	 * create table GTFOLDER ( FOLDERID VARCHAR(45) NOT NULL, FOLDERNAME
	 * VARCHAR(1024), PARENT_FOLDERID VARCHAR(45), IS_TMP INT(1) NOT NULL
	 * DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT 0, CREATEDATE TIMESTAMP NOT
	 * NULL DEFAULT '1980-01-01 00:00:00', PRIMARY KEY (FOLDERID) ) ENGINE =
	 * MyISAM DEFAULT CHARSET = utf8;
	 */

	private String folderId = "";
	private String folderName = "";
	private String parentFolderId = "";
	private String isTmp = "";
	private String delRow = "";
	private String folderCreateDate = "";

	public String getFolderId()
	{
		return folderId;
	}

	public void setFolderId(String folderId)
	{
		this.folderId = folderId;
	}

	public String getFolderName()
	{
		return folderName;
	}

	public void setFolderName(String folderName)
	{
		this.folderName = folderName;
	}

	public String getParentFolderId()
	{
		return parentFolderId;
	}

	public void setParentFolderId(String parentFolderId)
	{
		this.parentFolderId = parentFolderId;
	}

	public String getIsTmp()
	{
		return isTmp;
	}

	public void setIsTmp(String isTmp)
	{
		this.isTmp = isTmp;
	}

	public String getDelRow()
	{
		return delRow;
	}

	public void setDelRow(String delRow)
	{
		this.delRow = delRow;
	}

	public String getFolderCreateDate()
	{
		return folderCreateDate;
	}

	public void setFolderCreateDate(String folderCreateDate)
	{
		this.folderCreateDate = folderCreateDate;
	}

	@Override
	public String toString()
	{
		return "FolderBean [folderId=" + folderId + ", folderName=" + folderName
				+ ", parentFolderId=" + parentFolderId + ", isTmp=" + isTmp + ", delRow=" + delRow
				+ ", folderCreateDate=" + folderCreateDate + "]";
	}

}
