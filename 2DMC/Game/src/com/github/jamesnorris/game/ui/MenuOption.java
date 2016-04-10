package com.github.jamesnorris.game.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Stack;

import javax.swing.ImageIcon;

import com.github.jamesnorris.game.PaintTask;
import com.github.jamesnorris.game.Panel;
import com.github.jamesnorris.game.entity.Item;
import com.github.jamesnorris.game.entity.VisibleObject;

public class MenuOption extends Panel implements VisibleObject {
	private static final ImageIcon MENU_OPTION_IMAGE = new ImageIcon("resources/images/menu_option_ui.png");
	private String text;
	private Stack<Item> items = new Stack<Item>();
	private boolean selected;

	public MenuOption(Point loc, int size) {
		getUnderlyingPanel().setOpaque(false);
		setLocation(loc);
		setSize(size, size);
		setIcon(MENU_OPTION_IMAGE);
		getUnderlyingPanel().addPaintTask(new PaintTask() {

			@Override
			public void paintAfter(Graphics g) {
				g.setColor(new Color(82, 82, 82, 50));
				g.fillRect(0, size - 22, size, 22);
				int num = 0;
				if (!items.isEmpty() && items.peek().getIconImage() != null) {
					ImageIcon icon = items.peek().getIconImage();
					g.drawImage(icon.getImage(), Math.abs(icon.getIconWidth() - size) / 2, Math.abs(icon.getIconHeight() - size) / 2, null);
					g.setColor(Color.WHITE);
					num = getStack().size();
				}
				g.setColor(Color.WHITE);
				g.setFont(new Font("SERIF", 0, 18));
				g.drawString(text + " | " + num, 5, size - 6);
				if (selected) {
					g.setColor(new Color(255, 255, 255, 100));
					g.fillRect(0, 0, size, size);
				}
			}

			@Override
			public void paintBefore(Graphics g) {
			}
		});
		setVisible(true);
	}

	public Stack<Item> getStack() {
		return items;
	}

	public String getText() {
		return text;
	}

	public boolean isSelected() {
		return selected;
	}

	public void select(boolean select) {
		selected = select;
		// redraw();//TODO
	}

	public void setStack(Stack<Item> items) {
		this.items = items;
		// redraw();
	}

	public void setText(String text) {
		this.text = text;
		// redraw();
	}
	
	@Override
	public int compareTo(VisibleObject other) {
		return 1;
	}
}
