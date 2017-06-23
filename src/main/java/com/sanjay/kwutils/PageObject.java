package com.sanjay.kwutils;

public class PageObject {
	private String targetName;
	private String targetId;
	private String targetType;

	public PageObject(String targetName, String targetId, String targetType) {
		super();
		this.targetName = targetName;
		this.targetId = targetId;
		this.targetType = targetType;
	}

	public String getTargetName() {
		return this.targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getTargetId() {
		return this.targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getTargetType() {
		return this.targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
}
