package entity.campain;

import entity.Building;
import entity.Commander;
import entity.Entity;
import entity.Unit;
import entity.neutral.Kerit;
import entity.neutral.Tutorial;
import entity.robots.M1N1B0T;
import entity.robots.RobotsDepot;
import entity.robots.RobotsInfo;
import entity.robots.RobotsKaserne;
import entity.robots.RobotsKeritMine;
import game.ActivesGrid;
import game.AimHandler;
import game.HUD;
import game.Map;
import game.aim.BuildAim;
import game.aim.MineAim;
import game.aim.MineAim.BuildMineActive;
import main.Setting;
import main.appdata.SettingHandler;
import main.preGame.MainPreGame;
import shared.Helper;
import shared.Helper.Timer;
import shared.Nation;
import shared.ref;

public class MapRobots1 extends CampainMapCode {
	public MapRobots1(Map map) {
		super(map);
	}

	@Override
	public void setup() {
		MainPreGame.addPlayer("Scientists", Nation.SCIENTISTS);
	}

	String name = "AWI.ai";
	private Entity kerit1;
	private Entity kerit2;

	@Override
	public void onGameStart() {
		mb = ref.player.mainBuilding;
		HUD.activesGrid.removeActives();
		ActivesGrid grid = HUD.activesGrid.baseGrid;
		grid.addActive(1, 1, Unit.AttackActive.class);
		grid.addActive(2, 1, Unit.WalkActive.class);
		grid.addActive(3, 1, Unit.StopActive.class);
		grid.addActive(5, 3, BuildMineActive.class, Commander.class, new RobotsInfo().getKeritMine());
		grid.addActive(1, 1, Building.SetTargetActive.class);
		ref.updater.selectionChanged = true;

		ref.updater.send("<spawn Tutorial " + ref.player.getUser().ip + " 0 0 " + mb.x + " " + mb.y);
		ref.updater.send("<give " + ref.player.getUser().ip + " kerit " + 150);
	}

	@Override
	public void onEntitySpawn(Entity e) {
		if (e instanceof Tutorial)
			tutorial = e;
		else if (e instanceof Kerit && e.isInRange(mb.x, mb.y, mb.commandRange()))
			kerit1 = e;
		else if (e instanceof Kerit)
			kerit2 = e;
	}

	@Override
	public void update() {
		i = 0;
		if (isNext()) {
			HUD.chat.println(name, "have to find and select our Mainbuilding");
		}
		nextIf(ref.player.mainBuilding.isSelected);
		if (isNext()) {
			HUD.chat.println(name, "have to collect kerit");
			HUD.chat.println(name, "should " + getActiveBindingText(5, 3) + " to choose the Mine");
		}
		nextIf(AimHandler.getAim() instanceof MineAim);
		if (isNext()) {
			HUD.chat.println(name, "should " + Setting.getKeyName(SettingHandler.setting.mouseCommand)
					+ " on the rock to build a Mine");
			MarkerTo(kerit1);
		}
		nextIf(Helper.listContainsInstancesOf(RobotsKeritMine.class, ref.updater.entities) == 1);
		if (isNext()) {
			HUD.chat.println(name, "need more");
			HUD.chat.println(name, "detected some kerit here, but it is out of range");
			HUD.chat.println(name, "started design.ai to create a robotfactory to get there");
			mb.progress = new Timer(20000);
			mb.progress.startCooldown();
			MarkerTo(kerit2);
		}
		nextIf(mb.progress != null && mb.progress.isNotOnCooldown());
		if (isNext()) {
			ActivesGrid grid = HUD.activesGrid.baseGrid;
			grid.addActive(4, 2, M1N1B0T.BuildDepotActive.class);
			grid.addBuildActive(4, 3, Commander.class, RobotsKaserne.class);
			grid.addTrainActive(1, 3, RobotsKaserne.class, M1N1B0T.class);
			ref.updater.selectionChanged = true;
			ref.updater.keepGrid = true;

		}
		nextIf(ref.player.kerit >= 400);
		if (isNext()) {
			HUD.chat.println(name, "should " + getActiveBindingText(4, 3) + " to choose the new Robotfactory");
		}
		nextIf(AimHandler.getAim() instanceof BuildAim
				&& ((BuildAim) AimHandler.getAim()).getToBuild().equals(RobotsKaserne.class));
		if (isNext()) {
			MarkerTo(mb.x - 55, mb.y);
			HUD.chat.println(name,
					"should " + Setting.getKeyName(SettingHandler.setting.mouseCommand) + " near the Mainbuilding");
		}
		nextIf(Helper.listContainsInstancesOf(RobotsKaserne.class, ref.updater.entities) >= 1);
		if (isNext()) {
			HUD.chat.println(name, "should " + getActiveBindingText(1, 3) + " to produce the new robot:M1N1B0T");
		}
		nextIf(Helper.listContainsInstancesOf(M1N1B0T.class, ref.updater.entities) >= 1);
		if (isNext()) {
			HUD.chat.println(name, "should " + Setting.getKeyName(SettingHandler.setting.mouseCommand)
					+ " to send the M1N1B0T to the kerit");
			MarkerTo(kerit2.x + 20, kerit2.y);
		}
		{
			boolean b = false;
			for (Entity e : ref.updater.entities)
				if (e instanceof M1N1B0T && kerit2.isInRange(e.x, e.y, new RobotsDepot(null).commandRange()))
					b = true;
			nextIf(b);
		}
		if (isNext()) {
			HUD.chat.println(name, "should " + getActiveBindingText(4, 2) + " to create a depot");
		}
		{
			boolean b = false;
			for (Entity e : ref.updater.entities)
				if (e instanceof RobotsDepot && kerit2.isInRange(e.x, e.y, ((RobotsDepot) e).commandRange()))
					b = true;
			nextIf(b);
		}
		if (isNext()) {
			HUD.chat.println(name, "should " + getActiveBindingText(5, 3) + " to choose the Mine");
		}
		nextIf(AimHandler.getAim() instanceof MineAim);
		if (isNext()) {
			HUD.chat.println(name, "should " + Setting.getKeyName(SettingHandler.setting.mouseCommand)
					+ " on the rock to build a Mine");
			MarkerTo(kerit2);
		}
	}
}
