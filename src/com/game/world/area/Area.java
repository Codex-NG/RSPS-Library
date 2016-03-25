package com.game.world.area;

import com.game.entity.Entity;
import com.game.world.Coordinates;

/**
 * @author Albert Beaupre
 */
public abstract class Area implements MBR {

	/**
	 * The bottom left corner (South-West most)
	 */
	private Coordinates min;

	/**
	 * The top right corner (North-East most)
	 */
	private Coordinates max;

	/**
	 * Constructs a new {@code AbstractArea} within the given locations to
	 * create a cubic area region inside the specified {@code min} and
	 * {@code max} arguments.
	 * 
	 * @param cornerA
	 *            the first corner of the area
	 * @param cornerB
	 *            the opposite corner of the area
	 */
	public Area(Coordinates cornerA, Coordinates cornerB) {

		this.min = Coordinates.min(cornerA, cornerB);
		this.max = Coordinates.max(cornerA, cornerB);
	}

	public final Coordinates getMin() {
		return min;
	}

	public final Coordinates getMax() {
		return max;
	}

	@Override
	public int getMin(int axis) {
		return min.getMin(axis);
	}

	@Override
	public int getDimension(int axis) {
		return max.getMin(axis) - min.getMin(axis);
	}

	@Override
	public int getDimensions() {
		return min.getDimensions(); // three
	}

	public boolean contains(MBR m) {
		return MBRUtil.isOverlap(this, m);
	}

	/**
	 * Returns true if the given position is within this area. This
	 * 
	 * @param p
	 * @return
	 */
	public boolean contains(Coordinates p) {
		if (p.getX() < min.getX())
			return false;
		if (p.getY() < min.getY())
			return false;
		if (p.getX() > max.getX())
			return false;
		if (p.getY() > max.getY())
			return false;

		return true;
	}

	public abstract void onEnter(Entity entity, AreaChangeState state);

	public abstract void onLeave(Entity mob, AreaChangeState state);
}