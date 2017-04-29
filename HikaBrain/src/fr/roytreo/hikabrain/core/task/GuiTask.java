package fr.roytreo.hikabrain.core.task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.event.gui.GuiUpdateEvent;
import fr.roytreo.hikabrain.core.gui.base.GuiScreen;

public class GuiTask extends BukkitRunnable {

	private final HikaBrainPlugin plugin;
	private final Player player;
	private final GuiScreen gui;

	public GuiTask(HikaBrainPlugin plugin, Player player, GuiScreen gui) {
		this.plugin = plugin;
		this.player = player;
		this.gui = gui;
		gui.open();
	}

	@Override
	public void run() {

		if (!gui.getInventory().getViewers().contains(this.player)) {
			this.cancel();
			return;
		}

		this.plugin.getServer().getPluginManager().callEvent(new GuiUpdateEvent(this.player, this.gui, false));
		this.gui.drawScreen();
	}
}