package dev.blackcat.porlalivre.utils;

public class StringUtils
{

	public static String capitalize(String line)
	{
	  return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}
	
	public static String cropText(String text, int maxLenght)
	{
		if (text.length() <= maxLenght)
			return text;
		
		int pos = maxLenght;
		while (pos > 0 && Character.isLetterOrDigit(text.charAt(pos)))
			pos--;
		while (pos > 0 && !Character.isLetterOrDigit(text.charAt(pos)))
			pos--;
		if (pos == 0)
			pos = maxLenght;
		return text.substring(0, pos + 1) + "...";
	}

	public static String formatCurrency(double number)
	{
		int intPart = (int)number;
		double rest = number - intPart;
		rest = rest * 100;
		int decPart = (int)rest;

		String intStr = Integer.toString(intPart);
		String decStr = Integer.toString(decPart);
		if (decStr.length() == 1)
			decStr = "0" + decStr;

		return intStr + "." + decStr;
	}

}
