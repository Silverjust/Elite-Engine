package entity.ahnen;

import entity.Building;
import entity.Commander;
import entity.MainBuilding;
import entity.Unit;
import entity.neutral.ArcanumMine;
import entity.neutral.KeritMine;
import entity.neutral.PaxDrillTower;
import entity.neutral.PrunamHarvester;
import game.ActivesGrid;
import game.aim.MineAim.BuildMineActive;
import shared.NationInfo;

public class AhnenInfo extends NationInfo {

	@Override
	public Class<? extends MainBuilding> getMainBuilding() {
		return AhnenMainBuilding.class;
	}

	@Override
	public Class<? extends KeritMine> getKeritMine() {
		return AhnenKeritMine.class;
	}

	@Override
	public Class<? extends PaxDrillTower> getPaxDrillTower() {
		return AhnenPaxDrillTower.class;
	}

	@Override
	public Class<? extends ArcanumMine> getArcanumMine() {
		return AhnenArcanumMine.class;
	}

	@Override
	public Class<? extends PrunamHarvester> getPrunamHarvester() {
		return AhnenPrunamHarvester.class;
	}

	public void setupActives(ActivesGrid grid) {
		grid.addActive(1, 1, Unit.AttackActive.class, true);
		grid.addActive(2, 1, Unit.WalkActive.class, true);
		grid.addActive(3, 1, Unit.StopActive.class, true);

		grid.addActive(1, 3, Berserker.LeuchteActive.class, Berserker.class,
				Leuchte.class, true);
		grid.addActive(2, 3, Witcher.UpgradeActive.class, true);
		grid.addActive(3, 3, Destructor.UpgradeActive.class, true);
		grid.addActive(1, 2, Angel.CloakActive.class, true);
		grid.addActive(2, 2, Witcher.BurstActive.class, true);

		grid.addActive(1, 1, Building.SetTargetActive.class, false);
		grid.addBuildActive(4, 3, Commander.class, AhnenKaserne.class, false);
		grid.addActive(5, 3, BuildMineActive.class, Commander.class,
				getKeritMine(), false);

		grid.addTrainActive(1, 3, AhnenKaserne.class, Berserker.class, false);
		grid.addTrainActive(2, 3, AhnenKaserne.class, Witcher.class, false);
		grid.addTrainActive(3, 3, AhnenKaserne.class, Warrior.class, false);
		grid.addTrainActive(1, 2, AhnenKaserne.class, Angel.class, false);

		grid.addTrainActive(3, 2, AhnenKaserne.class, Destructor.class, false);

	}
}
