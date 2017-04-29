package fr.roytreo.hikabrain.core.manager;

import java.util.HashMap;

import org.bukkit.entity.Player;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.gui.base.GuiScreen;
import fr.roytreo.hikabrain.core.task.GuiTask;

public class GuiManager {

	public static HashMap<String, Class<?>> openGuis = new HashMap<>();

	public static GuiScreen openGui(HikaBrainPlugin plugin, GuiScreen gui) {
		openPlayer(gui.getPlayer(), gui.getClass());
		if (gui.isUpdate())
			new GuiTask(plugin, gui.getPlayer(), gui).runTaskTimer(plugin, 0, 20);
		else {
			gui.open();
		}
		return gui;
	}

	@SuppressWarnings("rawtypes")
	public static void openPlayer(Player p, Class gui) {
		if (openGuis.containsKey(p.getName())) {
			openGuis.remove(p.getName());
			openGuis.put(p.getName(), gui);
		} else {
			openGuis.put(p.getName(), gui);
		}
	}

	public static void closePlayer(Player p) {
		if (openGuis.containsKey(p.getName())) {
			openGuis.remove(p.getName());
		}
	}

	public static boolean isPlayer(Player p) {
		if (openGuis.containsKey(p.getName()))
			return true;
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isOpened(Class clas) {
		for (Class cla : openGuis.values()) {
			if (cla.equals(clas))
				return true;
		}
		return false;
	}
}