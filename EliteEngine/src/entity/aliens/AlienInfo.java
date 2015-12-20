package entity.aliens;

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

public class AlienInfo extends NationInfo {

	@Override
	public Class<? extends MainBuilding> getMainBuilding() {
		return AlienMainBuilding.class;
	}

	@Override
	public Class<? extends KeritMine> getKeritMine() {
		return AlienKeritMine.class;
	}

	@Override
	public Class<? extends PaxDrillTower> getPaxDrillTower() {
		return AlienPaxDrillTower.class;
	}

	@Override
	public Class<? extends ArcanumMine> getArcanumMine() {
		return AlienArcanumMine.class;
	}

	@Override
	public Class<? extends PrunamHarvester> getPrunamHarvester() {
		return AlienPrunamHarvester.class;
	}

	public void setupActives(ActivesGrid grid, ActivesGridHandler handler) {
		grid.addActive(1, 1, Unit.AttackActive.class);
		grid.addActive(2, 1, Unit.WalkActive.class);
		grid.addActive(3, 1, Unit.StopActive.class);

		grid.addActive(3, 2, Ticul.Flash.class);

		grid.addActive(1, 1, Building.SetTargetActive.class);
		grid.addBuildActive(5, 1, Commander.class, SpawnTower.class);
		grid.addBuildActive(4, 3, Commander.class, AlienKaserne.class);
		grid.addBuildActive(4, 2, AlienKaserne.class,
				AlienKaserneArcanum.class);
		grid.addBuildActive(4, 1, AlienKaserne.class, AlienKasernePrunam.class);
		grid.addActive(5, 3, BuildMineActive.class, Commander.class,
				getKeritMine());

		grid.addTrainActive(1, 3, AlienKaserne.class, Ticul.class);
		grid.addTrainActive(2, 3, AlienKaserne.class, Brux.class);
		grid.addTrainActive(3, 3, AlienKaserne.class, Valcyrix.class);
		grid.addTrainActive(2, 2, AlienKaserneArcanum.class, Colum.class);
		grid.addTrainActive(3, 2, AlienKaserneArcanum.class, Arol.class);
		grid.addTrainActive(2, 1, AlienKasernePrunam.class, Rug.class);
		grid.addTrainActive(3, 1, AlienKasernePrunam.class, Ker.class);
	}
}
