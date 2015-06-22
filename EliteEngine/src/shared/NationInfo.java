package shared;

import entity.MainBuilding;
import entity.aliens.AlienArcanumMine;
import entity.aliens.AlienPaxDrillTower;
import entity.aliens.AlienPrunamHarvester;
import entity.neutral.KeritMine;

public abstract class NationInfo {
	public abstract Class<? extends MainBuilding> getMainBuilding();

	public abstract Class<? extends KeritMine> getKeritMine();

	public abstract Class<? extends AlienPaxDrillTower> getPaxDrillTower();

	public abstract Class<? extends AlienArcanumMine> getArcanumMine();

	public abstract Class<? extends AlienPrunamHarvester> getPrunamHarvester();
}
