package com.sanjay.kwutils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jxl.read.biff.BiffException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.sanjay.base.DriverFactory;
import com.sanjay.base.WebPage;
import com.sanjay.utils.PageUtils;
import com.sanjay.utils.TwfException;
import com.sanjay.utils.XMLHandler;

public class StepExecutor extends WebPage {
	protected Log logger = LogFactory.getLog(StepExecutor.class);

	public boolean execute(Step step, final WebDriver driver)
			throws BiffException, IOException, TwfException {
		WebElement element = null;
		String stepAction = step.getCommand();
		if (stepAction.contains("c:")) {
			customKw(step);
			return true;
		}
		if (stepAction.contains("cx:")) {
			customKw(step);
			return true;
		}
		step = getTestDataValue(step);
		String value = getActualValue(step.getValue());
		if (value.startsWith("var_")
				&& !stepAction.equalsIgnoreCase("storeValue")) {
			value = step.getStepVariableValue(value);
		}
		// Commands which doesn't require target Element
		if (!stepAction.equalsIgnoreCase("goToUrl")
				&& !stepAction.equalsIgnoreCase("waitUntilDefaultTimeout")
				&& !stepAction.equalsIgnoreCase("isTextPresent")
				&& !stepAction.equalsIgnoreCase("clickLinkWithText")
				&& !stepAction.equalsIgnoreCase("navigateBack")
				&& !stepAction.equalsIgnoreCase("clickElementByXpath")
				&& !stepAction.equalsIgnoreCase("windowMaximize")
				&& !stepAction.equalsIgnoreCase("getAlert")
				&& !stepAction.equalsIgnoreCase("dragAndDrop")
				&& !stepAction.equalsIgnoreCase("keyPress")
				&& !stepAction.equalsIgnoreCase("controlAndKeyPress")
				&& !stepAction.equalsIgnoreCase("isElementPresent")
				&& !stepAction.equalsIgnoreCase("isElementNotPresent")
				&& !stepAction.equalsIgnoreCase("selectFrame")
				&& !stepAction.equalsIgnoreCase("debugStatement")
				&& !stepAction.equalsIgnoreCase("switchToDefaultContent")
				&& !stepAction.equalsIgnoreCase("delay")) {
			try {
				element = getElementByUsing(step.getTargetElement());
			} catch (TwfException e) {
				throw new TwfException("Not able to find Element "
						+ step.getTargetElement()
						+ "<br> <b>Step Details:</b> " + step.toString()
						+ "<br>");
			}
		}
		try {
			if (stepAction.equalsIgnoreCase("containsText")) {
				String expectedData[] = null;
				if (value.startsWith("xmlfile~")) {
					expectedData = value.split("~");
					XMLHandler namespace = new XMLHandler(new File(
							expectedData[1]));
					value = namespace
							.performXPathQueryReturnString(expectedData[2]);
				}
				Boolean failed = true;
				if (element.getText().contains(value)) {
					failed = false;
				}
				if (getValue(element) != null) {
					if (getValue(element).contains(value)) {
						failed = false;
					}
				}

				if (failed) {
					onStepFailureThrowException(step, element, value);

				}
			} else if (stepAction.equalsIgnoreCase("storeValue")) {
				if (!element.getText().equals("")) {
					step.storeStepVariableValue(value, element.getText());
				} else {
					step.storeStepVariableValue(value, getValue(element));
				}

			} else if (stepAction.equalsIgnoreCase("sendKeys")) {
				type(element, value);
			} else if (stepAction.equalsIgnoreCase("typeAndPressTab")) {
				typeAndTab(element, value);
			} else if (stepAction.equalsIgnoreCase("typeAndBlur")) {
				System.out.println("Type_And_Blur_Disabled");//typeAndBlur(element);
			} else if (stepAction.equalsIgnoreCase("clickLinkWithText")) {
				logger.info("Link:" + value);
				clickLinkwithText(value);
				/*String target = "//a[contains(text(),'" + value + "')]";
				DriverFactory.getDriver().findElement(By.xpath(target)).click();*/
			} else if (stepAction.equalsIgnoreCase("clickElementByXpath")) {
				logger.info("XpathValue:" + value);
				DriverFactory.getDriver().findElement(By.xpath(value)).click();
			} else if (stepAction.equalsIgnoreCase("click")) {
				click(element);
			} else if (stepAction.equalsIgnoreCase("submit")) {
				submit(element);
			} else if (stepAction.equalsIgnoreCase("waitUntilDefaultTimeout")) {
				waitUntilDefaultTimeout();
			} else if (stepAction.equalsIgnoreCase("doubleClick")) {
				doubleClick(element);
			} else if (stepAction.equalsIgnoreCase("clearAndType")) {
				clearAndSendKeys(element, value);
			} else if (stepAction.equalsIgnoreCase("selectValue")) {
				selectByValue(element, value);
			} else if (stepAction.equalsIgnoreCase("selectByValue")) {
				String[] values = value.split(",");
				for (String element2 : values) {
					selectByVisibleText(element, element2);
				}
			} else if (stepAction.equalsIgnoreCase("waitForElement")) {
				waitForElement(element, Long.valueOf(value));
			} else if (stepAction.equalsIgnoreCase("verifyText")) {
				String expectedData[] = null;
				if (value.startsWith("xmlfile~")) {
					expectedData = value.split("~");
					XMLHandler namespace = new XMLHandler(new File(
							expectedData[1]));
					value = namespace
							.performXPathQueryReturnString(expectedData[2]);
				}
				Boolean failed = true;
				if (value.equalsIgnoreCase(element.getText())) {
					failed = false;
				}
				if (value.equalsIgnoreCase(getValue(element))) {
					failed = false;
				}

				if (failed) {
					onStepFailureThrowException(step, element, value);
				}
			} else if (stepAction.equalsIgnoreCase("isTextPresent")) {
				if (getWebDriver().getPageSource().contains(value)) {
					throw new TwfException(
							value
									+ "is not available in Page <br> <b>Step Details:</b> "
									+ step.toString() + "<br>");
				}
			} else if (stepAction.equalsIgnoreCase("windowMaximize")) {
				windowMaximize();
			} else if (stepAction.equalsIgnoreCase("goToUrl")) {
				getWebDriver().navigate().to(value);

			} else if (stepAction.equalsIgnoreCase("navigateBack")) {
				clickBrowserBackButton();
			} else if (stepAction.equalsIgnoreCase("setValue")) {
				setValue(element, value);
			} else if (stepAction.equalsIgnoreCase("addSelectByValue")) {
				addSelectByValue(element, value);
			} else if (stepAction.equalsIgnoreCase("selectByIndex")) {
				selectByIndex(element, Integer.valueOf(value).intValue());
			} else if (stepAction.equalsIgnoreCase("deSelect")) {
				deSelect(element, value);
			} else if (stepAction.equalsIgnoreCase("mouseOver")) {
				mouseOver(element);
			} else if (stepAction.equalsIgnoreCase("selectWindow")) {
				selectWindow(value);
			} else if (stepAction.equalsIgnoreCase("selectFrame")) {
				if (isInteger(value)) {
					selectFrame(Integer.valueOf(value).intValue());
				} else {
					selectFrame(value);
				}
			} else if (stepAction.equalsIgnoreCase("switchToDefaultContent")) {
				switchToDefaultContent();
			} else if (stepAction.equalsIgnoreCase("getAlert")) {
				getAlert();
			} else if (stepAction.equalsIgnoreCase("getAlertWithNo")) {
				getAlertWithNo();
			} else if (stepAction.equalsIgnoreCase("dragAndDrop")) {
				List<String> items = Arrays.asList(value.split(","));
				dragAndDrop(getElementByUsing(items.get(0)),
						getElementByUsing(items.get(1)));
			} else if (stepAction.equalsIgnoreCase("keyPress")) {
				keyPress(value);
			} else if (stepAction.equalsIgnoreCase("controlAndKeyPress")) {
				controlAndKeyPress(value);
			} else if (stepAction.equalsIgnoreCase("isElementNotPresent")) {
				isElementNotPresent(value);
			} else if (stepAction.equalsIgnoreCase("isElementPresent")) {
				isElementPresent(value);
			}  else if(stepAction.equalsIgnoreCase("debugStatement")){
				  consolePrintDebugStmt(value);
			}else if(stepAction.equalsIgnoreCase("delay")){
				  waitForgiventime(value);
			}

		} catch (Exception e) {
			throw e.getClass().equals(TwfException.class)
					? (TwfException) e
					: new TwfException("Element Not found: Action:"
							+ stepAction + "  ,<b>Element:</b>"
							+ step.getTargetElement() + " Value: " + value);
		}

		return true;
	}

	private void onStepFailureThrowException(Step step, WebElement element,
			String value) throws TwfException {
		step.setTestCaseDescription(step.getStepDescription());
		throw new TwfException(
				"Mismatch in the text verification :<font color=\"solid orange\">  Actual :["
						+ (element.getText().equalsIgnoreCase("")
								? getValue(element)
								: element.getText())
						+ "]</font><font color=\"EE7600\"> Expected :[" + value
						+ "]</font><br> <b>Step Details:</b> "
						+ step.toString() + "<br>");
	}

	public Step getTestDataValue(final Step step) throws BiffException,
			IOException {
		try {
			if (step.getValue().startsWith("TD_")) {
				String acutalValue = TestDataHandler.getTestData(step
						.getValue()
						+ ":" + step.getTargetElement());
				step.setValue(acutalValue);
				return step;
			}

		} catch (Exception e) {
			// Do nothing...
		}
		return step;
	}

	public boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void checkPage() {
		// TODO Auto-generated method stub

	}

	// TODO need to change by using RegExpr
	// To extract actual value from Global varible.
	public static String getActualValue(String value) throws BiffException,
			IOException, TwfException {
		Map<String, String> variables;
		StringBuffer actualString = new StringBuffer();
		actualString.append("");
		if (value == null) {
			return "";
		}
		if (value.equalsIgnoreCase("")) {
			return "";
		}
		String dynamicValues[] = value.split("\\{");

		try {
			variables = KWVariables.getVariables();
			for (int i = 0; i < dynamicValues.length; i++) {
				if (dynamicValues[i].contains("}")) {
					String temp[] = dynamicValues[i].split("}");
					temp[0] = variables.get(temp[0]);
					if (temp.length == 1) {
						dynamicValues[i] = temp[0];
					} else {
						dynamicValues[i] = temp[0] + temp[1];
					}
				}

				// if (dynamicValues[i].charAt(0) == '{') {
				// dynamicValues[i] = dynamicValues[i].replace("{", "");
				// dynamicValues[i] = variables.get(dynamicValues[i]);
				// }

				actualString.append(dynamicValues[i]);

			}
			return actualString.toString();

		} catch (Exception e) {
			throw new TwfException(
					"Problem in finding/identifying KW Variable " + value);
		}
	}

	// TODO ..
	public void customKw(Step step) throws TwfException {
		try {
			Boolean extendedCustomClass = false;
			String methodName = step.getCommand();

			Class classInstance = Class.forName(CustomKW
					.getCustomMethodClass(methodName));
			if (methodName.startsWith("cx:")) {
				extendedCustomClass = true;
			}

			methodName = methodName.replaceAll("c:", "");
			methodName = methodName.replaceAll("cx:", "");

			if (extendedCustomClass.equals(true)) {
				Object objx = PageUtils.create(classInstance, step);
				Method method = objx.getClass().getDeclaredMethod(methodName);
				method.invoke(objx);
				return;
			}

			Object obj = PageUtils.create(classInstance);

			// if step having variable as Input/Output
			/*
			 * if (step.getValue().startsWith("var_")) { Class[] argTypes = new
			 * Class[1]; argTypes[0] = String.class; // argTypes[0] =
			 * Step.class; String data =
			 * step.getStepVariableValue(step.getValue());
			 * System.out.println("dd :"+data); Method method =
			 * classInstance.getDeclaredMethod(methodName, argTypes);
			 * //method.invoke(obj, data, step); method.invoke(obj, data); }
			 * 
			 * //else
			 */

			// Custom method requires DataPool values , So give "-" at
			// Input/Output
			if (step.getValue().equals("-")) {
				logger.info("Inside Custom Method Initiator");
				Class[] argTypes = new Class[1];
				argTypes[0] = Map.class;
				Map<String, String> data = step.getDataValues();
				logger.info("After setting datavalues");

				Method method = classInstance.getDeclaredMethod(methodName,
						argTypes);
				method.invoke(obj, data);
				logger.info("Completed Custom Method Initiator");
			}
			// Custom method requires DataPool values and Step Instance , So
			// give "--" at Input/Output
			else if (step.getValue().equals("--")) {
				Class[] argTypes = new Class[2];
				argTypes[0] = Map.class;
				argTypes[1] = Step.class;
				Map<String, String> data = step.getDataValues();
				Method method = classInstance.getDeclaredMethod(methodName,
						argTypes);
				method.invoke(obj, data, step);
			}
			// Custom method requires String parameter
			else {
				Class[] argTypes = new Class[1];
				argTypes[0] = String.class;
				Method method = classInstance.getDeclaredMethod(methodName,
						argTypes);
				// Condition is for to get actual value of variable.
				if (step.getValue().startsWith("var_")) {
					method.invoke(obj, step.getStepVariableValue(step
							.getValue()));
				} else {
					method.invoke(obj, step.getValue());
				}
			}

		} catch (Exception e) {

			if (!e.getClass().equals(TwfException.class)
					|| !e.getClass().getName().contains(
							"InvocationTargetException")) {
				throw new TwfException(e.getCause().getMessage());
			} else {
				throw (TwfException) e;
			}

		}
	}
}
