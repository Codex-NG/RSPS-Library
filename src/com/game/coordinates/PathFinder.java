package com.game.coordinates;

import java.util.Collection;

public interface PathFinder<E extends Coordinates> {

	Collection<E> findPath(E start, E goal);

	boolean blocked(E from, E to);

}
