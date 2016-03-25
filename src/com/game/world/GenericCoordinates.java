package com.game.world;

public class GenericCoordinates implements Coordinates {

	private int x, y, z;

	public GenericCoordinates(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getZ() {
		return z;
	}

	@Override
	public GenericCoordinates setX(int x) {
		this.x = x;
		return this;
	}

	@Override
	public GenericCoordinates setY(int y) {
		this.y = y;
		return this;
	}

	@Override
	public GenericCoordinates setZ(int z) {
		this.z = z;
		return this;
	}

	@Override
	public int getDimension(int axis) {
		return 1;
	}

	@Override
	public int getDimensions() {
		return 3;
	}

	@Override
	public int getMin(int axis) {
		switch (axis) {
		case 0:
			return x;
		case 1:
			return y;
		case 2:
			return z;
		}
		throw new IllegalArgumentException("Invalid axis requested. Given " + axis);
	}

}
