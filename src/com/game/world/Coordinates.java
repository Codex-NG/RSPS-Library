package com.game.world;

import com.game.world.area.MBR;

/**
 * @author Albert Beaupre
 */
public interface Coordinates extends MBR {

	/**
	 * Returns the x coordinate of this {@code Coordinates} class.
	 * 
	 * @return the x coordinate
	 */
	int getX();

	/**
	 * Returns the y coordinate of this {@code Coordinates} class.
	 * 
	 * @return the y coordinate
	 */
	int getY();

	/**
	 * Returns the z coordinate of this {@code Coordinates} class.
	 * 
	 * @return the z coordinate
	 */
	int getZ();

	/**
	 * Sets the x coordinates of this {@code Coordinates} class.
	 * 
	 * @return the instance of coordinates with the new x value
	 */
	Coordinates setX(int x);

	/**
	 * Sets the y coordinates of this {@code Coordinates} class.
	 * 
	 * @return the instance of coordinates with the new y value
	 */
	Coordinates setY(int y);

	/**
	 * Sets the z coordinates of this {@code Coordinates} class.
	 * 
	 * @return the instance of coordinates with the new z value
	 */
	Coordinates setZ(int z);

	/**
	 * Returns the distance between the specified {@code coordinates} and this
	 * {@code Coordinates}.
	 * 
	 * @param coordinates
	 *            the coordinates to get the distance between
	 * @return the distance
	 */
	default double distance(Coordinates coordinates) {
		double px = coordinates.getX() - this.getX();
		double py = coordinates.getY() - this.getY();
		double pz = coordinates.getZ() - this.getZ();
		return Math.sqrt(px * px + py * py + pz * pz);
	}

	default Coordinates setCoordinates(Coordinates c) {
		return setX(c.getX()).setY(c.getY()).setZ(c.getZ());
	}

	default Coordinates setCoordinates(int x, int y, int z) {
		return setX(x).setY(y).setZ(z);
	}

	public static Coordinates max(Coordinates l, Coordinates... locs) {
		int x = l.getX();
		int y = l.getY();
		int z = l.getZ();

		for (Coordinates m : locs) {
			if (m.getX() > x)
				x = m.getX();
			if (m.getY() > y)
				y = m.getY();
			if (m.getZ() > z)
				z = m.getZ();
		}

		return new GenericCoordinates(x, y, z);
	}

	public static Coordinates min(Coordinates l, Coordinates... locs) {
		int x = l.getX();
		int y = l.getY();
		int z = l.getZ();

		for (Coordinates m : locs) {
			if (m.getX() < x)
				x = m.getX();
			if (m.getY() < y)
				y = m.getY();
			if (m.getZ() < z)
				z = m.getZ();
		}

		return new GenericCoordinates(x, y, z);
	}

}
