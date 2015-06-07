package entity;

import entity.animation.Attack;

public interface Attacker {

	Attack getBasicAttack();
	
	void calculateDamage(Attack a);

}
