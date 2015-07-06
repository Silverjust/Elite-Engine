package entity.humans;

import processing.core.PImage;
import shared.ref;
import entity.Building;
import entity.Trainer;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.Build;
import entity.animation.Death;
import entity.animation.Training;
import game.ImageHandler;

public class HumanMechKaserne extends Building implements  Trainer {
	//TODO animation richtig anzeigen
	protected float xTarget;
	protected float yTarget;

	private Ability training;

	private static PImage standImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = ImageHandler.load(path, "HumanMechKaserne");
	}

	public HumanMechKaserne(String[] c) {
		super(c);
 
		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 5000);
		death = new Death(standImg, 1000);
		training = new Training(standImg, 100);

		animation = nextAnimation = build;
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
	public void updateDecisions() {
		training.updateAbility(this);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		Training.updateExecTraining(c, this);
	}	

	@Override
	public void renderUnder() {
		if (isSelected) {
			ref.app.stroke(player.color);
			ref.app.line(xToGrid(x), yToGrid(y), xToGrid(xTarget),
					yToGrid(yTarget));
			ref.app.stroke(0);
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		animation.draw(this, (byte) 0, currentFrame);
	}

	@Override
	public void display() {
		super.display();
		if (animation == training)
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
