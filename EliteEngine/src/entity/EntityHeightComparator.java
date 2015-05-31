package entity;

import java.util.Comparator;

public class EntityHeightComparator implements Comparator<Entity> {

	@Override
	public int compare(Entity e1, Entity e2) {
		if (e1.y > e2.y) {
			return 1;
		} else if (e1.y < e2.y) {
			return -1;
		} else {
			return 0;
		}
	}
}
