package com.github.jamesnorris.game.location;

public enum Direction {
	NORTH(90),
	SOUTH(270),
	EAST(0),
	WEST(180);

	private double theta;

	Direction(double theta) {
		this.theta = theta;
	}

	public double getTheta() {
		return theta;
	}

}
