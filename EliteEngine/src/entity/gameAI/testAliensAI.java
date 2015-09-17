package entity.gameAI;

import entity.Entity;
import entity.MainBuilding;
import entity.aliens.AlienKaserne;
import entity.aliens.AlienKeritMine;
import entity.aliens.AlienPrunamHarvester;
import entity.aliens.Ticul;
import entity.aliens.Valcyrix;
import entity.neutral.Kerit;
import entity.neutral.Prunam;
import shared.Helper;
import shared.Nation;
import shared.ref;

public class testAliensAI extends GameAI {

	public testAliensAI(String ip, String name) {
		super(ip, name);
		nation = Nation.ALIENS;
	}

	@Override
	public void update() {
		if (Helper.listContainsInstancesOf(AlienKaserne.class,
				player.visibleEntities) == 0) {// kaserne & range
			Entity e = new AlienKaserne(null);
			spawn(e, player.mainBuilding.x, player.mainBuilding.y,
					ref.updater.map.width / 2, ref.updater.map.height / 2,
					player.mainBuilding.commandRange() - e.radius);

		}
		if (Helper.listContainsInstancesOf(AlienKaserne.class,
				player.visibleEntities) > 0
				&& Helper.listContainsInstancesOf(AlienKeritMine.class,
						player.visibleEntities) < 4) {// kerit mienen
			Entity e = new AlienKeritMine(null);
			for (Entity e2 : player.visibleEntities) {
				if (e2 instanceof Kerit) {
					upgrade(e, e2);
				}
			}
		}
		if (Helper.listContainsInstancesOf(AlienKaserne.class,
				player.visibleEntities) > 0
				&& Helper.listContainsInstancesOf(AlienKeritMine.class,
						player.visibleEntities) >= 4
				&& Helper.listContainsInstancesOf(Ticul.class,
						player.visibleEntities) < 10) {// anfangs army
			Entity t = new Ticul(null);
			for (Entity e : player.visibleEntities) {
				if (e instanceof AlienKaserne) {
					e.sendAnimation("setTarget " + ref.updater.map.width / 2
							+ " " + ref.updater.map.height / 2);
					train(t, e);
				}
			}
		}
		if (Helper.listContainsInstancesOf(AlienKaserne.class,
				player.visibleEntities) > 0
				&& Helper.listContainsInstancesOf(AlienKeritMine.class,
						player.visibleEntities) >= 4
				&& Helper.listContainsInstancesOf(AlienPrunamHarvester.class,
						player.visibleEntities) == 0) {// prunam miene
			Entity e = new AlienPrunamHarvester(null);
			for (Entity e2 : player.visibleEntities) {
				if (e2 instanceof Prunam) {
					upgrade(e, e2);
				}
			}
		}
		if (Helper.listContainsInstancesOf(AlienKaserne.class,
				player.visibleEntities) > 0
				&& Helper.listContainsInstancesOf(AlienKeritMine.class,
						player.visibleEntities) >= 4
				&& Helper.listContainsInstancesOf(Valcyrix.class,
						player.visibleEntities) < 6) {// annoy army
			Entity t = new Valcyrix(null);
			if (t.canBeBought(player))
				for (Entity e : player.visibleEntities) {
					if (e instanceof AlienKaserne) {
						e.sendAnimation("setTarget " + e.x + 50 + " " + e.y
								+ 50);
						train(t, e);
					}
				}
		}
		if (Helper.listContainsInstancesOf(Valcyrix.class,
				player.visibleEntities) >= 6) {// attack with annoy army
			MainBuilding target = getWeakestEnemy().mainBuilding;
			for (Entity e : player.visibleEntities) {
				if (e instanceof Valcyrix//
						&& ((Valcyrix) e).xTarget != target.x
						&& ((Valcyrix) e).yTarget != target.y) {
					System.out
							.println("testAliensAI.update() attack with Valc");
					e.sendAnimation("walk " + target.x + " " + target.y + "  "
							+ true);
				}
			}
		}

	}
}
