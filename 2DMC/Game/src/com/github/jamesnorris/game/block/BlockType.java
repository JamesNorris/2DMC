package com.github.jamesnorris.game.block;

import javax.swing.ImageIcon;

public enum BlockType {
	AIR(null, 0, 0, 0, -1, MatterState.GAS),
	DIRT(new ImageIcon("resources/images/dirt_block.png"), .3F, 1, .1F, 5, MatterState.SOLID),
	GRASS(new ImageIcon("resources/images/grass_block.png"), .5F, 1, 0, 5, MatterState.SOLID),
	WATER(new ImageIcon("resources/images/water_block.png"), .5F, .7F, 0/*
	 * can't
	 * walk
	 * on
	 */, -1, MatterState.LIQUID),
	 STONE(new ImageIcon("resources/images/stone_block.png"), .1F, 1, 0, 15, MatterState.SOLID),
	 BEDROCK(new ImageIcon("resources/images/bedrock_block.png"), 1, 0, 0, -1, MatterState.SOLID);

	private ImageIcon icon;
	private float occurrence, friction, walkFriction;
	private int health;
	private MatterState state;

	BlockType(ImageIcon icon, float occurrence/* % out of 1 */, float friction, float walkFriction, int health, MatterState state) {
		this.icon = icon;
		this.occurrence = occurrence;
		this.friction = friction;// friction constant
		this.walkFriction = walkFriction;
		this.health = health;
		this.state = state;
	}

	public float getFricton() {
		return friction;
	}

	public int getHealth() {
		return health;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public float getNaturalOccurrence() {
		return occurrence;
	}

	public MatterState getState() {
		return state;
	}

	public float getWalkFriction() {
		return walkFriction;
	}

	public boolean isGas() {
		return state == MatterState.GAS;
	}

	public boolean isLiquid() {
		return state == MatterState.LIQUID;
	}

	public boolean isSolid() {
		return state == MatterState.SOLID;
	}
}
