package com.sanjay.kwutils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jxl.read.biff.BiffException;

import com.sanjay.utils.ExcelUtil;
import com.sanjay.utils.PropertiesUtil;

public class CustomKW {
	private static Map<String, String> variable;
	private static Map<String, String> aliasName;
	private static Map<String, String> actualKey;

	private static void loadCustomVariables() throws BiffException, IOException {
		ExcelUtil excel = new ExcelUtil(PropertiesUtil.getTestCasePath() + PropertiesUtil.getProperty("objectRepository"),
				"CustKeywords");
		Object[][] variablex = excel.getSheetInfoAsObjectArray(false);
		excel.close();
		variable = new HashMap<String, String>();
		aliasName = new HashMap<String, String>();
		actualKey = new HashMap<String, String>();
		for (Object[] element : variablex) {
			variable.put(String.valueOf(element[1]), String.valueOf(element[0]));
			aliasName.put(String.valueOf(element[3]), String.valueOf(element[1]));
			actualKey.put(String.valueOf(element[1]), String.valueOf(element[3]));
		}

	}

	public static Map<String, String> getCustomVariables() throws BiffException, IOException {
		if (variable == null)
			loadCustomVariables();
		return variable;
	}

	public static String getCustomMethodClass(String key) throws BiffException, IOException {
		if (variable == null)
			loadCustomVariables();
		return variable.get(key);
	}

	public static String getCustomMethodName(String key) throws BiffException, IOException {
		if (variable == null)
			loadCustomVariables();
		return aliasName.get(key);
	}

	public static String getKWMethodName(String key) throws BiffException, IOException {
		if (variable == null)
			loadCustomVariables();
		return actualKey.get(key);
	}
}
