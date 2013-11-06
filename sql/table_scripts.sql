#Query to update a number for the event

#update GTTELNUMBERS SET TELNUMBER = '14155992671', FK_EVENTID='8f337e98-714e-4249-b1e0-aeed47741b7c',FK_ADMINID='11d12027-e1b2-495b-8f74-ab5a16a3cd0c' WHERE TELNUMBERID='0b5d55ae-049f-4ec9-906a-a11b58dd4be7';


#command to create the database
#create database guestadmin;

#create the password encrypted 41 byte password
#select password('FD3C08EEE89__________');

#command to create a user for the webapplication to read write. Use the 41 byte password encryption.
#CREATE USER 'appadmin'@'localhost' IDENTIFIED BY PASSWORD '*XXXXXXXXXXXXXXXXXX';

#assign permissions to application user
#GRANT SELECT, INSERT, UPDATE, DELETE ON guestadmin.* TO 'appadmin'@'localhost';

create table GTUSERINFO ( USERINFOID VARCHAR(45) NOT NULL, FIRST_NAME VARCHAR(256) NOT NULL, LAST_NAME VARCHAR(256), ADDRESS_1 VARCHAR(1024) , ADDRESS_2 VARCHAR(1024), CITY VARCHAR(1024), STATE VARCHAR(30), COUNTRY VARCHAR(45), IP_ADDRESS VARCHAR(1024), CELL_PHONE VARCHAR(15), PHONE_NUM VARCHAR(15), EMAIL VARCHAR(100), IS_TMP INT(1) NOT NULL DEFAULT 1 , DEL_ROW INT(1) NOT NULL DEFAULT 0,CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, PRIMARY KEY (USERINFOID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8; 
create table GTADMIN ( ADMINID VARCHAR(45) NOT NULL, FK_USERINFOID VARCHAR(45) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0,IS_TMP INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT 0 , PRIMARY KEY (ADMINID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTGUESTS ( GUESTID VARCHAR(45) NOT NULL, FK_USERINFOID VARCHAR(45) NOT NULL , FK_ADMINID VARCHAR(45) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0 , TOTAL_SEATS INT(11) NOT NULL DEFAULT 1, IS_TMP INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT 0, PRIMARY KEY (GUESTID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTTABLE (TABLEID VARCHAR(45) NOT NULL, TABLENAME VARCHAR(1024), TABLENUM INT(11), NUMOFSEATS INT(11) NOT NULL DEFAULT 1 , IS_TMP INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT 0, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0 , FK_ADMINID VARCHAR(45) NOT NULL , MODIFYDATE BIGINT(20) NOT NULL DEFAULT 0, MODIFIEDBY VARCHAR(45) NOT NULL, PRIMARY KEY (TABLEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8; 
create table GTFOLDER ( FOLDERID VARCHAR(45) NOT NULL, FOLDERNAME VARCHAR(1024), PARENT_FOLDERID VARCHAR(45), IS_TMP INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT 0, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, PRIMARY KEY (FOLDERID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTEVENT (EVENTID VARCHAR(45) NOT NULL, EVENTNUM INT(11) NOT NULL AUTO_INCREMENT , EVENTNAME VARCHAR(2048) NOT NULL DEFAULT 'New Event', FK_FOLDERID VARCHAR(45) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0 , FK_ADMINID VARCHAR(45) NOT NULL, IS_TMP INT(1) NOT NULL DEFAULT 1 , DEL_ROW INT(1) NOT NULL DEFAULT 0,  EVENTDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45) NOT NULL, HUMANEVENTDATE VARCHAR(45) NOT NULL, EVENTTIMEZONE VARCHAR(45) NOT NULL,  RSVPDEADLINEDATE BIGINT(20) NOT NULL DEFAULT 0,HUMANRSVPDEADLINEDATE VARCHAR(45) NOT NULL, PRIMARY KEY (EVENTNUM) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTEVENTTABLES ( EVENTTABLEID VARCHAR(45) NOT NULL, FK_EVENTID VARCHAR(45) NOT NULL, FK_TABLEID VARCHAR(45) NOT NULL , ASSIGN_TO_EVENT INT(1) NOT NULL DEFAULT 0, IS_TMP INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT 0, PRIMARY KEY (EVENTTABLEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTTABLEGUESTS ( TABLEGUESTID VARCHAR(45) NOT NULL, FK_TABLEID VARCHAR(45) NOT NULL , FK_GUESTID VARCHAR(45) NOT NULL, IS_TMP INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT 0, PRIMARY KEY (TABLEGUESTID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8; 
create table GTEVENTGUESTS ( EVENTGUESTID VARCHAR(45) NOT NULL, FK_EVENTID VARCHAR(45) NOT NULL, FK_GUESTID VARCHAR(45) NOT NULL, IS_TMP INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT 0,TOTAL_INVITED_SEATS INT(11), RSVP_SEATS INT(11), PRIMARY KEY (EVENTGUESTID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8; 
ALTER TABLE GTUSERINFO ADD COLUMN HUMAN_CREATEDATE VARCHAR(45) NULL ;
ALTER TABLE GTTABLE ADD COLUMN HUMAN_CREATEDATE VARCHAR(50) NULL;
ALTER TABLE GTTABLE ADD COLUMN HUMAN_MODIFYDATE VARCHAR(45) NULL;
ALTER TABLE GTTABLEGUESTS ADD COLUMN ASSIGNED_SEATS INT(11) NOT NULL DEFAULT 0 AFTER DEL_ROW ;
ALTER TABLE GTGUESTS ADD COLUMN RSVP_SEATS INT(11) DEFAULT -1;
ALTER TABLE GTUSERINFO ADD COLUMN TIMEZONE VARCHAR(15) NOT NULL;
ALTER TABLE GTGUESTS ADD COLUMN HUMANCREATEDATE VARCHAR(45) NOT NULL;
ALTER TABLE GTADMIN ADD COLUMN HUMANCREATEDATE VARCHAR(45) NOT NULL;
create table GTTELNUMBERS ( TELNUMBERID VARCHAR(45) NOT NULL, TELNUMBER VARCHAR(45) NOT NULL, FK_TELNUMBERTYPEID VARCHAR(45) NOT NULL, FK_EVENTID VARCHAR(45) NOT NULL, FK_ADMINID VARCHAR(45) NOT NULL , DEL_ROW INT(1) NOT NULL DEFAULT 0, PRIMARY KEY (TELNUMBERID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTTELNUMBERTYPE (TELNUMBERTYPEID VARCHAR(45) NOT NULL, DESCRIPTION TEXT, TELNUMTYPE VARCHAR(45) NOT NULL, PRIMARY KEY (TELNUMBERTYPEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTINCOMINGCALL (INCOMINGCALLID VARCHAR(45) NOT NULL, GUESTS_TELNUMBER VARCHAR(45) NOT NULL, CALLED_TELNUMBER VARCHAR(45) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45), PRIMARY KEY (INCOMINGCALLID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTGUESTINCOMINGCALLS ( GUESTINCOMINGCALLID VARCHAR(45) NOT NULL, FK_INCOMINGCALLID VARCHAR(45) NOT NULL, FK_GUESTID VARCHAR(45) NOT NULL, FK_EVENTID VARCHAR(45), FK_ADMINID VARCHAR(45), CALL_STATUS VARCHAR(45) NOT NULL, CALL_ACTION VARCHAR(45) NOT NULL DEFAULT 'DO_NOTHING',PRIMARY KEY (GUESTINCOMINGCALLID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTORPHANINCOMINGCALLS ( ORPHANINCOMINGCALLID VARCHAR(45) NOT NULL, FK_INCOMINGCALLID VARCHAR(45) NOT NULL, CALL_STATUS VARCHAR(45) NOT NULL, CALL_ACTION VARCHAR(45) NOT NULL DEFAULT 'DO_NOTHING' , PRIMARY KEY (ORPHANINCOMINGCALLID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
insert into GTTELNUMBERTYPE (TELNUMBERTYPEID,DESCRIPTION,TELNUMTYPE) VALUES ('bb6f4f95-1a09-4f4c-9913-3f9ae04457fa','Rsvp Telephone Number','RSVP_TEL');
insert into GTTELNUMBERTYPE (TELNUMBERTYPEID,DESCRIPTION,TELNUMTYPE) VALUES ('6041479d-8a47-4a85-bb6e-5d31f786ca13','Seating Telephone Number','SEATING_TEL');
insert into GTTELNUMBERTYPE (TELNUMBERTYPEID,DESCRIPTION,TELNUMTYPE) VALUES ('74026712-77ff-47eb-8931-1bd5abace0b3','Demo Rsvp Telephone Number','DEMO_RSVP_TEL');
insert into GTTELNUMBERTYPE (TELNUMBERTYPEID,DESCRIPTION,TELNUMTYPE) VALUES ('dd9e5a18-58aa-49b0-ad7f-7a573f9a0eb2','Demo Seating Telephone Number','DEMO_SEATING_TEL');
create table GTPASSWORD ( PASSWORDID VARCHAR(45) NOT NULL, PASSWORD VARCHAR(75) NOT NULL, FK_ADMINID VARCHAR(45) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45) , PASSWORD_STATUS VARCHAR(5) NOT NULL, PRIMARY KEY (PASSWORDID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTADMINTELEPHONYACCOUNT ( ADMINTELEPHONYACCOUNT VARCHAR(45) NOT NULL, FK_ADMINID VARCHAR(45) NOT NULL, ACCOUNTSID VARCHAR(45) NOT NULL, AUTH_TOKEN VARCHAR(45) NOT NULL , DEL_ROW INT(1) NOT NULL DEFAULT 0 ,CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45), PRIMARY KEY (ADMINTELEPHONYACCOUNT) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
ALTER TABLE GTTELNUMBERS ADD COLUMN IS_ACTIVE INT(1) DEFAULT 0, ADD COLUMN IS_PURCHASED INT(1) DEFAULT 0;
create table GTEVENTPRICEGROUP ( EVENTPRICEGROUPID VARCHAR(45) NOT NULL, FK_ADMINID VARCHAR(45) NOT NULL, FK_EVENTID VARCHAR(45) NOT NULL, FK_PRICEGROUPID VARCHAR(45) NOT NULL, PURCHASE_COMPLETE INT(1) NOT NULL DEFAULT 0, PRIMARY KEY (EVENTPRICEGROUPID) )ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTBILLADDRESS ( BILLADDRESSID VARCHAR(45) NOT NULL, FK_ADMINID VARCHAR(45) NOT NULL, FK_EVENTID VARCHAR(45) NOT NULL, FIRSTNAME VARCHAR(400) NOT NULL,MIDDLENAME VARCHAR(400), LASTNAME VARCHAR(400) NOT NULL, ADDRESS1 VARCHAR(400) NOT NULL, ADDRESS2 VARCHAR(400) NOT NULL, CITY VARCHAR(400) NOT NULL, ZIP VARCHAR(15) NOT NULL, STATE VARCHAR(400) NOT NULL, COUNTRY VARCHAR(400) NOT NULL, PRIMARY KEY (BILLADDRESSID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTBILLCREDITCARD ( BILLCREDITCARDID VARCHAR(45) NOT NULL,FK_BILLADDRESSID VARCHAR(45) NOT NULL, FK_ADMINID VARCHAR(45) NOT NULL, FK_EVENTID VARCHAR(45) NOT NULL, CREDITCARDNUM VARCHAR(45) NOT NULL, SECURITYCODE VARCHAR(10) NOT NULL, AMOUNT DECIMAL (10,2) NOT NULL, PRIMARY KEY (BILLCREDITCARDID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
ALTER TABLE GTEVENT ADD INDEX EVENT_ID (EVENTID ASC) ;
create table GTEMAILQUEUE ( EMAILQUEUEID VARCHAR(45) NOT NULL, FROM_ADDRESS VARCHAR(256) NOT NULL,FROM_ADDRESS_NAME VARCHAR(256), TO_ADDRESS VARCHAR(256) NOT NULL,TO_ADDRESS_NAME VARCHAR(256), EMAIL_SUBJECT TEXT, HTML_BODY TEXT NOT NULL, TEXT_BODY TEXT NOT NULL, STATUS VARCHAR(10) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, MODIFIEDDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45), HUMANMODIFYDATE VARCHAR(45), PRIMARY KEY (EMAILQUEUEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTEMAILTEMPLATE( EMAILTEMPLATEID VARCHAR(45) NOT NULL, EMAILTEMPLATENAME VARCHAR(20) NOT NULL, FROM_ADDRESS_NAME VARCHAR(256) NOT NULL, FROM_EMAIL_ADDRESS VARCHAR(256) NOT NULL, TO_ADDRESS_NAME VARCHAR(256) , TO_EMAIL_ADDRESS VARCHAR(256), EMAIL_SUBJECT TEXT, HTML_BODY TEXT NOT NULL, TEXT_BODY TEXT NOT NULL, PRIMARY KEY (EMAILTEMPLATEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
INSERT INTO GTEMAILTEMPLATE ( EMAILTEMPLATEID, EMAILTEMPLATENAME, FROM_ADDRESS_NAME, FROM_EMAIL_ADDRESS, EMAIL_SUBJECT , HTML_BODY, TEXT_BODY ) VALUES ( '21f93eb0-8367-11e1-b99c-78f135b03cf1', 'REGISTRATION', 'Guest Seater Notification', 'noreply@callseat.com','New User Registered', '<div class="row"  style="background-image: url(\'https://www.callseat.com/web/img/header_bkg.png\');margin-left: 0px; height:103px "><div class="span3" style>&nbsp;<br></div></div><br>Hello __GIVENNAME__,<br><br>Thank you for registering and creating a username for Callseat.<br><br>Your username is __USERNAME__<br><br>Thank You.','Hello __GIVENNAME__,\n\nThank you for registering and creating a username.\n\nYour username is __USERNAME__\n\nThank You.');
INSERT INTO GTEMAILTEMPLATE ( EMAILTEMPLATEID, EMAILTEMPLATENAME, FROM_ADDRESS_NAME, FROM_EMAIL_ADDRESS, EMAIL_SUBJECT , HTML_BODY, TEXT_BODY ) VALUES ( '88f6ab36-8455-11e1-88a0-35d635b03cf1', 'NEWPASSWORD', 'Guest Seater Notification', 'noreply@callseat.com','Password Reset Request', '<div class="row"  style="background-image: url(\'https://www.callseat.com/web/img/header_bkg.png\');margin-left: 0px; height:103px "><div class="span3" style>&nbsp;<br></div></div><br>Hello __GIVENNAME__,<br><br>We received a request to reset your password.<br>Please click the link below.<br> <br>__NEW_PASSWORD_RESET_LINK__<br><br>Thank You,<br>__PRODUCT_NAME__ Customer Support','Hello __GIVENNAME__,\n\nWe received a request to reset your password.\nPlease copy the link below to a browser.\n\n__NEW_PASSWORD_RESET_LINK__\n\nThank You,\n__PRODUCT_NAME__ Customer Support');
INSERT INTO GTEMAILTEMPLATE ( EMAILTEMPLATEID, EMAILTEMPLATENAME, FROM_ADDRESS_NAME, FROM_EMAIL_ADDRESS, EMAIL_SUBJECT , HTML_BODY, TEXT_BODY ) VALUES ( 'fa6ca454-80db-4d1b-a33b-5bd2373cedb7', 'NEWTELNUMBERPURCHASE', 'Guest Seater Notification', 'noreply@callseat.com','Congratulations! Personalized Phone Numbers for __SEATINGPLANNAME__', '<div class="row"  style="background-image: url(\'https://www.callseat.com/web/img/header_bkg.png\');margin-left: 0px; height:103px "><div class="span3" style>&nbsp;<br></div></div><br>Hello __GIVENNAME__,<br><br><p>You can now use your personalized phone number for RSVP and for seating your guests. <br><br> The phone numbers is : __NEW_TELEPHONE_TELNUM__<br><br>Thank You,<br>__PRODUCT_NAME__ Customer Support<br>Phone: __PRODUCT_PHONE__<br>__PRODUCT_ADDRESS__', 'Hello __GIVENNAME__,\n\nYou can now use your personalized phone number for RSVP and for seating your guests.\n The phone number is : __NEW_TELEPHONE_TELNUM__\n\nThank You,\n__PRODUCT_NAME__ Customer Support\nPhone: __PRODUCT_PHONE__\n__PRODUCT_ADDRESS__');

CREATE TABLE GTDEMOTELNUMBERS ( DEMOTELNUMBERID VARCHAR(45) NOT NULL, FK_TELNUMBERTYPEID VARCHAR(45) NOT NULL,  TELNUMBER varchar(45) NOT NULL,HUMAN_TELNUMBER VARCHAR(45) NOT NULL, PRIMARY KEY (DEMOTELNUMBERID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
ALTER TABLE GTTELNUMBERS ADD COLUMN  SECRET_KEY VARCHAR(15) DEFAULT '-';
ALTER TABLE GTTELNUMBERS ADD COLUMN SECRET_EVENT_NUMBER VARCHAR(15) DEFAULT '-';
ALTER TABLE GTTELNUMBERS ADD COLUMN HUMAN_TELNUMBER VARCHAR(45) NOT NULL;
INSERT INTO GTDEMOTELNUMBERS VALUES('c435a3d7-22d3-4acc-8f23-62b680affba5','dd9e5a18-58aa-49b0-ad7f-7a573f9a0eb2','13333333333','(333)-333-3333');
INSERT INTO GTDEMOTELNUMBERS VALUES( '53a90c02-6813-4d55-a865-79e891fa6a17','74026712-77ff-47eb-8931-1bd5abace0b3','12222222222', '(222)-222-2222');
CREATE TABLE GTTEMPCONTACT ( TEMPCONTACTID VARCHAR(45) NOT NULL, FK_ADMINID VARCHAR(45) NOT NULL,  TMPEMAILADDRESS varchar(255), CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45), PRIMARY KEY (TEMPCONTACTID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTSECURITYFORGOTINFO( SECURITYFORGOTINFOID VARCHAR(45) NOT NULL,FK_ADMINID VARCHAR(45), SECURE_TOKEN_ID TEXT, TOKEN_ADMIN_ID_COMBO TEXT, CREATEDATE bigint(20), HUMANCREATEDATE VARCHAR(45), IS_USABLE INT(1) NOT NULL DEFAULT 1, ACTION_TYPE VARCHAR(10), PRIMARY KEY (SECURITYFORGOTINFOID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;


create table GTPURCHASETRANSACTIONS ( PURCHASETRANSACTIONID VARCHAR(45) NOT NULL, FK_EVENTID VARCHAR(45) NOT NULL, FK_ADMINID VARCHAR(45) NOT NULL, IS_PURCHASE_COMPLETE INT(1) NOT NULL DEFAULT 0, RSVP_TELNUMBER VARCHAR(45) , SEATING_TELNUMBER VARCHAR(45), FK_PRICEGROUPID VARCHAR(45), FIRSTNAME TEXT, LASTNAME TEXT, ADDRESS1 TEXT, STATE TEXT, ZIPCODE VARCHAR(45), COUNTRY TEXT , STRIPE_CUSTOMER_ID TEXT, STRIPE_TOKEN TEXT, CREDIT_CARD_LAST4_DIGITS VARCHAR(45), CREATEDATE bigint(20), HUMANCREATEDATE VARCHAR(45) , UNIQUE_PURCHASE_TOKEN VARCHAR(45), PRIMARY KEY (PURCHASETRANSACTIONID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTPRICEGROUP ( PRICEGROUPID VARCHAR(45) NOT NULL, PRICEGROUPNAME VARCHAR(45) NOT NULL, MIN_GUEST_NUM INT(10) NOT NULL DEFAULT 0, MAX_GUEST_NUM INT(10) NOT NULL DEFAULT 0,PRICE DECIMAL(10,2) NOT NULL, MAX_MINUTES INT(6), MIN_MINUTES INT(6), SMS_COUNT INT(6), IS_DEFAULT INT(1) NOT NULL DEFAULT 0, PRIMARY KEY (PRICEGROUPID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
insert into GTPRICEGROUP (PRICEGROUPID,PRICEGROUPNAME,MIN_GUEST_NUM,MAX_GUEST_NUM,MIN_MINUTES,MAX_MINUTES,SMS_COUNT,PRICE,IS_DEFAULT) VALUES ('f64e4d1e-5dd2-11e1-a697-592f61e300d6','Tiny',0,50,0,200,200,99.00,0);
insert into GTPRICEGROUP (PRICEGROUPID,PRICEGROUPNAME,MIN_GUEST_NUM,MAX_GUEST_NUM,MIN_MINUTES,MAX_MINUTES,SMS_COUNT,PRICE,IS_DEFAULT) VALUES ('7f55012a-5dd3-11e1-bfe5-304562ecd4d0','Minimalist',0,50,0,500,500,149.00,0);
insert into GTPRICEGROUP (PRICEGROUPID,PRICEGROUPNAME,MIN_GUEST_NUM,MAX_GUEST_NUM,MIN_MINUTES,MAX_MINUTES,SMS_COUNT,PRICE,IS_DEFAULT) VALUES ('a976e446-5dd3-11e1-bf5a-452f61e300d6','Generous',0,50,0,1000,1000,199.00,1);
insert into GTPRICEGROUP (PRICEGROUPID,PRICEGROUPNAME,MIN_GUEST_NUM,MAX_GUEST_NUM,MIN_MINUTES,MAX_MINUTES,SMS_COUNT,PRICE,IS_DEFAULT) VALUES ('a06ffa1e-b8cb-4327-ac6f-6731679ad291','Big Family',0,50,0,1500,1500,240.00,0);
insert into GTPRICEGROUP (PRICEGROUPID,PRICEGROUPNAME,MIN_GUEST_NUM,MAX_GUEST_NUM,MIN_MINUTES,MAX_MINUTES,SMS_COUNT,PRICE,IS_DEFAULT) VALUES ('022a1b19-9482-4d7c-9244-24f31be44f60','Large',0,50,0,3000,3000,320.00,0);
insert into GTPRICEGROUP (PRICEGROUPID,PRICEGROUPNAME,MIN_GUEST_NUM,MAX_GUEST_NUM,MIN_MINUTES,MAX_MINUTES,SMS_COUNT,PRICE,IS_DEFAULT) VALUES ('50100ab8-ffe1-440c-9325-33f692da0b80','Demo',0,50,0,50,50,00.00,0);

create table GTSTATETAX (STATETAXID VARCHAR(45) NOT NULL, TAXTYPE VARCHAR(45) NOT NULL, STATE VARCHAR(45) NOT NULL, COUNTRY VARCHAR(45), TAXPERCENTAGE VARCHAR(45) NOT NULL,PRIMARY KEY (STATETAXID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
insert into  GTSTATETAX(STATETAXID,TAXTYPE,STATE,COUNTRY ,TAXPERCENTAGE ) VALUES('f8d6a4a4-f7b2-4242-bcc5-f3660a2d0ef0','SALES_TAX','TX', 'USA', '8.25');

ALTER TABLE GTBILLCREDITCARD ADD COLUMN PAYMENT_CHANNEL_CUSTOMERID VARCHAR(45);

CREATE TABLE GTEVENTFEATURES (FEATUREID VARCHAR(45) NOT NULL, FEATURE_NAME VARCHAR(350) NOT NULL, PRIMARY KEY (FEATUREID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTEVENTFEATUREVALUES ( FEATUREVALUEID VARCHAR(45) NOT NULL, FK_FEATUREID VARCHAR(45) NOT NULL, FK_EVENTID VARCHAR(45) NOT NULL, FEATUREVALUE TEXT, PRIMARY KEY (FEATUREVALUEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
insert into  GTEVENTFEATURES(FEATUREID,FEATURE_NAME) VALUES('e18e4f2f-b8d7-4e8f-b4a2-cd4ac245acc8','SEATING_CALL_FORWARD_NUMBER');
insert into  GTEVENTFEATURES(FEATUREID,FEATURE_NAME) VALUES('7ccf6e93-d665-4e11-a936-a0ad32b5599e','RSVP_CALL_FORWARD_NUMBER');
insert into  GTEVENTFEATURES(FEATUREID,FEATURE_NAME) VALUES('b16a6919-39c8-45b6-ae97-619d041e78a6','SEATING_SMS_GUEST_AFTER_CALL');
insert into  GTEVENTFEATURES(FEATUREID,FEATURE_NAME) VALUES('ca32b1f8-9798-4904-9362-fe15de6a3a15','SEATING_EMAIL_GUEST_AFTER_CALL');
insert into  GTEVENTFEATURES(FEATUREID,FEATURE_NAME) VALUES('b78acdd5-6c9f-4b11-83ad-96cdfcbad6dd','RSVP_SMS_CONFIRMATION');
insert into  GTEVENTFEATURES(FEATUREID,FEATURE_NAME) VALUES('9c2eaa81-56e5-44db-b036-3bdd61647ad1','RSVP_EMAIL_CONFIRMATION');
insert into  GTEVENTFEATURES(FEATUREID,FEATURE_NAME) VALUES('c7371b19-045f-444b-916a-0ff15516dbd1','DEMO_TOTAL_CALL_MINUTES');
insert into  GTEVENTFEATURES(FEATUREID,FEATURE_NAME) VALUES('fea8811b-2e1c-49e8-8502-ecc07c9e40fc','DEMO_TOTAL_TEXT_MESSAGES');
insert into  GTEVENTFEATURES(FEATUREID,FEATURE_NAME) VALUES('00db53d8-3e1e-4932-842f-1ebdac8c4873','PREMIUM_TOTAL_CALL_MINUTES');
insert into  GTEVENTFEATURES(FEATUREID,FEATURE_NAME) VALUES('d9ce3687-3722-467b-a2c1-6d6499aa957f','PREMIUM_TOTAL_TEXT_MESSAGES');
insert into  GTEVENTFEATURES(FEATUREID,FEATURE_NAME) VALUES('2afccad3-2469-44c5-9418-0b2626b5c268','DEMO_FINAL_CALL_MINUTES_USED');
insert into  GTEVENTFEATURES(FEATUREID,FEATURE_NAME) VALUES('8e34c3e2-f952-4de5-8833-cfc7cb36c46b','DEMO_FINAL_TEXT_MESSAGES_SENT');
insert into  GTEVENTFEATURES(FEATUREID,FEATURE_NAME) VALUES('b3ef81f8-8281-45aa-a112-652d3a8ed013','PREMIUM_FINAL_CALL_MINUTES_USED');
insert into  GTEVENTFEATURES(FEATUREID,FEATURE_NAME) VALUES('15db7ed7-b84d-44f2-b4ae-f16401c38a85','PREMIUM_FINAL_TEXT_MESSAGES_SENT');
insert into  GTEVENTFEATURES(FEATUREID,FEATURE_NAME) VALUES('238b385c-4e22-4edc-905b-2ca0a24a51fc','USAGE_LIMIT_REACHED_ACTION');
insert into  GTEVENTFEATURES(FEATUREID,FEATURE_NAME) VALUES('d69cc3f4-53f2-4d16-9952-3d1e07046d12','SEATINGPLAN_TELNUMBER_TYPE');


CREATE TABLE GTCALLTRANSACTION (CALLTRANSACTIONID varchar(45) NOT NULL, TELCOM_SERVICE_ACC_SID varchar(100), TELCOM_SERVICE_CALL_SID varchar(100),TELCOM_SERVICE_BILL_DURATION varchar(100),TELCOM_SERVICE_ACTUAL_DURATION varchar(100), GUEST_TELNUMBER varchar(45) NOT NULL,TO_TELNUMBER varchar(45) NOT NULL,CALL_TYPE varchar(45) NOT NULL, SECRET_EVENT_NUMBER varchar(20),SECRET_EVENT_KEY varchar(20),FK_GUESTID varchar(45), FK_EVENTID varchar(45), FK_ADMINID varchar(45), CALL_CREATEDATE bigint(20), HUMAN_CALL_CREATEDATE varchar(45), CALL_ENDDATE bigint(20), HUMAN_CALL_ENDDATE varchar(45), CALL_STATUS varchar(45),  PRIMARY KEY (CALLTRANSACTIONID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTSMSQUEUE ( SMSQUEUEID VARCHAR(45) NOT NULL, FROM_TELNUMBER VARCHAR(45) NOT NULL, TO_TELNUMBER VARCHAR(45) NOT NULL, FK_SMSTEMPLATEID VARCHAR(45) NOT NULL, SMS_MESSAGE TEXT, STATUS VARCHAR(20) DEFAULT 'NEW', FK_EVENTID VARCHAR(45), FK_ADMINID VARCHAR(45), FK_GUESTID VARCHAR(45), CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45), MODIFIEDDATE BIGINT(20) DEFAULT 0, HUMANMODIFIEDDATE VARCHAR(45), EVENT_TYPE VARCHAR(45),PRIMARY KEY (SMSQUEUEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTSMSTRANSACTION ( SMSTRANSACTIONID VARCHAR(45) NOT NULL, TELCOM_SERVICE_ACC_SID VARCHAR(45), TELCOM_SERVICE_SMS_SID VARCHAR(45), FROM_TELNUMBER VARCHAR(45) NOT NULL, TO_TELNUMBER VARCHAR(45) NOT NULL, SMS_MESSAGE VARCHAR(160), SMS_STATUS VARCHAR(45), FK_EVENTID VARCHAR(45), FK_ADMINID VARCHAR(45), FK_GUESTID VARCHAR(45), CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45), EVENT_TYPE VARCHAR(45),PRIMARY KEY (SMSTRANSACTIONID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTSMSSCHEDULE( SMSSCHEDULEID VARCHAR(45) NOT NULL, FK_SMSTEMPLATEID VARCHAR(45) NOT NULL,  FK_EVENTID VARCHAR(45), FK_ADMINID VARCHAR(45), FK_GUESTID VARCHAR(45), CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45), SCHEDULEDSENDDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANSCHEDULEDSENDDATE VARCHAR(45), SCHEDULE_STATUS VARCHAR(45) NOT NULL,PRIMARY KEY (SMSSCHEDULEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTEMAILSCHEDULE ( EMAILSCHEDULEID VARCHAR(45) NOT NULL, FK_EMAILTEMPLATEID VARCHAR(45) NOT NULL,  FK_EVENTID VARCHAR(45), FK_ADMINID VARCHAR(45), FK_GUESTID VARCHAR(45), CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45), SCHEDULEDSENDDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANSCHEDULEDSENDDATE VARCHAR(45), SCHEDULE_STATUS VARCHAR(45) NOT NULL,PRIMARY KEY (EMAILSCHEDULEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTSMSTEMPLATE (SMSTEMPLATEID VARCHAR(45) NOT NULL, SMSTEMPLATENAME VARCHAR(100) NOT NULL, SMS_BODY TEXT NOT NULL, PRIMARY KEY (SMSTEMPLATEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
INSERT INTO GTSMSTEMPLATE (SMSTEMPLATEID,SMSTEMPLATENAME, SMS_BODY) VALUES('7548856c-f61e-48ee-b2cc-4d2f79e540e2','SMS_RSVP_CONFIRMATION','__EVENT_NAME__. Your RSVP of __NO_OF_SEAT__ was accepted.');
INSERT INTO GTSMSTEMPLATE (SMSTEMPLATEID,SMSTEMPLATENAME, SMS_BODY) VALUES('ed3f8787-7260-43e5-a0aa-63c145eee26d','SMS_SEATING_CONFIRMATION','__EVENT_NAME__. You are seated at __TABLE_LIST__.');
INSERT INTO GTEMAILTEMPLATE ( EMAILTEMPLATEID, EMAILTEMPLATENAME, FROM_ADDRESS_NAME, FROM_EMAIL_ADDRESS, EMAIL_SUBJECT , HTML_BODY, TEXT_BODY ) VALUES ( '3d28fb50-051b-44d0-995e-66fdebc8740c', 'RSVP_CONFIRMATION', 'Guest Seater Notification', 'noreply@callseat.com','RSVP Confirmation', '<div class="row"  style="background-image: url(\'https://www.callseat.com/web/img/header_bkg.png\');margin-left: 0px; height:103px "><div class="span3" style>&nbsp;<br></div></div><br>Hello __GIVENNAME__,<br><p>Your RSVP for __SEATINGPLANNAME__ has been confirmed.<br><br>__RSVPRESPONSE__.<br><br>You may change your RSVP by calling <b>__RSVPPHONENUM__</b>.</p><br>Thank You,<br>__HOSTNAME__', 'Hello __GIVENNAME__,\n\nYour RSVP for __SEATINGPLANNAME__ has been confirmed.\n\n__RSVPRESPONSE__.\n\nYou may change your RSVP by calling __RSVPPHONENUM__.\n\nThank You,\n__HOSTNAME__');
INSERT INTO GTEMAILTEMPLATE ( EMAILTEMPLATEID, EMAILTEMPLATENAME, FROM_ADDRESS_NAME, FROM_EMAIL_ADDRESS, EMAIL_SUBJECT , HTML_BODY, TEXT_BODY ) VALUES ( '3e2ff27d-835e-43c3-8426-3acd0f0d1133', 'SEATING_CONFIRMATION', 'Guest Seater Notification', 'noreply@callseat.com','Seating Confirmation', '<div class="row"  style="background-image: url(\'https://www.callseat.com/web/img/header_bkg.png\');margin-left: 0px; height:103px "><div class="span3" style>&nbsp;<br></div></div><br>Hello __GIVENNAME__,<br><p>Welcome to __SEATINGPLANNAME__.<br><br>You are seated at __SEATING_CONFIRMATION__.</p><br>Thank You,<br>__HOSTNAME__', 'Hello __GIVENNAME__,\nWelcome to __SEATINGPLANNAME__.\n\nYou are seated at __SEATING_CONFIRMATION__.\n\nThank You,\n__HOSTNAME__');
INSERT INTO GTEMAILTEMPLATE ( EMAILTEMPLATEID, EMAILTEMPLATENAME, FROM_ADDRESS_NAME, FROM_EMAIL_ADDRESS, EMAIL_SUBJECT , HTML_BODY, TEXT_BODY ) VALUES ( 'cf6cb16e-cafc-4d1f-b938-6e853d358d52', 'RSVPRESPONSEDEMO', 'Guest Seater', 'noreply@callseat.com','Invitation to __SEATINGPLANNAME__', '<div class="row"  style="background-image: url(\'https://www.callseat.com/web/img/header_bkg.png\');margin-left: 0px; height:103px "><div class="span3" style>&nbsp;<br></div></div><br>Hello __GIVENNAME__,<br><p>You have been invited to __SEATINGPLANNAME__.<br>Please RSVP before __RSVPDEADLINE__.<br></p> <a href="__RSVPLINK__" target="_blank">Click here RSVP</a><br><br>Thank You,<br>__HOSTNAME__', 'Hello __GIVENNAME__,\n You have been invited to __SEATINGPLANNAME__.\nPlease RSVP before __RSVPDEADLINE__.\nTo RSVP copy and paste the link below to a browser. \n\nRSVP Link : __RSVPLINK__\n\nThank You,\n__HOSTNAME__');
INSERT INTO GTEMAILTEMPLATE ( EMAILTEMPLATEID, EMAILTEMPLATENAME, FROM_ADDRESS_NAME, FROM_EMAIL_ADDRESS, EMAIL_SUBJECT , HTML_BODY, TEXT_BODY ) VALUES ( 'ad0ccaeb-16e4-4086-b003-3a1d05740fa3', 'RSVPRESPONSE', 'Guest Seater', 'noreply@callseat.com','Invitation to __SEATINGPLANNAME__', '<div class="row"  style="background-image: url(\'https://www.callseat.com/web/img/header_bkg.png\');margin-left: 0px; height:103px "><div class="span3" style>&nbsp;<br></div></div><br>Hello __GIVENNAME__,<br><p>You have been invited to __SEATINGPLANNAME__.<br>Please RSVP before __RSVPDEADLINE__.<br></p> <a href="__RSVPLINK__" target="_blank">Click here RSVP</a><br><br>You may also RSVP by calling <b>__RSVPPHONENUM__</b><br><br>Thank You,<br>__HOSTNAME__', 'Hello __GIVENNAME__,\n You have been invited to __SEATINGPLANNAME__.\nPlease RSVP before __RSVPDEADLINE__.\nTo RSVP copy and paste the link below to a browser. \n\nRSVP Link : __RSVPLINK__\n\nYou may also RSVP by calling __RSVPPHONENUM__\n\nThank You,\n__HOSTNAME__');
CREATE TABLE GTGUESTWEBRESPONSE ( GUESTWEBRESPONSEID VARCHAR(45) NOT NULL, WEB_RESPONSE_TYPE  VARCHAR(45) NOT NULL, LINKID  VARCHAR(45) NOT NULL,  LINK_DOMAIN  TEXT, FK_GUESTID VARCHAR(45) NOT NULL ,  FK_EVENTID VARCHAR(45) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45),RESPONSE_STATUS VARCHAR(45) NOT NULL ,FK_ADMINID VARCHAR(45) NOT NULL, RESPONSEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANRESPONSEDATE VARCHAR(45),  PRIMARY KEY (GUESTWEBRESPONSEID,WEB_RESPONSE_TYPE,LINKID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

CREATE TABLE GTGROUPS ( GROUPID VARCHAR(45) NOT NULL, GROUP_NAME VARCHAR(100) NOT NULL, GROUP_DESCRIPTION VARCHAR(200),  PRIMARY KEY (GROUPID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTUSERGROUPS ( USERGROUPID VARCHAR (45) NOT NULL, FK_ADMINID  VARCHAR (45) NOT NULL, FK_GROUPID   VARCHAR (45) NOT NULL,  CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45) NOT NULL,  IS_ACTIVE INT(1) NOT NULL DEFAULT 1,  MODIFIEDDATE BIGINT(20) DEFAULT 0, HUMANMODIFIEDDATE VARCHAR(45),  PRIMARY KEY (USERGROUPID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTPERMISSIONS ( PERMISSIONID VARCHAR(45) NOT NULL, PERMISSION_NAME VARCHAR(100) NOT NULL,  PERMISSION_DESCRIPTION VARCHAR(200),  PRIMARY KEY (PERMISSIONID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTGROUPPERMISSIONS ( GROUPPERMISSIONID  VARCHAR (45) NOT NULL, FK_GROUPID VARCHAR (45) NOT NULL, FK_PERMSSIONID VARCHAR(45) NOT NULL,  CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45) NOT NULL,  IS_ACTIVE INT(1) NOT NULL DEFAULT 1,  MODIFIEDDATE BIGINT(20) DEFAULT 0, HUMANMODIFIEDDATE VARCHAR(45) ,  PRIMARY KEY (GROUPPERMISSIONID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTBLOCKEDUSERPERMISSION ( BLOCKEDUSERPERMISSIONID   VARCHAR (45) NOT NULL, FK_ADMINID  VARCHAR (45) NOT NULL, FK_PERMSSIONID VARCHAR(45) NOT NULL,   CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45) NOT NULL,  IS_ACTIVE INT(1) NOT NULL DEFAULT 1,  MODIFIEDDATE BIGINT(20) DEFAULT 0, HUMANMODIFIEDDATE VARCHAR(45) ,  PRIMARY KEY (BLOCKEDUSERPERMISSIONID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

INSERT INTO GTGROUPS ( GROUPID, GROUP_NAME, GROUP_DESCRIPTION) VALUES ('68207a80-0a76-49ff-af3d-4d1d85752472','SUPERUSER', 'This user should have permission to do everything in the system.');
INSERT INTO GTUSERGROUPS (USERGROUPID,FK_ADMINID,FK_GROUPID,CREATEDATE,HUMANCREATEDATE,IS_ACTIVE,MODIFIEDDATE) VALUES ('2c3ef07d-6c1e-4726-96a4-4c8117cf4690','74468c4c-e76f-4608-85ec-15b96bbf713d', '68207a80-0a76-49ff-af3d-4d1d85752472', '1378276660', '2013-09-04 01:37:44 UTC' ,'1','0');
INSERT INTO GTPERMISSIONS(PERMISSIONID,PERMISSION_NAME,PERMISSION_DESCRIPTION) VALUES ('7aca9db6-f0a1-47b2-b40b-70c7be477b40','USE_PAYMENT_CHANNEL_TEST_API_KEY', 'This permission will allow a user to be able to make purchases using the testing API key.');
INSERT INTO GTGROUPPERMISSIONS(GROUPPERMISSIONID,FK_GROUPID, FK_PERMSSIONID, CREATEDATE , HUMANCREATEDATE, IS_ACTIVE, MODIFIEDDATE ) VALUES('b5b48377-eab3-4645-aa23-134c23835c36','68207a80-0a76-49ff-af3d-4d1d85752472','7aca9db6-f0a1-47b2-b40b-70c7be477b40','0','2013-09-04 01:37:44 UTC','1','0');

ALTER TABLE GTPURCHASETRANSACTIONS ADD COLUMN API_KEY_TYPE VARCHAR(45);

update GTPRICEGROUP set PRICEGROUPNAME = 'Max', PRICE='490.00',MAX_MINUTES='4500', SMS_COUNT='4500', IS_DEFAULT='0' WHERE PRICEGROUPID='f64e4d1e-5dd2-11e1-a697-592f61e300d6';
update GTPRICEGROUP set PRICEGROUPNAME = 'Basic', PRICE='120.00' WHERE PRICEGROUPID='7f55012a-5dd3-11e1-bfe5-304562ecd4d0';
update GTPRICEGROUP set PRICEGROUPNAME = 'Pro', PRICE='180.00',MAX_MINUTES='1000', SMS_COUNT='1000', IS_DEFAULT='0' WHERE PRICEGROUPID='a976e446-5dd3-11e1-bf5a-452f61e300d6';
update GTPRICEGROUP set PRICEGROUPNAME = 'Plus', PRICE='270.00',MAX_MINUTES='2000', SMS_COUNT='2000',IS_DEFAULT='1' WHERE PRICEGROUPID='a06ffa1e-b8cb-4327-ac6f-6731679ad291';
update GTPRICEGROUP set PRICEGROUPNAME = 'Premium', PRICE='360.00',MAX_MINUTES='3000', SMS_COUNT='3000', IS_DEFAULT='0' WHERE PRICEGROUPID='022a1b19-9482-4d7c-9244-24f31be44f60';

ALTER TABLE GTEMAILQUEUE ADD COLUMN BCC_ADDRESS VARCHAR(256), ADD COLUMN  BCC_ADDRESSNAME VARCHAR(256);
ALTER TABLE GTEMAILQUEUE ADD COLUMN CC_ADDRESS VARCHAR(256), ADD COLUMN  CC_ADDRESSNAME VARCHAR(256);

INSERT INTO GTTELNUMBERTYPE VALUES('b2d64df4-e31d-4099-8614-0424af767177', 'Demo Telephone Number','DEMO_TELEPHONE_NUMBER');
INSERT INTO GTTELNUMBERTYPE VALUES('c6f9119d-da4b-46d3-950c-82669a05218a', 'Premium Telephone Number','PREMIUM_TELEPHONE_NUMBER');
ALTER TABLE GTTELNUMBERS ADD COLUMN CREATEDATE BIGINT (20) NOT NULL DEFAULT 0, ADD COLUMN HUMANCREATEDATE VARCHAR(45) NOT NULL;
INSERT INTO  GTEVENTFEATURES(FEATUREID,FEATURE_NAME) VALUES('84a26afd-cd40-43da-be63-50898c8b4374','SEATINGPLAN_MODE');
ALTER TABLE GTPURCHASETRANSACTIONS ADD COLUMN TELEPHONE_NUMBER VARCHAR(45) ;
UPDATE GTDEMOTELNUMBERS SET FK_TELNUMBERTYPEID = 'b2d64df4-e31d-4099-8614-0424af767177' WHERE DEMOTELNUMBERID='c435a3d7-22d3-4acc-8f23-62b680affba5';

delete from GTPRICEGROUP where PRICEGROUPID in ('f64e4d1e-5dd2-11e1-a697-592f61e300d6','7f55012a-5dd3-11e1-bfe5-304562ecd4d0','a976e446-5dd3-11e1-bf5a-452f61e300d6','a06ffa1e-b8cb-4327-ac6f-6731679ad291','022a1b19-9482-4d7c-9244-24f31be44f60');

insert into GTPRICEGROUP (PRICEGROUPID,PRICEGROUPNAME,MIN_GUEST_NUM,MAX_GUEST_NUM,MIN_MINUTES,MAX_MINUTES,SMS_COUNT,PRICE,IS_DEFAULT) VALUES ('a976e446-5dd3-11e1-bf5a-452f61e300d6','Tiny',0,50,0,100,100,29.00,0);
insert into GTPRICEGROUP (PRICEGROUPID,PRICEGROUPNAME,MIN_GUEST_NUM,MAX_GUEST_NUM,MIN_MINUTES,MAX_MINUTES,SMS_COUNT,PRICE,IS_DEFAULT) VALUES ('a06ffa1e-b8cb-4327-ac6f-6731679ad291','Base',0,50,0,500,500,69.00,1);
insert into GTPRICEGROUP (PRICEGROUPID,PRICEGROUPNAME,MIN_GUEST_NUM,MAX_GUEST_NUM,MIN_MINUTES,MAX_MINUTES,SMS_COUNT,PRICE,IS_DEFAULT) VALUES ('022a1b19-9482-4d7c-9244-24f31be44f60','Plus',0,50,0,1000,1000,89.00,0);

CREATE TABLE GTCOUPON( COUPONID  VARCHAR(45) NOT NULL, COUPONTEXT   VARCHAR(15) NOT NULL , FK_ADMINID VARCHAR(45) NOT NULL, STARTDATE  BIGINT(20) NOT NULL DEFAULT 0, HUMANSTARTDATE VARCHAR(45) NOT NULL , ENDDATE  BIGINT(20) NOT NULL DEFAULT 0, HUMANENDDATE VARCHAR(45) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45), DISCOUNT  VARCHAR(45) NOT NULL, DISCOUNT_TYPE  VARCHAR(45) NOT NULL,  PRIMARY KEY (COUPONID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

