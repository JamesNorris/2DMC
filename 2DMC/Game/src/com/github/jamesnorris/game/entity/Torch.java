package com.github.jamesnorris.game.entity;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.ImageIcon;

import com.github.jamesnorris.game.Utilities;
import com.github.jamesnorris.game.block.Block;
import com.github.jamesnorris.game.lighting.LightBuilder;
import com.github.jamesnorris.game.lighting.Luminous;
import com.github.jamesnorris.game.location.Location;

public class Torch extends TileEntity implements Item, Placeable, Breakable, Luminous {
	private static final ImageIcon TORCH_TILE_IMAGE = new ImageIcon("resources/images/torch_tile.png");
	private static final ImageIcon TORCH_ITEM_IMAGE = new ImageIcon("resources/images/torch_item.png");
	private int health = getInitialHealth();
	private Polygon shape;

	public Torch(Location loc) {
		super(loc, 1, 1);
		getUnderlyingPanel().setResizeEnabled(false);
		getUnderlyingPanel().setOpaque(false);
		generateShape();
	}

	@Override
	public void damage(int amt) {
		health -= amt;
		if (health <= 0) {
			toPlayerHand();
		}
	}

	@Override
	public Stack<Item> getBreakRewards() {
		Stack<Item> stack = new Stack<Item>();
		stack.push(this);
		return stack;
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public ImageIcon getIconImage() {
		return TORCH_ITEM_IMAGE;
	}

	@Override
	public ImageIcon getImage() {
		return Utilities.resize(TORCH_TILE_IMAGE, Block.BLOCK_SIZE, Block.BLOCK_SIZE);
	}

	@Override
	public int getInitialHealth() {
		return 1;
	}

	@Override
	public Paint getPaint() {
		return new LightBuilder().setCenter(getCenter())
				.setColors(new Color[] { new Color(255, 240, 180, 150), new Color(255, 255, 200, 80), new Color(255, 255, 255, 5) })
				.setRadius(Block.BLOCK_SIZE * 5 / 2).setDistances(new float[] { .3F, .7F, 1F }).build();
	}

	@Override
	public Location getPlaceLocation() {
		return getLocation();
	}

	protected void generateShape() {
		Point pt = getCenter();
		List<Integer> listX = new ArrayList<Integer>();
		List<Integer> listY = new ArrayList<Integer>();
		int r = Block.BLOCK_SIZE * 5 / 2;
		for (int i = 0; i < 360; i += 1) {
			int angle = i;
			int x1 = (int) (r * Math.cos(angle * Math.PI / 180));
			int y1 = (int) (r * Math.sin(angle * Math.PI / 180));
			listX.add(pt.x + x1);
			listY.add(pt.y + y1);
		}
		int[] xs = new int[listX.size()];
		int[] ys = new int[listY.size()];
		for (int i = 0; i < listX.size(); i++) {
			xs[i] = listX.get(i);
			ys[i] = listY.get(i);
		}
		shape = new Polygon(xs, ys, listX.size());
	}

	@Override
	public Shape getShape() {
		return shape;
	}

	@Override
	public void place(Location loc) {//TODO block below disappears
		setHealth(getInitialHealth());
		setLocation(loc);
		generateShape();
		loc.getWorld().add(this);
	}

	@Override
	public void remove() {
		getLocation().getWorld().remove(this);
	}

	@Override
	public void setHealth(int health) {
		this.health = health;
		if (health == 0) {
			damage(1);
		}
	}
}
