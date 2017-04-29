package fr.roytreo.hikabrain.core.manager;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.handler.Cuboid;
import fr.roytreo.hikabrain.core.handler.Messages;
import fr.roytreo.hikabrain.core.handler.Sounds;
import fr.roytreo.hikabrain.core.util.Utils;
import net.md_5.bungee.api.ChatColor;

public class ArenaManager {

	private static final String PREFIX = HikaBrainPlugin.PREFIX;
	private static ArrayList<ArenaManager> arenas;
	
	static {
		arenas = new ArrayList<>();
	}
	
	public HashMap<Player, Team> players = new HashMap<>();
	private ArrayList<Block> signs = new ArrayList<>();
	
	private ArrayList<Location> redSpawns = new ArrayList<>();
	private ArrayList<Location> blueSpawns = new ArrayList<>();
	
	private ArrayList<Block> redGoals = new ArrayList<>();
	private ArrayList<Block> blueGoals = new ArrayList<>();
	
	private int redScore = 0;
	private int blueScore = 0;
	private int maxPlayers = 2;
	
	private Location lobby;
	
	private HikaBrainPlugin plugin;
	private PlayerRestorer playerRestorer;
	private String displayName;
	private String rawName;
	private FileManager fileManager;
	private Cuboid cubo;
	
	public ArenaManager(HikaBrainPlugin plugin, String name, Location pos1, Location pos2, Player creator) {
		this.displayName = name;
		this.rawName = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("[+.^:,%$@*§]","").replaceAll("/", "").replaceAll("\\\\", "").trim().replaceAll(" ", "_").toLowerCase();
		
		if (alreadyExistArena()) {
			creator.sendMessage(PREFIX + ChatColor.RED + "An arena with that name already exists :(");
			return;
		}
		
		this.plugin = plugin;
		this.playerRestorer = new PlayerRestorer();
		this.fileManager = new FileManager(this.rawName);
		this.cubo = new Cuboid(pos1, pos2);
		
		for (Block block : this.cubo.getWalls()) {
			Utils.playSoundAll(block.getLocation(), Sounds.STEP_STONE, 1.0f, 0.0f);
			block.setType(Material.OBSIDIAN);
		}
		
		arenas.add(this);
	}
	
	public ArenaManager(HikaBrainPlugin plugin, File fileToLoad) {
		
		this.plugin = plugin;
		this.playerRestorer = new PlayerRestorer();
		this.fileManager = new FileManager(fileToLoad);
		
		arenas.add(this);
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public int getRedScore() {
		return this.redScore;
	}
	
	public int getBlueScore() {
		return this.blueScore;
	}
	
	public ArrayList<Block> getRedGoals() {
		return this.redGoals;
	}
	
	public ArrayList<Block> getBlueGoals() {
		return this.blueGoals;
	}
	
	public ArrayList<Location> getRedSpawns() {
		return this.redSpawns;
	}
	
	public ArrayList<Location> getBlueSpawns() {
		return this.blueSpawns;
	}
	
	public Set<Player> getPlayers() {
		return this.players.keySet();
	}
	
	public int getMaxPlayers() {
		return this.maxPlayers;
	}
	
	public void setMaxPlayers(int i) {
		this.maxPlayers = i;
	}
	
	public boolean join(Player player) {
		if (isPlayerInArena(player)) {
			return false;
		}
		this.playerRestorer.saveProperties(player);
		player.setMaxHealth(20.0);
		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.setExp(0.0f);
		player.setLevel(0);
		player.teleport(this.lobby);
		broadcastMessage(Messages.PLAYER_JOIN_GAME, player);
		
		this.players.put(player, Team.NONE);
		return true;
	}
	
	public boolean quit(Player player) {
		this.playerRestorer.restoreProperties(player);
		this.players.remove(player);
		
		broadcastMessage(Messages.PLAYER_QUIT_GAME, player);
		return true;
	}
	
	public void respawn(Player player) {
		player.setHealth(player.getMaxHealth());
		PlayerInventory inv = player.getInventory();
		inv.all(new ItemStack(Material.SANDSTONE, 64));
		player.setFallDistance(0.0f);
		if (getTeam(player) == Team.BLUE) {
			player.teleport(blueSpawns.get(getPlayerIndex(player)));
		}
		if (getTeam(player) == Team.RED) {
			player.teleport(redSpawns.get(getPlayerIndex(player)));
		}
	}
	
	public boolean alreadyExistArena() {
		for (ArenaManager arena : arenas)
			if (arena.rawName.equals(this.rawName))
				return true;
		return false;
	}
	
	public void broadcastMessage(Messages message, Player target) {
		for (Player players : players.keySet())
			message.sendMessage(players, target);
	}
	
	public void saveArena() {
		this.fileManager.save();
	}
	
	private class FileManager {
		
		private File file;
		private FileConfiguration config;
		
		public FileManager(String fileName) {
			this.file = new File(plugin.getDataFolder(), fileName + ".yml");
			
			if (!this.file.exists()) {
				try {
					this.file.createNewFile();
				} catch (IOException e) {
					Utils.registerException(e, true);
				}
			}
			
			this.config = YamlConfiguration.loadConfiguration(this.file);
		}
		
		public FileManager(File fileToLoad) {
			this.file = fileToLoad;
			try {
				this.config = YamlConfiguration.loadConfiguration(this.file);
				
				displayName = this.config.getString("name");
				rawName = Normalizer.normalize(displayName, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("[+.^:,%$@*§]","").replaceAll("/", "").replaceAll("\\\\", "").trim().replaceAll(" ", "_").toLowerCase();
				lobby = Utils.toLocation(this.config.getString("lobby"));
				cubo = new Cuboid(Utils.toLocation(this.config.getString("pos1")), Utils.toLocation(this.config.getString("pos2")));
				
			} catch (Exception ex) {
				this.file.delete();
				plugin.getLogger().warning("Arena file \"" + fileToLoad.getName() + "\" is corrupted. Deleting it... Done!");
			}
			
		}
		
		public boolean save() {
			int i;
			this.config.set("name", displayName);
			this.config.set("lobby", Utils.toString(lobby));
			this.config.set("pos1", Utils.toString(new Location(cubo.getWorld(), cubo.getXmax(), cubo.getYmax(), cubo.getZmax())));
			this.config.set("pos2", Utils.toString(new Location(cubo.getWorld(), cubo.getXmin(), cubo.getYmin(), cubo.getZmin())));
			for (i = 0; i < signs.size(); i++)
				this.config.set("signs." + i, Utils.toString(signs.get(i).getLocation()));
			for (i = 0; i < redSpawns.size(); i++)
				this.config.set("red-spawns." + i, Utils.toString(redSpawns.get(i)));
			for (i = 0; i < blueSpawns.size(); i++)
				this.config.set("blue-spawns." + i, Utils.toString(blueSpawns.get(i)));
			for (i = 0; i < redGoals.size(); i++)
				this.config.set("red-goals." + i, Utils.toString(redGoals.get(i).getLocation()));
			for (i = 0; i < blueGoals.size(); i++)
				this.config.set("blue-goals." + i, Utils.toString(blueGoals.get(i).getLocation()));
			try {
				this.config.save(this.file);
				return true;
			} catch (IOException e) {
				Utils.registerException(e, true);
				return false;
			}
		}
	}
	
	public static enum Team {
		RED(),
		BLUE(),
		NONE();
	}
	
	public void setTeam(Player player, Team team) {
		if (players.containsKey(player))
			players.remove(player);
		players.put(player, team);
	}
	
	public Team getTeam(Player player) {
		return players.get(player);
	}
	
	public int getRedTeamSize() {
		int output = 0;
		for (Player player : players.keySet())
			if (players.get(player) == Team.RED)
				output++;
		return output;
	}
	
	public int getBlueTeamSize() {
		int output = 0;
		for (Player player : players.keySet())
			if (players.get(player) == Team.BLUE)
				output++;
		return output;
	}
	
	public int getPlayerIndex(Player player) {
		int output = 0;
		for (Player pla : players.keySet()) {
			if (!pla.getName().equals(player.getName())) {
				if (players.get(pla) == getTeam(player))
					output++;
				continue;
			}
			break;
		}
		return output;
	}
	
	private class PlayerRestorer {
		
		private final static String inventoryMetadata = "last_saved_inventory";
		private final static String locationMetadata = "last_saved_location";
		private final static String healthhungerMetadata = "last_saved_health_hunger";
		private final static String experienceMetadata = "last_saved_experience";
		
		public void saveProperties(Player player) {
			clearProperties(player);
			player.setMetadata(inventoryMetadata, new FixedMetadataValue(HikaBrainPlugin.getInstance(), player.getInventory().getContents()));
			player.setMetadata(locationMetadata, new FixedMetadataValue(HikaBrainPlugin.getInstance(), player.getLocation()));
			player.setMetadata(healthhungerMetadata, new FixedMetadataValue(HikaBrainPlugin.getInstance(), player.getHealth() + "_" + player.getMaxHealth() + "_" + player.getFoodLevel()));
			player.setMetadata(experienceMetadata, new FixedMetadataValue(HikaBrainPlugin.getInstance(), player.getExp() + "_" + player.getLevel()));
			player.getInventory().clear();
		}
		
		public void restoreProperties(Player player) {
			player.getInventory().clear();
			for (MetadataValue value : player.getMetadata(inventoryMetadata))
				if (value.getOwningPlugin().getName().equals(plugin.getName()))
					player.getInventory().setContents((ItemStack[]) value.value());
			for (MetadataValue value : player.getMetadata(locationMetadata))
				if (value.getOwningPlugin().getName().equals(plugin.getName()))
					player.teleport((Location) value.value());
			for (MetadataValue value : player.getMetadata(healthhungerMetadata))
				if (value.getOwningPlugin().getName().equals(plugin.getName())) {
					String[] split = value.value().toString().split("_");
					player.setMaxHealth(Double.parseDouble(split[1]));
					player.setHealth(Double.parseDouble(split[0]));
					player.setFoodLevel(Integer.parseInt(split[2]));
				}
			for (MetadataValue value : player.getMetadata(experienceMetadata))
				if (value.getOwningPlugin().getName().equals(plugin.getName())) {
					String[] split = value.value().toString().split("_");
					player.setExp(Float.parseFloat(split[0]));
					player.setLevel(Integer.parseInt(split[1]));
				}
			clearProperties(player);
		}
		
		public void clearProperties(Player player) {
			if (player.hasMetadata(inventoryMetadata))
				player.removeMetadata(inventoryMetadata, plugin);
			if (player.hasMetadata(locationMetadata))
				player.removeMetadata(locationMetadata, plugin);
			if (player.hasMetadata(healthhungerMetadata))
				player.removeMetadata(healthhungerMetadata, plugin);
			if (player.hasMetadata(experienceMetadata))
				player.removeMetadata(experienceMetadata, plugin);
		}
	}
	
	public static boolean isPlayerInArena(Player player) {
		return (getPlayerArena(player) != null);
	}
	
	public static ArenaManager getPlayerArena(Player player) {
		for (ArenaManager arena : arenas) 
			if (arena.players.containsKey(player))
				return arena;
		return null;
	}
	
	public static ArenaManager getArena(String name) {
		name = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("[+.^:,%$@*§]","").replaceAll("/", "").replaceAll("\\\\", "").trim().replaceAll(" ", "_").toLowerCase();
		
		for (ArenaManager arena : arenas) 
			if (arena.rawName.equals(name))
				return arena;
		return null;
	}

}
