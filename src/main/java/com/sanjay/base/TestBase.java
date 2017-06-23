package com.sanjay.base;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.read.biff.BiffException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.sanjay.kwutils.KWTestExecuter;
import com.sanjay.utils.DateUtil;
import com.sanjay.utils.PageUtils;
import com.sanjay.utils.PropertiesUtil;
import com.sanjay.utils.ReportDetailStatus;
import com.sanjay.utils.Reportor;
import com.sanjay.utils.TwfException;

public class TestBase implements IHookable {

	// protected String uiStartURL;
	Process chrome;
	Process ie;
	Runtime rt;
	protected Log log = LogFactory.getLog(getClass());

	// private Hub hub;
	// private URL hubURL;

	public void startDriver() throws IOException{
		
		try{
		Runtime rt = Runtime.getRuntime() ;
		chrome= rt.exec(System.getProperty("user.dir")+File.separator+"ext"+File.separator+"chromedriver.exe") ;
		}catch(IOException e){
			//do nothing
			}
			
			try{
				Runtime rt = Runtime.getRuntime() ;
				ie= rt.exec(System.getProperty("user.dir")+File.separator+"ext"+File.separator+"iedriver.exe") ;
				}catch(IOException e){//do nothing
					}
	}
	
	public void stopDriver(){
		try{	
		chrome.destroy();
		}catch(Exception e){}
		try{	
			ie.destroy();
			}catch(Exception e){}
		
	}
	
	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() throws Exception {
		
		startDriver();

		try {
			new PropertiesUtil();
			PageUtils.baseURL = PropertiesUtil.getTestURL();

		} catch (Exception e) {

			throw new TwfException("properities initialization failed!: ");
		}
		// Create test-output folder if not available.
		try {
			FileUtils.deleteDirectory(new File(System.getProperty("user.dir")
					+File.separator+ "test-output"));
		} catch (Exception e) {
		}
		try {
			File testOutputFolder = new File(System.getProperty("user.dir")
					+ File.separator+ "test-output");
			if (!testOutputFolder.exists()) {
				FileUtils.forceMkdir(new File(System.getProperty("user.dir")
						+ File.separator+"test-output"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Update XML(s) with random SSNs "699-99-9101";
		// String outputFile =
		// "D:\\D_Drive\\QAAutomation\\trunk\\fireKW\\src\\test\\resources\\XMLData\\SampleTest.xml";
		// new XMLHandler(new
		// File("D:\\D_Drive\\QAAutomation\\trunk\\fireKW\\src\\test\\resources\\XMLData\\Test.xml"))
		// .updateXMLAttributeElementValue("/LOAN_APPLICATION/BORROWER/@_SSN",
		// SSN.getRandomSSN().toString(),
		// outputFile);
		// new XMLHandler(new
		// File("D:\\D_Drive\\QAAutomation\\trunk\\fireKW\\src\\test\\resources\\XMLData\\Test3.xml"))
		// .updateXMLAttributeElementValue("/LOAN_APPLICATION/BORROWER/@_SSN",
		// SSN.getRandomSSN().toString(),
		// outputFile);

	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws Exception {
		stopDriver();
		try {
			Reportor.writeReport();

			// Rename generate a copy of testoutput.
			String timeStamp = DateUtil.getTimeStamp();
			timeStamp = timeStamp.replaceAll("[\\/\\-\\:]", "_");
			FileUtils.copyDirectory(new File(System.getProperty("user.dir")+ File.separator+"test-output"), new File(System.getProperty("user.dir")+ File.separator+ "test-output_" + timeStamp));

		} catch (Exception e) {
			this.log.info("Reportor is not configured");
		}
		// Reset License

	}

	@BeforeMethod(alwaysRun = true)
	@Parameters({"browser"})
	public void beforeClass(@Optional("none")
	String browser) throws TwfException {
		if (browser.equals("none")) {
			DriverFactory.setBrowser(PropertiesUtil.getbrowserType());
		} else {
			DriverFactory.setBrowser(PropertiesUtil.getbrowserType(browser));
		}

	}

	@AfterMethod(alwaysRun = true)
	public void closeDriver() throws TwfException {
		if (DriverFactory.getBrowserAsString().equals("reusefirefox")) {
			DriverFactory.remove();
			return;
		}
		if (!DriverFactory.driverIsClosed()) {
			DriverFactory.getDriver().quit();
		}

	}

	public TestBase() {

	}

	/**
	 * Log the start and end of the test
	 * 
	 * @throws IOException
	 */
	public void run(final IHookCallBack arg0, ITestResult arg1) {
		Long methodRunTime;
		System.out.println("****** STARTING TEST "
				+ arg1.getName()+
				 " *****");
		this.log.info("****** STARTING TEST "
				+ arg1.getMethod().getMethodName() + "-"
				+ arg1.getMethod().getTestClass() + " *****");
		methodRunTime = System.currentTimeMillis();
		arg1.setAttribute("name", "value");
		arg0.runTestMethod(arg1);
		methodRunTime = System.currentTimeMillis() - methodRunTime;
		System.out.println("***** ENDING TEST " + arg1.getName()
				+ " *****");
		this.log.info("***** ENDING TEST " + arg1.getMethod().getMethodName()
				+ "-" + arg1.getMethod().getTestClass() + " *****");

	}
}
