package com.github.jamesnorris.game.lighting.old;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import com.github.jamesnorris.game.PaintTask;
import com.github.jamesnorris.game.Panel;
import com.github.jamesnorris.game.World;
import com.github.jamesnorris.game.entity.Physical;
import com.github.jamesnorris.game.location.Location;

public class Shadow extends Panel implements Physical {

	private World world;

	private Point light;

	private Point[] objCorners;

	private int shadowLength;

	private Polygon shape = new Polygon();

	public Shadow(World world, Point light, Point[] objCorners, int shadowLength, int z) {
		setZ(z);
		this.world = world;
		this.light = light;
		this.objCorners = objCorners;
		this.shadowLength = shadowLength;
		recalculate();
		setLocation(World.ORIGIN);
		setSize(world.getSize());
		getUnderlyingPanel().setOpaque(false);
		getUnderlyingPanel().addPaintTask(new PaintTask() {

			@Override
			public void paintAfter(Graphics g) {
			}

			@Override
			public void paintBefore(Graphics g) {
				g.setColor(new Color(0, 0, 0, 50));
				g.fillPolygon(shape);
			}
		});
		setVisible(true);
	}

	protected double angleTo(Point from, Point to) {
		return Math.atan2(to.y - from.y, to.x - from.x) + Math.PI;
	}

	protected Point centroid(int[] xs, int[] ys, int npts) {
		int x = 0;
		int y = 0;
		for (int i = 0; i < npts; i++) {
			x += xs[i];
			y += ys[i];
		}
		x = x / npts;
		y = y / npts;
		return new Point(x, y);
	}

	protected Point centroid(Point[] pts) {
		int[] xs = new int[pts.length];
		int[] ys = new int[pts.length];
		for (int i = 0; i < pts.length; i++) {
			xs[i] = pts[i].x;
			ys[i] = pts[i].y;
		}
		return centroid(xs, ys, pts.length);
	}

	protected Point centroid(Polygon poly) {
		return centroid(poly.xpoints, poly.ypoints, poly.npoints);
	}

	@Override
	public Point getCenter() {
		return centroid(shape);
	}

	protected Point getClosestPoint(Point pt, Point[] points) {
		Point closest = points[0];
		for (Point p : points) {
			if (p.distance(pt) < closest.distance(pt)) {
				closest = p;
			}
		}
		return closest;
	}

	public Point getLightLocation() {
		return light;
	}

	@Override
	@Deprecated
	public Location getLocation() {
		return Location.toNearestLocation(world, light, getZ());
	}

	public int getShadowLength() {
		return shadowLength;
	}

	@Override
	public World getWorld() {
		return world;
	}

	protected void recalculate() {
		shape.reset();
		Point center = centroid(objCorners);
		double angle = angleTo(center, light);
		double fraction = (1000 - shadowLength) / 1000.0;
		if (fraction < 0) {
			fraction = 0;
		}
		ArrayList<Point> ordered = new ArrayList<Point>();
		ArrayList<Point> unordered = new ArrayList<Point>();
		Point lowAngle = null;
		Point highAngle = null;
		for (Point p : objCorners) {
			unordered.add(p);
			if (p.distance(light) <= center.distance(light)) {
				continue;
			}
			double x = Math.ceil((p.x - center.x) * fraction) + center.x + Math.cos(angle) * shadowLength;
			double y = Math.ceil((p.y - center.y) * fraction) + center.y + Math.sin(angle) * shadowLength;
			Point pt = new Point((int) x, (int) y);
			double ptAngle = angleTo(pt, light);
			if (lowAngle == null || ptAngle < angleTo(lowAngle, light)) {
				lowAngle = pt;
			}
			if (highAngle == null || ptAngle > angleTo(highAngle, light)) {
				highAngle = pt;
			}
		}
		unordered.add(lowAngle);
		unordered.add(highAngle);
		Point sc = centroid(unordered.toArray(new Point[unordered.size()]));
		for (int j = 0; j < unordered.size(); j++) {
			Point cw = null;// sorted clockwise (cw)
			for (Point pt : unordered) {
				if (ordered.contains(pt)) {
					continue;
				}
				if (cw == null) {
					cw = pt;
					continue;
				}
				double ptheta = angleTo(pt, sc);
				double ctheta = angleTo(cw, sc);
				if (ptheta > ctheta) {
					cw = pt;
				}
			}
			ordered.add(cw);
		}
		for (Point p : ordered) {
			shape.addPoint(p.x, p.y);
		}
	}

	public void setLightLocation(Point light) {
		this.light = light;
		recalculate();
		// redraw();//TODO
	}

	@Override
	public void setLocation(Location loc) {
		world = loc.getWorld();
		setLightLocation(loc.toPoint());
	}

	public void setShadowLength(int shadowLength) {
		this.shadowLength = shadowLength;
		recalculate();
	}
}
