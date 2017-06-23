package com.sanjay.kwutils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jxl.read.biff.BiffException;

import com.sanjay.utils.ExcelUtil;
import com.sanjay.utils.PropertiesUtil;

public class SanjayKeyWord {
	private static Map<String, String> aliasName;
	private static Map<String, String> actualName;

	private static void loadCustomVariables() throws BiffException, IOException {
		ExcelUtil excel = new ExcelUtil(PropertiesUtil.getTestCasePath() + PropertiesUtil.getProperty("objectRepository"),
				"KeyWords");
		Object[][] variablex = excel.getSheetInfoAsObjectArray(false);
		excel.close();
		aliasName = new HashMap<String, String>();
		actualName = new HashMap<String, String>();
		for (Object[] element : variablex) {
			aliasName.put(String.valueOf(element[1]), String.valueOf(element[0]));
			actualName.put(String.valueOf(element[0]), String.valueOf(element[1]));
		}

	}

	public static String getKWName(String key) throws BiffException, IOException {
		if (aliasName == null)
			loadCustomVariables();
		return aliasName.get(key);
	}

	public static String getActualKWName(String key) throws BiffException, IOException {
		if (aliasName == null)
			loadCustomVariables();
		return actualName.get(key);
	}
}
