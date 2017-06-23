package com.sanjay.kwutils;

import java.util.HashMap;

public class KWModule {
	private HashMap<Integer, KWTestCase> testCases;

	public KWModule(HashMap<Integer, KWTestCase> testCases) {
		super();
		this.testCases = testCases;
	}

	public HashMap<Integer, KWTestCase> getTestCases() {
		return this.testCases;
	}

	public void setTestCases(HashMap<Integer, KWTestCase> testCases) {
		this.testCases = testCases;
	}

}
