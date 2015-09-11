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

		grid.addActive(4, 2, M1N1B0T.BuildDepotActive.class, true);
		grid.addActive(3, 2, SN41L10N.AnchorActive.class, true);
		grid.addActive(3, 3, W4SP.SpeedActive.class, true);

		grid.addActive(1, 1, Building.SetTargetActive.class, false);
		grid.addBuildActive(4, 3, Commander.class, RobotsKaserne.class, false);

		grid.addActive(5, 3, BuildMineActive.class, Commander.class,
				getKeritMine(), false);

		grid.addTrainActive(1, 3, RobotsKaserne.class, M1N1B0T.class, false);
		grid.addTrainActive(2, 3, RobotsKaserne.class, B0T.class, false);
		grid.addTrainActive(3, 3, RobotsKaserne.class, W4SP.class, false);
		grid.addTrainActive(2, 2, RobotsKaserne.class, PL0S10N.class, false);
		grid.addTrainActive(3, 2, RobotsKaserne.class, SN41L10N.class, false);
		grid.addTrainActive(2, 1, RobotsKaserne.class, KR4B1T.class, false);
		grid.addTrainActive(3, 1, RobotsKaserne.class, F4CT0RY.class, false);

	}

}
