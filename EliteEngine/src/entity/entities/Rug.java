package entity.entities;

import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.Death;
import entity.animation.TargetAttack;
import processing.core.PImage;
import shared.Nation;
import shared.ref;

public class Rug extends Unit implements Attacker {
	private static PImage walking;

	public static void loadImages() {
		String path = path(Nation.ALIENS, new Object() {
		});
		walking = game.ImageHandler.load(path, "rug");
	}

	private TargetAttack basicAttack;

	public Rug(String[] c) {
		super(c);

		iconImg = walking;

		stand = new Animation(walking, 100);
		walk = new Animation(walking, 800);
		death = new Death(walking, 500);
		basicAttack = new TargetAttack(walking, 800);
		animation = nextAnimation = walk;
		// **************************************
		hp = hp_max = 10;
		speed = 1;
		sight = 100;
		radius = 20;
		groundPosition = Entity.GroundPosition.GROUND;
	}

	@Override
	public void updateDecisions() {
	}

	@Override
	public void renderGround() {
		drawSelected();
		ref.app.image(walking, xToGrid(x), yToGrid(y), 60, 60);// GameClient.render.image(App.k,x,y);
	}

	@Override
	public Ability getBasicAttack() {
		return basicAttack;
	}

}
