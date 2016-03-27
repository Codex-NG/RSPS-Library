package com.game.world.area;

import java.util.HashSet;

import com.event.EventListener;

public class AreaManager implements EventListener {

	private AreaGrid<Area> areas;

	public AreaManager() {
		this.areas = new AreaGrid<Area>(16384, 16384, 8);
	}

	public HashSet<Area> getAreas(MBR overlap) {
		return areas.get(overlap, 16);
	}
}