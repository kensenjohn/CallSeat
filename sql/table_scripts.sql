#command to create the database
#create database guestadmin;

#command to create a user for the webapplication to read write. Use the 41 byte password encryption.
#CREATE USER 'appadmin'@'localhost' IDENTIFIED BY PASSWORD '*XXXXXXXXXXXXXXXXXX';

#assign permissions to application user
#GRANT SELECT, INSERT, UPDATE, DELETE ON guestadmin.* TO 'appadmin'@'localhost';


create table GTUSERINFO ( USERINFOID VARCHAR(45) NOT NULL, FIRST_NAME VARCHAR(256) NOT NULL, LAST_NAME VARCHAR(256), ADDRESS_1 VARCHAR(1024) , ADDRESS_2 VARCHAR(1024), CITY VARCHAR(1024), STATE VARCHAR(30), COUNTRY VARCHAR(45), IP_ADDRESS VARCHAR(1024), CELL_PHONE VARCHAR(15), PHONE_NUM VARCHAR(15), EMAIL VARCHAR(100), IS_TMP INT(1) NOT NULL DEFAULT 1 , DEL_ROW INT(1) NOT NULL DEFAULT 0,CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, PRIMARY KEY (USERINFOID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8; 

create table GTADMIN ( ADMINID VARCHAR(45) NOT NULL, FK_USERINFOID VARCHAR(45) NOT NULL, CREATEDATE  BIGINT(20) NOT NULL DEFAULT 0,IS_TMP INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT 0 , PRIMARY KEY (ADMINID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

create table GTGUESTS ( GUESTID VARCHAR(45) NOT NULL, FK_USERINFOID VARCHAR(45) NOT NULL , FK_ADMINID VARCHAR(45) NOT NULL, CREATEDATE  BIGINT(20) NOT NULL DEFAULT 0 , TOTAL_SEATS INT(11) NOT NULL DEFAULT 1, IS_TMP INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT 0, PRIMARY KEY (GUESTID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;


create table GTTABLE (TABLEID VARCHAR(45) NOT NULL, TABLENAME VARCHAR(1024), TABLENUM INT(11), NUMOFSEATS INT(11) NOT NULL DEFAULT 1 , IS_TMP INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT 0, CREATEDATE  BIGINT(20) NOT NULL DEFAULT 0 , FK_ADMINID VARCHAR(45) NOT NULL , MODIFYDATE BIGINT(20) NOT NULL DEFAULT 0, MODIFIEDBY VARCHAR(45) NOT NULL, PRIMARY KEY (TABLEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8; 

create table GTFOLDER ( FOLDERID VARCHAR(45) NOT NULL, FOLDERNAME VARCHAR(1024), PARENT_FOLDERID VARCHAR(45), IS_TMP INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT 0, CREATEDATE  BIGINT(20) NOT NULL DEFAULT 0, PRIMARY KEY (FOLDERID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8; 

create table GTEVENT (EVENTID VARCHAR(45) NOT NULL, EVENTNUM INT(11) NOT NULL AUTO_INCREMENT , EVENTNAME VARCHAR(2048) NOT NULL DEFAULT 'New Event', FK_FOLDERID VARCHAR(45) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0 , FK_ADMINID VARCHAR(45) NOT NULL, IS_TMP INT(1) NOT NULL DEFAULT 1 , DEL_ROW INT(1) NOT NULL DEFAULT 0,  PRIMARY KEY (EVENTNUM) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

create table GTEVENTTABLES ( EVENTTABLEID  VARCHAR(45) NOT NULL, FK_EVENTID VARCHAR(45) NOT NULL, FK_TABLEID VARCHAR(45) NOT NULL , ASSIGN_TO_EVENT INT(1) NOT NULL DEFAULT 0, IS_TMP INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT 0, PRIMARY KEY (EVENTTABLEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8; 

create table GTTABLEGUESTS ( TABLEGUESTID  VARCHAR(45) NOT NULL, FK_TABLEID VARCHAR(45) NOT NULL ,  FK_GUESTID VARCHAR(45) NOT NULL, IS_TMP INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT 0, PRIMARY KEY (TABLEGUESTID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8; 

create table GTEVENTGUESTS ( EVENTGUESTID VARCHAR(45) NOT NULL, FK_EVENTID VARCHAR(45) NOT NULL, FK_GUESTID VARCHAR(45) NOT NULL, IS_TMP INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT 0,TOTAL_INVITED_SEATS INT(11), RSVP_SEATS INT(11), PRIMARY KEY (EVENTGUESTID) )  ENGINE = MyISAM DEFAULT CHARSET = utf8; 

ALTER TABLE GTUSERINFO ADD COLUMN HUMAN_CREATEDATE VARCHAR(45) NULL ;
ALTER TABLE GTTABLE ADD COLUMN HUMAN_CREATEDATE VARCHAR(50) NULL , ADD COLUMN HUMAN_MODIFYDATE VARCHAR(45) NULL ;
ALTER TABLE GTTABLEGUESTS ADD COLUMN ASSIGNED_SEATS INT(11) NOT NULL DEFAULT 0  AFTER DEL_ROW ;
ALTER TABLE GTGUESTS ADD COLUMN RSVP_SEATS INT(11) DEFAULT -1;
ALTER TABLE GTEVENT ADD COLUMN EVENTDATE BIGINT(20) NOT NULL DEFAULT 0;
ALTER TABLE GTUSERINFO ADD COLUMN TIMEZONE VARCHAR(15) NOT NULL;
ALTER TABLE GTGUESTS ADD COLUMN HUMANCREATEDATE VARCHAR(45) NOT NULL;
ALTER TABLE GTADMIN ADD COLUMN HUMANCREATEDATE VARCHAR(45) NOT NULL;
ALTER TABLE GTEVENT ADD COLUMN HUMANCREATEDATE VARCHAR(45) NOT NULL;
ALTER TABLE GTEVENT ADD COLUMN HUMANEVENTDATE VARCHAR(45) NOT NULL;

#Indexes

ALTER TABLE GTEVENT ADD INDEX EVENT_ID (EVENTID ASC) ;

#11/15/11
create table GTTELNUMBERS ( TELNUMBERID VARCHAR(45) NOT NULL, TELNUMBER VARCHAR(45) NOT NULL, FK_TELNUMBERTYPEID  VARCHAR(45) NOT NULL, FK_EVENTID VARCHAR(45) NOT NULL, FK_ADMINID VARCHAR(45) NOT NULL , DEL_ROW INT(1) NOT NULL DEFAULT 0 , PRIMARY KEY (TELNUMBERID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

create table GTTELNUMBERTYPE (TELNUMBERTYPEID  VARCHAR(45) NOT NULL, DESCRIPTION TEXT, TELNUMTYPE VARCHAR(45) NOT NULL  , PRIMARY KEY (TELNUMBERTYPEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

create table GTINCOMINGCALL ( INCOMINGCALLID VARCHAR(45) NOT NULL, GUESTS_TELNUMBER VARCHAR(45) NOT NULL, CALLED_TELNUMBER VARCHAR(45) NOT NULL, CREATEDATE   BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE   VARCHAR(45) ,  PRIMARY KEY (INCOMINGCALLID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

create table GTGUESTINCOMINGCALLS ( GUESTINCOMINGCALLID  VARCHAR(45) NOT NULL, FK_INCOMINGCALLID  VARCHAR(45) NOT NULL, FK_GUESTID  VARCHAR(45) NOT NULL, FK_EVENTID VARCHAR(45), FK_ADMINID VARCHAR(45), CALL_STATUS VARCHAR(45) NOT NULL,  CALL_ACTION VARCHAR(45) NOT NULL DEFAULT 'DO_NOTHING' , PRIMARY KEY (GUESTINCOMINGCALLID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

create table GTORPHANINCOMINGCALLS ( ORPHANINCOMINGCALLID  VARCHAR(45) NOT NULL, FK_INCOMINGCALLID  VARCHAR(45) NOT NULL,  CALL_STATUS VARCHAR(45) NOT NULL,  CALL_ACTION VARCHAR(45) NOT NULL DEFAULT 'DO_NOTHING' , PRIMARY KEY (ORPHANINCOMINGCALLID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
#create table GTOUTGOINGCALL ( OUTGOINGCALLID VARCHAR(45) NOT NULL, GUESTS_TELNUMBER, 

insert into GTTELNUMBERTYPE (TELNUMBERTYPEID,DESCRIPTION,TELNUMTYPE) VALUES ('bb6f4f95-1a09-4f4c-9913-3f9ae04457fa','Rsvp Telephone Number','RSVP_TEL');
insert into GTTELNUMBERTYPE (TELNUMBERTYPEID,DESCRIPTION,TELNUMTYPE) VALUES ('6041479d-8a47-4a85-bb6e-5d31f786ca13','Seating Telephone Number','SEATING_TEL');