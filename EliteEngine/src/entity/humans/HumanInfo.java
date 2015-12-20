package entity.humans;

import entity.BuildWallActive;
import entity.Building;
import entity.Commander;
import entity.MainBuilding;
import entity.Unit;
import entity.neutral.ArcanumMine;
import entity.neutral.KeritMine;
import entity.neutral.PaxDrillTower;
import entity.neutral.PrunamHarvester;
import game.ActivesGrid;
import game.ActivesGridHandler;
import game.aim.MineAim.BuildMineActive;
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
	public void setupActives(ActivesGrid grid, ActivesGridHandler handler) {
		grid.addActive(1, 1, Unit.AttackActive.class);
		grid.addActive(2, 1, Unit.WalkActive.class);
		grid.addActive(3, 1, Unit.StopActive.class);
		
		grid.addActive(1, 1, Building.SetTargetActive.class);
		grid.addBuildActive(4, 3, Commander.class, HumanKaserne.class);
		grid.addBuildActive(4, 2, Commander.class, HumanMechKaserne.class);
		grid.addBuildActive(5, 1, Commander.class, HumanDepot.class);
		grid.addActive(5, 2, BuildWallActive.class, Commander.class,
				HumanWall.class);
		grid.addActive(5, 3, BuildMineActive.class, Commander.class,
				getKeritMine());

		grid.addTrainActive(1, 3, HumanKaserne.class, Scout.class);
		grid.addTrainActive(2, 3, HumanKaserne.class, HeavyAssault.class);
		grid.addTrainActive(1, 2, HumanKaserne.class, Medic.class);
		grid.addTrainActive(3, 3, HumanKaserne.class, Exo.class);
		grid.addTrainActive(2, 2, HumanMechKaserne.class, SmallTank.class);
		grid.addTrainActive(3, 2, HumanMechKaserne.class, Tank.class);
		grid.addTrainActive(2, 1, HumanMechKaserne.class, Drone.class);
		grid.addTrainActive(3, 1, HumanMechKaserne.class, Helicopter.class);
	}

}
