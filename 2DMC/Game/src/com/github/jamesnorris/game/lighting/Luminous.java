package com.github.jamesnorris.game.lighting;

import java.awt.Paint;

import com.github.jamesnorris.game.entity.Shaped;
import com.github.jamesnorris.game.entity.VisibleObject;

public interface Luminous extends VisibleObject, Shaped {
	public Paint getPaint();
}
