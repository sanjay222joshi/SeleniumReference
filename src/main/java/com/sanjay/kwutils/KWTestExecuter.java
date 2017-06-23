package com.sanjay.kwutils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.read.biff.BiffException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.DataProvider;

import com.sanjay.utils.ExcelUtil;
import com.sanjay.utils.PageUtils;
import com.sanjay.utils.PropertiesUtil;
import com.sanjay.utils.TwfException;
public class KWTestExecuter {
	protected static Log log = LogFactory.getLog(KWTestExecuter.class);
	@DataProvider(parallel = true)
	public static Iterator<Object[]> supplyTestCases() throws BiffException,
			IOException, CloneNotSupportedException, TwfException {
		List<Object[]> testsAsObjects = new ArrayList<Object[]>();

		List<KWTestCase> tests = getTestCaseInstances();

		for (KWTestCase testCase : tests) {
			testsAsObjects.add(new Object[]{testCase});
		}

		return testsAsObjects.iterator();
	}

	/**
	 * Read data from Excel sheet(s).
	 * 
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 * @throws CloneNotSupportedException
	 * @throws TwfException
	 */
	public static List<KWTestCase> getTestCaseInstances() throws BiffException,
			IOException, CloneNotSupportedException, TwfException {
		log.info("Inside getTestCaseInstances");
		loadPropertyFile();

		List<String> groups = Arrays.asList(PropertiesUtil.getProperty(
				"groupsToExecute").split(","));
		List<String> sheetNames = Arrays.asList(PropertiesUtil.getProperty(
				"sheetName").split(","));

		Boolean createtestCase = true;
		String testCaseId = "";
		List<KWTestCase> testCases = new ArrayList<KWTestCase>();
		Map<Integer, Step> steps = new HashMap<Integer, Step>();

		ExcelUtil excel = new ExcelUtil(PropertiesUtil.getTestCaseFile(),
				sheetNames.get(0));

		for (int i = 0; i < sheetNames.size(); i++) {
			excel.setSheetName(sheetNames.get(i));

			int stepId = 0;
			createtestCase = true;
			Object[][] data = excel.getSheetInfoAsObjectArray(false);
			steps = new HashMap<Integer, Step>();

			String curTestCase = String.valueOf(data[0][0]); // TestCaseID
			String curgroupName = String.valueOf(data[0][2]); // GroupName
			String valueType = String.valueOf(data[0][6]); // value

			for (Object[] element : data) {

				testCaseId = String.valueOf(element[0]);

				if (!curTestCase.equals(testCaseId)) {
					if (createtestCase) {
						steps = addCustomKWStepsIfAny(steps);
						// Before Adding a testcase , check if it is simple test
						// or linked with DataPool
						if (valueType.startsWith("dp")) {
							createDataDrivenTestCases(testCases, steps,
									valueType);
						} else {
							testCases.add(new KWTestCase(steps, steps.size()));
						}
					}

					stepId = 0;
					createtestCase = true;
					steps = new HashMap<Integer, Step>();
					curTestCase = testCaseId;
					curgroupName = String.valueOf(element[2]);
					valueType = String.valueOf(element[6]);
				}
				Collection<String> list1 = new ArrayList<String>(groups);
				Collection<String> list2 = new ArrayList<String>(Arrays
						.asList(curgroupName.split(",")));
				list2.retainAll(list1);

				if (steps.size() == 0 && !(list2.size() > 0)) {
					createtestCase = false;
				}

				if (createtestCase) {
					if (String.valueOf(element[6]).startsWith("dp:")) {
						valueType = String.valueOf(element[6]);
					}
					steps.put(steps.size(), new Step(
							String.valueOf(element[0]), String
									.valueOf(element[1]), String
									.valueOf(element[2]), Integer
									.toString(++stepId), String
									.valueOf(element[3]), getActualStep(String
									.valueOf(element[4])), String
									.valueOf(element[5]), String
									.valueOf(element[6])));
				}
			}

			if (createtestCase) {
				steps = addCustomKWStepsIfAny(steps);
				// Before Adding a testcase , check if it is simple test or
				// linked with DataPool
				if (valueType.startsWith("dp")) {
					createDataDrivenTestCases(testCases, steps, valueType);
				} else {
					testCases.add(new KWTestCase(steps, steps.size()));
				}
			}
		}

		excel.close();

		return testCases;

		// Chop tests to support for parallel execution
		// List<List<KWTestCase>> choppedTests = chopped(testCases, 4);
		// Object[] result = new Object[choppedTests.size()];
		// for (int i = 0; i < choppedTests.size(); i++)
		// result[i] = new TestCaseExecuter(choppedTests.get(i));
		// return result;

	}

	private static void loadPropertyFile() throws TwfException {
		try {
			new PropertiesUtil();
			PageUtils.baseURL = PropertiesUtil.getTestURL();

		} catch (Exception e) {
			throw new TwfException("properities initialization failed!: " + e);
		}
	}

	/*
	 * Add action steps to Scenarios steps.
	 */
	private static Map<Integer, Step> addCustomKWStepsIfAny(
			final Map<Integer, Step> steps) throws BiffException, IOException,
			CloneNotSupportedException {
		Map<Integer, Step> newSteps = new HashMap<Integer, Step>();
		String testCaseDescription = steps.get(0).getTestCaseDescription();
		String testCaseID = steps.get(0).getTestCaseId();
		CommonKWTestCase commonKWTestCase = new CommonKWTestCase();
		commonKWTestCase.loadCommonKWTestCase();
		int counter = 0;
		for (int i = 0; i < steps.size(); i++) {

			if (commonKWTestCase.isUserDefinedTestCase(steps.get(i)
					.getCommand())) {

				KWTestCase newTestCase = new KWTestCase(commonKWTestCase
						.getTestCase(steps.get(i).getCommand())); // Ref
				// Actual Step
				String testDataId = steps.get(i).getValue();
				for (int j = 0; j < newTestCase.getTotalSteps(); j++) {
					newTestCase.getStep(j).setTestCaseId(testCaseID);
					newTestCase.getStep(j).setTestCaseDescription(
							testCaseDescription);
					// Check if the step is assigned to TestDataID , if so get
					// the value and assign.
					// Verified only for Action Level - not at step-level
					if (testDataId.startsWith("TD_")) {
						if (!TestDataHandler.getTestData(
								testDataId
										+ ":"
										+ newTestCase.getStep(j)
												.getTargetElement())
								.equalsIgnoreCase("")) {
							if (!newTestCase.getStep(j).getValue().startsWith(
									"var_")) {

								newTestCase.getStep(j).setValue(testDataId);
							}
						}
					}
					newSteps.put(counter++, new Step(newTestCase.getStep(j)));
				}
			} else {
				newSteps.put(counter++, new Step(steps.get(i)));
			}

		}
		return newSteps;
	}

	// chops a list into non-view sublists of length L
	static <T> List<List<T>> chopped(List<T> list, final int length) {
		List<List<T>> parts = new ArrayList<List<T>>();
		final int N = list.size();
		for (int i = 0; i < N; i += length) {
			parts
					.add(new ArrayList<T>(list.subList(i, Math.min(N, i
							+ length))));
		}
		return parts;
	}

	private static void createDataDrivenTestCases(List<KWTestCase> testCases,
			Map<Integer, Step> steps, String dataSheet) throws BiffException,
			IOException {
		String[] excelDataSheet = dataSheet.split(":");

		String filePath = excelDataSheet.length > 3
				? System.getProperty("user.dir") + File.separator +"src"+ File.separator +"test"+ File.separator+"resources"+ File.separator
						+ excelDataSheet[3]
				: PropertiesUtil.getTestCaseFile();

		ExcelUtil excel = new ExcelUtil(filePath, excelDataSheet[1]);
		String[][] dataValues = (String[][]) excel
				.getSheetInfoAsObjectArray(true);
		// Convert to Map
		Map<Integer, Map<String, String>> testData = convertDataSheetToMap(dataValues);
		Map<Integer, Step> newSteps;
		String testDataId = "";

		// Pull only specified data
		String specificData = "";
		if (excelDataSheet.length > 2) {
			specificData = excelDataSheet[2];
		}
		for (int i = 1; i < dataValues.length; i++) {

			if (specificData.equalsIgnoreCase("")
					|| specificData.equalsIgnoreCase(testData.get(i - 1).get(
							"TestDataId"))) {
				newSteps = new HashMap<Integer, Step>();
				for (int j = 0; j < steps.size(); j++) {
					Step actualStep = new Step(steps.get(j));
					if (actualStep.getCommand().startsWith("cx:")) {
						actualStep.setDataValues(testData.get(i - 1));
					}
					if (actualStep.getCommand().startsWith("c:")
							|| actualStep.getCommand().startsWith("cx:")) {
						// if "-" takes data from Datapool while execution the
						// step.
						if (actualStep.getValue().equalsIgnoreCase("-")
								|| actualStep.getValue().equalsIgnoreCase("--")) {
							actualStep.setDataValues(testData.get(i - 1));
						} else if (!actualStep.getValue().startsWith("var_")) {
							if (!actualStep.getTargetElement().equals("")) {
								// For Custom Method-If target provided update
								// the value otherwise
								actualStep.setValue(testData.get(i - 1).get(
										actualStep.getTargetElement()));
							}

						}
						testDataId = testData.get(i - 1).get("TestDataId");
					} else {
						if (testData.get(i - 1).containsKey(
								actualStep.getTargetElement())) {
							if (!actualStep.getValue().startsWith("var_")) {
								actualStep.setValue(testData.get(i - 1).get(
										actualStep.getTargetElement()));
							}

						}
						testDataId = testData.get(i - 1).get("TestDataId");
					}

					newSteps.put(j, actualStep);
				}
				testCases.add(new KWTestCase(newSteps, newSteps.size(),
						testDataId));

			}

			// End For Loop
			// if(specificData.equalsIgnoreCase(testData.get(i -
			// 1).get("TestDataId")) && !specificData.equalsIgnoreCase(""))
			// return;

		}

	}

	public static Map<Integer, Map<String, String>> convertDataSheetToMap(
			String[][] dataValues) {
		Map<Integer, Map<String, String>> dataMap = new HashMap<Integer, Map<String, String>>();
		for (int i = 1; i < dataValues.length; i++) {
			Map<String, String> rowData = new HashMap<String, String>();
			for (int j = 0; j < dataValues[i].length; j++) {
				rowData.put(dataValues[0][j], dataValues[i][j]);
			}
			dataMap.put(i - 1, rowData);
		}
		return dataMap;
	}

	public static String getActualStep(String stepAction) throws BiffException,
			IOException {
		String action;
		if ((action = CustomKW.getCustomMethodName(stepAction)) != null) {
			return action;
		}
		if ((action = SanjayKeyWord.getKWName(stepAction)) != null) {
			return action;
		}

		return stepAction;
	}
}
