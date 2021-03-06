package com.gs.common;

public class ParseUtil {
	public static Integer sToI(String sInput) {
		Integer iOutput = 0;
		if (sInput != null && !"".equalsIgnoreCase(sInput)) {
			try {
				iOutput = new Integer(sInput);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return iOutput;
	}

    public static boolean isValidInteger( String sInput ) {
        boolean isValidInteger = false;
        if (sInput != null && !"".equalsIgnoreCase(sInput)) {
            try {
                Integer iOutput = new Integer(sInput);
                isValidInteger = true;
            } catch (NumberFormatException e) {
                isValidInteger = false;
            }
        }
        return isValidInteger;
    }

	public static Integer iToI(int sInput) {
		Integer iOutput = 0;

		try {
			iOutput = new Integer(sInput);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return iOutput;
	}

	public static Long sToL(String sInput) {
		Long lOutput = 0L;
		if (sInput != null && !"".equalsIgnoreCase(sInput)) {
			try {
				lOutput = new Long(sInput);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return lOutput;
	}

	public static Double sToD(String sInput) {
		Double dOutput = 0.0;
		if (sInput != null && !"".equalsIgnoreCase(sInput)) {
			try {
				dOutput = new Double(sInput);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return dOutput;
	}

	public static boolean sTob(String sInput) {
		boolean bOutput = false;
		if (sInput != null && !"".equalsIgnoreCase(sInput)) {
			if ("true".equalsIgnoreCase(sInput) || "1".equalsIgnoreCase(sInput)
					|| "yes".equalsIgnoreCase(sInput)
					|| "y".equalsIgnoreCase(sInput)
                    || "on".equalsIgnoreCase(sInput)) {
				bOutput = true;
			}
		}

		return bOutput;
	}

	public static String checkNullObject(Object sInput) {
		String sOutput = "";
		if (sInput != null) {
			sOutput = checkNull(sInput.toString());

		}
		return sOutput;
	}

	public static String checkNull(String sInput) {
		String sOutput = "";
		if (sInput != null && !"".equalsIgnoreCase(sInput)) {
			sOutput = sInput.trim();
		}
		return sOutput;
	}

	public static String iToS(Integer iInput) {
		return Integer.toString(iInput);
	}

    public static String dToS(Double dInput) {
        return Double.toString(dInput);
    }

    public static String LToS(Long lInput) {
        return Long.toString(lInput);
    }

    public static Integer LToI(Long lInput) {
        return lInput.intValue();
    }
}
