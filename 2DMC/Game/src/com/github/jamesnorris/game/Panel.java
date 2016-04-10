package com.github.jamesnorris.game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Observable;

import javax.swing.ImageIcon;

import com.github.jamesnorris.game.entity.VisibleObject;

public class Panel extends Observable implements Comparable<VisibleObject> {
	private IconPanel panel;

	public Panel() {
		panel = new IconPanel() {
			private static final long serialVersionUID = -6271319603383170472L;

			@Override
			public void invalidate() {
				super.invalidate();
				setChanged();
			}

			@Override
			public void update() {
				notifyObservers();
			}
		};
	}

	public void add(Component c) {
		add(c, -1);
	}

	public void add(Component c, int layer) {
		panel.add(c, layer);
	}

	public void add(Panel panel) {
		add(panel.getUnderlyingPanel());
	}

	public Color getBackground() {
		return panel.getBackground();
	}

	public Color getForeground() {
		return panel.getForeground();
	}

	public Dimension getSize() {
		return panel.getSize();
	}

	public IconPanel getUnderlyingPanel() {
		return panel;
	}

	public int getZ() {
		return panel.getZ();
	}

	public boolean isVisible() {
		return panel.isVisible();
	}

	public void remove(Component c) {
		panel.remove(c);
	}

	public void remove(Panel panel) {
		remove(panel.getUnderlyingPanel());
	}

	public void removeAll() {
		panel.removeAll();
	}
	
	public void repaint() {
		panel.repaint();
	}

	public void setBackground(Color bg) {
		panel.setBackground(bg);
	}

	public void setForeground(Color fg) {
		panel.setForeground(fg);
	}

	public void setIcon(ImageIcon icon) {
		panel.setIcon(icon);
		setChanged();
	}

	public void setLocation(int x, int y) {
		setLocation(new Point(x, y));
	}

	public void setLocation(Point loc) {
		panel.setLocation(loc);
	}

	public void setSize(Dimension dims) {
		panel.setSize(dims);
	}

	public void setSize(int x, int y) {
		setSize(new Dimension(x, y));
	}

	public void setVisible(boolean visible) {
		panel.setVisible(visible);
	}

	public void setZ(int z) {
		panel.setZ(z);
	}

	public void update() {
		setChanged();
		notifyObservers();
	}
	
	@Override
	public int compareTo(VisibleObject other) {
		return getZ() - other.getZ();
	}
}
