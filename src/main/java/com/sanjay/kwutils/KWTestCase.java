package com.sanjay.kwutils;

import java.util.HashMap;
import java.util.Map;

public class KWTestCase {
	private Map<Integer, Step> steps;
	private int totalSteps;
	private String testDataId; // use incase of datadriven tests

	public KWTestCase(KWTestCase testCase) {
		this.steps = new HashMap<Integer, Step>(testCase.getSteps());
		this.totalSteps = testCase.getTotalSteps();
	}

	public KWTestCase(Map<Integer, Step> steps, int totalSteps) {
		super();
		this.steps = new HashMap<Integer, Step>(steps);
		this.totalSteps = totalSteps;
	}

	public KWTestCase(Map<Integer, Step> steps, int totalSteps,
			String testDataId) {
		super();
		this.steps = new HashMap<Integer, Step>(steps);
		this.totalSteps = totalSteps;
		this.testDataId = testDataId;
	}

	public Map<Integer, Step> getSteps() {
		return this.steps;
	}

	public void setSteps(Map<Integer, Step> steps) {
		this.steps = steps;
	}

	public int getTotalSteps() {
		return this.totalSteps;
	}

	public void setTotalSteps(int totalSteps) {
		this.totalSteps = totalSteps;
	}

	public Step getStep(int stepNumber) {
		return this.steps.get(stepNumber);
	}

	public String getTestSteps() {
		StringBuffer steps = new StringBuffer();
		steps.append("\n");
		for (int i = 0; i < this.totalSteps; i++) {
			steps.append(this.steps.get(i).toString());
		}
		return steps.toString();
	}

	public String getTestDataId() {
		return this.testDataId;
	}

	public void setTestDataId(String testDataId) {
		this.testDataId = testDataId;
	}
}
