package entity.aliens;

import processing.core.PImage;
import entity.animation.Build;
import entity.animation.Death;
import entity.animation.Extract;
import entity.neutral.ArcanumMine;

public class AlienArcanumMine extends ArcanumMine   {

	private static PImage standImg;
	private static PImage previewImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		previewImg = standImg = game.ImageHandler.load(path, "AlienArcanumMine");
	}

	public AlienArcanumMine(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Extract(standImg, 1000);
		build = new Build(standImg, 1000);
		death = new Death(standImg, 1000);

		animation = nextAnimation = stand;
		// ************************************
		xSize = 50;
		ySize = 50;

		build.setBuildTime(buildTime);
		
		((Extract) stand).cooldown = cooldown;
		((Extract) stand).ressource = ressource;
		((Extract) stand).efficenty = efficenty;

		descr = " ";
		stats = "ressource/s: "
				+ (((Extract) stand).efficenty / ((Extract) stand).cooldown * 1000);
		// ************************************
	}

	public PImage preview() {
		return previewImg;
	}

}
