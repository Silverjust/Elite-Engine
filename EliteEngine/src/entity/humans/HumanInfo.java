package entity.humans;

import entity.MainBuilding;
import entity.aliens.AlienArcanumMine;
import entity.aliens.AlienPaxDrillTower;
import entity.aliens.AlienPrunamHarvester;
import entity.neutral.KeritMine;
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
	public Class<? extends AlienPaxDrillTower> getPaxDrillTower() {return HumanPaxDrillTower.class;
	}

	@Override
	public Class<? extends AlienArcanumMine> getArcanumMine() {
		return HumanArcanumMine.class;
	}

	@Override
	public Class<? extends AlienPrunamHarvester> getPrunamHarvester() {
		return HumanPrunamHarvester.class;
	}

}
