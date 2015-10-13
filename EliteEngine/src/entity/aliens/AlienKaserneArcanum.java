package entity.aliens;

import processing.core.PGraphics;
import processing.core.PImage;
import shared.ref;
import entity.Building;
import entity.Commander;
import entity.Trainer;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.Build;
import entity.animation.Death;
import entity.animation.Training;
import game.ImageHandler;

public class AlienKaserneArcanum extends Building implements Commander, Trainer {
	private int commandRange;
	protected float xTarget;
	protected float yTarget;

	private Training training;

	private static PImage standImg;
	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = ImageHandler.load(path, "AlienKaserneArcanum");

	}

	public AlienKaserneArcanum(String[] c) {
		super(c);
		AlienMainBuilding.getGround();

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 1000);
		death = new Death(standImg, 1000);
		training = new Training(standImg, 100);

		setAnimation(build);
		setupTarget();
		// ************************************
		xSize = 40;
		ySize = 40;

		kerit = 600;
		pax = 0;
		arcanum = 40;
		prunam = 0;
		build.setBuildTime(10000);

		sight = 50;

		hp = hp_max = 1000;
		radius = 18;

		commandRange = 250;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		Training.updateExecTraining(c, this);
	}

	@Override
	public void renderTerrain() {
		ImageHandler.drawImage(ref.app, AlienMainBuilding.groundImg, xToGrid(x),
				yToGrid(y), commandRange * 2, commandRange);
	}

	@Override
	public void renderUnder() {
		if (isSelected&& isAlive()) {
			ref.app.stroke(player.color);
			ref.app.line(xToGrid(x), yToGrid(y), xToGrid(xTarget),
					yToGrid(yTarget));
			ref.app.stroke(0);
		}
	}

	@Override
	public void drawOnMinimapUnder(PGraphics graphics) {
		graphics.image(AlienMainBuilding.groundImg, x, y, commandRange * 2,
				commandRange * 2);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, currentFrame);
	}

	@Override
	public void display() {
		super.display();
		if (getAnimation() == training)
			drawBar(training.getCooldownPercent());
	}

	public PImage preview() {
		return standImg;
	}

	@Override
	public int commandRange() {
		return commandRange;
	}

	@Override
	public Ability getTraining() {
		return training;
	}

	@Override
	public float getXTarget() {
		return xTarget;
	}

	@Override
	public float getYTarget() {
		return yTarget;
	}

	@Override
	public void setTarget(float x, float y) {
		xTarget = x;
		yTarget = y;

	}

}
