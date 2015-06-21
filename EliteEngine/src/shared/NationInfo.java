package shared;

import entity.MainBuilding;
import entity.neutral.KeritMine;

public abstract class NationInfo {
	public abstract Class<? extends MainBuilding> getMainBuilding();

	public abstract Class<? extends KeritMine> getKeritMine();

	public abstract Class<? extends MainBuilding> getPaxDrillTower();

	public abstract Class<? extends MainBuilding> getArcanumMine();

	public abstract Class<? extends MainBuilding> getPrunamHarvester();
}
