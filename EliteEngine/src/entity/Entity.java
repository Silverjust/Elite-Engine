package entity;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import shared.Nation;
import shared.Player;
import shared.Updater;
import shared.ref;
import entity.animation.Animation;
import entity.animation.Death;
import game.Chat;
import game.GameDrawer;
import game.ImageHandler;

public abstract class Entity implements Informing {
	// TODO upgrades f�r einheiten
	public static int entityCounter;
	public int number;
	public Player player;

	public boolean isSelected;
	public boolean isTaged;

	public int kerit, pax, arcanum, prunam;

	public float x, y;
	public byte radius = 1;
	public byte sight = 10;
	public byte height;
	public int hp;
	public int hp_max;
	public byte armor;
	public GroundPosition groundPosition;

	public byte currentFrame;
	public int xSize, ySize;

	private static PImage shadowImg;
	private static PImage selectedImg;
	private static PImage hpImg;
	public PImage iconImg;
	protected String descr = " ", stats = " ";

	public Death death;
	public Animation stand;

	protected Animation animation;
	protected Animation nextAnimation;

	public static void loadImages() {
		String path = path(Nation.NEUTRAL, new Object() {
		});
		shadowImg = ImageHandler.load(path, "shadow");
		selectedImg = ImageHandler.load(path, "selected");
		hpImg = ImageHandler.load(path, "hp");
	}

	public void updateAnimation() {
		animation = nextAnimation;
		animation.update(this);
	}

	public void updateDecisions() {
	}

	public void updateMovement() {
	}

	public void renderUnder() {
	}

	public void renderGround() {
	}

	public void renderAir() {
	}

	public void display() {
		drawHpBar();
	}

	public void exec(String[] c) {
		try {
			switch (c[2]) {
			case "stand":// Toremove
				if (this instanceof Unit)
					((Unit) this).isMoving = false;
				setAnimation(stand);
				break;
			case "death":
				if (this instanceof Unit)
					((Unit) this).isMoving = false;
				setAnimation(death);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			System.out.println(animation + " " + nextAnimation);
			PApplet.printArray(c);
			e.printStackTrace();
		}
	}

	public void hit(int damage, byte pirce) {

		if (isMortal()) {// only for nonimmortal objects
			hp -= damage
					* (1.0 - ((armor - pirce > 0) ? armor - pirce : 0) * 0.05);
			/** check if it was lasthit */
			if (hp <= 0 && hp != Integer.MAX_VALUE) {// marker
				hp = -32768;
				onDeath();
			}

		}
	}

	public void heal(int heal) {
		hp += heal;
		/** check if it was overheal */
		if (hp > hp_max) {
			hp = hp_max;
		}
	}

	protected void onDeath() {
		sendAnimation("death");
	}

	public void info() {
		Chat.println(this.getClass().getSimpleName() + number, "(" + x + "|"
				+ y + ")" + "\nhp:" + hp);
	}

	void drawShadow() {
		ref.app.image(shadowImg, xToGrid(x), yToGrid(y), radius * 2, radius);
	}

	protected void drawSelected() {
		if (isSelected && isAlive()) {
			ref.app.tint(player.color);
			ref.app.image(selectedImg, xToGrid(x), yToGrid(y - height),
					radius * 2, radius);
			ref.app.tint(255);
		}
	}

	protected void drawTaged() {
		/** just for debug */
		if (isTaged) {
			ref.app.fill(0, 0);
			ref.app.stroke(player.color);
			ref.app.rect(xToGrid(x), yToGrid(y - height) - radius * 0.3f,
					radius * 2, radius * 1.5f);
			ref.app.stroke(0);
		}
	}

	void drawHpBar() {
		int h = 2;
		if (isAlive() && isMortal()) {//
			ref.app.fill(0, 150);
			ref.app.rect(xToGrid(x), yToGrid(y - height) - radius * 1.5f,
					radius * 2, h);
			ref.app.tint(player.color);
			ref.app.image(hpImg, xToGrid(x), yToGrid(y - height) - radius
					* 1.5f, radius * 2 * hp / hp_max, h);
			ref.app.tint(255);
		}
	}

	public void drawBar(float f) {
		int h = 2;
		if (isAlive() && isMortal()) {//
			ref.app.fill(0, 150);
			ref.app.rect(xToGrid(x), yToGrid(y - height - h * 3) - radius
					* 1.5f, radius * 2, h);
			ref.app.tint(200);
			ref.app.image(hpImg, xToGrid(x), yToGrid(y - height - h * 3)
					- radius * 1.5f, radius * 2 * f, h);
			ref.app.tint(255);
		}
	}

	protected void drawCircle(int r) {
		ref.app.image(selectedImg, xToGrid(x), yToGrid(y), r * 2, r);

	}

	void drawLine(float tx, float ty) {
		ref.app.line(xToGrid(x), yToGrid(y), tx, ty / 2);

	}

	public void drawOnMinimap() {

	}

	public void drawSight(Updater updater) {
		int scale = updater.map.fogScale;
		updater.map.fogOfWar.ellipse(x / scale, y / scale / 2, sight * 2
				/ scale, sight / scale);
	}

	@Override
	public void drawIcon(PGraphics graphic, float x, float y, int size) {
		// TODO mit xsize,ysize verbinden
		graphic.image(iconImg, x, y, size, size);
	}

	@Override
	public String getDesription() {
		return descr;
	}

	@Override
	public String getStatistics() {
		String stats = "";
		if (kerit == 0 && pax == 0 && arcanum == 0 && prunam == 0)
			stats += "�����";
		else
			stats += "kerit: " + kerit + "�pax: " + pax + "�arcanum: "
					+ arcanum + "�prunam: " + prunam + "��";

		stats += "hp: " + hp_max + " (" + armor + ")�";

		if (this instanceof Attacker) {
			stats += "dps: " + ((Attacker) this).getBasicAttack().damage + "/"
					+ ((Attacker) this).getBasicAttack().cooldown / 1000.0
					+ " (" + ((Attacker) this).getBasicAttack().pirce + ")�";
		}
		return stats + this.stats;
	}

	public void sendDefaultAnimation(Animation oldAnimation) {
		sendAnimation("stand");
	}

	public void sendAnimation(String s) {
		if (s != "") {
			ref.updater.send("<execute " + number + " " + s);
		}
	}

	public void setAnimation(Animation a) {
		if (animation.getClass() != death.getClass() && animation != a) {// tut
			nextAnimation = a;
			a.setup(this);
		}
	}

	public Animation getAnimation() {
		return animation;
	}

	public float calcImportanceOf(Entity e) {
		float importance = PApplet.abs(10000 / (e.hp
				* PApplet.dist(x, y, e.x, e.y) - radius - e.radius));
		// TODO speziefische Thread werte
		if (e instanceof Attacker) {
			importance *= 20;
		}
		return importance;
	}

	public static float xToGrid(float x) {
		return x;
	}

	public static float yToGrid(float y) {
		return y / 2;
	}

	public static float gridToX() {
		return ((ref.app.mouseX - GameDrawer.xMapOffset) / GameDrawer.zoom);
	}

	public static float gridToY() {
		return ((ref.app.mouseY - GameDrawer.yMapOffset) / GameDrawer.zoom * 2);
	}

	public boolean isCollision(Entity e) {
		float f = PApplet.dist(x, y, e.x, e.y);
		boolean b = f < radius + e.radius && e.groundPosition == groundPosition;
		// if(b)System.out.println(number + "/" + e.number + ":" + f + b);
		return b;
	}

	public boolean isInArea(float X, float Y, int R) {
		float f = PApplet.dist(x, y, X, Y);
		boolean b = f < R;
		// System.out.println(number + "/" + f + "/" + R + " " + b);
		return b;
	}

	public boolean isEnemyTo(Entity e) {
		return (e != null) && (this.player != null) && (e.player != null)
				&& (this.player != e.player)
				&& (this.player != ref.updater.neutral)
				&& (e.player != ref.updater.neutral) && e.isAlive()
				&& isAlive();
	}

	public boolean isEnemyTo(Player p) {
		return (p != null) && (this.player != null) && (this.player != p)
				&& (this.player != ref.updater.neutral)
				&& (p != ref.updater.neutral) && isAlive();
	}

	public boolean isAllyTo(Entity e) {
		return (this.player == e.player);
	}

	public boolean isAllyTo(Player p) {
		return (this.player == p);
	}

	public boolean isAlive() {
		if (isMortal())
			return (animation.getClass() != death.getClass()) && hp > 0;
		return true;
	}

	public boolean isMortal() {
		return death != null;
	}

	public boolean isVisibleTo(Player p) {
		boolean isVisible = false;
		for (Entity spotter : ref.updater.entities) {
			if (spotter.player == p
					&& spotter.isInArea(x, y, spotter.sight + radius))
				isVisible = true;
			if (player == ref.updater.neutral)
				isVisible = true;
		}
		return isVisible;
	}

	public boolean canBeBought(Player player) {
		boolean buyable = true;
		if (!GameDrawer.nocosts) {
			if (kerit > player.kerit)
				buyable = false;
			if (pax > player.pax)
				buyable = false;
			if (arcanum > player.arcanum)
				buyable = false;
			if (prunam > player.prunam)
				buyable = false;
		}
		return buyable;
	}

	public void buyFrom(Player p) {
		ref.updater.send("<give " + p.ip + " " + "kerit" + " -" + kerit);
		ref.updater.send("<give " + p.ip + " " + "pax" + " -" + pax);
		ref.updater.send("<give " + p.ip + " " + "arcanum" + " -" + arcanum);
		ref.updater.send("<give " + p.ip + " " + "prunam" + " -" + prunam);
	}

	public void setupTarget() {
		try {
			((Trainer) this).setTarget(x + radius + 50, y + radius + 50);
		} catch (Exception e) {
		}
	}

	protected static String path(Nation nation, Object object) {
		String path = nation.toString() + "/"
				+ object.getClass().getEnclosingClass().getSimpleName() + "/";
		return path;
	}

	public enum GroundPosition {
		GROUND, AIR;
	}

}
