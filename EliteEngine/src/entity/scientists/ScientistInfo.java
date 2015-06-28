package entity.scientists;

import entity.Building;
import entity.Commander;
import entity.MainBuilding;
import entity.neutral.ArcanumMine;
import entity.neutral.KeritMine;
import entity.neutral.PaxDrillTower;
import entity.neutral.PrunamHarvester;
import entity.scientists.GuineaPig.EquipActive;
import game.ActivesGrid;
import game.aim.MineAim.BuildMineActive;
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
		grid.addActive(5, 3, BuildMineActive.class, Commander.class,
				getKeritMine(), false);
		grid.addBuildActive(4, 3, Commander.class, ScientistKaserne.class,
				false);
		grid.addTrainActive(1, 3, ScientistKaserne.class, GuineaPig.class,
				false);
		grid.addActive(2, 3, EquipActive.class, GuineaPig.class,
				ShieldGuineaPig.class, true);
		grid.addActive(3, 3, EquipActive.class, GuineaPig.class,
				RailgunGuineaPig.class, true);
	}

}
