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
	ALREADY_IN_GAME("&cYou are already in game."),
	SIGN_NOT_LINK_TO_ARENA("This sign isn't linked to any HikaBrain arena."),
	SIGN_HEADER("&6HikaBrain"),
	SIGN_LINE_2("&b%ARENA_NAME%"),
	SIGN_LINE_3_PLAYERS("&a%PLAYERS_IN_ARENA%/%MAX_PLAYERS_IN_ARENA%"),
	SIGN_LINE_3_SCORE("&c&l%RED_SCORE% &8&l- &9&l%BLUE_SCORE%"),
	SIGN_LINE_4("%ARENA_GAME_STATE%"),
	WAITING("&2➲ &a&lWaiting ..."),
	STARTING("&6• &e&lStarting ..."),
	INGAME("&4✖ &c&lIngame"),
	ENDING("&5☠ &d&lEnding ..."),
	LEAVE_GAME("&cYou left the game !");
	
	private static HashMap<Messages, String> messages = new HashMap<>();
	
	private String value;
	private Messages(String value) {
		this.value = value;
	}
	
	public void sendMessage(Player player, Player target) {
		target = (target == null ? player : target);
		
		String message = (messages.containsKey(this) ? messages.get(this) : this.value);
		message = message.replaceAll("%PLAYER_NAME%", target.getName());
		message = message.replaceAll("%PLAYER_DISPLAY_NAME%", target.getDisplayName());
		message = message.replaceAll("%PLAYER_CUSTOM_NAME%", target.getCustomName());
		
		if (ArenaManager.isPlayerInArena(player)) {
			ArenaManager arena = ArenaManager.getPlayerArena(player);
			message = message.replaceAll("%ARENA_NAME%", arena.getDisplayName());
			message = message.replaceAll("%BLUE_SCORE%", arena.getBlueScore() + "");
			message = message.replaceAll("%RED_SCORE%", arena.getRedScore() + "");
			message = message.replaceAll("%PLAYERS_IN_ARENA%", arena.getPlayers().size() + "");
			message = message.replaceAll("%MAX_PLAYERS_IN_ARENA%", arena.getMaxPlayers() + "");
		}
		
		player.sendMessage(HikaBrainPlugin.PREFIX + message);
	}
	
	public String getMessage() {
		return (messages.containsKey(this) ? messages.get(this) : this.value);
	}
	
	public String getMessage(ArenaManager arena) {
		String message = (messages.containsKey(this) ? messages.get(this) : this.value);
		message = message.replaceAll("%ARENA_NAME%", arena.getDisplayName());
		message = message.replaceAll("%BLUE_SCORE%", arena.getBlueScore() + "");
		message = message.replaceAll("%RED_SCORE%", arena.getRedScore() + "");
		message = message.replaceAll("%PLAYERS_IN_ARENA%", arena.getPlayers().size() + "");
		message = message.replaceAll("%MAX_PLAYERS_IN_ARENA%", arena.getMaxPlayers() + "");
		message = message.replaceAll("%ARENA_GAME_STATE%", arena.getGameState().getMessage() + "");
		
		return message;
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
