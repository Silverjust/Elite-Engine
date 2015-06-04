package entity.entities;

import processing.core.PImage;
import shared.Nation;
import shared.ref;
import entity.Buildable;
import entity.Building;
import entity.animation.Build;
import entity.animation.Death;
import entity.animation.Extract;

public class PaxDrillTower extends Building implements Buildable {

	private static PImage standImg;
	private static PImage previewImg;

	public static void loadImages() {
		String path = path(Nation.ALIENS, new Object() {
		});
		previewImg = standImg = game.ImageHandler.load(path, "PaxDrillTower");
	}

	public PaxDrillTower(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Extract(standImg, 1000);
		build = new Build(standImg, 1000);
		death = new Death(standImg, 100);

		animation = nextAnimation = stand;
		// ************************************
		xSize = 40;
		ySize = 40;

		kerit = 500;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(10000);

		hp = hp_max = 500;
		radius = 15;
		sight = 50;

		((Extract) stand).cooldown = 1000;
		((Extract) stand).resource = "pax";
		((Extract) stand).efficenty = 14;

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
