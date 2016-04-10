package com.github.jamesnorris.game.entity;

public enum Relation {
	UP(0, 0, 1), 
	DOWN(0, 0, -1), 
	NORTH(0, 1, 0), 
	SOUTH(0, -1, 0), 
	EAST(1, 0, 0), 
	WEST(-1, 0, 0), 
	NORTHEAST(1, 1, 0), 
	SOUTHEAST(1, -1, 0), 
	SOUTHWEST(-1, -1, 0), 
	NORTHWEST(-1, 1, 0);
	
	protected int x, y, z;
	
	Relation(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
