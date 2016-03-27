package com.lib.attribute;

import java.util.LinkedHashMap;

import com.lib.config.ConfigSection;
import com.lib.config.YMLSerializable;

/**
 * The {@code AttributedNumber} class allows linking multiple {@code Attribute}
 * values for calculating numbers only from the attributes linked.
 * 
 * @author Albert Beaupre
 */
public class AttributedNumber extends Number implements YMLSerializable {

	private static final long serialVersionUID = -7732086605299969350L;
	private LinkedHashMap<String, Attribute> values; // A HashMap to hold to values for calculating
	private double defaultNumber; // The default value set to this AttributedNumber

	/**
	 * Contructs a new {@code AttributedNumber} from the specified
	 * {@code defaultNumber}.
	 * 
	 * @param defaultNumber
	 *            the default number
	 */
	public AttributedNumber(double defaultNumber) {
		this.defaultNumber = defaultNumber;
		this.values = new LinkedHashMap<>();
	}

	/**
	 * Constructs a new {@code AttributedNumber} with a default number of 0.
	 */
	public AttributedNumber() {
		this(0);
	}

	/**
	 * Adds the specified {@code attribute} to this {@code AttributedNumber} for
	 * the specified {@code name}.
	 * 
	 * @param name
	 *            the attribute name
	 * @param attribute
	 *            the attribute associated with the name
	 */
	public AttributedNumber putAttribute(String name, Attribute attribute) {
		values.put(name, attribute);
		return this;
	}

	/**
	 * Removes an {@code Attribute} from this {@code AttributedNumber}, if
	 * possible, for the specified {@code name}
	 * 
	 * @param name
	 *            the name of the attribute to remove
	 */
	public AttributedNumber removeAttribute(String name) {
		values.remove(name);
		return this;
	}

	/**
	 * Returns the a divisible {@code Attribute} and adds it to this
	 * {@code AttributedNumber} for the specified {@code name}. The default
	 * value is later divided by the specified {@code byValue}.
	 * 
	 * @param name
	 *            the name of the divisible attribute
	 * @param byValue
	 *            the value to divide by
	 * @return the divisible attribute
	 */
	public Attribute divide(String name, double byValue) {
		Attribute atr = Attribute.divide(byValue);
		putAttribute(name, atr);
		return atr;
	}

	/**
	 * Returns the a multiplicable {@code Attribute} and adds it to this
	 * {@code AttributedNumber} for the specified {@code name}. The default
	 * value is later multiplied by the specified {@code byValue}.
	 * 
	 * @param name
	 *            the name of the multiplicable attribute
	 * @param byValue
	 *            the value to multiply by
	 * @return the multiplicable attribute
	 */
	public Attribute multiply(String name, double byValue) {
		Attribute atr = Attribute.multiply(byValue);
		putAttribute(name, atr);
		return atr;
	}

	/**
	 * Returns the an additive {@code Attribute} and adds it to this
	 * {@code AttributedNumber} for the specified {@code name}. The default
	 * value is later added by the specified {@code byValue}.
	 * 
	 * @param name
	 *            the name of the additive attribute
	 * @param byValue
	 *            the value to add by
	 * @return the additive attribute
	 */
	public Attribute add(String name, double byValue) {
		Attribute atr = Attribute.add(byValue);
		putAttribute(name, atr);
		return atr;
	}

	/**
	 * Returns the a subtractive {@code Attribute} and adds it to this
	 * {@code AttributedNumber} for the specified {@code name}. The default
	 * value is later subtracted by the specified {@code byValue}.
	 * 
	 * @param name
	 *            the name of the subtractive attribute
	 * @param byValue
	 *            the value to subtract by
	 * @return the subtractive attribute
	 */
	public Attribute subtract(String name, double byValue) {
		Attribute atr = Attribute.subtract(byValue);
		putAttribute(name, atr);
		return atr;
	}

	/**
	 * Returns the value of this {@code AttributedNumber}.
	 * 
	 * @return the value
	 */
	public double getValue() {
		double amount = defaultNumber;
		for (Attribute i : values.values())
			amount = i.getType().getCalculator().calculated(amount, i.getValue());
		return amount;
	}

	/**
	 * Returns the default number set to this {@code AttributedNumber}.
	 * 
	 * @return the default number
	 */
	public double getDefaultNumber() {
		return defaultNumber;
	}

	public AttributedNumber setDefaultNumber(double defaultNumber) {
		this.defaultNumber = defaultNumber;
		return this;
	}

	@Override
	public int intValue() {
		return (int) getValue();
	}

	@Override
	public long longValue() {
		return (long) getValue();
	}

	@Override
	public float floatValue() {
		return (float) getValue();
	}

	@Override
	public double doubleValue() {
		return (double) getValue();
	}

	@Override
	public ConfigSection serialize() {
		return new ConfigSection().set("original", defaultNumber).set("attributes", this.values);
	}

	@Override
	public AttributedNumber deserialize(ConfigSection map) {
		this.defaultNumber = map.getDouble("original");
		this.values = new LinkedHashMap<>(map.getMap("attributes", String.class, Attribute.class));
		return this;
	}

}