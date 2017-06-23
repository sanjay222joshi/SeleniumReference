package com.sanjay.kwutils;

import java.util.HashMap;
import java.util.Map;

import com.sanjay.kwutils.Commands.Command;

public class Step {
	private String testCaseId;
	private String testCaseDescription;
	private String testCasegroup;
	private String stepDescription;
	private String command;
	private String targetElement;
	private String value;
	private Map<String, String> dataValues;
	private String stepId;
	private Map<String, String> keywordVariableValues;

	/*
	 * public Step(String moduleName, String testCaseDescription, String
	 * testCaseId, String testCasegroup, String command, String targetPage,
	 * String targetElement, String value) { this.moduleName = moduleName;
	 * this.testCaseDescription = testCaseDescription; this.testCaseId =
	 * testCaseId; this.testCasegroup = testCasegroup; this.command = command;
	 * this.targetPage = targetPage; this.targetElement = targetElement;
	 * this.value = value; }
	 */
	public Step(String testCaseId, String testCaseDescription,
			String testCasegroup, String stepId, String stepDescription,
			String command, String targetElement, String value) {
		this.testCaseId = testCaseId;
		this.testCaseDescription = testCaseDescription;
		this.testCasegroup = testCasegroup;
		this.stepId = stepId;
		this.stepDescription = stepDescription;
		this.command = command;
		this.targetElement = targetElement;
		this.value = value;
		this.keywordVariableValues = new HashMap<String, String>();
	}

	public Step(Step step) {
		this.testCaseId = step.testCaseId;
		this.testCaseDescription = step.testCaseDescription;
		this.testCasegroup = step.testCasegroup;
		this.stepId = step.stepId;
		this.stepDescription = step.stepDescription;
		this.command = step.command;
		this.targetElement = step.targetElement;
		this.value = step.value;
		if (step.dataValues != null) {
			this.dataValues = new HashMap<String, String>(step.dataValues);
		}
		this.keywordVariableValues = new HashMap<String, String>(
				step.keywordVariableValues);
	}

	/**
	 * To create a variable and store value. key should start with "var_xxx"
	 * 
	 * @param key
	 * @param value
	 */
	public void storeStepVariableValue(String key, String value) {
		if (this.keywordVariableValues == null) {
			this.keywordVariableValues = new HashMap<String, String>();
		}
		this.keywordVariableValues.put(key, value);
	}

	/**
	 * To get the variable value - getStepVariableValue("var_xxx")
	 * 
	 * @param key
	 * @return
	 */
	public String getStepVariableValue(String key) {
		if (this.keywordVariableValues.containsKey(key)) {
			return this.keywordVariableValues.get(key);
		} else {
			return "";
		}
	}

	public String getTestCaseDescription() {
		return this.testCaseDescription;
	}

	public void setTestCaseDescription(String testCaseDescription) {
		this.testCaseDescription = testCaseDescription;
	}

	public String getTestCaseId() {
		return this.testCaseId;
	}

	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}

	public String getCommand() {
		return this.command;
	}

	// TODO
	public Command getAction() {

		if (this.command.equals("goToUrl")) {
			return Command.goToUrl;
		}
		return null;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getTargetElement() {
		return this.targetElement;
	}

	public void setTargetElement(String targetElement) {
		this.targetElement = targetElement;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFireCommand() {

		String action = "";
		try {
			if ((action = CustomKW.getKWMethodName(this.command)) != null) {
				return action;
			}
			if ((action = SanjayKeyWord.getActualKWName(this.command)) != null) {
				return action;
			}
		} catch (Exception e) {
		}
		return action;
	}

	@Override
	public String toString() {
		String step = "<table border=\"1\"> <tr>" + " <th>StepId</th>"
				+ "  <th>Test Case Description</th>"
				+ "  <th>Action/Command</th>" + " <th>Target Element</th>"
				+ " <th>Data</th>" + "</tr>" + "<tr>" + "<td>" + this.stepId
				+ "</td>" + " <td>" + this.testCaseDescription + "</td>"
				+ "  <td>" + getFireCommand() + "</td>" + "  <td>"
				+ this.targetElement + "</td>";

		try {
			if (this.value.equals("") || this.value.equals("-")) {
				step = step + "<td>" + getDataValuesAsString() + "</td>";
			} else {
				step = step + "<td>" + this.value + "</td>";
			}
		} catch (NullPointerException n) {
			step = step + "<td>" + "" + "</td>";
		}
		return step = step + " </tr> 	</table>";

	}

	public String getStepId() {
		return this.stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public Map<String, String> getDataValues() {
		return this.dataValues;
	}
	public void removeDataValue(String key) {
		this.dataValues.remove(key);
	}
	public void setDataValues(Map<String, String> dataValues) {
		this.dataValues = dataValues;
	}

	/**
	 * Returns the datapool value - getDataValue("colName")
	 * 
	 * @param key
	 * @return
	 */
	public String getDataValue(String key) {
		return dataValues.get(key);
	}

	public String getDataValuesAsString() {
		StringBuffer data = new StringBuffer();
		data.append("");
		for (Map.Entry<String, String> entry : this.dataValues.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			data.append(" " + key + " : " + value);
		}

		return data.toString();
	}

	public String getStepDescription() {
		return this.stepDescription;
	}

	public void setStepDescription(String stepDescription) {
		this.stepDescription = stepDescription;
	}

	public Map<String, String> getKwValueVariables() {
		return this.keywordVariableValues;
	}

	public void setKwValueVariables(Map<String, String> kwValueVariables) {
		this.keywordVariableValues = kwValueVariables;
	}
}
