package entity;

import processing.core.PGraphics;

public abstract class Informing {
	public abstract void drawIcon(PGraphics graphic, float x, float y, int size);

	public abstract String getDesription();

	public abstract String getStatistics();

}
