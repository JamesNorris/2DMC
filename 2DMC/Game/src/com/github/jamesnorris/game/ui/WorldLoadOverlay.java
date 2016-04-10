package com.github.jamesnorris.game.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import com.github.jamesnorris.game.PaintTask;
import com.github.jamesnorris.game.Panel;
import com.github.jamesnorris.game.entity.VisibleObject;

public class WorldLoadOverlay extends Panel implements VisibleObject {
	private String text = "Loading world...";

	public WorldLoadOverlay(Dimension size) {
		setLocation(0, 0);
		setSize(size);
		getUnderlyingPanel().setOpaque(false);
		getUnderlyingPanel().addPaintTask(new PaintTask() {
			@Override
			public void paintAfter(Graphics g) {
				Dimension size = getSize();
				g.setColor(Color.DARK_GRAY);
				g.fillRect(0, 0, size.width, size.height);
				if (text == null) {
					System.out.println("null");
					return;
				}
				g.setColor(Color.WHITE);
				g.setFont(new Font("DIALOGINPUT", Font.BOLD, 26));
				g.drawString(text, 50, size.height - 100);
			}

			@Override
			public void paintBefore(Graphics g) {
			}
		});
		setVisible(true);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public int compareTo(VisibleObject other) {
		return 1;
	}
}
