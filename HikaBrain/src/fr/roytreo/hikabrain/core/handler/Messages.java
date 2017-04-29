package fr.roytreo.hikabrain.core.handler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.manager.ArenaManager;
import fr.roytreo.hikabrain.core.util.Utils;

public enum Messages {

	PLAYER_JOIN_GAME("&a%PLAYER_NAME% &7join the game ! &e(%PLAYERS_IN_ARENA%/%ARENA_MAX_PLAYERS%)"),
	PLAYER_QUIT_GAME("&a%PLAYER_NAME% &7left the game ! &e(%PLAYERS_IN_ARENA%/%ARENA_MAX_PLAYERS%)"),
	TEAM_BLUE_ITEM_NAME("&9Join Blue"),
	TEAM_RED_ITEM_NAME("&cJoin Red"),
	LEAVE_GAME_ITEM_NAME("&6Leave"),
	TEAM_RED_JOIN("&7You join the &cRed team&7."),
	TEAM_BLUE_JOIN("&7You join the &9Blue team&7."),
	TEAM_ALREADY_IN_IT("&cYou are already in this team."),
	TEAM_FULL("&cThis team is full."),
	LEAVE_GAME("&cYou left the game !");
	
	private static HashMap<Messages, String> messages = new HashMap<>();
	
	private String value;
	private Messages(String value) {
		this.value = value;
	}
	
	public void sendMessage(Player player, Player target) {
		target = (target == null ? player : target);
		
		String message = (messages.containsKey(this) ? messages.get(this) : this.value);
		message.replaceAll("%PLAYER_NAME%", target.getName());
		message.replaceAll("%PLAYER_DISPLAY_NAME%", target.getDisplayName());
		message.replaceAll("%PLAYER_CUSTOM_NAME%", target.getCustomName());
		
		if (ArenaManager.isPlayerInArena(player)) {
			ArenaManager arena = ArenaManager.getPlayerArena(player);
			message.replaceAll("%ARENA_NAME%", arena.getDisplayName());
			message.replaceAll("%BLUE_SCORE%", arena.getBlueScore() + "");
			message.replaceAll("%RED_SCORE%", arena.getRedScore() + "");
			message.replaceAll("%PLAYERS_IN_ARENA%", arena.getRedScore() + "");
		}
		
		player.sendMessage(HikaBrainPlugin.PREFIX + message);
	}
	
	public String getMessage() {
		return (messages.containsKey(this) ? messages.get(this) : this.value);
	}
	
	public static class FileManager {
		
		public void init(HikaBrainPlugin instance) {
			File file = new File(instance.getDataFolder(), "messages.yml");
			
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					Utils.registerException(e, true);
				}
			}
			
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			for (Messages m : Messages.values())
	    	{
				String key = config.getString(m.toString().toLowerCase().replace('_', '-'));
	    		if (key != null)
	    		{
	    			messages.put(m, ChatColor.translateAlternateColorCodes('&', key));
	    		} else {
	    			config.set(key, m.value.replaceAll("§", "&"));
	    			messages.put(m, ChatColor.translateAlternateColorCodes('&', m.value));
	    		}
	    	}
	    	try {
				config.save(file);
			} catch (IOException e) {
				Utils.registerException(e, true);
			}
		}
	}
}
