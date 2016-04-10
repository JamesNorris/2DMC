package com.github.jamesnorris.game;

import com.github.jamesnorris.game.block.Block;
import com.github.jamesnorris.game.block.BlockType;
import com.github.jamesnorris.game.entity.TileEntity;
import com.github.jamesnorris.game.location.Location;

public class WorldGenerator {
	private World world;

	public void generate(World world) {
		this.world = world;
		for (int x = 1; x <= world.getBlockWidth(); x++) {
			for (int y = 1; y <= world.getBlockHeight(); y++) {
				for (int z = 1; z < World.HIGHEST_Z; z++) {
					place(x, y, z);
				}
			}
		}
	}

	protected void place(int x, int y, int z) {// regular coords (non-block)
		double percent = Math.random() * 100;
		BlockType type = BlockType.GRASS;
		if (percent <= 2 || (z < World.HIGHEST_Z / 2 && percent <= 30)) {
			type = BlockType.STONE;
		} else if (percent <= 10 + z * 5) {
			type = BlockType.AIR;
		} else if (percent <= 35) {
			type = BlockType.WATER;
		} else if (percent <= 40) {
			type = BlockType.DIRT;
		} else {
			type = BlockType.GRASS;
		}
		Block below = world.getBlockAt(x, y, true, z - 1);
		if (below != null && !below.getBlockType().isSolid()) {
			type = BlockType.AIR;
		}
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == j || !world.isWithinBounds(x + i, y + j, z)) {
					continue;
				}
				TileEntity e = world.getEntityAt(x + i, y + j, true, 1);
				if (!(e instanceof Block)) {
					continue;
				}
				Block b = (Block) e;
				if (b != null) {
					if (Math.random() <= b.getBlockType().getNaturalOccurrence()) {
						type = b.getBlockType();
					}
				}
			}
		}
		world.add(new Block(type, new Location(world, x, y, z)));
		if (z == 1) {
			world.add(new Block(BlockType.BEDROCK, new Location(world, x, y, 0)));
		}
	}
}
