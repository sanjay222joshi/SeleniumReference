package com.sanjay.utils;

public class ReportDetailStatus {
	private String methodName;

	private String status;

	private String reason;
	private String description;
	private String errorFile;
	private String testCaseDetails;
	private String stackTrace;

	public void setErrorFile(String errorFile) {
		this.errorFile = errorFile;
	}

	public String getErrorFile() {
		return this.errorFile;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description == null || this.description.equals("")
				? this.methodName
				: this.description;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return this.reason == null ? "-" : this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
		// this.reason = StringEscapeUtils.escapeHtml(reason);
	}

	public String getMethodeName() {
		return this.methodName;
	}

	public void setMethodeName(String methodeName) {
		this.methodName = methodeName;
	}

	public String getTestCaseDetails() {
		return this.testCaseDetails;
	}

	public void setTestCaseDetails(String testCaseDetails) {
		this.testCaseDetails = testCaseDetails;
	}

	public String getStackTrace() {
		return this.stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

}
