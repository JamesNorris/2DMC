package com.github.jamesnorris.game.entity;

import java.util.Stack;

public interface Breakable extends Physical {
	public void damage(int amt);

	public Stack<Item> getBreakRewards();

	public int getHealth();

	public int getInitialHealth();

	public void remove();

	public void setHealth(int health);
}
