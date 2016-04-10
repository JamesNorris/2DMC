package com.github.jamesnorris.game.ui;

import com.github.jamesnorris.game.Driver;
import com.github.jamesnorris.game.Panel;
import com.github.jamesnorris.game.entity.VisibleObject;

public class InterfaceOverlay extends Panel implements VisibleObject {
	private Hotbar hotBar = new Hotbar(60, 10);

	public InterfaceOverlay() {
		setLocation(0, 0);
		setSize(Driver.getFrame().getSize());
		getUnderlyingPanel().setOpaque(false);
		setVisible(true);
		add(hotBar);
	}

	public Hotbar getHotbar() {
		return hotBar;
	}
	
	@Override
	public int compareTo(VisibleObject other) {
		return 1;
	}
}
