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
		basicAttack = new TargetAttack(attackImg, 800);

		animation = nextAnimation = walk;
		// ************************************
		xSize = 20;
		ySize = 20;

		kerit = 35;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		trainTime = 1500;

		hp = hp_max = 50;
		speed = 0.9f;
		radius = 5;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 50);
		basicAttack.range = (byte) (radius + 10);
		basicAttack.damage = 8;
		basicAttack.cooldown = 800;
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
						if (e.isCollision(x, y, aggroRange + e.radius)) {
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
						if (e.isCollision(x, y, basicAttack.range + e.radius)
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
	public void renderGround() {
		drawSelected();
		animation.draw(this, direction, currentFrame);
		drawTaged();
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	static public class Smite extends Active {// ******************************************************
		public Smite(int x, int y, char n) {
			super(x, y, n, smiteImg);
			clazz = Ticul.class;
			cooldown = 5000;
		}

		@Override
		public void onButtonPressed(GGameButton gamebutton, GEvent event) {

			for (Entity e : ref.player.visibleEntities) {
				if (e.isEnemyTo(ref.player)) {
					ClientHandler.send("<hit " + e.number + " " + 5);
				}
			}

		}

		@Override
		public String getDesription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getStatistics() {
			// TODO Auto-generated method stub
			return null;
		}
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
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getStatistics() {
			// TODO Auto-generated method stub
			return null;
		}
	}

}
