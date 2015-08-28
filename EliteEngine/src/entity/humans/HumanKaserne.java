package entity.humans;

import processing.core.PImage;
import entity.Building;
import entity.Trainer;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.Build;
import entity.animation.Death;
import entity.animation.Training;
import game.ImageHandler;

public class HumanKaserne extends Building implements  Trainer {
	protected float xTarget;
	protected float yTarget;

	private Ability training;

	private static PImage standImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = ImageHandler.load(path, "HumanKaserne");
	}

	public HumanKaserne(String[] c) {
		super(c);
 
		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 5000);
		death = new Death(standImg, 1000);
		training = new Training(standImg, 100);

		setAnimation(build);
		setupTarget();
		// ************************************
		xSize = 30;
		ySize = 30;

		kerit = 500;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(5000);

		sight = 50;

		hp = hp_max = 1000;
		radius = 15;

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
