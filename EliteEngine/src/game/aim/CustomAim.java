package game.aim;

import entity.AimingActive;
import game.AimHandler.Cursor;

public class CustomAim extends Aim {

	private   Cursor cursor;
	private AimingActive aiming;

	public CustomAim(AimingActive a,Cursor c) {
		aiming=a;
		cursor=c;
	}
	@Override
	public Cursor getCursor() {
		return cursor;
	}

	@Override
	public void execute(float x, float y) {
		aiming.execute( x,  y);
	}

}
