package com.lib.yml.config;

/**
 * Represents an object which may be serialized and deserialized from a YML
 * config.
 * 
 * @author Albert Beaupre
 */
public interface YMLSerializable {
	/**
	 * Serializes this object into a map.
	 * 
	 * @return the map
	 */
	public ConfigSection serialize();

	/**
	 * Deserializes this object from the given map.
	 * 
	 * @param map
	 *            the map.
	 */
	public void deserialize(ConfigSection map);
}