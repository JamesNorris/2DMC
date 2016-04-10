package com.github.jamesnorris.game.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import com.github.jamesnorris.game.Driver;
import com.github.jamesnorris.game.GameFrame;
import com.github.jamesnorris.game.PaintTask;
import com.github.jamesnorris.game.Panel;
import com.github.jamesnorris.game.entity.VisibleObject;

public class Hotbar extends Panel implements VisibleObject {
	private int size;
	private MenuOption[] options;

	public Hotbar(int size, int options) {
		this.size = size;
		this.options = new MenuOption[options];
		getUnderlyingPanel().setOpaque(false);
		Point topLeft = nearBottomRight(size * options, size);
		setLocation(topLeft);
		setSize(size * options, size);
		for (int i = 0; i < options; i++) {
			MenuOption option = new MenuOption(new Point(size * i, 0), size);
			setOption(i, option);
			option.setText(i + "");
			add(option);
		}
		getUnderlyingPanel().addPaintTask(new PaintTask() {

			@Override
			public void paintAfter(Graphics g) {
			}

			@Override
			public void paintBefore(Graphics g) {
				g.setColor(new Color(230, 230, 255, 150));
				g.fillRect(0, 0, size * options, size);
			}
		});
		setVisible(true);
	}

	public MenuOption getOption(int index) {
		return options[index];
	}

	public MenuOption getOptionAt(Point loc) {
		Point topLeft = nearBottomRight(size * options.length, size);
		if (loc.x < topLeft.x || loc.y < topLeft.y) {
			return null;
		}
		return options[(int) Math.floor((loc.x - topLeft.x) / size)];
	}

	public MenuOption getSelected() {
		for (MenuOption option : options) {
			if (option.isSelected()) {
				return option;
			}
		}
		return null;
	}

	private Point nearBottomRight(int widthAway, int heightAway) {
		GameFrame frame = Driver.getFrame();
		return new Point(frame.getWidth() - frame.getInsets().left - frame.getInsets().right - widthAway, frame.getHeight() - frame.getInsets().top
				- frame.getInsets().bottom - heightAway);
	}

	public void select(int index) {
		select(getOption(index));
	}

	public void select(MenuOption option) {
		for (MenuOption other : options) {
			if (other == option) {// same pointer
				other.select(true);
			} else {
				other.select(false);
			}
			other.repaint();
		}
	}

	public void setOption(int index, MenuOption option) {
		options[index] = option;
	}
}
