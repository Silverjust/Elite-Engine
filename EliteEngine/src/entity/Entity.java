package entity;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import shared.Nation;
import shared.Player;
import shared.Updater;
import shared.ref;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import game.GameDrawer;
import game.HUD;
import game.ImageHandler;

public abstract class Entity implements Informing {
	// TODO upgrades für einheiten
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
	protected static PImage selectedImg;
	private static PImage hpImg;
	public PImage iconImg;
	// private static AudioSnippet hit;
	protected String descr = " ", stats = " ";

	public Death death;
	public Animation stand;

	private Animation animation;
	private Animation nextAnimation;

	public static void loadImages() {
		String path = path(new Object() {
		});
		shadowImg = ImageHandler.load(path, "shadow");
		selectedImg = ImageHandler.load(path, "selected");
		hpImg = ImageHandler.load(path, "hp");

		// hit = ref.minim.loadSnippet("test.mp3");
	}

	public void updateAnimation() {
		animation = getNextAnimation();
		if (Animation.observe.isAssignableFrom(this.getClass())) {
			System.out.println("Entity.updateAnimation()"
					+ animation.getName(this));
		}
		animation.update(this);
	}

	public void updateDecisions(boolean isServer) {
	}

	public void updateMovement() {
	}

	public void renderTerrain() {
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

	public void renderRange() {
		if (this instanceof Attacker
				&& ((Attacker) this).getBasicAttack() != null) {
			Attack a = ((Attacker) this).getBasicAttack();
			drawCircle(a.range);
			drawCircle((int) (a.range * a.getCooldownPercent()));
		}
		if (this instanceof Unit) {
			ref.app.line(x, y / 2, ((Unit) this).xTarget,
					((Unit) this).yTarget / 2);
		}
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
			System.err.println(getAnimation() + " " + getNextAnimation());
			PApplet.printArray(c);
			e.printStackTrace();
		}
	}

	public void hit(int damage, byte pirce) {

		if (isMortal()) {// only for nonimmortal objects
			// SoundHandler.startIngameSound(hit, x, y);

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
		HUD.chat.println(this.getClass().getSimpleName() + number, "(" + x
				+ "|" + y + ")" + "\nhp:" + hp);
	}

	protected void drawShadow() {
		ref.app.image(shadowImg, xToGrid(x), yToGrid(y), radius * 2, radius);
	}

	public byte flyHeight() {
		if (groundPosition == GroundPosition.AIR)
			return 30;
		return 0;
	}

	protected void drawSelected() {
		if (isSelected && isAlive()) {
			ref.app.tint(player.color);
			ref.app.image(selectedImg, xToGrid(x), yToGrid(y - flyHeight()),
					radius * 2, radius);
			ref.app.tint(255);
		}
	}

	protected void drawTaged() {
		/** just for debug */
		if (isTaged) {
			ref.app.fill(0, 0);
			ref.app.stroke(player.color);
			ref.app.rect(xToGrid(x), yToGrid(y - flyHeight()) - radius * 0.3f,
					radius * 2, radius * 1.5f);
			ref.app.stroke(0);
		}
		isTaged = false;
	}

	void drawHpBar() {
		int h = 1;
		if (isAlive() && isMortal()) {//
			ref.app.fill(0, 150);
			ref.app.rect(xToGrid(x), yToGrid(y - flyHeight()) - radius * 1.5f,
					radius * 2, h);
			ref.app.tint(player.color);
			ref.app.image(hpImg, xToGrid(x), yToGrid(y - flyHeight()) - radius
					* 1.5f, radius * 2 * hp / hp_max, h);
			ref.app.tint(255);
		}
	}

	public void drawBar(float f) {
		int h = 1;
		if (isAlive() && isMortal()) {//
			ref.app.fill(0, 150);
			ref.app.rect(xToGrid(x), yToGrid(y - flyHeight() - h * 3) - radius
					* 1.5f, radius * 2, h);
			ref.app.tint(200);
			ref.app.image(hpImg, xToGrid(x), yToGrid(y - flyHeight() - h * 3)
					- radius * 1.5f, radius * 2 * f, h);
			ref.app.tint(255);
		}
	}

	public void drawBar(float f, int c) {
		int h = 1;
		if (isAlive() && isMortal()) {//
			ref.app.fill(0, 150);
			ref.app.rect(xToGrid(x), yToGrid(y - flyHeight() - h * 3) - radius
					* 1.5f, radius * 2, h);
			ref.app.tint(c);
			ref.app.image(hpImg, xToGrid(x), yToGrid(y - flyHeight() - h * 3)
					- radius * 1.5f, radius * 2 * f, h);
			ref.app.tint(255);
		}
	}

	protected void drawCircle(int r) {
		ref.app.image(selectedImg, xToGrid(x), yToGrid(y), r * 2, r);
	}

	protected void drawCircle(float x, float y, byte range) {
		ref.app.image(selectedImg, xToGrid(x), yToGrid(y), range * 2, range);
	}

	void drawLine(float tx, float ty) {
		ref.app.line(xToGrid(x), yToGrid(y), tx, ty / 2);
	}

	public void drawOnMinimap(PGraphics graphics) {
	}

	public void drawOnMinimapUnder(PGraphics graphics) {
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
		if (descr.equals(" "))
			return this.getClass().getSimpleName();
		return descr;
	}

	@Override
	public String getStatistics() {
		String stats = "";
		if (kerit == 0 && pax == 0 && arcanum == 0 && prunam == 0)
			stats += "§§§§";
		else
			stats += "kerit: " + kerit + "§pax: " + pax + "§arcanum: "
					+ arcanum + "§prunam: " + prunam + "§";
		if (this instanceof Building && ((Building) this).build != null)
			stats += "time: " + ((Building) this).build.cooldown / 1000.0
					+ "§§";
		else if (this instanceof Unit)
			stats += "time: " + ((Unit) this).trainTime / 1000.0 + "§§";
		else
			stats += "§§";
		if (death != null)
			stats += "hp: " + hp_max + " (" + armor + ")§";
		else
			stats += "hp: Immortal Object§";

		if (this instanceof Attacker
				&& ((Attacker) this).getBasicAttack() != null) {
			Attack a = ((Attacker) this).getBasicAttack();
			if (a.pirce >= 0) {
				stats += "dps: " + a.damage + "/" + a.cooldown / 1000.0 + " ("
						+ a.pirce + ")";
				if (a.targetable == GroundPosition.GROUND)
					stats += " _§";
				else if (a.targetable == GroundPosition.AIR)
					stats += " °§";
				else if (a.targetable == null)
					stats += " _°§";
			} else {
				stats += "heal/s: " + a.damage + "/" + a.cooldown / 1000.0
						+ "§";
			}
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
		if ((a != null && animation == null)
				|| (a != null && animation.isInterruptable() && animation != a)) {
			if (Animation.observe.isAssignableFrom(this.getClass())) {
				System.out.println("Entity.setAnimation()" + a.getName(this));
			}
			if (animation == null)
				animation = a;
			nextAnimation = a;
			a.setup(this);
		}
	}

	public Animation getAnimation() {
		return animation;
	}

	public Animation getNextAnimation() {
		return nextAnimation;
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
		if (radius == 0 || e.radius == 0)
			return false;
		float f = PApplet.dist(x, y, e.x, e.y);
		boolean b = f < radius + e.radius && e.groundPosition == groundPosition
				&& e.isCollidable(this);
		// if(b)System.out.println(number + "/" + e.number + ":" + f + b);
		return b;
	}

	public boolean isCollidable(Entity entity) {
		return true;
	}

	public boolean isInRange(float X, float Y, int R) {
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
			return (getAnimation().getClass() != death.getClass()) && hp > 0;
		return true;
	}

	public boolean isMortal() {
		return death != null;
	}

	public boolean isVisibleTo(Player p) {
		boolean isVisible = false;
		for (Entity spotter : ref.updater.entities) {
			if (spotter.player == p
					&& spotter.isInRange(x, y, spotter.sight + radius))
				isVisible = true;
			if (player == ref.updater.neutral)
				isVisible = true;
		}
		return isVisible;
	}

	public boolean canBeBought(Player player) {
		return player.canBy(kerit, pax, arcanum, prunam);
	}

	public void buyFrom(Player p) {
		buyFrom(p, kerit, pax, arcanum, prunam);
	}

	public void buyFrom(Player p, int kerit, int pax, int arcanum, int prunam) {
		ref.updater.send("<give " + p.ip + " " + "kerit" + " -" + kerit);
		ref.updater.send("<give " + p.ip + " " + "pax" + " -" + pax);
		ref.updater.send("<give " + p.ip + " " + "arcanum" + " -" + arcanum);
		ref.updater.send("<give " + p.ip + " " + "prunam" + " -" + prunam);
	}

	public void setupTarget() {
		if (this instanceof Trainer) {
			((Trainer) this).setTarget(x + radius + 50, y + radius + 50);
		}
	}

	protected static String path(Object object) {
		String pack = object.getClass().getEnclosingClass().getPackage()
				.getName();
		if (pack.equals("entity"))
			pack = "neutral";
		else
			pack = pack.substring(pack.lastIndexOf('.') + 1, pack.length());

		String path = pack + "/"
				+ object.getClass().getEnclosingClass().getSimpleName() + "/";
		return path;
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
