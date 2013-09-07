package com.thedemgel.regions;

import java.awt.Color;

/**
 * ChatStyle coloring codes.
 */
public enum ChatStyle {

	/**
	 * Black
	 */
	BLACK('0', Color.BLACK, Color.BLACK),
	/**
	 * Dark Blue
	 */
	DARK_BLUE('1', new Color(0, 0, 170), new Color(0, 0, 42)),
	/**
	 * Dark Green
	 */
	DARK_GREEN('2', new Color(0, 170, 0), new Color(0, 42, 0)),
	/**
	 * Dark Aqua
	 */
	DARK_AQUA('3', new Color(0, 170, 170), new Color(0, 42, 42)),
	/**
	 * Dark Red
	 */
	DARK_RED('4', new Color(170, 0, 0), new Color(42, 0, 0)),
	/**
	 * Purple
	 */
	PURPLE('5', new Color(170, 0, 170), new Color(42, 0, 42)),
	/**
	 * Gold
	 */
	GOLD('6', new Color(255, 170, 0), new Color(42, 42, 0)),
	/**
	 * Gray
	 */
	GRAY('7', new Color(170, 170, 170), new Color(42, 42, 42)),
	/**
	 * Dark Gray
	 */
	DARK_GRAY('8', new Color(85, 85, 85), new Color(21, 21, 21)),
	/**
	 * Blue
	 */
	BLUE('9', new Color(85, 85, 255), new Color(21, 21, 63)),
	/**
	 * Green
	 */
	GREEN('a', new Color(85, 255, 85), new Color(21, 63, 21)),
	/**
	 * Aqua
	 */
	AQUA('b', new Color(85, 255, 255), new Color(21, 63, 63)),
	/**
	 * Red
	 */
	RED('c', new Color(255, 85, 85), new Color(63, 21, 21)),
	/**
	 * Pink
	 */
	PINK('d', new Color(255, 85, 255), new Color(63, 21, 63)),
	/**
	 * Yellow
	 */
	YELLOW('e', new Color(255, 255, 85), new Color(63, 63, 21)),
	/**
	 * White
	 */
	WHITE('f', Color.WHITE, new Color(63, 63, 63)),
	/**
	 * Obfuscated
	 */
	OBFUSCATED('k'),
	/**
	 * Bold
	 */
	BOLD('l'),
	/**
	 * Underline
	 */
	UNDERLINE('n'),
	/**
	 * Italic
	 */
	ITALIC('o'),
	/**
	 * Reset
	 */
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
