package com.github.jamesnorris.game.lighting;

import java.awt.Color;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

public class LightBuilder {
	private Point2D center, focus;
	private float radius = 0.1F;
	private float[] distances = new float[] { 0, 0.1F };
	private Color[] colors;
	private CycleMethod method = CycleMethod.NO_CYCLE;

	public RadialGradientPaint build() {
		if (center == null || colors == null) {
			return null;
		}
		if (colors.length == 1) {
			Color color = colors[0];
			colors = new Color[] { color, color };
			if (distances.length >= 2) {
				distances = new float[] { distances[0], distances[1] };
			}
		}
		if (focus == null) {
			return new RadialGradientPaint(center, radius, distances, colors, method);
		}
		return new RadialGradientPaint(center, radius, focus, distances, colors, method);
	}

	public LightBuilder setCenter(Point2D center) {
		this.center = center;
		return this;
	}

	public LightBuilder setColors(Color[] colors) {
		this.colors = colors;
		return this;
	}

	public LightBuilder setCycleMethod(CycleMethod method) {
		this.method = method;
		return this;
	}

	public LightBuilder setDistances(float[] distances) {
		this.distances = distances;
		return this;
	}

	public LightBuilder setFocus(Point2D focus) {
		this.focus = focus;
		return this;
	}

	public LightBuilder setRadius(float radius) {
		this.radius = radius;
		return this;
	}
}
