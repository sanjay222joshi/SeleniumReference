package com.sanjay.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TwfException extends Exception implements InvocationHandler {

	private static final long serialVersionUID = -2914738734453704519L;

	public TwfException() {
		super();
	}

	public TwfException(final String message) {
		super(message);

	}

	@Override
	public String toString() {
		return this.getMessage();
	}

	public Object invoke(Object arg0, Method arg1, Object[] arg2)
			throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}
}
