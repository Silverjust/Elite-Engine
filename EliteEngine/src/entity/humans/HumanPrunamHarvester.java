package entity.humans;

import processing.core.PImage;
import entity.animation.Build;
import entity.animation.Death;
import entity.animation.Extract;
import entity.neutral.PrunamHarvester;

public class HumanPrunamHarvester extends PrunamHarvester   {

	private static PImage standImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = game.ImageHandler.load(path, "HumanPrunamHarvester");
	}

	public HumanPrunamHarvester(String[] c) {
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
		return standImg;
	}

}
