package game.aim;

import shared.NationInfo;
import shared.ref;
import entity.Building;
import entity.Entity;
import entity.UpgradeActive;
import entity.neutral.Arcanum;
import entity.neutral.Kerit;
import entity.neutral.Pax;
import entity.neutral.Prunam;
import game.AimHandler;

public class MineAim extends UpgradeAim {

	public MineAim(Entity builder, Entity newBuildable,
			Class<? extends Entity> oldBuildable) {
		super(builder, newBuildable, oldBuildable);
	}

	@Override
	protected boolean canPlaceAt(float x, float y) {
		boolean rightPlace = false;
		boolean inCommanderRange = false;
		for (Entity e : ref.updater.entities) {
			if (e.x == x && e.y == y) {
				if (e.getClass().equals(oldBuildable)) {
					replaced = (Building) e;
					rightPlace = true;
				} else
					changeMine(e);
			}
			if (isInCommandingRange(e, x, y))
				inCommanderRange = true;
		}
		return rightPlace && inCommanderRange
				&& ((Entity) buildable).canBeBought(builder.player);
	}

	private void changeMine(Entity e) {
		try {
			if (e.getClass().equals(Kerit.class)) {
				NationInfo n = builder.player.getNation().getNationInfo();
				buildable = n.getKeritMine().getConstructor(String[].class)
						.newInstance(new Object[] { null });
				oldBuildable = Kerit.class;
			} else if (e.getClass().equals(Pax.class)) {
				NationInfo n = builder.player.getNation().getNationInfo();
				buildable = n.getPaxDrillTower().getConstructor(String[].class)
						.newInstance(new Object[] { null });
				oldBuildable = Pax.class;
			} else if (e.getClass().equals(Arcanum.class)) {
				NationInfo n = builder.player.getNation().getNationInfo();
				buildable = n.getArcanumMine().getConstructor(String[].class)
						.newInstance(new Object[] { null });
				oldBuildable = Arcanum.class;

			} else if (e.getClass().equals(Prunam.class)) {
				NationInfo n = builder.player.getNation().getNationInfo();
				buildable = n.getPrunamHarvester()
						.getConstructor(String[].class)
						.newInstance(new Object[] { null });
				oldBuildable = Prunam.class;
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public static class BuildMineActive extends UpgradeActive {

		public BuildMineActive(int x, int y, char n, Entity b, Class<?> builder) {
			super(x, y, n, b, Kerit.class, builder);
			clazz = builder;
		}

		@Override
		public void onActivation() {
			Entity builder = null;
			for (Entity e : ref.updater.selected) {
				if (clazz.isAssignableFrom(e.getClass())) {
					builder = e;
				}
			}
			if (builder != null) {
				try {
					Building b = newBuilding.getConstructor(String[].class)
							.newInstance(new Object[] { null });
					AimHandler.setAim(new MineAim(builder, b, oldBuilding));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
}
