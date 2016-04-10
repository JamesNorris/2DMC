package com.github.jamesnorris.game.effect;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import com.github.jamesnorris.game.PaintTask;
import com.github.jamesnorris.game.Panel;
import com.github.jamesnorris.game.World;

public class BrokenPieces extends Panel implements ParticleEffect {
	private World world;
	private int radius, life;
	private Color color;

	public BrokenPieces(World world, int x, int y, int z, int radius, int life) {
		setZ(z);
		this.world = world;
		this.radius = radius;
		this.life = life;
		getUnderlyingPanel().setOpaque(false);
		setLocation(x - radius, y - radius);
		setSize(radius * 2, radius * 2);
		setVisible(true);
	}

	public BrokenPieces(World world, Point pt, int z, int radius, int life) {
		this(world, pt.x, pt.y, z, radius, life);
	}

	public Color getColor() {
		return color;
	}

	@Override
	public int getLife() {
		return life;
	}

	@Override
	public void play() {
		double[] partThetas = new double[2];
		BrokenPieces bp = this;
		getUnderlyingPanel().addPaintTask(new PaintTask() {

			@Override
			public void paintAfter(Graphics g) {
				if (life <= 0) {
					world.remove(bp);
					return;
				}
				g.setColor(color);
				for (int i = 0; i < partThetas.length; i++) {
					if (partThetas[i] == 0) {
						partThetas[i] = Math.toRadians(Math.random() * 360);
					}
					int partX = (int) Math.ceil(Math.cos(partThetas[i]) * ((Math.random() + .3) * (radius / life)));
					int partY = (int) Math.ceil(Math.sin(partThetas[i]) * ((Math.random() + .3) * (radius / life)));
					g.fillRect(radius - 2 + partX, radius - 2 + partY, 4, 4);
				}
				life--;
			}

			@Override
			public void paintBefore(Graphics g) {
			}

		});
		world.add(this);
		final BrokenPieces pcs = this;
		final Timer t = new Timer(50, null);
		t.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pcs.repaint();
				if (life <= 0) {
					t.stop();
				}
			}
		});
		t.start();
	}

	public void setColor(Color color) {// just to prevent confusion
		this.color = color;
	}
}
