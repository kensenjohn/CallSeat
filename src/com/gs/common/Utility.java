package com.gs.common;

import java.io.StringWriter;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class Utility
{
	public static String getNewGuid()
	{
		UUID uuid = UUID.randomUUID();

		return uuid.toString();
	}

	public static String convertTelNumToDigits(String sTelNumber)
	{
		String sFinalString = " ";
		if (sTelNumber != null && !"".equalsIgnoreCase(sTelNumber))
		{
			char[] chString = sTelNumber.toCharArray();
			for (char singleChar : chString)
			{
				if (singleChar == '1' || singleChar == '2' || singleChar == '3'
						|| singleChar == '4' || singleChar == '5' || singleChar == '6'
						|| singleChar == '7' || singleChar == '8' || singleChar == '9'
						|| singleChar == '0')
				{
					sFinalString = sFinalString + singleChar + " ";
				}
			}
		}
		return sFinalString;
	}

	public static <T> String getXmlFromBean(Class<T> className, T bean) throws JAXBException
	{
		JAXBContext jaxbContext = JAXBContext.newInstance(className);

		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		java.io.StringWriter sw = new StringWriter();

		marshaller.marshal(bean, sw);

		return sw.toString();

	}

}
