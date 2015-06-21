package entity.aliens;

import entity.MainBuilding;
import entity.neutral.KeritMine;
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
