package com.github.jamesnorris.game.location;

import java.awt.Point;

import com.github.jamesnorris.game.World;
import com.github.jamesnorris.game.block.Block;

public class Location implements Cloneable {
	public static double distance2D(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}

	public static double distance3D(Point p1, int z1, Point p2, int z2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) + Math.pow(z1 - z2, 2));
	}

	public static Location toNearestLocation(World world, Point p, int z) {
		return new Location(world, p.x / Block.BLOCK_SIZE + 1, p.y / Block.BLOCK_SIZE + 1, z);
	}

	// location uses to Point2D
	private World world;

	private int x, y, z;

	/**
	 * Represents a block location within the game world.
	 *
	 * @param world
	 *            The world which contains this location.
	 * @param x
	 *            The x-value of this location.
	 * @param y
	 *            The y-value of this location.
	 * @param z
	 *            The z-value of this location.
	 */
	public Location(World world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Location add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	@Override
	public Location clone() {
		try {
			return (Location) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public double distance2D(Location other) {
		return Math.sqrt(Math.pow(other.getX() - getX(), 2) + Math.pow(other.getY() - getY(), 2));
	}

	public double distance3D(Location other) {
		return Math.sqrt(Math.pow(other.getX() - getX(), 2) + Math.pow(other.getY() - getY(), 2) + Math.pow(other.getZ() - getZ(), 2));
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Location)) {
			return false;
		}
		Location loc = (Location) other;
		return loc.world.equals(world) && loc.x == x && loc.y == y && loc.z == z;
	}

	public World getWorld() {
		return world;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public Location subtract(int x, int y, int z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	public Point toPoint() {
		return new Point((x - 1) * Block.BLOCK_SIZE, (y - 1) * Block.BLOCK_SIZE);
	}

	@Override
	public String toString() {
		return "Location: (" + x + ", " + y + ", " + z + ")";
	}
}
