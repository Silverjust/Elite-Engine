package entity.scientists;

import entity.Building;
import entity.Commander;
import entity.MainBuilding;
import entity.Unit;
import entity.neutral.ArcanumMine;
import entity.neutral.KeritMine;
import entity.neutral.PaxDrillTower;
import entity.neutral.PrunamHarvester;
import entity.scientists.ScientistKaserne.EquipActive;
import game.ActivesGrid;
import game.ActivesGridHandler;
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
	public void setupActives(ActivesGrid grid, ActivesGridHandler handler) {
		grid.addActive(1, 1, Unit.AttackActive.class);
		grid.addActive(2, 1, Unit.WalkActive.class);
		grid.addActive(3, 1, Unit.StopActive.class);

		grid.addActive(1, 2, AirshipGuineaPig.AnchorActive.class);
		grid.addActive(4, 1, ChemLab.ScientistWallActive.class);
		grid.addActive(4, 2, BioLab.CreateSwampActive.class);
		grid.addActive(4, 3, PhysicsLab.TeleportActive.class);

		grid.addActive(1, 1, Building.SetTargetActive.class);
		grid.addActive(5, 3, BuildMineActive.class, Commander.class,
				getKeritMine());
		grid.addBuildActive(5, 2, Commander.class, ScientistKaserne.class);
		grid.addTrainActive(1, 3, ScientistKaserne.class, GuineaPig.class);
		grid.addTrainActive(4, 1, ScientistKaserne.class, ChemLab.class);
		grid.addTrainActive(4, 2, ScientistKaserne.class, BioLab.class);
		grid.addTrainActive(4, 3, ScientistKaserne.class, PhysicsLab.class);
		grid.addActive(2, 3, EquipActive.class, PhysicsLab.class,
				ShieldGuineaPig.class);
		grid.addActive(3, 3, EquipActive.class, PhysicsLab.class,
				RailgunGuineaPig.class);
		grid.addActive(2, 2, EquipActive.class, BioLab.class, Cell.class);
		grid.addActive(3, 2, EquipActive.class, BioLab.class,
				SpawnerGuineaPig.class);
		grid.addActive(3, 1, EquipActive.class, ChemLab.class,
				AirshipGuineaPig.class);
		grid.addActive(2, 1, EquipActive.class, ChemLab.class,
				RocketGuineaPig.class);
	}
}
