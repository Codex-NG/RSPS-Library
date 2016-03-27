package com.lib.config;

import java.io.File;

/**
 * Represents an object which may be serialized and deserialized from a YML
 * configuration.
 * 
 * @author Albert Beaupre
 */
public interface YMLSerializable {

	/**
	 * Serializes this object into a {@code ConfigSection}.
	 * 
	 * @return the serialized config section
	 */
	public ConfigSection serialize();

	/**
	 * Deserializes the specified {@code map}.
	 * 
	 * @param map
	 *            the map the deserialize
	 */
	public YMLSerializable deserialize(ConfigSection map);

	/**
	 * Saves this {@code YMLSerializable} to the specified {@code file} in YML
	 * format.
	 * 
	 * @param file
	 *            the file to be saved to
	 */
	public default void save(File file) {
		FileConfig config = new FileConfig(file);
		config.map = serialize();
		config.save();
	}
}