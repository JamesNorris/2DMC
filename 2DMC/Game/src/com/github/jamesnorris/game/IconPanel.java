package com.github.jamesnorris.game;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.github.jamesnorris.game.entity.VisibleObject;

public class IconPanel extends JPanel implements VisibleObject {
	private static final long serialVersionUID = 9150213895119148667L;
	private ImageIcon icon;
	private int paintX, paintY, z;
	private ArrayList<PaintTask> paintTasks = new ArrayList<PaintTask>();
	private boolean resize = true;
	private final PaintTask iconPaint = new PaintTask() {

		@Override
		public void paintAfter(Graphics g) {
			if (getIcon() != null) {
				ImageIcon icon = getIcon();
				if (resize && icon.getIconWidth() != getWidth() && icon.getIconHeight() != getHeight()) {
					icon = Utilities.resize(icon, getWidth(), getHeight());
				}
				g.drawImage(icon.getImage(), getPaintCoordinates().x, getPaintCoordinates().y, null);
			}
		}

		@Override
		public void paintBefore(Graphics g) {
		}

	};

	public IconPanel() {
		this(null);
	}

	public IconPanel(ImageIcon icon) {
		this(icon, 0, 0);
	}

	public IconPanel(ImageIcon icon, int paintX, int paintY) {
		setLayout(null);
		setIcon(icon);
		setPaintCoordinates(paintX, paintY);
		paintTasks.add(iconPaint);
		setVisible(true);
	}

	public boolean addPaintTask(PaintTask task) {
		return paintTasks.add(task);
	}

	public void clearPaintTasks() {// clears all except icon task
		for (PaintTask task : paintTasks) {
			if (task == iconPaint) {// "==" used to check pointer
				continue;
			}
			paintTasks.remove(task);
		}
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public Point getMidpoint() {
		Point pt = getLocation();
		Dimension size = getSize();
		return new Point(pt.x + size.width / 2, pt.y + size.height / 2);
	}

	public Point getPaintCoordinates() {
		return new Point(paintX, paintY);
	}

	@Override
	public int getZ() {
		return z;
	}

	public boolean isResizeEnabled() {
		return resize;
	}

	@Override
	public void paint(Graphics g) {
		for (PaintTask task : paintTasks) {
			task.paintBefore(g);
		}
		Component[] comps = getComponents();
		Arrays.sort(comps);		
		for (int i = 0; i < comps.length; i++) {
			Component c = comps[i];
			setComponentZOrder(c, comps.length - i - 1);
		}
		super.paint(g);
		for (PaintTask task : paintTasks) {
			task.paintAfter(g);
		}
		update();
	}

	public boolean removePaintTask(PaintTask task) {
		return paintTasks.remove(task);
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
		// redraw();//TODO
	}

	public void setPaintCoordinates(int paintX, int paintY) {
		this.paintX = paintX;
		this.paintY = paintY;
		// redraw();
	}

	public void setPaintCoordinates(Point pt) {
		setPaintCoordinates(pt.x, pt.y);
	}

	public void setResizeEnabled(boolean resize) {
		this.resize = resize;
	}

	public void setZ(int z) {
		this.z = z;
	}

	protected void update() {
	}// called on repaint

	@Override
	public int compareTo(VisibleObject other) {
		return getZ() - other.getZ();
	}
}
