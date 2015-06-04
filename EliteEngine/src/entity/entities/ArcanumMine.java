package entity.entities;

import processing.core.PImage;
import shared.Nation;
import shared.ref;
import entity.Buildable;
import entity.Building;
import entity.animation.Build;
import entity.animation.Death;
import entity.animation.Extract;

public class ArcanumMine extends Building implements Buildable {

	private static PImage standImg;
	private static PImage previewImg;

	public static void loadImages() {
		String path = path(Nation.ALIENS, new Object() {
		});
		previewImg = standImg = game.ImageHandler.load(path, "ArcanumMine");
	}

	public ArcanumMine(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Extract(standImg, 100);
		build = new Build(standImg, 100);
		death = new Death(standImg, 100);

		animation = nextAnimation = stand;
		// ************************************
		xSize = 30;
		ySize = 30;

		kerit = 1000;pax = 0;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(10000);

		hp = hp_max = 100;
		radius = 15;
		sight = 50;

		((Extract) stand).cooldown = 1000;
		((Extract) stand).resource = "kerit";
		((Extract) stand).efficenty = 20;

		descr = " ";
		stats = "resource/s: "
				+ (((Extract) stand).efficenty / ((Extract) stand).cooldown * 1000);
		// ************************************
	}

	@Override
	public void updateDecisions() {
		super.updateDecisions();
		((Extract) stand).updateAbility(this);
	}

	@Override
	protected void onDeath() {
		super.onDeath();
		ref.updater.send("<spawn Kerit 0 " + x + " " + y);
	}

	@Override
	public void renderGround() {
		drawSelected();
		animation.draw(this, (byte) 0, currentFrame);
	}

	public PImage preview() {
		return previewImg;
	}

}
