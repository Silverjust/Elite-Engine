package entity;

import processing.core.PGraphics;
import processing.core.PImage;
import shared.ref;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import game.HUD;

/**
 * Aktive Fähigkeit
 * */
public abstract class Active implements Informing {
	public GGameButton button;

	public Class<?> clazz;

	protected int cooldown;
	protected int cooldownTimer;

	private PImage symbol;

	static int w = 50;
	public static int x;
	public static int y;

	@SuppressWarnings("static-access")
	public Active(int x, int y, char n, PImage symbol) {
		button = new GGameButton(ref.app, this.x + x * (w + 10), this.y + y
				* (w + 10), w, w, HUD.buttonImageFilename);
		button.setText(n + "");
		button.setSymbol(symbol);
		button.addEventHandler(this, "handleActiveEvents");
		this.symbol = symbol;
	}

	public void update() {
		button.setCooldownState(1 - getCooldownPercent());
		if (isNotOnCooldown())
			button.setThisEnabled(true);
	}

	public void handleActiveEvents(GGameButton gamebutton, GEvent event) {
		if (event == GEvent.PRESSED && isNotOnCooldown()) {
			onButtonPressed(gamebutton, event);
			startCooldown();
		}
	}

	public abstract void onButtonPressed(GGameButton gamebutton, GEvent event);

	public void setVisible(boolean b) {
		button.setVisible(b);
	}

	public void pressManually() {
		if (isNotOnCooldown())
			button.pressManually();
	}

	protected void startCooldown() {
		cooldownTimer = ref.app.millis() + cooldown;
		button.setThisEnabled(false);
	}

	public boolean isNotOnCooldown() {
		return cooldownTimer <= ref.app.millis();
	}

	public float getCooldownPercent() {
		float f = 1 - (float) (cooldownTimer - ref.app.millis()) / cooldown;
		return f > 1 || f < 0 ? 1 : f;
	}

	public void drawIcon(PGraphics graphic, float x, float y, int size) {
		// TODO gleich wie entity
		graphic.image(symbol, x, y, size, size);
	}
}
