package entity;

import entity.animation.Ability;
import java.lang.reflect.Method;

import processing.core.PImage;
import shared.ref;

/**
 * Aktive Fähigkeit
 * */
public abstract class MultiCDActive extends Active {

	Method abilityGetter;
	Entity lowestCDEntity;

	public MultiCDActive(int x, int y, char n, PImage symbol) {
		super(x, y, n, symbol);
	}

	protected void setAbilityGetter(String name) {
		try {
			abilityGetter = clazz.getMethod(name, new Class[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		button.setCooldownState(1 - getCooldownPercent());
		if (isNotOnCooldown())
			button.setThisEnabled(true);
	}

	@Override
	public void startCooldown() {
		if (!isNotOnCooldown()) {
			searchEntity();
		}
	}

	private void searchEntity() {
		if (lowestCDEntity == null) {
			for (Entity e : ref.updater.selected)
				if (clazz.isAssignableFrom(e.getClass()))
					lowestCDEntity = e;
		}
		for (Entity e : ref.updater.selected) {
			if (clazz.isAssignableFrom(e.getClass())) {
				try {
					float f1 = ((Ability) abilityGetter.invoke(lowestCDEntity))
							.getCooldownPercent();
					float f2 = ((Ability) abilityGetter.invoke(e))
							.getCooldownPercent();
					if (f2 > f1) {
						lowestCDEntity = e;

					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	@Override
	public boolean isNotOnCooldown() {
		boolean b = false;
		if (lowestCDEntity == null)
			searchEntity();
		if (abilityGetter != null && lowestCDEntity != null) {
			try {
				b = ((Ability) abilityGetter.invoke(lowestCDEntity))
						.isNotOnCooldown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return b;
	}

	@Override
	public float getCooldownPercent() {
		float f = 0;
		if (lowestCDEntity != null && abilityGetter != null) {
			try {
				f = ((Ability) abilityGetter.invoke(lowestCDEntity))
						.getCooldownPercent();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return f > 1 || f < 0 ? 1 : f;
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (!ref.updater.selected.contains(lowestCDEntity))
			lowestCDEntity = null;
		searchEntity();
	}
}
