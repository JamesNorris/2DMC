package com.github.jamesnorris.game.lighting.old;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import com.github.jamesnorris.game.PaintTask;
import com.github.jamesnorris.game.Panel;
import com.github.jamesnorris.game.World;
import com.github.jamesnorris.game.block.Block;
import com.github.jamesnorris.game.entity.Physical;
import com.github.jamesnorris.game.lighting.LightBuilder;
import com.github.jamesnorris.game.location.Location;

public class LightSource extends Panel implements Physical {
	private Location loc;// top left corner
	private Color[] colors;
	private float[] distances;
	private int radius, intensity;// radius where the light is at initial alpha
	private float initialAlpha;

	public LightSource(Location loc, Color[] colors, float[] distances, int radius, int intensity, float initialAlpha) {
		this.colors = colors;
		this.distances = distances;
		this.radius = radius;
		this.intensity = intensity;
		this.initialAlpha = initialAlpha;
		setLocation(loc);
		setSize(radius * 2, radius * 2);
		getUnderlyingPanel().setOpaque(false);
		setVisible(true);
		draw();
		init(true, distances == null);
	}

	public LightSource(Location loc, int radius, int intensity, float initialAlpha) {
		this(loc, null, null, radius, intensity, initialAlpha);
	}

	private void draw() {
		getUnderlyingPanel().addPaintTask(new PaintTask() {

			@Override
			public void paintAfter(Graphics g) {
				LightBuilder builder = new LightBuilder();
				builder.setCenter(new Point2D.Float(radius, radius));
				builder.setRadius(radius);
				builder.setColors(colors);
				builder.setDistances(distances);
				((Graphics2D) g).setPaint(builder.build());
				((Graphics2D) g).fillOval(0, 0, radius * 2, radius * 2);
				((Graphics2D) g).create();
			}

			@Override
			public void paintBefore(Graphics g) {
			}

		});
	}

	@Override
	public Point getCenter() {
		Point p = getLocation().toPoint();
		return new Point(p.x + Block.BLOCK_SIZE / 2, p.y + Block.BLOCK_SIZE / 2);
	}

	public Color[] getColors() {
		return colors;
	}

	public float getInitialAlpha() {
		return initialAlpha;
	}

	public int getIntensity() {
		return intensity;
	}

	@Override
	public Location getLocation() {
		return loc.clone().add(radius, radius, 0);
	}

	public int getRadius() {
		return radius;
	}

	@Override
	public World getWorld() {
		return loc.getWorld();
	}

	private void init(boolean initColors, boolean initDistances) {
		int elements = distances == null ? (int) Math.ceil(radius / intensity) : distances.length;
		Color[] colors = new Color[elements];
		float[] distances = new float[elements];
		for (int i = 0; i < distances.length; i++) {
			distances[i] = 1 / (distances.length - i);
			if (i >= 1 && distances[i] == distances[i - 1]) {
				distances[i] += .00000000001;
			}
			int j = i;
			if (j >= getColors().length) {
				j = getColors().length - 1;
			}
			Color color = getColors()[j];
			colors[i] = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) initialAlpha / (i + 1));
		}
		if (initColors) {
			this.colors = colors;
		}
		if (initDistances) {
			this.distances = distances;
		}
	}

	public void setColors(Color[] colors) {
		this.colors = colors;
	}

	public void setInitialAlpha(float initialAlpha) {
		this.initialAlpha = initialAlpha;
	}

	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}

	@Override
	public void setLocation(Location loc) {
		this.loc = loc.clone().subtract(radius, radius, 0);
		setZ(loc.getZ());
		super.setLocation(this.loc.getX(), this.loc.getY());
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

}
