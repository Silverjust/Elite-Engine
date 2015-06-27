package entity.neutral;

import entity.Active;
import entity.Entity;
import entity.Unit;
import entity.aliens.AlienKaserne;
import entity.aliens.AlienKeritMine;
import entity.aliens.Ticul;
import entity.animation.Animation;
import game.Chat;
import game.HUD;
import processing.core.PImage;
import shared.ref;

public class Tutorial extends Unit {

	String tut = "tutorial";

	private static PImage standingImg;

	int i = 0;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Tutorial");
	}

	public Tutorial(String[] c) {
		super(c);
		player = ref.updater.neutral;// neutral
		iconImg = standingImg;

		stand = walk = new Animation(standingImg, 500);
		death = null;

		animation = nextAnimation = walk;
		// ************************************
		xSize = 10;
		ySize = 10;

		hp = hp_max = 0;
		speed = 2.5f;
		radius = 7;
		sight = 70;
		groundPosition = Entity.GroundPosition.AIR;
		height = 50;

		descr = " ";
		// ************************************
	}

	@Override
	public void updateDecisions() {
		float x = 0, y = 0;
		Active[][] bActives = HUD.activesGrid.buildingGrid;
		// System.out.println(xTarget + " " + yTarget);
		switch (i) {
		case 0:
			xTarget = ref.player.mainBuilding.x;
			yTarget = ref.player.mainBuilding.y;
			isMoving = true;
			Chat.println(tut,
					"move your mouse to the rim of your screen to move the camera");
			Chat.println(tut,
					"find the Building with the arrow above and leftclick it");
			i++;
			break;
		case 1:
			if (ref.player.mainBuilding.isSelected)
				i++;

			break;
		case 2:
			ref.updater.send("<give " + ref.player.ip + " kerit " + 100);
			for (Entity e : ref.updater.entities) {
				if (e.getClass().equals(Kerit.class)) {
					x = e.x;
					y = e.y;
				}
			}
			xTarget = x;
			yTarget = y;
			isMoving = true;
			Chat.println(tut, "this is your mainbuilding");
			Chat.println(tut, "press \"" + bActives[2][4].n + "\" or the "
					+ bActives[2][4].n
					+ "-button and place the building here with rightclick");
			i++;
			break;
		case 3:
			for (Entity e : ref.updater.entities) {
				if (e.getClass().equals(AlienKeritMine.class)
						&& e.player == ref.player)
					i = 4;
			}
			break;
		case 4:
			yTarget -= 100;
			isMoving = true;
			ref.updater.send("<give " + ref.player.ip + " kerit " + 400);
			Chat.println(tut, "you are producing kerit now");
			Chat.println(tut, "wait until you have 500 kerit and then press \""
					+ bActives[2][3].n + "\" or the " + bActives[2][3].n
					+ "-button and place the building here");
			i++;
			break;
		case 5:
			for (Entity e : ref.updater.entities) {
				if (e.getClass().equals(AlienKaserne.class)
						&& e.player == ref.player)
					i = 6;
			}
			break;
		case 6:
			ref.updater.send("<give " + ref.player.ip + " kerit " + 400);
			Chat.println(tut, "you have a kaserne");
			Chat.println(tut,
					"wait until it is built, select the building and then press \""
							+ bActives[2][0].n + "\" or the "
							+ bActives[2][0].n + "-button ");
			i++;
			break;
		case 7:
			for (Entity e : ref.updater.entities) {
				if (e.getClass().equals(Ticul.class) && e.player == ref.player)
					i = 8;
			}
			break;
		case 8:
			xTarget = 470;
			yTarget = 480;
			isMoving = true;
			Chat.println(tut,
					"you trained a " + bActives[2][0].clazz.getSimpleName());
			Chat.println(tut,
					"train some " + bActives[2][1].clazz.getSimpleName()
							+ " with \"" + bActives[2][1].n + "\" or the "
							+ bActives[2][1].n
							+ "-button and select them with leftdrag");
			Chat.println(tut, "move the camera here and rightclick to attack");
			Chat.println(tut, "defeat them");
			i++;
			break;
		case 9:
			i = 10;
			for (Entity e : ref.updater.entities) {
				if (e instanceof Unit && e.player != ref.player
						&& e.player != ref.updater.neutral) {
					i = 9;
				}
			}
			break;
		case 10:
			xTarget = 460;
			yTarget = 140;
			isMoving = true;
			Chat.println(tut, "destroy  the enemy mainbuilding to win");
			i++;
			break;
		default:
			break;
		}
	}

	@Override
	public void renderAir() {
		drawSelected();
		animation.draw(this, direction, currentFrame);
		drawTaged();
	}

}
