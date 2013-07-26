package com.gs.common;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.gs.bean.NumberVoiceBean;

import java.io.StringWriter;
import java.util.Random;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class Utility {
	public static String getNewGuid() {
		UUID uuid = UUID.randomUUID();

		return uuid.toString();
	}

	public static String convertTelNumToDigits(String sTelNumber) {
		String sFinalString = " ";
		if (sTelNumber != null && !"".equalsIgnoreCase(sTelNumber)) {
			char[] chString = sTelNumber.toCharArray();
			for (char singleChar : chString) {
				if (singleChar == '1' || singleChar == '2' || singleChar == '3'
						|| singleChar == '4' || singleChar == '5'
						|| singleChar == '6' || singleChar == '7'
						|| singleChar == '8' || singleChar == '9'
						|| singleChar == '0') {
					sFinalString = sFinalString + singleChar + " ";
				}
			}
		}
		return sFinalString;
	}

	public static <T> String getXmlFromBean(Class<T> className, T bean)
			throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(className);

		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		java.io.StringWriter sw = new StringWriter();

		marshaller.marshal(bean, sw);

		return sw.toString();

	}

	public static Integer getRandomInteger(Integer iLimit) {

		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(iLimit);
		return randomInt;
	}

	public static String generateSecretKey(Integer iNumOfDigits) {
		long seed = (System.nanoTime()) | (System.nanoTime() << 16)
				| (System.nanoTime() << 32) ^ (System.nanoTime() << 40);

		Random rnd = new Random(seed);
		String pin = "";
		int last_n = -1, n = -1;

		for (int i = 0; i < iNumOfDigits; i++) {

			// Be sure the new digit is different from the last
			for (int j = 0; j < 25 && (last_n == n); j++) {
				n = (int) (rnd.nextFloat() * 9.0f);
			}

			last_n = n;
			pin += n;
		}

		return pin;
	}

	public static String getMultipleParamsList(Integer iNumber) {
		StringBuilder strParamQuestions = new StringBuilder();
		if (iNumber > 0) {
			boolean isFirst = true;
			for (int i = 0; i < iNumber; i++) {
				if (!isFirst) {
					strParamQuestions.append(",");
				}
				strParamQuestions.append("?");
				isFirst = false;
			}
		}
		return strParamQuestions.toString();
	}

	public static String convertHumanToInternationalTelNum(String sHuman) {
        String sInternationalTelNum = "";
		if (sHuman != null && !"".equalsIgnoreCase(sHuman)) {
			sHuman = sHuman.trim();
			/*sHuman = sHuman.replaceAll("+", "");
			sHuman = sHuman.replaceAll("\\(", "");
			sHuman = sHuman.replaceAll("\\)", "");
			sHuman = sHuman.replaceAll("-", "");
			sHuman = sHuman.replaceAll(" ", "");

			sHuman = "1" + sHuman;    */
            try {
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                com.google.i18n.phonenumbers.Phonenumber.PhoneNumber apiPhoneNumber = phoneUtil.parse(sHuman, "US");
                sInternationalTelNum = phoneUtil.format(apiPhoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
            }  catch(Exception e) {
                sInternationalTelNum = "";
            }

		}
		return sInternationalTelNum;
	}

	public static String convertInternationalToHumanTelNum(String sTelephoneNum) {
		String sTmpTelNum = "";
		if (sTelephoneNum != null && !"".equalsIgnoreCase(sTelephoneNum)) {
			if (sTelephoneNum.length() < 12) {
				sTelephoneNum = " " + sTelephoneNum;
			}
			if (sTelephoneNum.length() == 12) {
				String sAreaCode = sTelephoneNum.substring(2, 5);
				String sFirstSetNumber = sTelephoneNum.substring(5, 8);
				String sLastSetNumber = sTelephoneNum.substring(8);

				sTmpTelNum = "(" + sAreaCode + ")" + " " + sFirstSetNumber
						+ " " + sLastSetNumber;
			}
		}
		return sTmpTelNum;
	}

    public static String getHumanFormattedNumber(String sTelephoneNum )
    {
        return getHumanFormattedNumber(sTelephoneNum, "US");
    }

    public static String getHumanFormattedNumber(String sTelephoneNum, String sCountryCode)
    {
        String sHumanFormattedPhones = "";
        if(sTelephoneNum!=null && !"".equalsIgnoreCase(sTelephoneNum) && sCountryCode!=null && !"".equalsIgnoreCase(sCountryCode))
        {
            try {
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                com.google.i18n.phonenumbers.Phonenumber.PhoneNumber apiPhoneNumber = phoneUtil.parse(sTelephoneNum, sCountryCode);
                sHumanFormattedPhones = phoneUtil.format(apiPhoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
            }
            catch(Exception e)
            {
                sHumanFormattedPhones = "";
            }
        }
        return sHumanFormattedPhones;
    }

    private static String getSingleDigitVoice( int iNumber )
    {
        String sNumberVoiceName = "";
        if(iNumber==1)
        {
            sNumberVoiceName = "one_stereo.wav";
        }
        else if(iNumber==2)
        {
            sNumberVoiceName = "two_stereo.wav";
        }
        else if(iNumber==3)
        {
            sNumberVoiceName = "three_stereo.wav";
        }
        else if(iNumber==4)
        {
            sNumberVoiceName = "four_stereo.wav";
        }
        else if(iNumber==5)
        {
            sNumberVoiceName = "five_stereo.wav";
        }
        else if(iNumber==6)
        {
            sNumberVoiceName = "six_stereo.wav";
        }
        else if(iNumber==7)
        {
            sNumberVoiceName = "seven_stereo.wav";
        }
        else if(iNumber==8)
        {
            sNumberVoiceName = "eight_stereo.wav";
        }
        else if(iNumber==9)
        {
            sNumberVoiceName = "nine_stereo.wav";
        }
        else if(iNumber==10)
        {
            sNumberVoiceName = "ten_stereo.wav";
        }
        else if(iNumber==11)
        {
            sNumberVoiceName = "eleven_stereo.wav";
        }
        else if(iNumber==12)
        {
            sNumberVoiceName = "twelve_stereo.wav";
        }
        else if(iNumber==13)
        {
            sNumberVoiceName = "thirteen_stereo.wav";
        }
        else if(iNumber==14)
        {
            sNumberVoiceName = "fourteen_stereo.wav";
        }
        else if(iNumber==15)
        {
            sNumberVoiceName = "fifteen_stereo.wav";
        }
        else if(iNumber==16)
        {
            sNumberVoiceName = "sixteen_stereo.wav";
        }
        else if(iNumber==17)
        {
            sNumberVoiceName = "seventeen_stereo.wav";
        }
        else if(iNumber==18)
        {
            sNumberVoiceName = "eighteen_stereo.wav";
        }
        else if(iNumber==19)
        {
            sNumberVoiceName = "nineteen_stereo.wav";
        }
        return sNumberVoiceName;
    }

    public static NumberVoiceBean getNumberInVoice(int iNumber)
    {
        NumberVoiceBean numberVoiceBean = new NumberVoiceBean();

        String sOnesPlace = "";
        String sTenPlaceNumberVoiceName = "";
        if(iNumber>=0 && iNumber<=19)
        {
            sOnesPlace = getSingleDigitVoice( iNumber );
        }
        else if( iNumber>19 && iNumber<100 )
        {

            int iReminder = iNumber%10;
            int iTenPlace = iNumber - iReminder;

            if( iTenPlace == 20 )
            {
                sTenPlaceNumberVoiceName = "twenty_stereo.wav";
            }
            else if( iTenPlace == 30 )
            {
                sTenPlaceNumberVoiceName = "thirty_stereo.wav";
            }
            else if( iTenPlace == 40 )
            {
                sTenPlaceNumberVoiceName = "forty_stereo.wav";
            }
            else if( iTenPlace == 50 )
            {
                sTenPlaceNumberVoiceName = "fifty_stereo.wav";
            }
            else if( iTenPlace == 60 )
            {
                sTenPlaceNumberVoiceName = "sixty_stereo.wav";
            }
            else if( iTenPlace == 70 )
            {
                sTenPlaceNumberVoiceName = "seventy_stereo.wav";
            }
            else if( iTenPlace == 80 )
            {
                sTenPlaceNumberVoiceName = "eighty_stereo.wav";
            }
            else if( iTenPlace == 90 )
            {
                sTenPlaceNumberVoiceName = "ninety_stereo.wav";
            }


            if(iReminder>0)
            {
                sOnesPlace = getSingleDigitVoice( iReminder );
            }
        }


        numberVoiceBean.setNumber( iNumber );
        numberVoiceBean.setOnesPlace( sOnesPlace );
        numberVoiceBean.setTensPlace( sTenPlaceNumberVoiceName );

        return numberVoiceBean;
    }

}
