package com.sanjay.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

import com.sanjay.base.DriverFactory;
import com.sanjay.base.WebPage;
import com.sanjay.browsers.Browser.BrowserType;
import com.sanjay.kwutils.CustomStep;
import com.sanjay.kwutils.Step;

public class PageUtils {
	public static String baseURL = "";
	protected static Log log = LogFactory.getLog(PageUtils.class);

	public static <T extends WebPage> T create(Class<T> type)
			throws IOException, TwfException {
		T pageObject = PageFactory
				.initElements(DriverFactory.getDriver(), type);
		// PageUtils.writePageSource(pageObject.getClass().getName(),
		// DriverFactory.getDriver().getPageSource());
		pageObject.checkPageId();
		return pageObject;
	}

	public static <T extends CustomStep> T create(Class<T> type, Step s)
			throws IOException, TwfException {
		T pageObject = PageFactory
				.initElements(DriverFactory.getDriver(), type);
		// PageUtils.writePageSource(pageObject.getClass().getName(),
		// DriverFactory.getDriver().getPageSource());
		pageObject.setStepData(s);
		pageObject.checkPageId();
		return pageObject;
	}
	public static String getURL(String startPath) throws MalformedURLException {
		if (startPath.equals("")) {
			return new URL(baseURL).toString();
		} else {
			return new URL(baseURL + "/" + startPath).toString();
		}
	}

	public static <T extends WebPage> T gotoURLandGetPageObject(Class<T> type)
			throws IOException, TwfException {
		DriverFactory.getDriver().get(baseURL);
		return PageUtils.create(type);

	}

	public static <T extends WebPage> T gotoURLandGetPageObject(String url,
			Class<T> type) throws IOException, TwfException {
		DriverFactory.getDriver().get(PageUtils.getURL(url));
		return PageUtils.create(type);

	}

	public static <T extends WebPage> T loadURLandGetPageObject(String url,
			Class<T> type) throws IOException, TwfException {
		DriverFactory.getDriver().get(new URL(url).toString());
		return PageUtils.create(type);

	}

	public static void takeScreenShot(String fileName) throws TwfException {
		File scrFile = null;
		if (DriverFactory.getBrowser() == null) {
			throw new TwfException("Browser is not yet initilized..");
		}
		if (DriverFactory.getBrowser().equals(BrowserType.android)
				|| DriverFactory.getBrowser().equals(BrowserType.chrome)
				|| DriverFactory.getBrowser().equals(BrowserType.iexplore)
				|| DriverFactory.getBrowser().equals(
						BrowserType.iexplore_remote)) {
			try {
				WebDriver augmentedDriver = new Augmenter()
						.augment(DriverFactory.getDriver());
				scrFile = ((TakesScreenshot) augmentedDriver)
						.getScreenshotAs(OutputType.FILE);

				// scrFile = ((AndroidDriver) DriverFactory.getDriver())
				// .getScreenshotAs(OutputType.FILE);
			} catch (WebDriverException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TwfException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (DriverFactory.getBrowser().equals(BrowserType.remote)
				|| DriverFactory.getBrowser().equals(
						BrowserType.firefox_parallel)

		) {
			try {
				ScreenShot ss = new ScreenShot((RemoteWebDriver) DriverFactory
						.getDriver());
				scrFile = ss.getScreenshotAs(OutputType.FILE);
			} catch (TwfException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				scrFile = ((TakesScreenshot) DriverFactory.getDriver())
						.getScreenshotAs(OutputType.FILE);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {

			FileUtils.copyFile(scrFile,
					new File(System.getProperty("user.dir")
							+ File.separator+"test-output"+ File.separator+"screenshots"+ File.separator
							+ (DriverFactory.getBrowser().getText().equals(
									"iexplore_remote")
									? "iexplore"
									: DriverFactory.getBrowser().getText())
							+  File.separator + fileName + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void writePageSource(String pageObjectName, String pageHtmlSrc)
			throws IOException {

		// new PrintWriter(new BufferedWriter(new FileWriter(new File("dir",
		// "filename.html"))));
	}
}
