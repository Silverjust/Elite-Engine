package main.appdata;

public interface appdataInfos {

	static String nameOfFolder = "BattleOfOrion";
	static String path = System.getProperty("user.home").replace("\\", "/")
	+ "/AppData/Roaming/" + nameOfFolder + "/";

}