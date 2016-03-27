package com.lib.attribute;

/**
 * No copyright asserted on the source code of this class. May be used for any
 * purpose.
 */

/*
 * The {@code Attribute} class is used by the {@code AttributedNumber} to hold
 * the {@code AttributeType} and value for calculation.
 * 
 * @author Albert Beaupre
 */
public class Attribute {

	private final AttributeType type; // The AttributeType assigned to this Attribute
	private final double value; // The value assigned to this Attribute

	private Attribute(AttributeType type, double value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Creates a divisible {@code Attribute} object from the specified
	 * {@code byValue}.
	 * 
	 * @param byValue
	 *            the value later to divide by
	 * @return the attribute object
	 */
	public static Attribute divide(double byValue) {
		return new Attribute(AttributeType.DIVIDE, byValue);
	}

	/**
	 * Creates a multiplicable {@code Attribute} object from the specified
	 * {@code byValue}.
	 * 
	 * @param byValue
	 *            the value later to multiply by
	 * @return the attribute object
	 */
	public static Attribute multiply(double byValue) {
		return new Attribute(AttributeType.MULTIPLY, byValue);
	}

	/**
	 * Creates a additive {@code Attribute} object from the specified
	 * {@code byValue}.
	 * 
	 * @param byValue
	 *            the value later to add by
	 * @return the attribute object
	 */
	public static Attribute add(double byValue) {
		return new Attribute(AttributeType.ADD, byValue);
	}

	/**
	 * Creates a subtractive {@code Attribute} object from the specified
	 * {@code byValue}.
	 * 
	 * @param byValue
	 *            the value later to subtract by
	 * @return the attribute object
	 */
	public static Attribute subtract(double byValue) {
		return new Attribute(AttributeType.SUBTRACT, byValue);
	}

	/**
	 * Returns the {@code AttributeType} of this {@code Attribute}.
	 * 
	 * @return the attribute type
	 */
	public AttributeType getType() {
		return type;
	}

	/**
	 * Returns the value of this {@code Attribute}.
	 * 
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

}