/**
 madan.b
 */
package com.sanjay.utils;

/**
 * @(#)SSN.java
 */
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * An instance of the class stores a social security number where the three
 * components are held as integers Each part of the SSN can only have a specific
 * range. Exceptions are thrown if any are out of range. A static class is
 * provided that returns a random SSN.
 */

public class SSN implements Comparable<SSN>, Serializable

{
	protected int partA, partB, partC; // the three parts of the SSN
	private static Random randomPart = new Random(); // random number
														// generator
	private static final long serialVersionUID = 0; // for binary file output

	SSN() {
		// constructor that initializes parts with zeros
		this.partA = 0;
		this.partB = 0;
		this.partC = 0;
	}

	public static SSN getRandomSSN() {
		// if we don't take abs, we get negatives random numbers
		final int partA = Math.abs(randomPart.nextInt()) % 1000; // 0-999
		final int partB = Math.abs(randomPart.nextInt()) % 100; // 0-99
		final int partC = Math.abs(randomPart.nextInt()) % 10000; // 0-9999
		return new SSN(partA, partB, partC); // make it
	}

	public SSN(int pA, int pB, int pC) { // constructor. calls individual
		// methods so range is checked
		setPartsABC(pA, pB, pC);
	}

	public int getPartA() {
		return this.partA;
	}

	public void setPartA(int partA) {
		if (partA < 0 || partA > 999) {
			throw new IllegalArgumentException("SSN setPartA: "
					+ "partA <0 or > 999");
		}
		this.partA = partA;
	}

	public void setPartsABC(int partA, int partB, int partC) {
		// calls the individual set methods so range is checked
		setPartA(partA);
		setPartB(partB);
		setPartC(partC);
	}

	public int getPartB() {
		return this.partB;
	}

	public void setPartB(int partB) {
		if (partB < 0 || partB > 99) {
			throw new IllegalArgumentException("SSN setPartB: "
					+ "partB <0 or > 99");
		}
		this.partB = partB;
	}

	public int getPartC() {
		return this.partC;
	}

	public void setPartC(int partC) {
		if (partC < 0 || partC > 9999) {
			throw new IllegalArgumentException("SSN setPartC: "
					+ "partC <0 or > 9999");
		}
		this.partC = partC;
	}

	public int compareTo(SSN bObj) // internally, cast Object to SSN
	{
		long thisSSN, bSSN;
		SSN b = bObj; // no need to cast - using generic programming construct
		// <SSN>
		// double precision comparison fails. need to do long integer comparison
		thisSSN = this.partA * 1000000L + this.partB * 10000L + this.partC;
		bSSN = b.partA * 1000000L + b.partB * 10000L + b.partC;

		// ALSO parts A,B,C are protected, so this routine
		// can access the individual parts without using the getters

		if (thisSSN < bSSN) {
			return -1;
		} else if (thisSSN == bSSN) {
			return 0;
		} else {
			return 1;
		}

	}

	@Override
	public String toString() {
		DecimalFormat fmt2 = new DecimalFormat("00");
		DecimalFormat fmt3 = new DecimalFormat("000");
		DecimalFormat fmt4 = new DecimalFormat("0000");
		return fmt3.format(this.partA) + "-" + fmt2.format(this.partB) + "-"
				+ fmt4.format(this.partC);

	}

	public void main(String a[]) {
		new SSN().toString();
	}

}
