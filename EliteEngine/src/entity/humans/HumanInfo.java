package entity.humans;

import entity.Building;
import entity.Commander;
import entity.MainBuilding;
import entity.neutral.ArcanumMine;
import entity.neutral.KeritMine;
import entity.neutral.PaxDrillTower;
import entity.neutral.PrunamHarvester;
import game.ActivesGrid;
import shared.NationInfo;

public class HumanInfo extends NationInfo {

	@Override
	public Class<? extends MainBuilding> getMainBuilding() {
		return HumanMainBuilding.class;
	}

	@Override
	public Class<? extends KeritMine> getKeritMine() {
		return HumanKeritMine.class;
	}

	@Override
	public Class<? extends PaxDrillTower> getPaxDrillTower() {
		return HumanPaxDrillTower.class;
	}

	@Override
	public Class<? extends ArcanumMine> getArcanumMine() {
		return HumanArcanumMine.class;
	}

	@Override
	public Class<? extends PrunamHarvester> getPrunamHarvester() {
		return HumanPrunamHarvester.class;
	}

	@Override
	public void setupActives(ActivesGrid grid) {
		grid.addActive(1, 1, Building.SetTargetActive.class, false);
		grid.addBuildActive(4, 3, Commander.class, HumanKaserne.class, false);
		grid.addBuildActive(4, 2, Commander.class, HumanMechKaserne.class,
				false);
		grid.addBuildActive(4, 1, Commander.class, HumanDepot.class, false);
		grid.addBuildWallActive(5, 2, Commander.class, HumanWall.class, false);
		grid.addBuildMineActive(5, 3, Commander.class, false);

		grid.addTrainActive(1, 3, HumanKaserne.class, Scout.class, false);
		grid.addTrainActive(2, 3, HumanKaserne.class, HeavyAssault.class, false);
		grid.addTrainActive(1, 2, HumanKaserne.class, Medic.class, false);
		grid.addTrainActive(3, 3, HumanKaserne.class, Exo.class, false);
		grid.addTrainActive(2, 2, HumanMechKaserne.class, SmallTank.class,
				false);
		grid.addTrainActive(3, 2, HumanMechKaserne.class, Tank.class, false);
		grid.addTrainActive(2, 1, HumanMechKaserne.class, Drone.class, false);
		grid.addTrainActive(3, 1, HumanMechKaserne.class, Helicopter.class,
				false);
	}

}
