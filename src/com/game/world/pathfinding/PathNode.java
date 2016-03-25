package com.game.world.pathfinding;

import java.util.ArrayList;

import com.game.world.GenericCoordinates;

public class PathNode extends GenericCoordinates {

	private PathNode parent;
	private int x, y, z;

	/**
	 * Constructs a new {@code Location} from the specified coordinates.
	 * 
	 * @param x
	 *            the x coordinate of the node
	 * @param y
	 *            the y coordinate of the node
	 * @param z
	 *            the z coordinate of the node
	 */
	public PathNode(int x, int y, int z) {
		super(x, y, z);
		parent = this;
	}

	/**
	 * Constructs a new {@code Location} from the specified coordinates with the
	 * specified parent.
	 * 
	 * @param x
	 *            the x coordinate of the node
	 * @param y
	 *            the y coordinate of the node
	 * @param z
	 *            the z coordinate of the node
	 * @param parent
	 *            the parent of this node
	 */
	public PathNode(int x, int y, int z, PathNode parent) {
		this(x, y, z);
		this.parent = parent;
	}

	/**
	 * Creates a list of neighboring nodes around the specified goal within the
	 * specified radius.
	 * 
	 * @param goal
	 *            the goal to create a list of neighbors around
	 * @return the list of neighbors
	 */
	public ArrayList<PathNode> getNeighbors(PathFinder<PathNode> finder, PathNode goal, int radius) {
		ArrayList<PathNode> neighbors = new ArrayList<>();
		for (int nx = -radius; nx <= radius; nx++) {
			for (int ny = -radius; ny <= radius; ny++) {
				if (nx == 0 && ny == 0)
					continue;
				PathNode node = new PathNode(x + nx, y + ny, this.z, this);
				if (finder.blocked(node, goal))
					continue;
				neighbors.add(node);
			}
		}
		return neighbors;
	}

	/**
	 * Gets the closest neighboring nodes of the specified goal within the
	 * specified radius.
	 * 
	 * @param goal
	 *            the node to get the closest neighboring node
	 * @param radius
	 *            the radius from the goal to check neighboring nodes within
	 * @return the closest neighboring node from the goal within the radius
	 */
	public PathNode getClosestNeighbor(PathFinder<PathNode> finder, PathNode goal, int radius) {
		PathNode closest = null;
		double previous = 1000;
		for (int nx = -radius; nx <= radius; nx++) {
			for (int ny = -radius; ny <= radius; ny++) {
				if (nx == 0 && ny == 0)
					continue;
				PathNode node = new PathNode(x + nx, y + ny, this.z, this);
				if (finder.blocked(node, goal))
					continue;
				double dist = node.distance(goal);
				if (dist < previous) {
					previous = dist;
					closest = node;
				}
			}
		}
		return closest;
	}

	public PathNode getParent() {
		return parent;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PathNode) {
			PathNode n = (PathNode) o;
			return x == n.x && y == n.y && z == n.z;
		}
		return false;
	}

	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + ", z=" + z + "]";
	}

}
