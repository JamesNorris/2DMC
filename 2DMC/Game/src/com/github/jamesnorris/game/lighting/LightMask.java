package com.github.jamesnorris.game.lighting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.github.jamesnorris.game.PaintTask;
import com.github.jamesnorris.game.Panel;
import com.github.jamesnorris.game.World;
import com.github.jamesnorris.game.entity.VisibleObject;

public class LightMask extends Panel implements VisibleObject {
	protected class DarkLayer extends Panel {

		public DarkLayer(World world, List<Luminous> lights) {
			Dimension size = world.getSize();
			getUnderlyingPanel().setOpaque(false);
			getUnderlyingPanel().addPaintTask(new PaintTask() {

				@Override
				public void paintAfter(Graphics g) {
				}

				@Override
				public void paintBefore(Graphics g) {
					Graphics2D g2d = (Graphics2D) g;
					long time = world.getTime();
					g2d.setColor(new Color(0, 0, 0, (int) Math.abs(time - 300000) / 6000));
					g2d.fillRect(0, 0, size.width, size.height);
				}

			});
			setLocation(0, 0);
			setSize(size);
			setVisible(true);
		}	
		
		@Override
		public int compareTo(VisibleObject other) {
			return 1;
		}
	}

	protected class LightLayer extends Panel {

		public LightLayer(World world, List<Luminous> lights) {
			getUnderlyingPanel().setOpaque(false);
			getUnderlyingPanel().addPaintTask(new PaintTask() {

				@Override
				public void paintAfter(Graphics g) {
				}

				@Override
				public void paintBefore(Graphics g) {
					Graphics2D g2d = (Graphics2D) g;
					for (Luminous light : lights) {
						g2d.setPaint(light.getPaint());
						g2d.fill(light.getShape());
					}
				}

			});
			setLocation(0, 0);
			setSize(world.getSize());
			setVisible(true);
		}
		
		@Override
		public int compareTo(VisibleObject other) {
			return 1;
		}
	}

	private List<Luminous> lights = new ArrayList<Luminous>();

	private LightLayer light;

	private DarkLayer dark;

	public LightMask(World world, List<Luminous> lights) {
		this.lights = lights;
		light = new LightLayer(world, lights);
		dark = new DarkLayer(world, lights);
		getUnderlyingPanel().setOpaque(false);
		setLocation(0, 0);
		setSize(world.getSize());
		setVisible(true);
	}

	public void addLight(Luminous light) {
		lights.add(light);
	}

	public List<Luminous> getLights() {
		return lights;
	}

	public void paintTo(Panel pane) {
		pane.add(this);
		pane.add(dark);
		pane.add(light);
	}

	public void removeFrom(Panel pane) {
		pane.remove(light);
		pane.remove(dark);
		pane.remove(this);
	}

	public void removeLight(Luminous light) {
		lights.remove(light);
	}
	
	@Override
	public int compareTo(VisibleObject other) {
		return 1;
	}
}
