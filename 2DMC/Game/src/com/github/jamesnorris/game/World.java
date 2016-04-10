package com.github.jamesnorris.game;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import com.github.jamesnorris.game.block.Block;
import com.github.jamesnorris.game.entity.TileEntity;
import com.github.jamesnorris.game.entity.VisibleObject;
import com.github.jamesnorris.game.entity.player.Player;
import com.github.jamesnorris.game.lighting.LightMask;
import com.github.jamesnorris.game.lighting.Luminous;
import com.github.jamesnorris.game.location.Location;

public class World extends Panel implements VisibleObject {
	public static final Point ORIGIN = new Point(0, 0);
	public static final int HIGHEST_Z = 15;
	private long startTime = System.currentTimeMillis() - 300000;
	private TileEntity[][][] entities;
	private int width, height;
	private Point focus;
	private Player player;
	private LightMask lightMask;

	public World(WorldGenerator generator, int width, int height) {
		this.width = width;
		this.height = height;
		focus = new Point(width * Block.BLOCK_SIZE / 2, height * Block.BLOCK_SIZE / 2);
		entities = new TileEntity[width][height][HIGHEST_Z];
		setSize(width * Block.BLOCK_SIZE, height * Block.BLOCK_SIZE);
		getUnderlyingPanel().clearPaintTasks();
		generator.generate(this);
		addPlayer(new Player(new ImageIcon("resources/images/stone_block.png"), new Location(this, width / 2, height / 2, getHighestZAt(width / 2, height / 2, true) + 1)));
		setFocus(player.getUnderlyingPanel().getLocation());
		setBackground(Color.DARK_GRAY);
		lightMask = new LightMask(this, new ArrayList<Luminous>());
		lightMask.paintTo(this);
		setVisible(true);
	}

	public void add(TileEntity e) {
		Location loc = e.getLocation();
		if (!isWithinBlockBounds(loc.getX(), loc.getY(), loc.getZ())) {
			return;
		}
		if (e instanceof Luminous) {
			lightMask.addLight((Luminous) e);
		}
		super.add(e);
		entities[loc.getX() - 1][loc.getY() - 1][loc.getZ()] = e;
	}

	public void addPlayer(Player p) {
		player = p;
		add(p);
	}

	public boolean contains(TileEntity e) {
		TileEntity other = getEntityAt(e.getLocation());
		return other != null && other.equals(e);
	}

	public Block getBlockAt(int x, int y, boolean blockCoords, int z) {
		if (blockCoords) {
			if (!isWithinBlockBounds(x, y, z)) {
				return null;
			}
			TileEntity ent = entities[x - 1][y - 1][z];
			if (!(ent instanceof Block)) {
				return null;
			}
			return (Block) ent;
		}
		if (!isWithinBounds(x + Block.BLOCK_SIZE, y + Block.BLOCK_SIZE, z)) {
			return null;
		}
		TileEntity ent = entities[x / Block.BLOCK_SIZE][y / Block.BLOCK_SIZE][z];
		if (!(ent instanceof Block)) {
			return null;
		}
		return (Block) ent;
	}

	public Block getBlockAt(Location loc) {
		return getBlockAt(loc.getX(), loc.getY(), true, loc.getZ());
	}

	public int getBlockHeight() {
		return height;
	}

	public int getBlockWidth() {
		return width;
	}

	public TileEntity getEntityAt(int x, int y, boolean blockCoords, int z) {
		if (blockCoords) {
			if (!isWithinBlockBounds(x, y, z)) {
				return null;
			}
			return entities[x - 1][y - 1][z];
		}
		if (!isWithinBounds(x + Block.BLOCK_SIZE, y + Block.BLOCK_SIZE, z)) {
			return null;
		}
		return entities[x / Block.BLOCK_SIZE][y / Block.BLOCK_SIZE][z];
	}

	public TileEntity getEntityAt(Location loc) {
		return getEntityAt(loc.getX(), loc.getY(), true, loc.getZ());
	}

	public Point getFocus() {
		return focus;
	}

	public int getHighestZAt(int x, int y, boolean blockCoords) {
		return getHighestZAt(x, y, blockCoords, HIGHEST_Z - 1, 0);
	}

	public int getHighestZAt(int x, int y, boolean blockCoords, int topConstraint, int bottomConstraint) {// non-air
		for (int i = topConstraint; i >= bottomConstraint; i--) {
			TileEntity entity = getEntityAt(x, y, blockCoords, i);
			if (entity instanceof Block && ((Block) entity).getBlockType().isGas()) {
				continue;
			}
			if (entity != null) {
				return entity.getZ();
			}
		}
		return -1;// no blocks, not even bedrock? that's odd...
	}

	public int getHighestZAt(Location loc) {
		return getHighestZAt(loc, HIGHEST_Z - 1, 0);
	}

	public int getHighestZAt(Location loc, int topConstraint, int bottomConstraint) {
		return getHighestZAt(loc.getX(), loc.getY(), true, topConstraint, bottomConstraint);
	}

	public Player getPlayer() {
		return player;
	}

	public long getTime() {
		return (System.currentTimeMillis() - startTime) % 600000;// 10 minutes
	}

	public boolean isWithinBlockBounds(int x, int y, int z) {
		return isWithinBounds((x - 1) * Block.BLOCK_SIZE, (y - 1) * Block.BLOCK_SIZE, z);
	}

	public boolean isWithinBounds(int x, int y, int z) {
		if (x < 0 || y < 0 || z < 0 || x >= width * Block.BLOCK_SIZE || y >= height * Block.BLOCK_SIZE || z >= HIGHEST_Z) {
			return false;
		}
		return true;
	}

	public void remove(TileEntity e) {
		if (e instanceof Luminous) {
			lightMask.removeLight((Luminous) e);
		}
		super.remove(e);
		Location loc = e.getLocation();
		entities[loc.getX() - 1][loc.getY() - 1][loc.getZ()] = null;
	}

	public void setFocus(Point focus) {
		this.focus = focus;
		GameFrame frame = Driver.getFrame();
		setLocation(frame.getWidth() / 2 - focus.x - frame.getInsets().left - Block.BLOCK_SIZE / 2, frame.getHeight() / 2 - focus.y - frame.getInsets().top
				- Block.BLOCK_SIZE / 2);
	}
	
	public LightMask getLightMask() {
		return lightMask;
	}
}
