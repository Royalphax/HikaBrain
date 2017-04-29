package fr.roytreo.hikabrain.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;

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
					return true;
				} else if (args[0].equalsIgnoreCase("delete")) {
					return true;
				} else if (args[0].equalsIgnoreCase("save")) {
					return true;
				} 
			}
			if (args[0].equalsIgnoreCase("list")) {
				return true;
			} else if (args[0].equalsIgnoreCase("join")) {
				return true;
			} else if (args[0].equalsIgnoreCase("leave")) {
				return true;
			} 
		}
		return false;
	}
}
