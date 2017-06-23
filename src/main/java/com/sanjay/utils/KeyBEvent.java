package com.sanjay.utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class KeyBEvent {
	private static Robot rb;

	private static void init() {
		try {
			rb = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void PressAndReleaseTabKey() {
		init();
		rb.keyPress(KeyEvent.VK_TAB);

	}

}
