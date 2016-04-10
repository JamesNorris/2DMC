package com.github.jamesnorris.game.entity;

import java.awt.Point;
import java.util.Stack;

import javax.swing.ImageIcon;

import com.github.jamesnorris.game.Driver;
import com.github.jamesnorris.game.Panel;
import com.github.jamesnorris.game.World;
import com.github.jamesnorris.game.block.Block;
import com.github.jamesnorris.game.location.Location;
import com.github.jamesnorris.game.ui.Hotbar;
import com.github.jamesnorris.game.ui.MenuOption;

public abstract class TileEntity extends Panel implements Physical {
	private Location loc;
	private int width, height;

	public TileEntity(Location loc, int width/* blocks */, int height/* blocks */) {
		this.loc = loc;
		this.width = width;
		this.height = height;
		setLocation(loc);
		setSize(width * Block.BLOCK_SIZE, height * Block.BLOCK_SIZE);
		setIcon(getImage());
		setVisible(true);
	}
	
	public TileEntity getRelative(Relation rel) {
		return loc.getWorld().getEntityAt(loc.clone().add(rel.x, rel.y, rel.z));
	}

	@Override
	public Point getCenter() {
		Point p = getLocation().toPoint();
		return new Point(p.x + Block.BLOCK_SIZE * width / 2, p.y + Block.BLOCK_SIZE * height / 2);
	}

	public int getHeight() {
		return height;
	}

	public abstract ImageIcon getImage();

	@Override
	public Location getLocation() {
		return loc;
	}

	public abstract Location getPlaceLocation();

	public int getWidth() {
		return width;
	}

	@Override
	public World getWorld() {
		return loc.getWorld();
	}

	@Override
	public void setLocation(Location loc) {
		this.loc = loc;
		setZ(loc.getZ());
		super.setLocation((loc.getX() - 1) * Block.BLOCK_SIZE, (loc.getY() - 1) * Block.BLOCK_SIZE);
	}

	public boolean toPlayerHand() {
		if (!(this instanceof Breakable)) {
			return false;
		}
		((Breakable) this).remove();
		Stack<Item> thisStack = ((Breakable) this).getBreakRewards();
		if (thisStack.isEmpty()) {
			return true;
		}
		Hotbar hotBar = Driver.getFrame().getOverlay().getHotbar();
		boolean placed = false;
		int firstOpen = -1;
		for (int i = 0; i < 10; i++) {
			MenuOption opt = hotBar.getOption(i);
			if (opt.getStack().isEmpty()) {
				if (firstOpen == -1) {
					firstOpen = i;
				}
				continue;
			}
			if (opt.getStack().peek().getClass().equals(thisStack.peek().getClass())) {
				if (thisStack.peek() instanceof Block && ((Block) opt.getStack().peek()).getBlockType() != ((Block) thisStack.peek()).getBlockType()) {
					continue;
				}
				for (Item item : thisStack) {
					opt.getStack().push(item);
				}
				// opt.redraw();//TODO
				placed = true;
				break;
			}
		}
		if (firstOpen != -1 && !placed) {
			hotBar.getOption(firstOpen).setStack(thisStack);
		}
		return true;
	}
}
