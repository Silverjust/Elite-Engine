package shared;

public enum Nation {

	ALIENS, AHNEN, ROBOTS, SCIENTISTS, HUMANS, NEUTRAL;
	
	public String toString() {
		String S = null;
		switch (this) {
		case AHNEN:
			S = "ahnen";
			break;
		case ALIENS:
			S = "aliens";
			break;
		case ROBOTS:
			S = "robots";
			break;
		case SCIENTISTS:
			S = "scientists";
			break;
		case HUMANS:
			S = "humans";
			break;
		case NEUTRAL:
			S = "neutral";
			break;
		}
		return S;
	}
	
	public static Nation fromNumber(int i) {
		Nation nation = null;
		switch (i) {
		case 0:
			nation = Nation.ALIENS;
			break;
		case 1:
			nation = Nation.ROBOTS;
			break;
		case 2:
			nation = Nation.HUMANS;
			break;
		case 3:
			nation = Nation.SCIENTISTS;
			break;
		case 4:
			nation = Nation.AHNEN;
			break;
		default:
			break;
		}
		return nation;
	}

}
