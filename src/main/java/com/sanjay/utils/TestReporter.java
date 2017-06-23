package com.sanjay.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.exec.OS;
import org.apache.commons.io.FileUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.log4testng.Logger;

import com.sanjay.base.DriverFactory;

public class TestReporter implements ITestListener {
	private static final Logger LOG = Logger.getLogger(TestReporter.class);

	public void onFinish(ITestContext context) {
		Reportor.endDate = context.getEndDate();
	}

	public void onStart(ITestContext context) {
		Reportor.startDate = context.getStartDate();
	}

	public void onTestFailure(ITestResult result) {
		String uniqueFileName = uniqueFileNameFrom(result);
		Reportor.failCounter++;

		try {
			PageUtils.takeScreenShot(uniqueFileName);
			writePageSource(uniqueFileName, DriverFactory.getDriver()
					.getPageSource());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TwfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ReportDetailStatus reportDetailStatus = new ReportDetailStatus();
		reportDetailStatus.setMethodeName(result.getName());
		reportDetailStatus.setStatus("Fail");
		reportDetailStatus.setDescription(getTestParametersAsString(result));
		reportDetailStatus.setErrorFile(uniqueFileName);
		reportDetailStatus.setReason("Failed TestCase :"
				+ getTestCaseId(result) + "<br>"
				+ result.getThrowable().getLocalizedMessage()
				+ "<br> Duration>" + getDuration(result));

		// reportDetailStatus.setReason("Failed TestCase :" +
		// getTestCaseId(result) + "<br>"
		// + result.getThrowable().getLocalizedMessage() +
		// "<br><u>StackTrace:</u><br><font color=\"#0000\">"
		// + getStackTrace(result.getThrowable()) + " </font><br> " +
		// "Duration>" + getDuration(result));

		Reportor.setReportDetailStatus(reportDetailStatus);
		LOG.info("Test" + result.getName() + " Failed..:");

	}

	public void onTestSkipped(ITestResult result) {
		String uniqueFileName = uniqueFileNameFrom(result);
		Reportor.skipCounter++;

		ReportDetailStatus reportDetailStatus = new ReportDetailStatus();
		reportDetailStatus.setMethodeName(result.getName());
		reportDetailStatus.setDescription(getTestParametersAsString(result));
		reportDetailStatus.setStatus("Skipped");
		reportDetailStatus.setReason("Dependency Failure\n" + "StackTrace"
				+ getStackTrace(result.getThrowable()) + " - " + "\n Duration>"
				+ getDuration(result));
		Reportor.setReportDetailStatus(reportDetailStatus);

		try {
			PageUtils.takeScreenShot(uniqueFileName);
			writePageSource(uniqueFileName, DriverFactory.getDriver()
					.getPageSource());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TwfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onTestStart(ITestResult result) {
	}

	public void onTestSuccess(ITestResult result) {
		Reportor.passCounter++;
		ReportDetailStatus reportDetailStatus = new ReportDetailStatus();
		reportDetailStatus.setMethodeName(result.getName());
		reportDetailStatus.setDescription(getTestParametersAsString(result));
		reportDetailStatus.setStatus("Pass");
		reportDetailStatus.setReason("\n Duration: " + getDuration(result));
		Reportor.setReportDetailStatus(reportDetailStatus);

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	private static String uniqueFileNameFrom(ITestResult result) {
		String fileName = getTestCaseId(result);
		fileName = fileName.replaceAll("[^a-zA-Z0-9]+", "_"); // fileName.replaceAll("[\\^\\\\.\\-:;#_]",
		// );
		if (fileName.length() > 256) {
			return fileName.substring(0, 50);
		} else {
			return fileName;
		}

	}

	// We know that the parameter is KWTestCase.
	private static String getTestParametersAsString(ITestResult result) {
		return result.getTestName();
		// Object[] parameters = result.getParameters();
		// for (Object parameter : parameters) {
		// KWTestCase testcase = (KWTestCase) parameter;
		//
		// return testcase.getStep(0).getTestCaseDescription()
		// + (testcase.getTestDataId() == null ? "" : " With TestData :" +
		// testcase.getTestDataId());
		// }
		// return "";
	}

	private static String getTestCaseId(ITestResult result) {
		return result.getTestName();
		// Object[] parameters = result.getParameters();
		// for (Object parameter : parameters) {
		// KWTestCase testcase = (KWTestCase) parameter;
		//			
		// return testcase.getStep(0).getTestCaseId()
		// + (testcase.getTestDataId() == null ? "" : " With TestData :" +
		// testcase.getTestDataId());
		// }
		// return "";
	}

	/*
	 * private static List<String> getTestParametersAsString(ITestResult
	 * result) { List<String> parts = new ArrayList<String>(); // add
	 * parameters Object[] parameters = result.getParameters(); for (Object
	 * parameter : parameters) { KWTestCase testcase = (KWTestCase)parameter;
	 * return testcase.getStep(0).getTestCaseDescription(); parts.add(parameter !=
	 * null ? parameter.toString() : ""); } return parts; }
	 */
	// private static String getTestParameters(ITestResult result) {
	// String reason = "";
	//
	// List<String> parts = new ArrayList<String>();
	// // parts.addAll(getTestParametersAsString(result));
	// parts.add(getTestParametersAsString(result));
	// if (parts.size() > 0) {
	// reason = "With Data: " + StringUtils.join(parts.toArray(new
	// String[parts.size()]), ",");
	// }
	// return reason;
	// }
	private static void writePageSource(String pageObjectName,
			String pageHtmlSrc) throws IOException {
		FileUtils.forceMkdir(new File(System.getProperty("user.dir")
				+  File.separator+"test-output"+ File.separator+"pageSource"+ File.separator
				+ DriverFactory.getBrowser().getText()));

		// File theDir = new File(System.getProperty("user.dir") +
		// "\\test-output\\pageSource\\"
		// + DriverFactory.getBrowser().getText());
		// if (!theDir.exists())
		// theDir.mkdir();
		PrintWriter pw = new PrintWriter(new File(System
				.getProperty("user.dir")
				+ File.separator+"test-output"+File.separator+"pageSource"+File.separator
				+ DriverFactory.getBrowser().getText()
				+ File.separator
				+ pageObjectName
				+ ".html"));
		pw.write(pageHtmlSrc);
		pw.close();
	}

	public static String getDuration(ITestResult result) {
		return CommonUtils.getDurationBreakdown(result.getEndMillis()
				- result.getStartMillis());
	}

	public static String getStackTrace(Throwable throwable) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		return writer.toString();
	}

	public static void main(String[] args) throws Exception {
		String str = "<sentence> Here is the few words sentence  in the output, you will see how to escape a String for "
				+ "HTML using StringEscapeUtils. </sentence>";
		
		// String results = StringEscapeUtils.escapeHtml(str);
	}
}
