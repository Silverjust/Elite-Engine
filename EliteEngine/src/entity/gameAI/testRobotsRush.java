package entity.gameAI;

import entity.Entity;
import entity.MainBuilding;
import entity.robots.B0T;
import entity.robots.RobotsKaserne;
import shared.Helper;
import shared.Nation;
import shared.ref;

public class testRobotsRush extends GameAI {

	public testRobotsRush(String ip, String name) {
		super(ip, name);
		nation = Nation.ROBOTS;
	}

	@Override
	public void update() {
		if (true) {// kaserne & range
			int n = Helper.listContainsInstancesOf(RobotsKaserne.class,
					player.visibleEntities);
			Entity e = new RobotsKaserne(null);
			spawn(e, player.mainBuilding.x + n * 2 * e.radius,
					player.mainBuilding.y, ref.updater.map.width / 2 + n * 2
							* e.radius, ref.updater.map.height / 2,
					player.mainBuilding.commandRange() - e.radius);
		}
		/*
		 * if (Helper.listContainsInstancesOf(AlienKaserne.class,
		 * player.visibleEntities) > 0 &&
		 * Helper.listContainsInstancesOf(AlienKeritMine.class,
		 * player.visibleEntities) < 4) {// kerit mienen Entity e = new
		 * AlienKeritMine(null); for (Entity e2 : player.visibleEntities) { if
		 * (e2 instanceof Kerit) { upgrade(e, e2); } } }
		 */
		if (Helper.listContainsInstancesOf(RobotsKaserne.class,
				player.visibleEntities) > 0) {// anfangs army
			Entity t = new B0T(null);
			for (Entity e : player.visibleEntities) {
				if (e instanceof RobotsKaserne) {
					e.sendAnimation("setTarget " + ref.updater.map.width / 2
							+ " " + ref.updater.map.height / 2);
					train(t, e);
				}
			}
		}
		/*
		 * if (Helper.listContainsInstancesOf(AlienKaserne.class,
		 * player.visibleEntities) > 0 &&
		 * Helper.listContainsInstancesOf(AlienKeritMine.class,
		 * player.visibleEntities) >= 4 &&
		 * Helper.listContainsInstancesOf(AlienPrunamHarvester.class,
		 * player.visibleEntities) == 0) {// prunam miene Entity e = new
		 * AlienPrunamHarvester(null); for (Entity e2 : player.visibleEntities)
		 * { if (e2 instanceof Prunam) { upgrade(e, e2); } } } if
		 * (Helper.listContainsInstancesOf(AlienKaserne.class,
		 * player.visibleEntities) > 0 &&
		 * Helper.listContainsInstancesOf(AlienKeritMine.class,
		 * player.visibleEntities) >= 4 &&
		 * Helper.listContainsInstancesOf(Valcyrix.class,
		 * player.visibleEntities) < 6) {// annoy army Entity t = new
		 * Valcyrix(null); if (t.canBeBought(player)) for (Entity e :
		 * player.visibleEntities) { if (e instanceof AlienKaserne) {
		 * e.sendAnimation("setTarget " + e.x + 50 + " " + e.y + 50); train(t,
		 * e); } } }
		 */
		if (Helper.listContainsInstancesOf(B0T.class, player.visibleEntities) >= 6) {
			// attack with annoy army
			MainBuilding target = getWeakestEnemy().mainBuilding;
			for (Entity e : player.visibleEntities) {
				if (e instanceof B0T//
						&& ((B0T) e).xTarget != target.x
						&& ((B0T) e).yTarget != target.y) {
					System.out.println(this.getClass().getSimpleName()
							+ " attack with B0Ts");
					e.sendAnimation("walk " + target.x + " " + target.y + "  "
							+ true);
				}
			}
		}

	}
}
