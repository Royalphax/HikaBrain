package fr.roytreo.hikabrain.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.arena.Arena;
import fr.roytreo.hikabrain.core.gui.GuiArena;
import fr.roytreo.hikabrain.core.gui.GuiArenaList;
import fr.roytreo.hikabrain.core.gui.GuiArenaList.GuiAction;
import fr.roytreo.hikabrain.core.manager.GuiManager;
import fr.roytreo.hikabrain.core.version.AAnvilGUI;
import net.md_5.bungee.api.ChatColor;

public class HikaBrainCommand implements CommandExecutor {

	public HikaBrainPlugin plugin;

	public HikaBrainCommand(HikaBrainPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Please connect on the server, then use this command again.");
			return true;
		}
		Player player = (Player) sender;
		if (args.length > 0) {
			if (player.isOp() || player.hasPermission("hikabrain.admin")) {
				if (args[0].equalsIgnoreCase("create")) {
					this.plugin.versionManager.newAnvilGUI(player, this.plugin, new AAnvilGUI.AnvilClickEventHandler() {
			            @Override
			            public void onAnvilClick(AAnvilGUI.AnvilClickEvent event) {
			                if (event.getSlot() == AAnvilGUI.AnvilSlot.OUTPUT && event.getName() != null) {
			                    event.setWillClose(true);
			                    event.setWillDestroy(true);
			                    String output = event.getName();
			                    new BukkitRunnable() {
			                    	public void run() {
			                    		GuiManager.openGui(plugin, new GuiArena(plugin, player, output));
			                    	}
			                    }.runTaskLater(plugin, 20);
			                } else {
			                    event.setWillClose(false);
			                    event.setWillDestroy(false);
			                }
			            }
			        }, "Arena Name", "", ChatColor.GRAY + "Give a name to your arena.").open();
					return true;
				} else if (args[0].equalsIgnoreCase("delete")) {
					GuiManager.openGui(plugin, new GuiArenaList(plugin, player, GuiAction.DELETE));
					return true;
				} else if (args[0].equalsIgnoreCase("edit")) {
					GuiManager.openGui(plugin, new GuiArenaList(plugin, player, GuiAction.EDIT));
					return true;
				} else if (args[0].equalsIgnoreCase("tp")) {
					GuiManager.openGui(plugin, new GuiArenaList(plugin, player, GuiAction.TELEPORT));
					return true;
				} 
			}
			if (args[0].equalsIgnoreCase("join")) {
				GuiManager.openGui(plugin, new GuiArenaList(plugin, player, GuiAction.JOIN));
				return true;
			} else if (args[0].equalsIgnoreCase("leave")) {
				if (Arena.isPlayerInArena(player)) {
					Arena arena = Arena.getPlayerArena(player);
					arena.quit(player);
				}
				return true;
			} 
		} else {
			player.sendMessage("§6§m+----------+§r §3§l§oHika§b§l§oBrain §6§m+----------+");
			if (player.isOp() || player.hasPermission("hikabrain.admin")) {
				player.sendMessage("/hb create §e§l» §cCreate a new arena");
				player.sendMessage("/hb delete §e§l» §cDelete an arena");
				player.sendMessage("/hb edit §e§l» §cEdit an arena");
				player.sendMessage("/hb tp §e§l» §cTeleport to an arena");
				player.sendMessage("");
			}
			player.sendMessage("/hb join §e§l» §aOpen a GUI to join an arena");
			player.sendMessage("/hb leave §e§l» §aQuit your current arena");
			player.sendMessage("§6§m+----------------------------------+");
		}
		return false;
	}
}
