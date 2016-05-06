package vn.com.tuanminh.rollyfrog.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class Utils {
	public static Color randomColor() {
		Color[] colors = { Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA,
				Color.RED, Color.YELLOW };
		return colors[MathUtils.random(colors.length - 1)];
	}

	public static float calStringWidth(String s, float fontSize, float scale) {
		return s.length() * scale * (fontSize / 2);
	}

	public static float calMultiLineStringWidth(String s, float fontSize,
			float scale) {
		String subStrings[] = s.split("\n");
		String maxLengthString = subStrings[0];
		for (int i = 0; i < subStrings.length; i++) {
			if (subStrings[i].length() > maxLengthString.length())
				maxLengthString = subStrings[i];
		}
		return maxLengthString.length() * scale * (fontSize / 2);
	}
}
