package com.gs.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class GuestAdminCookie 
{

	public Cookie createCookie(String sCookieName , String sUserid, String sPath)
	{
		Cookie cookie = new Cookie(sCookieName,sUserid);
		cookie.setMaxAge(265 * 60 * 60);
		cookie.setDomain("");
		cookie.setPath("");
		cookie.setSecure(true);
		
		return cookie;
		
	}
	
	public static String readCookie( String sCookieName, HttpServletRequest httpRequest)
	{
		String sCookieValue = "";
		if(sCookieName!=null && !"".equalsIgnoreCase(sCookieName))
		{
			Cookie[] arrCookie = httpRequest.getCookies();
			if (arrCookie != null && arrCookie.length>0) 
			{
				 for (Cookie cookie : arrCookie) 
				 {
			           if( sCookieName.equals(cookie.getName()) )
			           {
			        	   sCookieValue = cookie.getValue();
			           }
				 }
			}
		}
		return sCookieValue;
		
	}
}
