package entity.robots;

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

public class RobotsInfo extends NationInfo {

	@Override
	public Class<? extends MainBuilding> getMainBuilding() {
		return RobotsMainBuilding.class;
	}

	@Override
	public Class<? extends KeritMine> getKeritMine() {
		return RobotsKeritMine.class;
	}

	@Override
	public Class<? extends PaxDrillTower> getPaxDrillTower() {
		return RobotsPaxDrillTower.class;
	}

	@Override
	public Class<? extends ArcanumMine> getArcanumMine() {
		return RobotsArcanumMine.class;
	}

	@Override
	public Class<? extends PrunamHarvester> getPrunamHarvester() {
		return RobotsPrunamHarvester.class;
	}

	@Override
	public void setupActives(ActivesGrid grid) {
		grid.addActive(1, 1, Unit.AttackActive.class, true);
		grid.addActive(2, 1, Unit.WalkActive.class, true);
		grid.addActive(3, 1, Unit.StopActive.class, true);
		
		grid.addActive(1, 1, Building.SetTargetActive.class, false);
		grid.addBuildActive(4, 3, Commander.class, RobotsKaserne.class, false);
	
		grid.addActive(5, 3, BuildMineActive.class, Commander.class,
				getKeritMine(), false);

	}

}
