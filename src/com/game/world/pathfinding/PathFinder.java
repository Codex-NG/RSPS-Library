package com.game.world.pathfinding;

import java.util.Collection;

import com.game.world.Coordinates;

public interface PathFinder<E extends Coordinates> {

	Collection<E> findPath(E start, E goal);

	boolean blocked(E from, E to);

}
