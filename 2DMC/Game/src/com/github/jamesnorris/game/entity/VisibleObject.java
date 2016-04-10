package com.github.jamesnorris.game.entity;

public interface VisibleObject extends Comparable<VisibleObject> {
	public int getZ();

	public void repaint();
}
