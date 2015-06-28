package entity.aliens;

import entity.Building;
import entity.Commander;
import entity.MainBuilding;
import entity.neutral.ArcanumMine;
import entity.neutral.KeritMine;
import entity.neutral.PaxDrillTower;
import entity.neutral.PrunamHarvester;
import game.ActivesGrid;
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

	public void setupActives(ActivesGrid grid) {
		grid.addActive(3, 2, Ticul.Flash.class, true);

		grid.addActive(1, 1, Building.SetTargetActive.class, false);
		grid.addBuildActive(5, 1, AlienMainBuilding.class, ThornTower.class, false);
		grid.addBuildActive(4, 3, AlienMainBuilding.class, AlienKaserne.class, false);
		grid.addBuildActive(4, 2, AlienKaserne.class, AlienKaserneArcanum.class,
				false);
		grid.addBuildActive(4, 1, AlienKaserne.class, AlienKasernePrunam.class,
				false);
		grid.addActive(5, 3, BuildMineActive.class, Commander.class,
				getKeritMine(), false);

		grid.addTrainActive(1, 3, AlienKaserne.class, Ticul.class, false);
		grid.addTrainActive(2, 3, AlienKaserne.class, Brux.class, false);
		grid.addTrainActive(3, 3, AlienKaserne.class, Valcyrix.class, false);
		grid.addTrainActive(2, 2, AlienKaserneArcanum.class, Colum.class, false);
		grid.addTrainActive(3, 2, AlienKaserneArcanum.class, Arol.class, false);
		grid.addTrainActive(2, 1, AlienKasernePrunam.class, Rug.class, false);
		grid.addTrainActive(3, 1, AlienKasernePrunam.class, Ker.class, false);
	}
}
