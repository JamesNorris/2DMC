package com.github.jamesnorris.game.entity.player;

import java.awt.Point;

import javax.swing.ImageIcon;

import com.github.jamesnorris.game.Panel;
import com.github.jamesnorris.game.World;
import com.github.jamesnorris.game.block.Block;
import com.github.jamesnorris.game.entity.Physical;
import com.github.jamesnorris.game.location.Direction;
import com.github.jamesnorris.game.location.Location;

public class Player extends Panel implements Physical {
	private static final Direction INITIAL_DIRECTION = Direction.SOUTH;
	private static final double DEFAULT_MOVEMENT_SPEED = 8;
	private World world;
	private Point loc;
	private Direction dir = INITIAL_DIRECTION;
	private double reach = Block.BLOCK_SIZE * 2, moveSpeed = DEFAULT_MOVEMENT_SPEED;

	private PlayerHandler handler = new PlayerHandler(this);

	public Player(ImageIcon icon, Location spawn) {
		setLocation(spawn);
		setSize(Block.BLOCK_SIZE, Block.BLOCK_SIZE);
		setIcon(icon);
		setVisible(true);
	}

	public Player(ImageIcon icon, World world, Point spawn) {
		this(icon, Location.toNearestLocation(world, spawn, world.getHighestZAt(Location.toNearestLocation(world, spawn, 3))));
	}

	@Override
	public Point getCenter() {
		return new Point(loc.x + Block.BLOCK_SIZE / 2, loc.y + Block.BLOCK_SIZE / 2);
	}

	public Direction getDirection() {
		return dir;
	}

	public PlayerHandler getHandler() {
		return handler;
	}

	@Override
	public Location getLocation() {
		return Location.toNearestLocation(world, loc, getZ());
	}

	public double getMovementSpeed() {
		return moveSpeed;
	}

	public double getReach() {
		return reach;
	}

	protected ImageIcon getUpdatedIcon() {
		return getUnderlyingPanel().getIcon();
	}

	@Override
	public World getWorld() {// convenience method
		return world;
	}

	public void setDirection(Direction dir) {
		this.dir = dir;
		// TODO set icon to player direction
	}

	@Override
	public void setLocation(Location loc) {
		world = loc.getWorld();
		this.loc = loc.toPoint();
		setZ(loc.getZ());
		super.setLocation(this.loc);
	}

	@Override
	@Deprecated
	public void setLocation(Point loc) {
		return;
	}

	public void setLocation(World world, Point loc) {
		this.world = world;
		super.setLocation(loc);
		this.loc = loc;
	}

	public void setMovementSpeed(double moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public void setReach(double reach) {
		this.reach = reach;
	}
}
