package com.sanjay.kwutils;

import java.io.IOException;

import jxl.read.biff.BiffException;

import org.testng.ITest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.sanjay.base.DriverFactory;
import com.sanjay.base.TestBase;
import com.sanjay.utils.PageUtils;
import com.sanjay.utils.PropertiesUtil;
import com.sanjay.utils.Reportor;
import com.sanjay.utils.TwfException;

public class MyTestCaseExecuter extends TestBase implements ITest {

	private KWTestCase testCase = null;

	@Factory(dataProviderClass = com.sanjay.kwutils.KWTestExecuter.class, dataProvider = "supplyTestCases")
	public MyTestCaseExecuter(KWTestCase tc) {
		this.testCase = tc;
	}

	@Test
	public void testCase() throws IOException, TwfException, BiffException,
			InterruptedException, CloneNotSupportedException {
		/*System.out.println("Starting.. " + testCase.getTestDataId() + ">"
				+ testCase.getStep(0).getTestCaseDescription());*/
		runTest();
		
		/*System.out.println(testCase.getTestDataId() + ">"
				+ testCase.getStep(0).getTestCaseDescription() + " Ended");*/
	}

	private void runTest() throws TwfException{
		Step step = null;
		try {
			DriverFactory.getDriver().get(PropertiesUtil.getTestURL());
			StepExecutor page = PageUtils.create(StepExecutor.class);
			for (int i = 0; i < this.testCase.getTotalSteps(); i++) {

				step = this.testCase.getStep(i);
				if (i != 0) {
					step.setKwValueVariables(this.testCase.getStep(i - 1)
							.getKwValueVariables());
				}

				page.execute(step, DriverFactory.getDriver());

			}
		} catch (NullPointerException np) {
			throw new TwfException(
					"Error in executing <br> <b>Step Details:</b> "
							+ step.toString() + "<br>");
		} catch (Exception e) {

			if (!e.getClass().equals(TwfException.class)) {
				throw new TwfException(
						"Error in executing <br> <b>Step Details:</b> "
								+ step.toString() + "<br>");
			} else {
				throw (TwfException) e;
			}

		}
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod() throws BiffException, IOException, CloneNotSupportedException,TwfException {
		System.out.println("====================================================================================");
		System.out.println("Total ::: No Of Test cases :" +KWTestExecuter.getTestCaseInstances().size() + "::: PASS:"+Reportor.passCounter + ":::Fail:"+Reportor.failCounter+":::Skipped:"+Reportor.skipCounter);
		System.out.println("====================================================================================");
		DriverFactory.getDriver().quit();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (this.testCase == null ? 0 : this.testCase.hashCode());
		return result;
	}

	public String getTestName() {
		return this.testCase.getStep(0).getTestCaseId()
				+ ":"
				+ this.testCase.getStep(0).getTestCaseDescription()
				+ (this.testCase.getTestDataId() == null
						? ""
						: " With TestData :" + this.testCase.getTestDataId());
	}

}
