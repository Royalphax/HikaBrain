package fr.roytreo.hikabrain.core.arena;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.arena.event.ArenaCreatedEvent;
import fr.roytreo.hikabrain.core.arena.event.ArenaDeletedEvent;
import fr.roytreo.hikabrain.core.arena.event.ArenaLoadedEvent;
import fr.roytreo.hikabrain.core.arena.event.ArenaPlayerRespawnEvent;
import fr.roytreo.hikabrain.core.arena.event.PlayerJoinArenaEvent;
import fr.roytreo.hikabrain.core.arena.event.PlayerQuitArenaEvent;
import fr.roytreo.hikabrain.core.arena.event.PlayerSwitchTeamEvent;
import fr.roytreo.hikabrain.core.arena.event.UpdateScoreEvent;
import fr.roytreo.hikabrain.core.arena.event.UpdateSignEvent;
import fr.roytreo.hikabrain.core.exception.AlreadyExistArenaException;
import fr.roytreo.hikabrain.core.exception.InvalidCuboidException;
import fr.roytreo.hikabrain.core.exception.InvalidWinningScoreException;
import fr.roytreo.hikabrain.core.exception.InvalidNumberOfMaxPlayersException;
import fr.roytreo.hikabrain.core.exception.NotEnoughSpawnsException;
import fr.roytreo.hikabrain.core.gui.GuiArena;
import fr.roytreo.hikabrain.core.gui.GuiValidate;
import fr.roytreo.hikabrain.core.handler.Cuboid;
import fr.roytreo.hikabrain.core.handler.Exception;
import fr.roytreo.hikabrain.core.handler.Messages;
import fr.roytreo.hikabrain.core.handler.Particles;
import fr.roytreo.hikabrain.core.handler.Team;
import fr.roytreo.hikabrain.core.manager.GuiManager;
import fr.roytreo.hikabrain.core.util.Utils;
import net.md_5.bungee.api.ChatColor;

public class Arena extends Game {

	private static ArrayList<Arena> arenas;

	static {
		arenas = new ArrayList<>();
	}

	private HashMap<Player, Team> players = new HashMap<>();
	private ArrayList<Block> signs = new ArrayList<>();

	private ArrayList<Location> redSpawns = new ArrayList<>();
	private ArrayList<Location> blueSpawns = new ArrayList<>();

	private int maxPlayers = 2;
	private int winning_score = 5;

	private Location lobby;

	private String displayName;
	private String rawName;
	private FileManager fileManager;
	private Cuboid cubo;
	private boolean beeingEdited;

	public Arena(HikaBrainPlugin plugin, Player creator, String name, Location pos1, Location pos2) throws AlreadyExistArenaException, InvalidCuboidException, IllegalArgumentException {
		super(plugin);

		if (plugin == null || name == null || pos1 == null || pos2 == null || name.trim() == "")
			throw new IllegalArgumentException("Input arguments can't be null.");
		this.displayName = name;
		this.rawName = Utils.getRaw(name);

		if (alreadyExistArena())
			throw new AlreadyExistArenaException("An arena with that name already exists.");

		this.fileManager = new FileManager(this.rawName);
		this.cubo = new Cuboid(pos1, pos2);

		if ((this.cubo.getXmax() - this.cubo.getXmin() < 5) || (this.cubo.getYmax() - this.cubo.getYmin() < 5) || (this.cubo.getZmax() - this.cubo.getZmin() < 5))
			throw new InvalidCuboidException("The selected cuboid is smaller than minimum size allowed (< 5*5*5).");

		if ((this.cubo.getXmax() - this.cubo.getXmin() > 50) || (this.cubo.getYmax() - this.cubo.getYmin() > 50) || (this.cubo.getZmax() - this.cubo.getZmin() > 50))
			plugin.getLogger().warning("Too big arena can cause lag.");

		for (Block block : this.cubo.iterator().iterateBlocks())
			block.setType(Material.AIR);
		for (Block block : this.cubo.getWalls())
			block.setType(Material.OBSIDIAN);

		arenas.add(this);

		ArenaCreatedEvent event = new ArenaCreatedEvent(creator, this);
		plugin.getServer().getPluginManager().callEvent(event);
	}

	public Arena(HikaBrainPlugin plugin, File fileToLoad) {
		super(plugin);

		this.fileManager = new FileManager(fileToLoad);

		arenas.add(this);

		ArenaLoadedEvent event = new ArenaLoadedEvent(this);
		plugin.getServer().getPluginManager().callEvent(event);
	}

	public Cuboid getCuboid() {
		return this.cubo;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public Location getRedSpawn(int index) {
		int size = this.redSpawns.size();
		while (index >= size)
			index = index - size;
		return this.redSpawns.get(index);
	}

	public Location getBlueSpawn(int index) {
		int size = this.blueSpawns.size();
		while (index >= size)
			index = index - size;
		return this.blueSpawns.get(index);
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

	public Location getLobby() {
		return this.lobby;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public int getWinningScore() {
		return this.winning_score;
	}

	public void setMaxPlayers(int i) throws InvalidNumberOfMaxPlayersException {
		if (i % 2 != 0)
			throw new InvalidNumberOfMaxPlayersException("The number of maximum players allowed to join this arena must be a multiple of two.");
		this.maxPlayers = i;
	}

	public void setWinningScore(int i) throws InvalidWinningScoreException {
		if (i <= 0)
			throw new InvalidWinningScoreException("The number of points to scored is less or equal to 0.");
		this.winning_score = i;
	}

	public void editArena(boolean b, Player player) {
		if (b && player != null) {
			if (this.beeingEdited) {
				player.sendMessage(HikaBrainPlugin.PREFIX + ChatColor.RED + "Somebody is already editing this Arena :(");
				return;
			}
			GuiManager.openGui(this.plugin, new GuiArena(this.plugin, player, this));
		}
		this.beeingEdited = b;
	}

	@Override
	public void join(Player player) {
		PlayerJoinArenaEvent event = new PlayerJoinArenaEvent(player, this);
		plugin.getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			playerRestorer.saveProperties(player);
			player.teleport(this.lobby);
			broadcastMessage(Messages.PLAYER_JOIN_GAME, player);

			this.players.put(player, Team.NONE);
			updateSigns(null);
		}
	}

	@Override
	public void quit(Player player) {
		PlayerQuitArenaEvent event = new PlayerQuitArenaEvent(player, this);
		plugin.getServer().getPluginManager().callEvent(event);

		playerRestorer.restoreProperties(player);
		this.players.remove(player);

		broadcastMessage(Messages.PLAYER_QUIT_GAME, player);
		updateSigns(null);
	}

	@Override
	public void respawn(Player player) {
		ArenaPlayerRespawnEvent event = new ArenaPlayerRespawnEvent(player, this);
		plugin.getServer().getPluginManager().callEvent(event);
	}

	@Override
	public void broadcastMessage(Messages message, Player target) {
		for (Player players : players.keySet())
			message.sendMessage(players, target);
	}

	public void delete(Player player, boolean sure) {
		if (sure) {
			ArenaDeletedEvent event = new ArenaDeletedEvent(player, this);
			plugin.getServer().getPluginManager().callEvent(event);

			if (!event.isCancelled()) {
				this.fileManager.delete();
				arenas.remove(this);
			}
		} else {
			GuiManager.openGui(plugin, new GuiValidate(plugin, player, "§l§nAre you sure to delete this arena ?", new GuiValidate.Action() {

				@Override
				public void then(boolean validate) {
					if (validate)
						delete(player, true);
				}
			}));
		}
	}

	public void updateScore(Team team, Player scoredPlayer) {
		UpdateScoreEvent event = new UpdateScoreEvent(this, team, scoredPlayer);
		plugin.getServer().getPluginManager().callEvent(event);

		this.redScore = this.redScore + (team == Team.RED ? 1 : 0);
		this.blueScore = this.redScore + (team == Team.RED ? 1 : 0);
	}

	public boolean alreadyExistArena() {
		for (Arena arena : arenas)
			if (arena.rawName.equals(this.rawName))
				return true;
		return false;
	}

	public void updateSigns(Block newSign) {
		if (newSign != null)
			this.signs.add(newSign);
		final Arena arena = this;
		new BukkitRunnable() {
			public void run() {
				ArrayList<Block> signsCopy = new ArrayList<>(signs);
				for (Block signBlock : signs) {
					if (signBlock.getType() == Material.SIGN_POST || signBlock.getType() == Material.WALL_SIGN || signBlock.getType() == Material.SIGN) {
						Sign sign = (Sign) signBlock.getState();
						UpdateSignEvent event = new UpdateSignEvent(sign, arena);
						plugin.getServer().getPluginManager().callEvent(event);
					} else {
						signs.remove(signBlock);
						plugin.versionManager.getParticleFactory().playParticles(Particles.FLAME, signBlock.getLocation(), 0.1f, 0.1f, 0.1f, 5, 0.0f);
					}
				}
				signs = signsCopy;
			}
		}.runTaskLater(plugin, newSign != null ? 15 : 0);
	}

	public ArrayList<Block> getSigns() {
		return this.signs;
	}

	public void saveArena() throws NotEnoughSpawnsException {
		if (redSpawns.size() <= 0 || blueSpawns.size() <= 0)
			throw new NotEnoughSpawnsException("You need at least 1 spawn for each team.");

		this.fileManager.save();
	}

	private class FileManager {

		private File file;
		private FileConfiguration config;

		private final String name_id = "name";
		private final String maxplayers_id = "maxplayers";
		private final String winning_score_id = "winning-score";
		private final String lobby_id = "lobby";
		private final String cuboid_pos1_id = "cuboid.pos1";
		private final String cuboid_pos2_id = "cuboid.pos2";
		private final String signs_id = "signs";
		private final String red_spawns_id = "red-spawns";
		private final String blue_spawns_id = "blue-spawns";

		public FileManager(String fileName) {
			this.file = new File(plugin.getDataFolder(), fileName + ".yml");

			if (!this.file.exists()) {
				try {
					this.file.createNewFile();
				} catch (IOException e) {
					new Exception(e).register(plugin, true);
				}
			}

			this.config = YamlConfiguration.loadConfiguration(this.file);
		}

		public FileManager(File fileToLoad) {
			int i;
			this.file = fileToLoad;
			try {
				this.config = YamlConfiguration.loadConfiguration(this.file);

				displayName = this.config.getString(name_id);
				rawName = Utils.getRaw(displayName);
				maxPlayers = this.config.getInt(maxplayers_id);
				winning_score = this.config.getInt(winning_score_id);
				lobby = Utils.toLocation(this.config.getString(lobby_id));
				cubo = new Cuboid(Utils.toLocation(this.config.getString(cuboid_pos1_id)), Utils.toLocation(this.config.getString(cuboid_pos2_id)));

				List<String> signsList = this.config.getStringList(signs_id);
				for (i = 0; i < signsList.size(); i++)
					signs.add(Utils.toLocation(signsList.get(i)).getBlock());

				List<String> redSpawnsList = this.config.getStringList(red_spawns_id);
				for (i = 0; i < redSpawnsList.size(); i++)
					redSpawns.add(Utils.toLocation(redSpawnsList.get(i)));

				List<String> blueSpawnsList = this.config.getStringList(blue_spawns_id);
				for (i = 0; i < blueSpawnsList.size(); i++)
					blueSpawns.add(Utils.toLocation(blueSpawnsList.get(i)));

			} catch (java.lang.Exception ex) {
				boolean b = this.file.delete();
				plugin.getLogger().warning("Arena file \"" + fileToLoad.getName() + "\" is corrupted." + (b ? " File deleted !" : ""));
			}

		}

		public boolean save() {
			int i;
			this.config.set(name_id, displayName);
			this.config.set(maxplayers_id, maxPlayers);
			this.config.set(winning_score_id, winning_score);
			this.config.set(lobby_id, Utils.toString(lobby));
			this.config.set(cuboid_pos1_id, Utils.toString(new Location(cubo.getWorld(), cubo.getXmax(), cubo.getYmax(), cubo.getZmax())));
			this.config.set(cuboid_pos2_id, Utils.toString(new Location(cubo.getWorld(), cubo.getXmin(), cubo.getYmin(), cubo.getZmin())));

			List<String> signsList = new ArrayList<>();
			for (i = 0; i < signs.size(); i++)
				signsList.add(Utils.toString(signs.get(i).getLocation()));
			this.config.set(signs_id, signsList);

			List<String> redSpawnsList = new ArrayList<>();
			for (i = 0; i < redSpawns.size(); i++)
				redSpawnsList.add(Utils.toString(redSpawns.get(i)));
			this.config.set(red_spawns_id, redSpawnsList);

			List<String> blueSpawnsList = new ArrayList<>();
			for (i = 0; i < blueSpawns.size(); i++)
				blueSpawnsList.add(Utils.toString(blueSpawns.get(i)));
			this.config.set(blue_spawns_id, blueSpawnsList);

			try {
				this.config.save(this.file);
				return true;
			} catch (IOException e) {
				new Exception(e).register(plugin, true);
				return false;
			}
		}

		public boolean delete() {
			return this.file.delete();
		}
	}

	public void setTeam(Player player, Team team) {
		Team oldTeam = Team.NONE;
		if (players.containsKey(player))
			oldTeam = players.get(player);

		PlayerSwitchTeamEvent event = new PlayerSwitchTeamEvent(player, team, oldTeam, this);
		plugin.getServer().getPluginManager().callEvent(event);

		if (!event.isCancelled()) {
			if (oldTeam != Team.NONE)
				players.remove(player);
			players.put(player, team);
		}
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
		Team team = getTeam(player);
		for (Player pla : players.keySet()) {
			if (!pla.getName().equals(player.getName())) {
				if (players.get(pla) == team)
					output++;
				continue;
			}
			break;
		}
		return output;
	}

	public ArenaIcon getArenasIcon() {
		return new ArenaIcon(this);
	}

	public static boolean isPlayerInArena(Player player) {
		return (getPlayerArena(player) != null);
	}

	public static Arena getPlayerArena(Player player) {
		for (Arena arena : arenas)
			if (arena.players.containsKey(player))
				return arena;
		return null;
	}

	public static Arena getArena(String name) {
		name = Utils.getRaw(name);

		for (Arena arena : arenas)
			if (arena.rawName.equals(name))
				return arena;
		return null;
	}

	public static ArrayList<Arena> getArenas() {
		return arenas;
	}

}
