package com.sanjay.pages;

import java.io.IOException;
import java.util.Map;

import jxl.read.biff.BiffException;

import org.openqa.selenium.WebElement;

import com.sanjay.base.DriverFactory;
import com.sanjay.base.WebPage;
import com.sanjay.utils.TwfException;

public class LoginPage extends WebPage {

	@Override
	public void checkPage() {
		// TODO Auto-generated method stub

	}

	public void Login(String data) throws TwfException {
		try {
			WebElement userId = getElementByUsing("UserID");
			WebElement passWord = getElementByUsing("Password");
			WebElement loginButton = getElementByUsing("SubmitButton");
			type(userId, "admin");
			type(passWord, "admin");
			loginButton.click();
		} catch (Exception e) {
			throw new TwfException(e.getMessage());
		}
	}

	public void Login(Map<String, String> data) throws BiffException, IOException, TwfException {
		System.out.println("Inside Login");
		System.out.println(DriverFactory.getDriver().hashCode());
		try {
			WebElement userId = getElementByUsing("UserID");
			WebElement passWord = getElementByUsing("Password");
			WebElement loginButton = getElementByUsing("SubmitButton");
			type(userId, data.get(("UserID")));
			type(passWord, data.get(("Password")));
			loginButton.click();
		} catch (Exception e) {
			throw new TwfException(e.getMessage());
		}
	}
}
