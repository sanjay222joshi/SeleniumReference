package com.sanjay.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface TestCaseNumber {
	String number();

}
