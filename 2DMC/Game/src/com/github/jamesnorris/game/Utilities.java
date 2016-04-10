package com.github.jamesnorris.game;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Utilities {
	public static ImageIcon resize(ImageIcon icon, int width, int height) {
		if (icon == null) {
			return null;
		}
		return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
	}
	
    public static String getLastMethodCalls(Thread thread, int number) {
        StackTraceElement[] stackTraceElements = thread.getStackTrace();
        StringBuilder sb = new StringBuilder();
        int start = 2;
        for (int i = start; i <= number + start - 1; i++) {
            sb.append(stackTraceElements[i] + "\n");
        }
        return sb.toString();
    }
}
