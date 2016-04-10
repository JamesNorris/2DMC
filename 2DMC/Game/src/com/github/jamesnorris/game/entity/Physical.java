package com.github.jamesnorris.game.entity;

import java.awt.Point;

import com.github.jamesnorris.game.World;
import com.github.jamesnorris.game.location.Location;

public interface Physical extends VisibleObject {
	public Point getCenter();

	public Location getLocation();

	public World getWorld();

	public void setLocation(Location loc);
}
