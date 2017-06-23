package com.sanjay.utils;

import java.util.Random;

public class RandomValue {

	private static final Random random = new Random();

	public static String getrandomValue() {
		return String.valueOf(random.nextFloat()).replace("0.", "");
	}

	public static String reasonDescription() {
		String randomval = " ";

		int rand1 = random.nextInt(100000);
		int rand2 = random.nextInt(100000);
		randomval = "Test" + rand1 + "" + rand2;
		return randomval;
	}

	public static String getRandomStringOfCharacters(int stringLength) {

		// Using StringBuffer is more efficient than using a String
		StringBuilder randomString = new StringBuilder();
		for (int i = 0; i < stringLength; i++) {
			randomString.append((char) (random.nextInt(74) + '0'));
		}
		return randomString.toString();
	}

	/**
	 * Outputs a random string of numbers of the specified length
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomStringOfNumbers(int stringLength) {

		// Using StringBuffer is more efficient than using a String
		StringBuffer number = new StringBuffer();
		for (int i = 0; i < stringLength; i++) {
			number.append(random.nextInt(10));
		}
		return number.toString();
	}

	public static void main(String arg[]) {
		for (int i = 1; i <= 1000; i++) {
			System.out.println(RandomValue.getrandomValue());
		}
	}
}
