package com.sanjay.utils;

import java.io.File;
import java.util.ResourceBundle;

import com.sanjay.browsers.Browser.BrowserType;

public class PropertiesUtil {

	private static ResourceBundle props;

	public PropertiesUtil() {
		String env = System.getProperty("env");
		// If no value for -Denv it will read default.properties file.
		if (env.equals("${env}")) {
			env = "default";
		}
		try {
			props = ResourceBundle.getBundle(env);
		} catch (Exception e) {
			throw new RuntimeException("Unable to load the property file", e);
		}
	}

	public static String getTestURL() {
		String prop = props.getString("url");
		return prop;
	}

	public static BrowserType getbrowserType(String browser) {
		if (browser.equals("android")) {
			return BrowserType.android;
		} else if (browser.equals("remote")) {
			return BrowserType.remote;
		} else if (browser.equals("iexplore")) {
			return BrowserType.iexplore;
		} else if (browser.equals("iexplore_parallel")) {
			return BrowserType.iexplore_parallel;
		} else if (browser.equals("firefox_parallel")) {
			return BrowserType.firefox_parallel;
		} else if (browser.equals("safari")) {
			return BrowserType.safari;
		} else if (browser.equals("chrome")) {
			return BrowserType.chrome;
		} else if (browser.equals("reusefirefox")) {
			return BrowserType.reusefirefox;
		} else if (browser.equals("iexplore_remote")) {
			return BrowserType.iexplore_remote;
		} else {

			return BrowserType.firefox;
		}
	}

	public static BrowserType getbrowserType() {
		return getbrowserType(props.getString("browser"));
	}

	public static String getProperty(String name) {
		String prop = props.getString(name);
		return prop;
	}

	public static String getTestCasePath() {
		return System.getProperty("user.dir") + File.separator +"src"+ File.separator +"test"+ File.separator+"resources"+ File.separator;
	}

	public static String getTestCaseFile() {
		return System.getProperty("user.dir") + File.separator +"src"+ File.separator +"test"+ File.separator+"resources"+ File.separator+"TestCases.xls";
	}

	public static void main(String arg[]) {
		System.out.println(getTestCaseFile());
	}
}
