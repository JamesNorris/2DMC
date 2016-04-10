package com.github.jamesnorris.game;

import java.awt.Graphics;

public interface PaintTask {
	public void paintAfter(Graphics g);

	public void paintBefore(Graphics g);
}
