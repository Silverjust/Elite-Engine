package entity.aliens;

import entity.MainBuilding;
import entity.neutral.ArcanumMine;
import entity.neutral.KeritMine;
import entity.neutral.PaxDrillTower;
import entity.neutral.PrunamHarvester;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends ArcanumMine> getArcanumMine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends PrunamHarvester> getPrunamHarvester() {
		// TODO Auto-generated method stub
		return null;
	}

}
