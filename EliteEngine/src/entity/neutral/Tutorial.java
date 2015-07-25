package entity.neutral;

import entity.Active;
import entity.Entity;
import entity.TrainActive;
import entity.Trainer;
import entity.Unit;
import entity.animation.Animation;
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

		setAnimation(walk);
		
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
			HUD.chat.println(tut,
					"move your mouse to the rim of your screen to move the camera");
			HUD.chat.println(tut,
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
			HUD.chat.println(tut, "###########################");
			HUD.chat.println(tut, "this is your mainbuilding");
			HUD.chat.println(tut, "press \"" + bActives[4][2].n + "\" or the "
					+ bActives[4][2].n
					+ "-button and place the building here with rightclick");
			i++;
			break;
		case 3:
			for (Entity e : ref.updater.entities) {
				if (e instanceof KeritMine && e.player == ref.player)
					i = 4;
			}
			break;
		case 4:
			yTarget -= 100;
			isMoving = true;
			ref.updater.send("<give " + ref.player.ip + " kerit " + 400);
			HUD.chat.println(tut, "###########################");
			HUD.chat.println(tut, "you are producing kerit now");
			HUD.chat.println(tut, "wait until you have 500 kerit and then press \""
					+ bActives[3][2].n + "\" or the " + bActives[3][2].n
					+ "-button and place the building here");
			i++;
			break;
		case 5:
			for (Entity e : ref.updater.entities) {
				if (e instanceof Trainer && e.player == ref.player)//kaserne
					i = 6;
			}
			break;
		case 6:
			ref.updater.send("<give " + ref.player.ip + " kerit " + 400);
			HUD.chat.println(tut, "###########################");
			HUD.chat.println(tut, "you built a barrack");
			HUD.chat.println(tut,
					"wait until it is built, select the building and then press \""
							+ bActives[0][2].n + "\" or the "
							+ bActives[0][2].n + "-button ");
			i++;
			break;
		case 7:
			for (Entity e : ref.updater.entities) {
				if (e instanceof Unit && e.player == ref.player)//anfangseinheit
					i = 8;
			}
			break;
		case 8:
			xTarget = 470;
			yTarget = 480;
			isMoving = true;
			HUD.chat.println(tut, "###########################");
			HUD.chat.println(tut, "you trained a "
					+ ((TrainActive) bActives[0][2]).unit.getSimpleName());
			HUD.chat.println(tut, "train some "
					+ ((TrainActive) bActives[1][2]).unit.getSimpleName()
					+ " with \"" + bActives[1][2].n + "\" or the "
					+ bActives[1][2].n
					+ "-button and select them with leftdrag");
			HUD.chat.println(tut, "move the camera here and rightclick to attack");
			HUD.chat.println(tut, "defeat them");
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
			HUD.chat.println(tut, "###########################");
			HUD.chat.println(tut, "destroy  the enemy mainbuilding to win");
			i++;
			break;
		default:
			break;
		}
	}

	@Override
	public void renderAir() {
		drawSelected();
		getAnimation().draw(this, direction, currentFrame);
		drawTaged();
	}

}
