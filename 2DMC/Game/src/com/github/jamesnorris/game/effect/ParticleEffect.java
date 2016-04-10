package com.github.jamesnorris.game.effect;

import com.github.jamesnorris.game.entity.VisibleObject;

public interface ParticleEffect extends VisibleObject {
	public int getLife();

	public void play();
}
