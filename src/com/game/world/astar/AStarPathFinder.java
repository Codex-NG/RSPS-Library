package com.game.world.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.game.world.pathfinding.PathFinder;

public abstract class AStarPathFinder implements PathFinder<AStarNode> {

	@Override
	public List<AStarNode> findPath(AStarNode startPoint, AStarNode endPoint) {
		ArrayList<AStarNode> open = new ArrayList<>();
		HashSet<AStarNode> closed = new HashSet<>();
		final AStarNode start = new AStarNode(startPoint.getX(), startPoint.getY(), startPoint.getZ());
		final AStarNode goal = new AStarNode(endPoint.getX(), endPoint.getY(), endPoint.getZ());
		if (start.getZ() != goal.getZ() || start.equals(goal))
			return Collections.emptyList();

		start.heuristic = heuristic(start, goal);
		open.add(start);

		while (open.size() > 0) {
			Collections.sort(open, (o1, o2) -> Double.compare(o1.getF(), o2.getF()));
			AStarNode current = open.get(0);
			if (current.equals(goal))
				return construct(start, current);
			open.remove(current);
			closed.add(current);
			for (AStarNode neighbor : current.getNeighbors(this, goal, 1)) {
				if (closed.contains(neighbor))
					continue;

				if (neighbor.equals(goal))
					return construct(start, neighbor);
				double tentative_cost = current.cost + current.distance(neighbor);
				if (!open.contains(neighbor) || tentative_cost < neighbor.cost) {
					neighbor.cost = tentative_cost;
					if (!open.contains(neighbor)) {
						open.add(neighbor);
					}
				}
			}
		}
		return Collections.emptyList();
	}

	private final List<AStarNode> construct(AStarNode start, AStarNode goal) {
		List<AStarNode> path = new ArrayList<>();
		AStarNode last = goal;
		while (!last.equals(start)) {
			path.add(last);
			last = (AStarNode) last.getParent();
		}
		Collections.reverse(path);
		return path;
	}

	@Override
	public abstract boolean blocked(AStarNode from, AStarNode to);

	public abstract double heuristic(AStarNode from, AStarNode to);

}
