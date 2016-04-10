package com.github.jamesnorris.game.entity;

import com.github.jamesnorris.game.location.Location;

public interface Placeable extends Physical {
	public void place(Location loc);
}
