package com.github.jamesnorris.game.block;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Stack;

import javax.swing.ImageIcon;

import com.github.jamesnorris.game.PaintTask;
import com.github.jamesnorris.game.Utilities;
import com.github.jamesnorris.game.entity.Breakable;
import com.github.jamesnorris.game.entity.Item;
import com.github.jamesnorris.game.entity.Placeable;
import com.github.jamesnorris.game.entity.Relation;
import com.github.jamesnorris.game.entity.Shaped;
import com.github.jamesnorris.game.entity.TileEntity;
import com.github.jamesnorris.game.location.Location;

public class Block extends TileEntity implements Placeable, Breakable, Item, Shaped {
	public static final int BLOCK_SIZE = 120;// px
	//@formatter:off
	private static final ImageIcon[] CRACKS = new ImageIcon[] {
		new ImageIcon("resources/images/cracks_1.png"),
		new ImageIcon("resources/images/cracks_2.png"),
		new ImageIcon("resources/images/cracks_3.png")
	};
	//@formatter:on

	private BlockType type;
	private int health;

	public Block(BlockType type, Location loc) {
		super(loc, BLOCK_SIZE, BLOCK_SIZE);
		this.type = type;
		health = type.getHealth();
		getUnderlyingPanel().setOpaque(false);
		setLocation(loc);
		setSize(BLOCK_SIZE, BLOCK_SIZE);
		setIcon(type.getIcon());
		getUnderlyingPanel().addPaintTask(new PaintTask() {
			private BlockType last = null;

			@Override
			public void paintAfter(Graphics g) {
				if (getBlockType().isSolid() && loc.getWorld().getHighestZAt(loc) > getZ()) {
					setVisible(false);
					return;
				}
				if (!isVisible()) {
					setVisible(true);
				}
				if (getBlockType().isSolid() && getHealth() < type.getHealth()) {
					double percent = getHealth() / (double) type.getHealth();
					g.drawImage(Utilities.resize(CRACKS[percent <= 1 / 3.0 ? 2 : percent <= 2 / 3.0 ? 1 : 0], BLOCK_SIZE, BLOCK_SIZE).getImage(), 0, 0, null);
				}
				if (last == getBlockType()) {
					return;
				}
				last = getBlockType();
			}

			@Override
			public void paintBefore(Graphics g) {

			}
		});
		//getWorld().addObserver(this);
		setVisible(true);
	}

//	private boolean isContained() {
//		Dimension view = Driver.getFrame().getSize();
//		Point focus = getLocation().getWorld().getFocus();
//		Point pt = getUnderlyingPanel().getLocation();
//		return Math.abs(pt.x - focus.x) <= view.width / 2 && Math.abs(pt.y - focus.y) <= view.height / 2;
//	}

	@Override
	public void damage(int amt) {
		if (!type.isSolid()) {
			TileEntity te = getRelative(Relation.DOWN);
			if (te != null && te instanceof Breakable) {
				((Breakable) te).damage(amt);
			}
			return;// cannot damage anything other than a solid
		}
		if (health != -1) {
			health -= amt;
		}
		// redraw();//TODO
		if (health == 0) {
			toPlayerHand();
			setBlockType(BlockType.AIR);
			Block below = getLocation().getWorld().getBlockAt(getLocation().clone().subtract(0, 0, 1));
			if (below != null) {
				below.setVisible(true);
			}
		}
	}

	public BlockType getBlockType() {
		return type;
	}

	@Override
	public Stack<Item> getBreakRewards() {
		Stack<Item> stack = new Stack<Item>();
		stack.push(new Block(type, new Location(getLocation().getWorld(), -1, -1, getLocation().getZ())));
		return stack;
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public ImageIcon getIconImage() {
		return getImage();
	}

	@Override
	public ImageIcon getImage() {
		if (type == null) {
			return null;
		}
		return type.getIcon();
	}

	@Override
	public int getInitialHealth() {
		return type.getHealth();
	}

	@Override
	public Location getPlaceLocation() {
		return type.isSolid() ? getLocation().clone().add(0, 0, 1) : getLocation().clone();
	}

	@Override
	public Shape getShape() {
		Point pt = getLocation().toPoint();
		return new Rectangle(pt.x, pt.y, BLOCK_SIZE, BLOCK_SIZE);
	}

	@Override
	public void place(Location loc) {
		TileEntity ent = loc.getWorld().getEntityAt(loc);
		if (ent != null) {
			loc.getWorld().remove(ent);
		}
		setLocation(loc);
		// redraw();//TODO
		loc.getWorld().add(this);
	}

	@Override
	public void remove() {
		getLocation().getWorld().remove(this);
	}

	public void setBlockType(BlockType type) {
		this.type = type;
		setIcon(Utilities.resize(type.getIcon(), BLOCK_SIZE, BLOCK_SIZE));
		if (!type.isSolid()) {
			getLocation().getWorld().getPlayer().getHandler().move(0, 0);
		}
	}

	@Override
	public void setHealth(int health) {
		this.health = health;
		if (health == 0) {
			damage(1);
		}
	}

	@Override
	public String toString() {
		return "Block: (" + type.toString() + " at " + getLocation() + ")";
	}
}
