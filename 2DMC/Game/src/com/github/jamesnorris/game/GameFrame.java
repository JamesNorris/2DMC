package com.github.jamesnorris.game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Arrays;

import javax.swing.JFrame;

import com.github.jamesnorris.game.ui.InterfaceOverlay;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {
	private Game game;
	private InterfaceOverlay overlay;

	public GameFrame(Game game, Dimension size) {
		this(game, size.width, size.height);
	}

	public GameFrame(Game game, int width, int height) {
		this.game = game;
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		getContentPane().setBackground(Color.DARK_GRAY);
		setVisible(true);
	}

	public void add(Panel p) {
		add(p.getUnderlyingPanel());
	}

	public Game getGame() {
		return game;
	}

	public InterfaceOverlay getOverlay() {
		return overlay;
	}

	protected void initOverlay() {
		overlay = new InterfaceOverlay();
		add(overlay.getUnderlyingPanel(), 0);
	}

	@Override
	public void paint(Graphics g) {
		Component[] comps = getComponents();
		Arrays.sort(comps);		
		for (int i = 0; i < comps.length; i++) {
			Component c = comps[i];
			setComponentZOrder(c, comps.length - i - 1);
		}
		super.paint(g);
	}

	public void remove(Panel p) {
		remove(p.getUnderlyingPanel());
	}

}
