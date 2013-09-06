package com.thedemgel.regions;

import java.awt.Color;

public enum ChatStyle {

	BLACK('0', Color.BLACK, Color.BLACK),
	DARK_BLUE('1', new Color(0, 0, 170), new Color(0, 0, 42)),
	DARK_GREEN('2', new Color(0, 170, 0), new Color(0, 42, 0)),
	DARK_AQUA('3', new Color(0, 170, 170), new Color(0, 42, 42)),
	DARK_RED('4', new Color(170, 0, 0), new Color(42, 0, 0)),
	PURPLE('5', new Color(170, 0, 170), new Color(42, 0, 42)),
	GOLD('6', new Color(255, 170, 0), new Color(42, 42, 0)),
	GRAY('7', new Color(170, 170, 170), new Color(42, 42, 42)),
	DARK_GRAY('8', new Color(85, 85, 85), new Color(21, 21, 21)),
	BLUE('9', new Color(85, 85, 255), new Color(21, 21, 63)),
	GREEN('a', new Color(85, 255, 85), new Color(21, 63, 21)),
	AQUA('b', new Color(85, 255, 255), new Color(21, 63, 63)),
	RED('c', new Color(255, 85, 85), new Color(63, 21, 21)),
	PINK('d', new Color(255, 85, 255), new Color(63, 21, 63)),
	YELLOW('e', new Color(255, 255, 85), new Color(63, 63, 21)),
	WHITE('f', Color.WHITE, new Color(63, 63, 63)),
	OBFUSCATED('k'),
	BOLD('l'),
	UNDERLINE('n'),
	ITALIC('o'),
	RESET('r');
	public static final char COLOR_CHAR = '\u00A7';
	private final char c;
	private final Color fgColor, bgColor;

	private ChatStyle(char character) {
		this(character, null, null);
	}

	private ChatStyle(char character, Color foregroundColor, Color backgroundColor) {
		this.c = character;
		this.fgColor = foregroundColor;
		this.bgColor = backgroundColor;
	}

	public char getChar() {
		return c;
	}

	public Color getForegroundColor() {
		return fgColor;
	}

	public Color getBackgroundColor() {
		return bgColor;
	}

	@Override
	public String toString() {
		return "" + COLOR_CHAR + c;
	}
}
