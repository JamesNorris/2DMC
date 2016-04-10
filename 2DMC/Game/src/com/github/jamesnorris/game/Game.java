package com.github.jamesnorris.game;

public class Game {
	private int width, height;
	private World world;

	public Game(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public World getWorld() {
		return world;
	}

	public void initWorld() {
		world = new World(new WorldGenerator(), width, height);
	}
}
