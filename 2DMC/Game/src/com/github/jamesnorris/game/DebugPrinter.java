package com.github.jamesnorris.game;

public class DebugPrinter {
	private boolean enable;

	public DebugPrinter(boolean enable) {
		this.enable = enable;
	}

	public boolean isEnabled() {
		return enable;
	}

	public void print(String s) {
		if (enable) {
			System.out.print("[" + Driver.GAME_NAME + "][DEBUG]" + s);
		}
	}

	public void println(String s) {
		print(s + "\n");
	}

	public void setEnabled(boolean enable) {
		this.enable = enable;
	}

}
