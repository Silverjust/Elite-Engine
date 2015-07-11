package game.aim;

import processing.core.PApplet;
import shared.ref;
import entity.Entity;
import entity.Unit;
import entity.scientists.Lab;
import entity.scientists.PhysicsLab;
import entity.scientists.PhysicsLab.TeleportActive;
import game.AimHandler;
import game.AimHandler.Cursor;

public class TeleportAim extends Aim {

	private PhysicsLab origin;
	private TeleportActive active;

	public TeleportAim(PhysicsLab origin, TeleportActive active) {
		this.origin = origin;
		this.active = active;
		System.out.println("TeleportAim.TeleportAim()");
	}

	@Override
	public Cursor getCursor() {
		return Cursor.SELECT;
	}

	@Override
	public void update() {
	}

	@Override
	public void execute() {
		float x, y;
		Entity target = null;
		x = Entity.xToGrid(Entity.gridToX());
		y = Entity.xToGrid(Entity.gridToY());
		for (Entity e : ref.updater.entities) {
			if (e.isAllyTo(ref.player) && e instanceof PhysicsLab
					&& PApplet.dist(x, y, e.x, e.y - e.height) <= e.radius)
				target = e;
		}
		if (target != null) {
			System.out.println("TeleportAim.execute()");
			origin.getTeleport().startCooldown(); 
			target.sendAnimation("recieveTeleport");
			for (Entity e : ref.updater.entities) {
				if (e.isAllyTo(ref.player) && e instanceof Unit
						&& !(e instanceof Lab)
						&& e.isInRange(origin.x, origin.y, origin.equipRange))
					ref.updater.send("<tp " + e.number + " "
							+ (e.x + target.x - origin.x) + " "
							+ (e.y + target.y - origin.y));
			}
			active.startCooldown();
			AimHandler.end();
		}
	}
}
