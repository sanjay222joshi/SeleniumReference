
package com.sanjay.kwutils;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import jxl.read.biff.BiffException;

import com.sanjay.utils.ExcelUtil;
import com.sanjay.utils.PropertiesUtil;
/**
 * Used to read data from Action-Steps and supply back wherever required.
 * @author sanjay.joshi
 *
 */
public class CommonKWTestCase {
	
	protected  Log log = LogFactory.getLog(CommonKWTestCase.class);
	
	private Map<String, KWTestCase> commonKWTest; // Maps kw:xxxx with Test
	// Action details
	private Set<String> userDefinedKeyWords;

	public CommonKWTestCase() {
		this.commonKWTest = new HashMap<String, KWTestCase>();
		this.userDefinedKeyWords = new HashSet<String>();
	}

	// TODO
	public void linkActionWithInAnAction() {

	}

	/**
	 * Reads data from Actions_Step
	 * @throws BiffException
	 * @throws IOException
	 */
	public void loadCommonKWTestCase() throws BiffException, IOException {
		log.info("loadCommonKWTestCase");
		TestCaseHandler testCaseHandler = new TestCaseHandler();
		ExcelUtil excel = new ExcelUtil(PropertiesUtil.getTestCaseFile(),
				"Action-Steps");
		Object[][] data = excel.getSheetInfoAsObjectArray(false);
		excel.close();
		Boolean createtestCase = true;
		
		Map<Integer, Step> steps;

		int stepId = 0;

		steps = new HashMap<Integer, Step>();
		String curTestCase = String.valueOf(data[0][0]); // TestCaseID
		String testCaseId="";
		for (Object[] element : data) {
			
			testCaseId = String.valueOf(element[0]);
			this.userDefinedKeyWords.add(testCaseId);
			if (!curTestCase.equals(testCaseId)) {
			
				insertActionTest(testCaseHandler, createtestCase, steps,
						testCaseId);

				stepId = 0;
				steps = new HashMap<Integer, Step>();
				curTestCase = testCaseId;
			}
			
			Step myNewStep = new Step(String.valueOf(element[0]), String
					.valueOf(element[1]), String.valueOf(element[2]), Integer
					.toString(++stepId), String.valueOf(element[3]),
					getActualStep(String.valueOf(element[4])), String
							.valueOf(element[5]), String.valueOf(element[6]));
			
				insertStep(steps,myNewStep);
			
			

		}
		
		insertActionTest(testCaseHandler, createtestCase, steps, testCaseId);

	}

	private void insertActionTest(TestCaseHandler testCaseHandler,
			Boolean createtestCase, Map<Integer, Step> steps, String testCaseId) {
		if (createtestCase && !testCaseHandler.isTestIdAvailable(testCaseId)) {
			this.commonKWTest.put(steps.get(0).getTestCaseId(), new KWTestCase(
					steps, steps.size()));
		}
	}
	
	/**
	 * uses this method to insert a new step .or. insert action in case of if it is already available. 
	 * @param steps
	 * @param element
	 * @param counter
	 */
	private void insertStep(Map<Integer, Step> steps,Step myNewStep){
		log.info("Inside insert step..");
		if(this.userDefinedKeyWords.contains(myNewStep.getCommand())){
				//Put values one by one from parent Action
				Map<Integer, Step> existingSteps = commonKWTest.get(myNewStep.getCommand()).getSteps();
				
				for(Step existingStep:existingSteps.values()){
					
					Step newActionSteps = new Step(existingStep);
					newActionSteps.setTestCaseId(myNewStep.getTestCaseId());
					newActionSteps.setTestCaseDescription(myNewStep.getTestCaseDescription());
					newActionSteps.setDataValues(myNewStep.getDataValues());
					
					if(!myNewStep.getValue().isEmpty())
						newActionSteps.setValue(myNewStep.getValue());
					newActionSteps.setKwValueVariables(myNewStep.getKwValueVariables());
					
					steps.put(steps.size(),new Step(newActionSteps));
				}
		}
			else
				steps.put(steps.size(),new Step(myNewStep));
		
	}

	public String getActualStep(final String stepAction) throws BiffException,
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

	public KWTestCase getTestCase(String key) throws BiffException, IOException {
		if (this.commonKWTest == null) {
			loadCommonKWTestCase();
		}
		return this.commonKWTest.get(key);
	}

	public Boolean isUserDefinedTestCase(String key) throws BiffException,
			IOException {
		log.info("Inside isuserdefinedTestCase");
		if (this.commonKWTest == null) {
			loadCommonKWTestCase();
		}
		return this.userDefinedKeyWords.contains(key);
	}
}
