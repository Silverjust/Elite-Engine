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
	public Class<? extends AlienPaxDrillTower> getPaxDrillTower() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends AlienArcanumMine> getArcanumMine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends AlienPrunamHarvester> getPrunamHarvester() {
		// TODO Auto-generated method stub
		return null;
	}

}
