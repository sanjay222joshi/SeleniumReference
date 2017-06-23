package com.sanjay.kwutils;

public interface Commands {
	public enum Command {
		goToUrl("goToUrl"), storeValue("storeValue"), waitUntilDefaultTimeout(
				"waitUntilDefaultTimeout"), isTextPresent("isTextPresent"), clickLinkWithText(
				"clickLinkWithText"), navigateBack("navigateBack"), clickElementByXpath(
				"clickElementByXpath"), containsText("containsText"), sendKeys(
				"sendKeys"), typeAndPressTab("typeAndPressTab"), typeAndBlur(
				"typeAndBlur"), click("click"), submit("submit"), doubleClick(
				"doubleClick"), clearAndType("clearAndType"), selectValue(
				"selectValue"), selectByValue("selectByValue"), waitForElement(
				"waitForElement"), verifyText("verifyText"), setValue(
				"setValue"), addSelectByValue("addSelectByValue"), selectByIndex(
				"selectByIndex"), deSelect("deSelect"), mouseOver("mouseOver"), selectWindow(
				"selectWindow"), selectFrame("selectFrame"), switchToDefaultContent(
				"switchToDefaultContent"), getAlert("getAlert"), getAlertWithNo(
				"getAlertWithNo");

		private String text;

		Command(String text) {
			this.text = text;
		}

		public String getText() {
			return this.text;
		}

	}

}
