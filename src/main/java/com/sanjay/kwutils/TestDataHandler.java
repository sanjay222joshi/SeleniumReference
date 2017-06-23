package com.sanjay.kwutils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jxl.read.biff.BiffException;

import com.sanjay.utils.ExcelUtil;
import com.sanjay.utils.PropertiesUtil;

public class TestDataHandler {
	private static Map<String, String> testDataSets;

	public static Map<String, String> getTestDataSets() {
		return testDataSets;
	}


	

	private static int index;
	public static void loadTestData() throws BiffException, IOException {
		ExcelUtil excel = new ExcelUtil(PropertiesUtil.getTestCaseFile(),
				"TestDataSet");
		Object[][] data = excel.getSheetInfoAsObjectArray(false);
		excel.close();
		testDataSets = new HashMap<String, String>();

		for (Object[] element : data) {
			testDataSets.put(String.valueOf(element[1]) + ":"
					+ String.valueOf(element[2]), String.valueOf(element[3]));
		}
		System.out.println("TestDataHandler.loadTestData" + index++);

	}

	
	public static String getTestData(String key) throws BiffException,
			IOException {
		if (testDataSets == null) {
			loadTestData();
		}
		if (key.startsWith("var_")) {
			return key;
		} else if (testDataSets.containsKey(key)) {
			return testDataSets.get(key);
		} else {
			return "";
		}
	}
}
