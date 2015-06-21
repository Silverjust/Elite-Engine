package entity.humans;

import entity.MainBuilding;
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
	public Class<? extends MainBuilding> getPaxDrillTower() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends MainBuilding> getArcanumMine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends MainBuilding> getPrunamHarvester() {
		// TODO Auto-generated method stub
		return null;
	}

}
