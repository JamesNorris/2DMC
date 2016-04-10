package com.github.jamesnorris.game.entity.player;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import com.github.jamesnorris.game.Driver;
import com.github.jamesnorris.game.GameFrame;
import com.github.jamesnorris.game.World;
import com.github.jamesnorris.game.block.Block;
import com.github.jamesnorris.game.effect.BrokenPieces;
import com.github.jamesnorris.game.entity.Breakable;
import com.github.jamesnorris.game.entity.Item;
import com.github.jamesnorris.game.entity.Placeable;
import com.github.jamesnorris.game.entity.TileEntity;
import com.github.jamesnorris.game.location.Location;
import com.github.jamesnorris.game.ui.Hotbar;
import com.github.jamesnorris.game.ui.MenuOption;

public class PlayerHandler implements KeyListener, MouseListener {
	private Player player;

	public PlayerHandler(Player player) {
		this.player = player;
	}

	public TileEntity getEventEntity(MouseEvent evt) {
		Point coords = getEventPoint(evt);
		if (coords == null) {
			return null;
		}
		World world = Driver.getFrame().getGame().getWorld();
		return world.getEntityAt(coords.x, coords.y, false, world.getHighestZAt(coords.x, coords.y, false));
	}

	protected Point getEventPoint(MouseEvent evt) {
		if (!(evt.getSource() instanceof GameFrame)) {
			return null;
		}
		GameFrame frame = Driver.getFrame();
		World world = frame.getGame().getWorld();
		int x = evt.getX() - frame.getInsets().left - world.getUnderlyingPanel().getX();
		int y = evt.getY() - frame.getInsets().top - world.getUnderlyingPanel().getY();
		return new Point(x, y);
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public void keyPressed(KeyEvent evt) {
		int keyCode = evt.getKeyCode();
		switch (keyCode) {
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
				move(0, -player.getMovementSpeed());
				break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				move(0, player.getMovementSpeed());
				break;
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				move(-player.getMovementSpeed(), 0);
				break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				move(player.getMovementSpeed(), 0);
				break;
			case KeyEvent.VK_0:
			case KeyEvent.VK_1:
			case KeyEvent.VK_2:
			case KeyEvent.VK_3:
			case KeyEvent.VK_4:
			case KeyEvent.VK_5:
			case KeyEvent.VK_6:
			case KeyEvent.VK_7:
			case KeyEvent.VK_8:
			case KeyEvent.VK_9:
				Driver.getFrame().getOverlay().getHotbar().select(evt.getKeyChar() - '0');
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent evt) {
	}

	@Override
	public void keyTyped(KeyEvent evt) {
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
	}

	@Override
	public void mouseEntered(MouseEvent evt) {

	}

	@Override
	public void mouseExited(MouseEvent evt) {

	}

	@Override
	public void mousePressed(MouseEvent evt) {
		Hotbar hotBar = Driver.getFrame().getOverlay().getHotbar();
		MenuOption option = hotBar.getOptionAt(evt.getPoint());
		if (option != null) {
			hotBar.select(option);
		} else {
			TileEntity e = getEventEntity(evt);
			if (e == null || Location.distance2D(e.getLocation().toPoint(), player.getUnderlyingPanel().getLocation()) > player.getReach()) {
				return;
			}
			if (evt.getButton() == MouseEvent.BUTTON1 && e instanceof Breakable) {
				((Breakable) e).damage(1);
				Point pt = getEventPoint(evt);
				BrokenPieces pieces = new BrokenPieces(player.getWorld(), pt, e.getZ() + 1, Block.BLOCK_SIZE / 2, 6);
				Rectangle rect = new Rectangle(evt.getX(), evt.getY(), 1, 1);
				Color color = new Color(120, 100, 0);
				try {
					BufferedImage image = new Robot().createScreenCapture(rect);
					color = new Color(image.getRGB(0, 0));
					color = color.darker();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				pieces.setColor(color);
				pieces.play();
				e.repaint();
			} else if (evt.getButton() == MouseEvent.BUTTON3 && e instanceof Block) {
				MenuOption selected = hotBar.getSelected();
				if (selected == null || selected.getStack().isEmpty()) {
					return;
				}
				Location place = e.getPlaceLocation();
				Item item = selected.getStack().pop();
				if (item instanceof Placeable) {
					Driver.debug.println(item.getClass().getSimpleName() + " placed at " + place);
					((Placeable) item).place(place);
					place.getWorld().getLightMask().repaint();
					item.repaint();
					selected.repaint();
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
	}
	
	private Point toMidPoint(Point p) {
		return new Point(p.x + Block.BLOCK_SIZE / 2, p.y + Block.BLOCK_SIZE / 2);
	}
	
	private Point fromMidPoint(Point p) {
		return new Point(p.x - Block.BLOCK_SIZE / 2, p.y - Block.BLOCK_SIZE / 2);
	}

	public void move(double x, double y) {
		World world = player.getWorld();
		
		Point current = toMidPoint(player.getUnderlyingPanel().getLocation());
		Point next = new Point((int) Math.ceil(current.x + x), (int) Math.ceil(current.y + y));
		
		int curZ = player.getZ() - 1;
		int nextZ = world.getHighestZAt(next.x, next.y, false, player.getZ() + 2, 0);
		
		boolean fall = false;
		
		if (curZ <= 0) {
			System.err.println("Player is below bedrock.");
			return;
		}
		
		if (!world.isWithinBounds(next.x, next.y, nextZ)) {
			Driver.debug.println("Player cannot move in that direction. (out of bounds)");
			return;
		}
		
		if (nextZ - curZ >= 2) {//blocked TODO check if liquid, if so, rise 1
			Driver.debug.println("Player cannot move in that direction. (blocked)");
			return;
		}
		
		if (nextZ < curZ) {//falling
			Driver.debug.println("Player fell 1 block.");
			player.setZ(player.getZ() - 1);
			fall = true;
		}
		
		if (nextZ - curZ == 1) {//rise
			Driver.debug.println("Player rose 1 block." );
			player.setZ(player.getZ() + 1);
			return;//no forward movement
		}
		
		Point corrected = fromMidPoint(next);
		
		player.getUnderlyingPanel().setLocation(corrected);
		world.setFocus(corrected);
		
		//TODO player direction
		
		if (fall) {
			move(0, 0);
		}
	}
}
