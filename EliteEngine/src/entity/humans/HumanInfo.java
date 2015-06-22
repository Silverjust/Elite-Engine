package entity.humans;

import entity.MainBuilding;
import entity.neutral.ArcanumMine;
import entity.neutral.KeritMine;
import entity.neutral.PaxDrillTower;
import entity.neutral.PrunamHarvester;
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

}
