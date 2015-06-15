package entity.aliens;

import main.ClientHandler;
import entity.Active;
import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.TargetAttack;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import game.ImageHandler;
import processing.core.PApplet;
import processing.core.PImage;
import shared.Helper;
import shared.Nation;
import shared.ref;

public class Ticul extends Unit implements Attacker {

	private static PImage[][] standingImg;
	private static PImage[][] walkingImg;
	private static PImage[][] attackImg;
	private static PImage smiteImg;

	byte aggroRange;

	TargetAttack basicAttack;

	public static void loadImages() {
		String path = path(Nation.ALIENS, new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Ticul", 's', (byte) 8,
				(byte) 1);
		walkingImg = game.ImageHandler.load(path, "Ticul", 'w', (byte) 8,
				(byte) 8);
		attackImg = game.ImageHandler.load(path, "Ticul", 'b', (byte) 8,
				(byte) 8);

		smiteImg = ImageHandler.load(Nation.ALIENS.toString() + "/symbols/",
				"smite");
	}

	public Ticul(String[] c) {
		super(c);
		iconImg = walkingImg[0][0];

		stand = new Animation(standingImg, 100);
		walk = new Animation(walkingImg, 800);
		death = new Death(attackImg, 500);
		basicAttack = new TargetAttack(attackImg, 600);

		animation = nextAnimation = walk;
		// ************************************
		xSize = 20;
		ySize = 20;

		kerit = 28;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		trainTime = 1500;

		hp = hp_max = 50;
		speed = 
				1.2f;
		radius = 5;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 50);
		basicAttack.range = (byte) (radius + 10);
		basicAttack.damage = 8;
		basicAttack.cooldown = 600;
		basicAttack.eventTime = 500;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions() {

		// isTaged = false;
		if (animation == stand) {// ****************************************************
			String s = "";
			for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {// server
						if (e.isInRange(x, y, aggroRange + e.radius)) {
							s = ("walk " + e.x + " " + e.y);
						}
					}
				}
			}
			sendAnimation(s);
		}
		if (animation == walk) {// ****************************************************
			float importance = 0;
			Entity importantEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(x, y, basicAttack.range + e.radius)
								&& e.groundPosition == GroundPosition.GROUND) {
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
					}
				}
			}
			if (importantEntity != null && getBasicAttack().isNotOnCooldown()) {
				// System.out.println(thread);
				sendAnimation("basicAttack " + importantEntity.number);
			}
		}
		basicAttack.updateAbility(this);
	}

	@Override
	public void calculateDamage(Attack a) {
		ref.updater.send("<hit " + basicAttack.getTarget().number + " "
				+ a.damage + " " + a.pirce);
	}

	@Override
	public void renderGround() {
		drawSelected();
		animation.draw(this, direction, currentFrame);
		drawTaged();
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	static public class Flash extends Active {// ******************************************************
		private int range = 100;

		public Flash(int x, int y, char n) {
			super(x, y, n, smiteImg);
			clazz = Ticul.class;
			cooldown = 1000;
		}

		@Override
		public void onButtonPressed(GGameButton gamebutton, GEvent event) {
			for (Entity e : ref.updater.selected) {
				float tx = Helper.gridToX(ref.app.mouseX);
				float ty = Helper.gridToY(ref.app.mouseY);
				float x = e.x + (tx - e.x) / PApplet.dist(e.x, e.y, tx, ty)
						* range;
				float y = e.y + (ty - e.y) / PApplet.dist(e.x, e.y, tx, ty)
						* range;
				ClientHandler.send("<tp " + e.number + " " + x + " " + y);
			}

		}

		@Override
		public String getDesription() {
			return "short range teleport§work in progress";
		}

	}

}
