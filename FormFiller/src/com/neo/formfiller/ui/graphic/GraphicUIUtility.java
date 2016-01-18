package com.neo.formfiller.ui.graphic;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

public final class GraphicUIUtility {

	public static Rectangle getCentralRectangle(int width, int height) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		final int x = (dimension.width - width) / 2;
		final int y = (dimension.height - height) / 2;
		return new Rectangle(x, y, width, height);
	}
}
