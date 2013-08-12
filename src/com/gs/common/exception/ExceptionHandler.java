package com.gs.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ExceptionHandler
{

	public static String getStackTrace(Throwable exception)
	{
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		exception.printStackTrace(printWriter);
		return exception.getMessage() + "\n" + result.toString();
	}
}
