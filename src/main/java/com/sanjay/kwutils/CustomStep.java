package com.sanjay.kwutils;

import com.sanjay.base.WebPage;

public abstract class CustomStep extends WebPage {
	protected Step step;
	public void setStepData(Step s) {
		this.step = s;
	}

}
