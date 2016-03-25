package com.game.world.astar;

import java.util.ArrayList;

import com.game.world.pathfinding.PathNode;

/**
 * @author Albert Beaupre
 */
public class AStarNode extends PathNode {

	public double cost, heuristic;

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
	public AStarNode(int x, int y, int z) {
		super(x, y, z);
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
	public AStarNode(int x, int y, int z, AStarNode parent) {
		super(x, y, z, parent);
	}

	/**
	 * Creates a list of neighboring nodes around the specified goal within the
	 * specified radius.
	 * 
	 * @param goal
	 *            the goal to create a list of neighbors around
	 * @return the list of neighbors
	 */
	public ArrayList<AStarNode> getNeighbors(AStarPathFinder finder, AStarNode goal, int radius) {
		ArrayList<AStarNode> neighbors = new ArrayList<>();
		for (int nx = -radius; nx <= radius; nx++) {
			for (int ny = -radius; ny <= radius; ny++) {
				if (nx == 0 && ny == 0)
					continue;
				AStarNode node = new AStarNode(getX() + nx, getY() + ny, getZ(), this);
				if (finder.blocked(node, goal))
					continue;
				node.cost = node.distance(this);
				node.heuristic = finder.heuristic(node, goal);
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
	public AStarNode getClosestNeighbor(AStarPathFinder finder, AStarNode goal, int radius) {
		AStarNode closest = null;
		double previous = 1000;
		for (int nx = -radius; nx <= radius; nx++) {
			for (int ny = -radius; ny <= radius; ny++) {
				if (nx == 0 && ny == 0)
					continue;
				AStarNode node = new AStarNode(getX() + nx, getY() + ny, getZ(), this);
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

	public double getF() {
		return cost + heuristic;
	}

}
