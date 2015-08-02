package entity;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import shared.Updater;
import shared.Updater.GameState;
import shared.ref;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import game.HUD;
import game.SelectionDisplay;

/**
 * Aktive Fähigkeit
 * */
public abstract class Active implements Informing {
	public GGameButton button;

	public Class<?> clazz;
	public char n;

	protected int cooldown;
	private int cooldownTimer;

	private PImage symbol;

	static int w = 50;
	public static int x;
	public static int y;

	@SuppressWarnings("static-access")
	public Active(int x, int y, char n, PImage symbol) {
		button = new GGameButton(ref.app, this.x + x * (w + 10), this.y + y
				* (w + 10), w, w, HUD.buttonImageFilename());
		button.setText(n + "");
		button.setSymbol(symbol);
		button.addEventHandler(this, "handleActiveEvents");
		this.symbol = symbol;
		this.n = n;
	}

	public void update() {
		button.setCooldownState(1 - getCooldownPercent());
		if (isNotOnCooldown() && button.isVisible())
			button.setEnabled(true);
	}

	public void handleActiveEvents(GGameButton gamebutton, GEvent event) {
		if (ref.app.keyPressed || ref.app.mouseButton != PConstants.RIGHT) {
			if (event == GEvent.PRESSED && gamebutton.isVisible()
					&& isNotOnCooldown()
					&& ref.updater.gameState == GameState.PLAY) {
				onActivation();
				// startCooldown();
			}
		} else {
			SelectionDisplay.setIforming(this);
		}
	}

	public abstract void onActivation();

	public void setVisible(boolean b) {
		button.setVisible(b);
	}

	public boolean isVisible() {
		return button.isVisible();
	}

	public void pressManually() {
		if (isNotOnCooldown())
			button.pressManually();
	}

	public void startCooldown() {
		cooldownTimer = Updater.Time.getMillis() + cooldown;
		button.setEnabled(false);
	}

	public boolean isNotOnCooldown() {
		return cooldownTimer <= Updater.Time.getMillis();
	}

	public float getCooldownPercent() {
		float f = 1 - (float) (cooldownTimer - Updater.Time.getMillis())
				/ cooldown;
		return f > 1 || f < 0 ? 1 : f;
	}

	public void drawIcon(PGraphics graphic, float x, float y, int size) {
		// TODO gleich wie entity
		if (symbol != null)
			graphic.image(symbol, x, y, size, size);
	}

	@Override
	public String getStatistics() {
		if (cooldown != 0)
			return "cooldown: " + cooldown;
		return " ";

	}

}
