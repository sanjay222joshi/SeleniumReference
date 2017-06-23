package com.sanjay.kwutils;

import java.util.ArrayList;
import java.util.List;

public class TestCaseHandler {

	private List<String> testCaseExecutionList;

	public TestCaseHandler() {
		testCaseExecutionList = new ArrayList<String>();
	}
	public void addTestCaseId(String testId) {
		testCaseExecutionList.add(testId);
	}

	public void clear() {
		testCaseExecutionList.clear();
	}

	public Boolean isTestIdAvailable(String testId) {
		if (testCaseExecutionList.size() == 0)
			return false;
		if (testCaseExecutionList.contains(testId)) {
			return true;
		} else {
			return false;
		}
	}
}
