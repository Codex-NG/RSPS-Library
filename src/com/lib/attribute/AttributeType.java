package com.lib.attribute;

/**
 * No copyright asserted on the source code of this class.
 * May be used for any purpose.
 */

/**
 * Represents the type of {@code Attribute} used for the
 * {@code AttributedNumber} implementation.
 * 
 * @author Albert Beaupre
 */
public enum AttributeType {
	DIVIDE((n, i) -> n / i), MULTIPLY((n, i) -> n * i), ADD((n, i) -> n + i), SUBTRACT((n, i) -> n - i);

	private final Calculator calculator; // The calculator object used by the AttributeType

	private AttributeType(Calculator calculator) {
		this.calculator = calculator;
	}

	/**
	 * Returns the {@code Calculator} of this {@code AttributeType}.
	 * 
	 * @return calculator
	 */
	public Calculator getCalculator() {
		return calculator;
	}

	/**
	 * The {@code Calculator} interface is used by the {@code AttributeType} for
	 * calculations.
	 * 
	 * @author Albert Beaupre
	 */
	public static interface Calculator {
		double calculated(double n, double i);
	}
}