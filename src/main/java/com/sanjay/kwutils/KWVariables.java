package com.sanjay.kwutils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jxl.read.biff.BiffException;

import com.sanjay.utils.ExcelUtil;
import com.sanjay.utils.PropertiesUtil;

public class KWVariables {
	private static Map<String, String> variable;

	private static void loadVariables() throws BiffException, IOException {
		ExcelUtil excel = new ExcelUtil(PropertiesUtil.getTestCasePath()
				+ PropertiesUtil.getProperty("objectRepository"), "CommonTestData");

		Object[][] variablex = excel.getSheetInfoAsObjectArray(false);
		excel.close();
		variable = new HashMap<String, String>();
		for (Object[] element : variablex) {
			variable.put(String.valueOf(element[0]), String.valueOf(element[1]));
		}

	}

	public static Map<String, String> getVariables() throws BiffException, IOException {
		if (variable == null)
			loadVariables();
		return variable;
	}
}
