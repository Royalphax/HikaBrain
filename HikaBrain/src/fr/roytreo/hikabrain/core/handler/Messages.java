package fr.roytreo.hikabrain.core.handler;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.arena.Arena;

public enum Messages {
	
	GAME_PREFIX(ChatColor.GRAY + "[" + ChatColor.GOLD + "HikaBrain" + ChatColor.GRAY + "]"),
	
	INVENTORY_ORDERING_ITEM_NAME("§nOrdering by ..."),
	INVENTORY_ORDERING_ITEM_INSTRUCTIONS("§eLeft-Click: switch order\n§eRight-Click: increasing/descending"),
	INVENTORY_CLOSE_ITEM_NAME("§cClose"),
	INVENTORY_ARENA_ITEM_NAME("&f&l&n%ARENA_NAME%"),
	INVENTORY_ARENA_ITEM_LORE_WAITING("\n&7Players: &e&l%PLAYERS_IN_ARENA%§f / §e§l%MAX_PLAYERS_IN_ARENA%\n&aWaiting for players...\n\n§d§n» Click to join"),
	INVENTORY_ARENA_ITEM_LORE_STARTING("\n&7Players: &e&l%PLAYERS_IN_ARENA%§f / §e§l%MAX_PLAYERS_IN_ARENA%\n&2Starting...\n\n&e• &e6Starting ..."),
	INVENTORY_ARENA_ITEM_LORE_INGAME("\n&7Players: &e&l%PLAYERS_IN_ARENA%§f / §e§l%MAX_PLAYERS_IN_ARENA%\n&7Score: &9&l%BLUE_SCORE% &f&l- &c&l%RED_SCORE%\n\n&c✖ &4Ingame ..."),
	INVENTORY_ARENA_ITEM_LORE_ENDING("\n&7Players: &e&l%PLAYERS_IN_ARENA%§f / §e§l%MAX_PLAYERS_IN_ARENA%\n&7Score: &9&l%BLUE_SCORE% &f&l- &c&l%RED_SCORE%\n\n&e☠ &6Ending ..."),

	INVENTORY_NEXT_PAGE("Next slide"),
	INVENTORY_PREVIOUS_PAGE("Previous slide"),
	
	ORDERING_BY_DEFAULT("Default"),
	ORDERING_BY_NAME("Name"),
	ORDERING_BY_GAME_STATE("Game State"),
	ORDERING_BY_PLAYERS("Players"),
	
	TEAM_RED_NAME("Red Team"),
	TEAM_BLUE_NAME("Blue Team"),
	TEAM_RED_WON("&eTeam §c§lRed won &f- &aCongratulations"),
	TEAM_BLUE_WON("&eTeam §9§lBlue won &f- &aCongratulations"),
	TEAM_RED_SCORED("&c&l%PLAYER_NAME% &escored!"),
	TEAM_BLUE_SCORED("&9&l%PLAYER_NAME% &escored!"),
	
	PLAYER_CANT_BREAK_ARENA_WALLS("&cYou can't break arena's wall blocks !"),

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
	
	private static EnumMap<Messages, String> messages = new EnumMap<>(Messages.class);
	
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
		
		if (Arena.isPlayerInArena(player)) {
			Arena arena = Arena.getPlayerArena(player);
			message = message.replaceAll("%ARENA_NAME%", arena.getDisplayName());
			message = message.replaceAll("%BLUE_SCORE%", Integer.toString(arena.getBlueScore()));
			message = message.replaceAll("%RED_SCORE%", Integer.toString(arena.getRedScore()));
			message = message.replaceAll("%PLAYERS_IN_ARENA%", Integer.toString(arena.getPlayers().size()));
			message = message.replaceAll("%MAX_PLAYERS_IN_ARENA%", Integer.toString(arena.getMaxPlayers()));
		}
		
		player.sendMessage(HikaBrainPlugin.PREFIX + message);
	}
	
	public String getMessage() {
		return (messages.containsKey(this) ? messages.get(this) : this.value);
	}
	
	public String getMessage(Arena arena) {
		String message = (messages.containsKey(this) ? messages.get(this) : this.value);
		message = message.replaceAll("%ARENA_NAME%", arena.getDisplayName());
		message = message.replaceAll("%BLUE_SCORE%", Integer.toString(arena.getBlueScore()));
		message = message.replaceAll("%RED_SCORE%", Integer.toString(arena.getRedScore()));
		message = message.replaceAll("%PLAYERS_IN_ARENA%", Integer.toString(arena.getPlayers().size()));
		message = message.replaceAll("%MAX_PLAYERS_IN_ARENA%", Integer.toString(arena.getMaxPlayers()));
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
					new Exception(e).register(HikaBrainPlugin.getInstance(), true);
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
				new Exception(e).register(HikaBrainPlugin.getInstance(), true);
			}
	    	
	    	String game_prefix = config.getString(GAME_PREFIX.toString().toLowerCase().replace('_', '-'));
	    	HikaBrainPlugin.updatePrefix(ChatColor.translateAlternateColorCodes('&', game_prefix));
		}
	}
}
