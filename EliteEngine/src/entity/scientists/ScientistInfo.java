package entity.scientists;

import entity.Building;
import entity.Commander;
import entity.MainBuilding;
import entity.neutral.ArcanumMine;
import entity.neutral.KeritMine;
import entity.neutral.PaxDrillTower;
import entity.neutral.PrunamHarvester;
import game.ActivesGrid;
import shared.NationInfo;

public class ScientistInfo extends NationInfo {

	@Override
	public Class<? extends MainBuilding> getMainBuilding() {
		return ScientistMainBuilding.class;
	}

	@Override
	public Class<? extends KeritMine> getKeritMine() {
		return ScientistKeritMine.class;
	}

	@Override
	public Class<? extends PaxDrillTower> getPaxDrillTower() {
		return ScientistPaxDrillTower.class;
	}

	@Override
	public Class<? extends ArcanumMine> getArcanumMine() {
		return ScientistArcanumMine.class;
	}

	@Override
	public Class<? extends PrunamHarvester> getPrunamHarvester() {
		return ScientistPrunamHarvester.class;
	}

	@Override
	public void setupActives(ActivesGrid grid) {
		grid.addActive(1, 1, Building.SetTargetActive.class, false);
		grid.addBuildMineActive(5, 3, Commander.class, false);
	}

}
