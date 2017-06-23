package com.sanjay.base;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.sanjay.browsers.Browser.BrowserType;
import com.sanjay.utils.PropertiesUtil;
import com.sanjay.utils.RandomValue;
import com.sanjay.utils.TwfException;

/**
 * Using to create a driver instance (browser instance).
 * 
 * @author sanjay.joshi
 */
public class DriverFactory {
	private static ThreadLocal<MyWebDriver> driver = new ThreadLocal<MyWebDriver>();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.parseInt(RandomValue.getrandomValue());
		return result;
	}

	public static boolean driverIsClosed() {
		try {
			DriverFactory.driver.get().getDriver().getPageSource();
			return false;
		} catch (Exception e) {
			return true;
		}
	}

	public static WebDriver getDriver() throws TwfException {
		if (DriverFactory.driver.get() == null || DriverFactory.driverIsClosed()) {
			DriverFactory.initilaze();
		}

		return DriverFactory.driver.get().getDriver();
	}

	public static void remove() throws TwfException {

		DriverFactory.driver.remove();
	}

	public static BrowserType getBrowser() {
		return DriverFactory.driver.get().getBrowserType();
	}

	public static String getBrowserAsString() {
		return DriverFactory.driver.get().getBrowserType().getText();
	}

	public static void setBrowser(BrowserType btype) {
		MyWebDriver localDriver = new MyWebDriver();
		localDriver.setBrowserType(btype);
		DriverFactory.driver.set(localDriver);
	}

	private static boolean isSupportedPlatform() {
		Platform current = Platform.getCurrent();
		return Platform.MAC.is(current) || Platform.WINDOWS.is(current);
	}

	public static void initilaze() throws TwfException {
		MyWebDriver localDriver = driver.get();
		DesiredCapabilities capability = new DesiredCapabilities();
		FirefoxProfile firefoxProfile = null;
		File f = new File("./ffprofile");
		if (f.exists() && f.isDirectory()) {
		firefoxProfile = new FirefoxProfile(f);
		} else {
		firefoxProfile = new FirefoxProfile();
		}
		
		switch (localDriver.getBrowserType()) {
			case reusefirefox :
				firefoxProfile.setAcceptUntrustedCertificates(true);
				capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
				capability.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
				capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				capability.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
				capability.setPlatform(org.openqa.selenium.Platform.ANY);

				localDriver.setDriver(new ReuseFirefoxDriver(firefoxProfile));
				localDriver.getDriver().manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
				DriverFactory.driver.set(localDriver);
				break;
			case firefox :
				firefoxProfile.setAcceptUntrustedCertificates(true);
				capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
				capability.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
				capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				capability.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
				capability.setPlatform(org.openqa.selenium.Platform.ANY);

				localDriver.setDriver(new FirefoxDriver(firefoxProfile));
				localDriver.getDriver().manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
				DriverFactory.driver.set(localDriver);
				break;
			case firefox_parallel :
				capability.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
				FirefoxProfile profile = new FirefoxProfile();
				capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
				capability.setCapability(FirefoxDriver.PROFILE, profile);
				capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				capability.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
				capability.setPlatform(org.openqa.selenium.Platform.ANY);
				try {

					localDriver.setDriver(new RemoteWebDriver(new URL(PropertiesUtil.getProperty("remoteurl") + ":"
							+ PropertiesUtil.getProperty("remoteport") + "/wd/hub"), capability));
					localDriver.getDriver().manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
					DriverFactory.driver.set(localDriver);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error in connecting to RemoteHub");
				}
				break;
			case iexplore_remote :
				capability.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
				capability.setJavascriptEnabled(true);
				capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
				capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				capability.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
				capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				try {
					localDriver.setDriver(new RemoteWebDriver(new URL(PropertiesUtil.getProperty("remoteurl") + ":"
							+ PropertiesUtil.getProperty("remoteport") + "/wd/hub"), capability));
					localDriver.getDriver().manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
					DriverFactory.driver.set(localDriver);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//
				// InternetExplorerDriver ie = new InternetExplorerDriver(capability);
				// localDriver.setDriver(ie);
				break;
			case iexplore_parallel :
				throw new TwfException("IE Browser not supports for parallel execution..");

			case android :
				capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
				capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				capability.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
				//localDriver.setDriver(new AndroidDriver());
				DriverFactory.driver.set(localDriver);
				break;
			case iexplore :
				capability = DesiredCapabilities.internetExplorer();
				capability.setJavascriptEnabled(true);
				capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
				capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				capability.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
				capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				try {
					localDriver.setDriver(new RemoteWebDriver(new URL("http://127.0.0.1:5555"), capability));
					localDriver.getDriver().manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
					DriverFactory.driver.set(localDriver);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//
				// InternetExplorerDriver ie = new InternetExplorerDriver(capability);
				// localDriver.setDriver(ie);
				break;
			case safari :
				if (!isSupportedPlatform())
					throw new TwfException("No browser specified");
				localDriver.setDriver(new SafariDriver());
				localDriver.getDriver().manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
				DriverFactory.driver.set(localDriver);
				break;
			case chrome :
				
				try {
					capability = DesiredCapabilities.chrome();
					capability.setJavascriptEnabled(true);
					capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
					capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					capability.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
					localDriver.setDriver(new RemoteWebDriver(new URL("http://127.0.0.1:9515"), capability));
					localDriver.getDriver().manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
					DriverFactory.driver.set(localDriver);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// localDriver.setDriver(Chrome.getChromeDriver());
				break;

			default :
				throw new TwfException("No browser specified");

		}

	}
	
}

class MyWebDriver {
	private WebDriver driver;
	private BrowserType browserType;

	public WebDriver getDriver() {
		return this.driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public BrowserType getBrowserType() {
		return this.browserType;
	}

	public void setBrowserType(BrowserType browserType) {
		this.browserType = browserType;
	}
}
