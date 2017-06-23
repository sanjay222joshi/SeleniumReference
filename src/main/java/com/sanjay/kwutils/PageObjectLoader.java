package com.sanjay.kwutils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jxl.read.biff.BiffException;

import com.sanjay.utils.ExcelUtil;
import com.sanjay.utils.PropertiesUtil;

public class PageObjectLoader {
	private static Map<String, PageObject> objectRepo;

	private static void loadObjectRepo() throws BiffException, IOException {
		ExcelUtil excel = new ExcelUtil(PropertiesUtil.getTestCasePath() + PropertiesUtil.getProperty("objectRepository"),
				"PageObject");

		Object[][] pageObject = excel.getSheetInfoAsObjectArray(false);
		excel.close();
		objectRepo = new HashMap<String, PageObject>();
		for (Object[] element : pageObject) {
			objectRepo.put(String.valueOf(element[1]), new PageObject(String.valueOf(element[1]), String
					.valueOf(element[2]), String.valueOf(element[3])));
		}

	}

	public static Map<String, PageObject> getObjectRepo() throws BiffException, IOException {
		if (objectRepo == null)
			loadObjectRepo();
		return objectRepo;
	}

	public static PageObject getPageObject(String key) throws BiffException, IOException {
		if (objectRepo == null)
			loadObjectRepo();
		return objectRepo.get(key);
	}
}
