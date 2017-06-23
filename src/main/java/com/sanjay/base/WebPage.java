package com.sanjay.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import jxl.read.biff.BiffException;

import org.apache.commons.exec.OS;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.internal.seleniumemulation.JavascriptLibrary;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sanjay.kwutils.PageObject;
import com.sanjay.kwutils.PageObjectLoader;
import com.sanjay.utils.CommonUtils;
import com.sanjay.utils.PageUtils;
import com.sanjay.utils.PropertiesUtil;
import com.sanjay.utils.TwfException;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

/**
 * @author sanjay.joshi
 */
public abstract class WebPage {

	public static final String WAIT_TIME = "7000";
	public static final String LONG_WAIT_TIME = "300000";
	private static final long DEFAULT_TIMEOUT = 5000;

	// private TestContext context;

	abstract public void checkPage();

	final public void checkPageId() throws TwfException {
		// this.context = new WebDriverTestContext(DriverFactory.getDriver());
		this.checkPage();
	}

	/**
	 * Get HTML source
	 * 
	 * @return String
	 * @throws TwfException
	 */
	private String getHTMLSourceMsg() throws TwfException {
		String htmlSource = DriverFactory.getDriver().getPageSource();
		return htmlSource;
	}

	/**
	 * @param <T>
	 * @param webelement
	 * @param T
	 * @return T
	 * @throws IOException
	 * @throws TwfException
	 */
	public <T extends WebPage> T click(WebElement webelement, Class<T> T)
			throws IOException, TwfException {
		webelement.click();
		return PageUtils.create(T);
	}

	public <T extends WebPage> T click(By by, Class<T> T) throws IOException,
			TwfException {
		DriverFactory.getDriver().findElement(by).click();
		return PageUtils.create(T);
	}

	/**
	 * windowMaximize
	 * 
	 * @throws TwfException
	 */
	public void windowMaximize() throws TwfException {
		/*
		 * ((JavascriptExecutor) DriverFactory.getDriver()) .executeScript("if
		 * (window.screen){window.moveTo(0,
		 * 0);window.resizeTo(window.screen.availWidth,window.screen.availHeight);};");
		 */
        WebDriver drv = DriverFactory.getDriver();
        if (OS.isFamilyMac()) {
              Point targetPosition = new Point(0, 0);
              drv.manage().window().setPosition(targetPosition);

              String w = "return screen.availWidth";
              String h = "return screen.availHeight";
              int width = ((Long) ((JavascriptExecutor) drv).executeScript(w))
                          .intValue();
              int height = ((Long) ((JavascriptExecutor) drv).executeScript(h))
                          .intValue();
              Dimension targetSize = new Dimension(width, height);
              drv.manage().window().setSize(targetSize);
              return;
        }

		DriverFactory.getDriver().manage().window().maximize();
	}

	/**
	 * To clear the contents and input value.
	 * 
	 * @param webElement
	 * @param value
	 */
	public void clearAndSendKeys(WebElement webElement, String value) {
		webElement.clear();
		webElement.sendKeys(value);
	}

	/**
	 * To input value.
	 * 
	 * @param webElement
	 * @param value
	 */
	public void clearAndtype(WebElement webElement, String value) {
		webElement.clear();
		webElement.sendKeys(value);
	}

	/**
	 * To Set a value to a webelement which is having 'value' attribute
	 * 
	 * @param webElement
	 * @param value
	 * @throws TwfException
	 */
	public void setValue(WebElement webElement, String value)
			throws TwfException {
		((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
				"arguments[0].setAttribute('value',arguments[1]);", webElement,
				value);

	}

	/**
	 * To clear the contents and input value.
	 * 
	 * @param webElement
	 * @param value
	 */
	public void type(WebElement webElement, String value) {
		webElement.sendKeys(value);

	}

	public void typeAndTab(WebElement webElement, String value) {
		webElement.sendKeys(value, Keys.TAB);

	}

	/*public void typeAndBlur(WebElement webElement) throws TwfException {
		JavascriptLibrary javascript = new JavascriptLibrary();
		javascript.callEmbeddedSelenium(DriverFactory.getDriver(),
				"triggerEvent", webElement, "onblur");
	}*/

	/**
	 * Performs doubleClick operation on the given element Note: Supports for
	 * FireFox.
	 * 
	 * @param webElement
	 * @throws TwfException
	 */
	public void doubleClick(WebElement webElement) throws TwfException {
		((JavascriptExecutor) DriverFactory.getDriver())
				.executeScript(
						"var evt = document.createEvent('MouseEvents');"
								+ "evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
								+ "arguments[0].dispatchEvent(evt);",
						webElement);

		// For IE
		// ((JavascriptExecutor)driver).executeScript("arguments[0].fireEvent('ondblclick');",
		// webElement);
	}

	/**
	 * Execute a javascript method available in page. executeScript("return
	 * jscript;");
	 * 
	 * @param js
	 * @throws TwfException
	 */
	public String executeJSScript(String js) throws TwfException {
		return (String) ((JavascriptExecutor) DriverFactory.getDriver())
				.executeScript(js);

	}

	public void waitFor(long timeOut) {
		try {
			Thread.sleep(timeOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void waitForgiventime(String timeOut) {
		try {
			
			Thread.sleep(Long.parseLong(timeOut));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Default timeout set to 3000ms
	 */
	public void waitUntilDefaultTimeout() {
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Uses selenium waitForPageToLoad
	 * 
	 * @throws Exception
	 */
	public static void waitForPageToLoad() throws Exception {
		WebDriverBackedSelenium driver = new WebDriverBackedSelenium(
				DriverFactory.getDriver(), PropertiesUtil.getTestURL());
		driver.waitForPageToLoad(WAIT_TIME);
	}

	/**
	 * Uses selenium waitForPageToLoad
	 * 
	 * @throws Exception
	 */
	public static void waitForPageToLoad(String timeToWait) throws Exception {
		WebDriverBackedSelenium driver = new WebDriverBackedSelenium(
				DriverFactory.getDriver(), PropertiesUtil.getTestURL());
		driver.waitForPageToLoad(timeToWait);
	}

	/**
	 * isElementPresent
	 * 
	 * @param by
	 * @return boolean
	 */
	public boolean isElementPresent(By by) {
		try {
			DriverFactory.getDriver().findElement(by);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// KW Method
	public void isElementPresent(String element) throws BiffException,
			IOException, TwfException {
		getElementByUsing(element);
	}

	public void isElementNotPresent(String element) throws BiffException,
			IOException, TwfException {
		try {
			getElementByUsing(element);
		} catch (TwfException exception) {
			return;
		}
		throw new TwfException("Expecting that " + element
				+ " should not present, But element is available");
	}

	public boolean isElementDisplayed(By by) {
		try {
			return DriverFactory.getDriver().findElement(by).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Find button by its label
	 * 
	 * @param label
	 * @return WebElement
	 * @throws TwfException
	 */
	protected WebElement button(String label) throws TwfException {
		try {
			return DriverFactory.getDriver().findElement(
					By.xpath("//input[@type = 'submit' and @value = '" + label
							+ "']"));
		} catch (NoSuchElementException e) {
			for (WebElement button : DriverFactory.getDriver().findElements(
					By.xpath("//button"))) {
				if (button.getText().equals(label)) {
					return button;
				}
			}
			throw e;
		}
	}

	/**
	 * Wait for element until element visible until given number of seconds
	 */
	public void waitForElement(final By by, long timeout) throws TwfException {
		(new WebDriverWait(DriverFactory.getDriver(), timeout))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return isElementPresent(by);
					}
				});

	}

	/**
	 * Wait for element until element visible until given number of seconds
	 * 
	 * @param element
	 * @param timeout
	 * @throws TwfException
	 */
	public void waitForElement(final WebElement element, long timeout)
			throws TwfException {
		(new WebDriverWait(DriverFactory.getDriver(), timeout))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return isElementPresent(element);
					}
				});

	}

	/**
	 * Returns true if element is visible.
	 * 
	 * @param element
	 * @return boolean
	 */
	public boolean isElementPresent(WebElement element) {
		try {
			return element.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Clicks on a link, button, checkbox or radio button.
	 */
	public void click(WebElement element) {
		element.click();
	}

	public void submit(WebElement element) {
		element.submit();
	}

	/*public Keyboard getKeyboard() throws TwfException {
		return ((HasInputDevices) DriverFactory.getDriver()).getKeyboard();
	}

	public Mouse getMouse() throws TwfException {
		return ((HasInputDevices) DriverFactory.getDriver()).getMouse();

	}

	
	 * Press ctrl+f5
	 
	public void refreshWithCtrlF5() throws TwfException {
		getKeyboard().sendKeys(Keys.CONTROL, Keys.F5);
	}

	public void keyPress(Keys key) throws TwfException {
		getKeyboard().pressKey(key);
	}*/

	public void controlAndKeyPress(String keyValue) throws TwfException {
		WebDriver driver = DriverFactory.getDriver();
		Actions action = new Actions(driver);
		action.keyDown(Keys.CONTROL).sendKeys(Keys.valueOf(keyValue)).perform();

	}

	public void keyPress(String keyValue) throws TwfException {
		WebDriver driver = DriverFactory.getDriver();
		Actions action = new Actions(driver);
		action.sendKeys(Keys.valueOf(keyValue)).perform();
	}

	/**
	 * press key (Keys.)
	 * 
	 * @param key
	 * @throws TwfException
	 */
//	public void keyPress(CharSequence... key) throws TwfException {
//		getKeyboard().sendKeys(key);
//	}

	/**
	 * Select an option by visibleText from a drop-down using an option locator.
	 */
	public void select(WebElement listbox, String value) {
		// Select comboBox = new Select(listbox);
		// comboBox.selectByVisibleText(option);
		List<WebElement> allOptions = listbox
				.findElements(By.tagName("option"));
		for (WebElement option : allOptions) {
			if (option.getAttribute("value").equals(value)) {
				option.click();
				return;
			}
		}
	}

	public void selectByVisibleText(WebElement listbox, String value) {
		Select comboBox = new Select(listbox);
		comboBox.selectByVisibleText(value);
	}

	/**
	 * Select an option by value from a drop-down using an option locator.
	 */
	public void selectByValue(WebElement listbox, String value) {
		// Select comboBox = new Select(listbox);
		// comboBox.selectByValue(value);
		select(listbox, value);
	}

	/**
	 * Add an option by value from a drop-down using an option locator.
	 * 
	 * @throws TwfException
	 */
	public void addSelectByValue(WebElement listbox, String value)
			throws TwfException {
		Select comboBox = new Select(listbox);
		comboBox.selectByValue("label=" + value);
	}

	/**
	 * Select an option by index from a drop-down using an option locator.
	 */
	public void selectByIndex(WebElement listbox, int index) {
		// Select comboBox = new Select(listbox);
		// comboBox.selectByIndex(index);
		List<WebElement> allOptions = listbox
				.findElements(By.tagName("option"));
		WebElement option = allOptions.get(index);
		option.click();
	}

	public void selectValueFromJQueryListBox(List<WebElement> list, String value) {
		for (WebElement a : list) {
			WebElement link = a.findElement(By.xpath("a"));
			if (link.getText().equalsIgnoreCase(value)) {
				link.click();
			}
		}
	}

	public void selectValueFromJQueryListBox(WebElement listbox, String value) {
		List<WebElement> list = listbox.findElements(By.xpath("li"));
		for (WebElement a : list) {
			WebElement link = a.findElement(By.xpath("a"));
			if (link.getText().equalsIgnoreCase(value)) {
				link.click();
			}
		}
	}

	public void jQuerysendKeys(WebElement element, WebElement listbox,
			String value) throws InterruptedException {
		String partialValue = StringUtils.mid(value, 0, value.length() - 1);
		element.clear();
		Thread.sleep(1000);
		element.sendKeys(partialValue);
		selectValueFromJQueryListBox(listbox, value);
	}

	/**
	 * DeSelect an option by index from a drop-down using an option locator.
	 */
	public void deSelectByIndex(WebElement listbox, int index) {
		Select comboBox = new Select(listbox);
		comboBox.deselectByIndex(index);
	}

	/**
	 * DSelect an option by visibleText from a drop-down using an option
	 * locator.
	 */
	public void deSelect(WebElement listbox, String option) {
		Select comboBox = new Select(listbox);
		comboBox.deselectByVisibleText(option);
	}

	/**
	 * Select an option by value from a drop-down using an option locator.
	 */
	public void deselectByValue(WebElement listbox, String value) {
		Select comboBox = new Select(listbox);
		comboBox.deselectByValue(value);
	}

	/**
	 * DeSelect all options from a drop-down using an option locator.
	 */
	public void deselectAll(WebElement listbox) {
		Select comboBox = new Select(listbox);
		comboBox.deselectAll();
	}

	/**
	 * MovesOver
	 * 
	 * @param element
	 * @throws TwfException
	 */
	public void mouseOver(WebElement element) throws TwfException {
		Actions action = new Actions(DriverFactory.getDriver());
		action.moveToElement(element).build().perform();
		action.moveByOffset(1, 1).build().perform();
	}

	/**
	 * Select window
	 * 
	 * @throws TwfException
	 */
	public void selectWindow(String window) throws TwfException {
		if (!window.equals("null")) {
			DriverFactory.getDriver().switchTo().window(window);
		} else {
			DriverFactory.getDriver().switchTo().defaultContent();
		}
	}

	/**
	 * Select frame
	 * 
	 * @throws TwfException
	 */
	public WebDriver selectFrame(String frame) throws TwfException {
		System.out.println("inn");
		// switchToDefaultContent(); removed to selectFrame with in a frame.
		/*WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), 10);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame));*/
		return DriverFactory.getDriver().switchTo().frame(frame);
	}

	private WebDriver selectFrame(WebDriver driver, String frame)
			throws TwfException {
		try {
			int frameNo = Integer.parseInt(frame);
			return selectFrame(driver, frameNo);
		} catch (NumberFormatException ex) {
			return driver.switchTo().frame(frame);
		}
	}

	/**
	 * Switch to default from a selected frame.
	 * 
	 * @throws TwfException
	 */
	public void switchToDefaultContent() throws TwfException {
		DriverFactory.getDriver().switchTo().defaultContent();
	}

	/**
	 * Select frame by its index.
	 * 
	 * @throws TwfException
	 */
	public WebDriver selectFrame(int frameNo) throws TwfException {
		return DriverFactory.getDriver().switchTo().frame(frameNo);
	}

	private WebDriver selectFrame(WebDriver driver, int frameNo)
			throws TwfException {
		return DriverFactory.getDriver().switchTo().frame(frameNo);
	}

	/**
	 * Click Yes/Ok button in Alert popup and return alert text.
	 * 
	 * @return String
	 * @throws TwfException
	 */
	public String getAlert() throws TwfException {
		WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), 10);
		wait.until(ExpectedConditions.alertIsPresent());
		Alert alert = DriverFactory.getDriver().switchTo().alert();
		String alertText = alert.getText();
		alert.accept();
		return alertText;
	}

	/**
	 * Click No/Cancel button in Alert Popup and return alert text.
	 * 
	 * @return String
	 * @throws TwfException
	 */
	public String getAlertWithNo() throws TwfException {
		Alert alert = DriverFactory.getDriver().switchTo().alert();
		String alertText = alert.getText();
		alert.dismiss();
		return alertText;
	}

	/**
	 * DragAndDrop
	 * 
	 * @param source
	 * @param target
	 * @throws TwfException
	 */
	public void dragAndDrop(WebElement source, WebElement target)
			throws TwfException {
		new Actions(DriverFactory.getDriver()).dragAndDrop(source, target)
				.build().perform();
		// new
		// Actions(DriverFactory.getDriver()).clickAndHold(source).moveToElement(target).release(target).build().perform();
		// new Actions(DriverFactory.getDriver()).dragAndDrop(source, target);
	}

	/**
	 * dragAndDropBetweenFrames
	 * 
	 * @param fromFrame
	 * @param source
	 * @param toFrame
	 * @param target
	 * @throws TwfException
	 */
	public void dragAndDropBetweenFrames(String fromFrame, WebElement source,
			String toFrame, WebElement target) throws TwfException {
		// TODO -Refactor
		WebDriver driver = DriverFactory.getDriver();
		String[] fromFrames = fromFrame.split(">");
		for (String element : fromFrames) {
			driver = selectFrame(driver, element);
		}
		new Actions(DriverFactory.getDriver()).clickAndHold(source).build()
				.perform();
		DriverFactory.getDriver().switchTo().defaultContent();
		driver = DriverFactory.getDriver();
		String[] toFrames = toFrame.split(">");
		for (String element : toFrames) {
			driver = selectFrame(driver, element);
		}
		new Actions(DriverFactory.getDriver()).moveToElement(target).release(
				target).build().perform();
		// new Actions(DriverFactory.getDriver()).dragAndDrop(source, target);
	}

	/**
	 * Get text value from webelement.
	 * 
	 * @param element
	 * @return String
	 */
	public String getText(WebElement element) {
		return element.getText();
	}

	/**
	 * GetValue
	 * 
	 * @param element
	 * @return String
	 */
	public String getValue(WebElement element) {
		return element.getAttribute("value");
	}

	/**
	 * GetTable data with keyValue as key.
	 * 
	 * @param tableId
	 * @param keyCol
	 * @return String
	 */
	public Map<String, List<String>> getTableData(WebElement tableId, int keyCol) {
		HashMap<String, List<String>> tableData = new HashMap<String, List<String>>();
		// Now get all the TR elements from the table
		List<WebElement> allRows = tableId.findElements(By.tagName("tr"));
		// And iterate over them, getting the cells
		for (WebElement row : allRows) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			List<String> cellValue = new ArrayList<String>();
			for (WebElement cell : cells) {
				String cellText = cell.getText();
				cellValue.add(cellText);
			}
			tableData.put(cellValue.get(keyCol), cellValue);
		}
		return tableData;
	}

	/**
	 * GetTable data with keyValue as RowNo.
	 * 
	 * @param tableId
	 * @return String
	 */
	public Map<Integer, List<String>> getTableData(WebElement tableId) {
		HashMap<Integer, List<String>> tableData = new HashMap<Integer, List<String>>();
		int rowNumber = 1;

		if (isElementPresent(tableId.findElement(By.tagName("tbody")))) {
			tableId = tableId.findElement(By.tagName("tbody"));
		}

		// Now get all the TR elements from the table
		List<WebElement> allRows = tableId.findElements(By.tagName("tr"));
		// And iterate over them, getting the cells
		for (WebElement row : allRows) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			List<String> cellValue = new ArrayList<String>();
			for (WebElement cell : cells) {
				cellValue.add(cell.getText());
			}
			tableData.put(rowNumber, cellValue);
			rowNumber++;
		}
		return tableData;
	}

	/**
	 * Get table Row count
	 * 
	 * @param by
	 * @return int
	 * @throws Exception
	 */
	public int getRowCount(By by) throws Exception {
		try {
			WebElement table = DriverFactory.getDriver().findElement(by);
			if (isElementPresent(table.findElement(By.tagName("tbody")))) {
				table = table.findElement(By.tagName("tbody"));
			}
			List<WebElement> rows = table.findElements(By.tagName("tr"));
			return rows.size();
		} catch (Exception e) {
			return -1;
		}

	}

	public int getRowCount(WebElement tableId) throws Exception {
		try {
			if (isElementPresent(tableId.findElement(By.tagName("tbody")))) {
				tableId = tableId.findElement(By.tagName("tbody"));
			}
			List<WebElement> rows = tableId.findElements(By.tagName("tr"));
			return rows.size();
		} catch (Exception e) {
			return -1;
		}

	}

	/**
	 * Get table Row Number
	 * 
	 * @param by
	 * @return int
	 * @throws Exception
	 */
	public int getRowNo(WebElement tableID, int colNo, String searchItem)
			throws Exception {
		try {
			Map<Integer, List<String>> tableData = getTableData(tableID);
			return CommonUtils.getRowNo(tableData, colNo, searchItem);
		} catch (Exception e) {
			return -1;
		}

	}

	/**
	 * @param url
	 * @throws TwfException
	 */
	public void openPage(String url) throws TwfException {
		DriverFactory.getDriver().navigate().to(url);
	}

	/**
	 * @throws TwfException
	 */
	public void clickBrowserBackButton() throws TwfException {
		DriverFactory.getDriver().navigate().back();
	}

	/**
	 * Harmcrest way of implementation - getWebDriver
	 * 
	 * @throws TwfException
	 */

	protected WebDriver getWebDriver() throws TwfException {
		return DriverFactory.getDriver();
	}

	public WebElement getElementByUsing(String target) throws BiffException,
			IOException, TwfException {
		try {
			Map<String, PageObject> objectRepo = PageObjectLoader
					.getObjectRepo();
			String elementId = objectRepo.get(target).getTargetId();

			String elementIdentificationType = objectRepo.get(target)
					.getTargetType();
			WebElement element = null;
			if (elementIdentificationType.equalsIgnoreCase("id")) {
				element = DriverFactory.getDriver().findElement(
						By.id(elementId));
			} else if (elementIdentificationType.equalsIgnoreCase("xpath")) {
				element = DriverFactory.getDriver().findElement(
						By.xpath(elementId));
			} else if (elementIdentificationType.equalsIgnoreCase("name")) {
				element = DriverFactory.getDriver().findElement(
						By.name(elementId));
			} else if (elementIdentificationType.equalsIgnoreCase("css")) {
				element = DriverFactory.getDriver().findElement(
						By.cssSelector(elementId));
			} else if (elementIdentificationType.equalsIgnoreCase("linktext")) {
				element = DriverFactory.getDriver().findElement(
						By.linkText(elementId));

			}
			return element;
		} catch (NullPointerException e) {
			throw new TwfException("Problem in finding Element " + target
					+ " Check for Identification type");
		} catch (Exception e) {
			throw new TwfException("Problem in finding Element " + target);
		}
		
		
	
	}
	
	public void clickLinkwithText(String value) throws TwfException {
		DriverFactory.getDriver().findElement(By.linkText(value)).click();
		}

	
	public void consolePrintDebugStmt(String stmt){
		System.out.println("Console stmt :"+stmt);
	}

	/**
	 * Harmcrest way of implementation - clickOn
	 */
	// protected void clickOn(Finder<WebElement, WebDriver> finder) {
	// this.context.clickOn(finder);
	// }
	//
	// protected void assertPresenceOf(Finder<WebElement, WebDriver> finder) {
	// this.context.assertPresenceOf(finder);
	// }
	//
	// protected void assertPresenceOf(Matcher<Integer> cardinalityConstraint,
	// Finder<WebElement, WebDriver> finder) {
	// this.context.assertPresenceOf(cardinalityConstraint, finder);
	// }
	//
	// protected void waitFor(Finder<WebElement, WebDriver> finder) {
	// waitFor(finder, DEFAULT_TIMEOUT);
	// }
	//
	// protected void waitFor(Finder<WebElement, WebDriver> finder, long
	// timeout) {
	// this.context.waitFor(finder, timeout);
	// }
	//
	// /**
	// * * Cause the browser to navigate to the given URL * *
	// *
	// * @param url
	// */
	// protected void goTo(String url) {
	// this.context.goTo(url);
	// }
	//
	// /**
	// * * Type characters into an element of the page, typically an input field
	// * *
	// *
	// * @param text -
	// * characters to type *
	// * @param inputFinder -
	// * specification for the page element
	// */
	// protected void type(String text, Finder<WebElement, WebDriver>
	// inputFinder) {
	// this.context.type(text, inputFinder);
	// }
	//
	// /**
	// * Syntactic sugar to use with , e.g. type("cheese", into(textbox())); The
	// into() method simply returns its *
	// * argument.
	// */
	// protected Finder<WebElement, WebDriver> into(Finder<WebElement,
	// WebDriver> input) {
	// return input;
	// }
	//
	// /** * replace the default {@link TestContext} */
	// void setContext(TestContext context) {
	// this.context = context;
	// }
	//
	// /** * Returns the current page source */
	// public String getPageSource() {
	// return getWebDriver().getPageSource();
	// }
	//
	// /**
	// * Returns the current page title
	// *
	// * @return String
	// */
	// public String getTitle() {
	// return getWebDriver().getTitle();
	// }
	//
	// /**
	// * Returns the current URL
	// *
	// * @return String
	// */
	// public String getCurrentUrl() {
	// return getWebDriver().getCurrentUrl();
	// }
}
