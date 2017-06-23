package com.sanjay.kwutils;

import java.io.IOException;
import java.util.List;

import jxl.read.biff.BiffException;

import org.testng.ITest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.sanjay.base.DriverFactory;
import com.sanjay.base.TestBase;
import com.sanjay.utils.PageUtils;
import com.sanjay.utils.PropertiesUtil;
import com.sanjay.utils.TwfException;

public class TestCaseExecuter extends TestBase implements ITest{

	private List<KWTestCase> testCases;

	TestCaseExecuter(List<KWTestCase> tc) {
		this.testCases = tc;
	}

	@DataProvider(name = "DP1", parallel = true)
	public Object[][] createData() throws BiffException, IOException {
		return getTestCaseAsData();
	}

	@Test(dataProvider = "DP1", groups = "paralleltest")
	public void testCase(KWTestCase testcase) throws IOException, TwfException, BiffException, InterruptedException {
		runTest(testcase);
	}

	@DataProvider(name = "DP2")
	public Object[][] createData2() throws BiffException, IOException {
		return getTestCaseAsData();
	}

	@Test(groups = "normaltest", dataProvider = "DP2")
	public void testCases(KWTestCase testcase) throws IOException, TwfException, BiffException, InterruptedException {
		runTest(testcase);
	}

	private Object[][] getTestCaseAsData() {
		int counter = 0;
		Object[][] retObjArr = new Object[this.testCases.size()][1];
		for (KWTestCase x : this.testCases) {
			retObjArr[counter++][0] = x;
		}
		return retObjArr;
	}

	private void runTest(KWTestCase testcase) throws TwfException {
		System.out.println("testcase.getTotalSteps()" + testcase.getTotalSteps() + "TestCase "
				+ testcase.getStep(0).getTestCaseDescription());
		Step step = null;
		try {
			DriverFactory.getDriver().get(PropertiesUtil.getTestURL());
			StepExecutor page = PageUtils.create(StepExecutor.class);
			for (int i = 0; i < testcase.getTotalSteps(); i++) {

				step = testcase.getStep(i);
				if (i != 0) // Used to handle store method
					step.setKwValueVariables(testcase.getStep(i).getKwValueVariables());
				System.out.println("At RunTest>>");
				System.out.println("At RunTest"+step.getDataValues().size());
				page.execute(step, DriverFactory.getDriver());

			}
		} catch (Exception e) {

			if (!e.getClass().equals(TwfException.class))
				throw new TwfException("Error in executing <br> <b>Step Details:</b> " + step.toString() + "<br>");
			else
				throw (TwfException) e;

		}
	}

	
	
	@AfterMethod(alwaysRun = true)
	public void afterMethod() throws TwfException {
		DriverFactory.getDriver().quit();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.testCases == null) ? 0 : this.testCases.hashCode());
		return result;
	}

	public String getTestName() {
		return "";
	}

}


