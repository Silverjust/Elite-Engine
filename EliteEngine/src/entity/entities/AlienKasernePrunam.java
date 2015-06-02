package entity.entities;

import processing.core.PImage;
import shared.Nation;
import entity.Buildable;
import entity.Building;
import entity.Commanding;
import entity.animation.Animation;
import entity.animation.Death;
import game.ImageHandler;

public class AlienKasernePrunam extends Building implements Buildable, Commanding {
	private int commandingRange;

	private static PImage standImg;

	public static void loadImages() {
		String path = path(Nation.ALIENS, new Object() {
		});
		standImg = ImageHandler.load(path, "AlienKasernePrunam");
	}

	public AlienKasernePrunam(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 100);
		build = new Animation(standImg, 100);
		death = new Death(standImg, 100);

		animation = nextAnimation = stand;
		// ************************************
		xSize = 60;
		ySize = 60;

		kerit=600;
		
		sight = 50;

		hp = hp_max = 1000;
		radius = 15;

		commandingRange = 250;
		// ************************************
	}

	@Override
	public void updateDecisions() {
		
	}

	@Override
	public void renderGround() {
		drawSelected();
		animation.draw(this, (byte) 0, currentFrame);
	}

	public PImage preview() {
		return standImg;
	}

	@Override
	public int commandRange() {
		return commandingRange;
	}

}
