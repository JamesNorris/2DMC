package com.github.jamesnorris.game;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Stack;

import com.github.jamesnorris.game.entity.Item;
import com.github.jamesnorris.game.entity.Torch;
import com.github.jamesnorris.game.entity.player.Player;
import com.github.jamesnorris.game.location.Location;
import com.github.jamesnorris.game.ui.WorldLoadOverlay;

public class Driver {
	public static final String GAME_NAME = "Game";
	private static GameFrame frame;
	public static DebugPrinter debug;

	public static GameFrame getFrame() {
		return frame;
	}

	public static void main(String[] args) {
		new Driver().init();
	}

	protected void init() {
		debug = new DebugPrinter(true);
		Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = new Dimension(screen_size.width - 100, screen_size.height - 150);
		Game game = new Game(10, 10);// TODO resize
		frame = new GameFrame(game, size);
		frame.setLocation(screen_size.width / 2 - frame.getWidth() / 2, screen_size.height / 2 - frame.getHeight() / 2);
		frame.setTitle(GAME_NAME);
		debug.println("Frame initialized.");
		WorldLoadOverlay worldLoad = new WorldLoadOverlay(size);
		frame.add(worldLoad);
		game.initWorld();
		frame.add(game.getWorld());
		debug.println("Game started and world created.");
		Player player = game.getWorld().getPlayer();
		game.getWorld().setFocus(player.getUnderlyingPanel().getLocation());
		frame.addKeyListener(player.getHandler());
		debug.println("Player movement enabled.");
		frame.addMouseListener(player.getHandler());
		debug.println("Player object selection enabled.");
		frame.initOverlay();
		debug.println("UI Overlay initialized.");
		frame.remove(worldLoad);
		frame.getOverlay().getHotbar().getOption(0).setStack(new Stack<Item>() {
			private static final long serialVersionUID = 8561689877824493124L;

			{
				for (int i = 0; i < 5; i++) {
					push(new Torch(new Location(game.getWorld(), -1, -1, 0)));
				}
			}
		});
		frame.repaint();//call once to set world focus on player
	}

}
