package fr.roytreo.hikabrain.core.gui;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.arena.Arena;
import fr.roytreo.hikabrain.core.exception.AlreadyExistArenaException;
import fr.roytreo.hikabrain.core.exception.InvalidCuboidException;
import fr.roytreo.hikabrain.core.exception.InvalidNumberOfMaxPlayersException;
import fr.roytreo.hikabrain.core.exception.InvalidWinningScoreException;
import fr.roytreo.hikabrain.core.exception.NotEnoughSpawnsException;
import fr.roytreo.hikabrain.core.gui.base.GuiScreen;
import fr.roytreo.hikabrain.core.handler.Exception;
import fr.roytreo.hikabrain.core.handler.Hologram;
import fr.roytreo.hikabrain.core.handler.Particles;
import fr.roytreo.hikabrain.core.handler.Sounds;
import fr.roytreo.hikabrain.core.util.ItemBuilder;

public class GuiArena extends GuiScreen {

	public final HikaBrainPlugin plugin;
	public final Player player;
	public final String name;
	public final Arena arena;

	public int maxplayers = 2;
	public int winning_score = 0;
	public boolean pos1 = false;
	public boolean pos2 = false;
	
	public ArrayList<Location> redSpawns = new ArrayList<>();
	public ArrayList<Location> blueSpawns = new ArrayList<>();
	
	public Location loc1 = null;
	public Location loc2 = null;

	public GuiArena(HikaBrainPlugin plugin, Player player, String name) {
		super(plugin, ChatColor.DARK_GRAY + name.substring(0, 14), 2, player, false);
		this.plugin = plugin;
		this.player = player;
		this.name = name;
		this.arena = null;
	}

	public GuiArena(HikaBrainPlugin plugin, Player player, Arena arena) {
		super(plugin, ChatColor.DARK_GRAY + arena.getDisplayName().substring(0, 14), 2, player, false);
		this.plugin = plugin;
		this.player = player;
		this.arena = arena;
		this.name = arena.getDisplayName();

		this.redSpawns = arena.getRedSpawns();
		this.blueSpawns = arena.getBlueSpawns();
		this.maxplayers = arena.getMaxPlayers();
		this.winning_score = arena.getWinningScore();
		
		this.pos1 = true;
		this.pos2 = true;
	}

	@Override
	public void drawScreen() {

		loadItems();
		
		setItem(new ItemBuilder(Material.WOOL).setWoolColor(DyeColor.RED).setName(ChatColor.RED + "" + ChatColor.BOLD + "ADD BLUE TEAM'S GOAL").setLore("", ChatColor.GRAY + "This is the blue team's goal", ChatColor.GRAY + "because blue players have to", ChatColor.GRAY + "reach it to score.", "", ChatColor.YELLOW + "» Click to obtain").toItemStack(), 2);
		setItem(new ItemBuilder(Material.WOOL).setWoolColor(DyeColor.BLUE).setName(ChatColor.BLUE + "" + ChatColor.BOLD + "ADD RED TEAM'S GOAL").setLore(ChatColor.GRAY + "This is the red team's goal", ChatColor.GRAY + "because red players have to", ChatColor.GRAY + "reach it to score.", "", ChatColor.YELLOW + "» Click to obtain").toItemStack(), 3);

		if (!pos1)
			setItem(new ItemBuilder(Material.GRASS, 1).setName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "FIRST POSITION").setLore("", ChatColor.GRAY + "These positions allows you to", ChatColor.GRAY + "define the area of the arena.", "", ChatColor.GREEN + "If you're a bit lost, type", ChatColor.GREEN + "« how to cuboid minecraft »", ChatColor.GREEN + "on google image.", "", ChatColor.YELLOW + "» Click to set the first position", ChatColor.YELLOW + "to the block under your feet.").toItemStack(), 9);
		if (!pos2)
			setItem(new ItemBuilder(Material.GRASS, 2).setName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "SECOND POSITION").setLore("", ChatColor.GRAY + "These positions allows you to", ChatColor.GRAY + "define the area of the arena.", "", ChatColor.GREEN + "If you're a bit lost, type", ChatColor.GREEN + "« how to cuboid minecraft »", ChatColor.GREEN + "on google image.", "", ChatColor.YELLOW + "» Click to set the second position", ChatColor.YELLOW + "to the block under your feet.").toItemStack(), 10);
		
		setItem(new ItemBuilder(Material.SLIME_BALL).setName(ChatColor.GREEN + "" + ChatColor.BOLD + "SAVE ARENA").setLore("", ChatColor.GRAY + "This will create the arena if", ChatColor.GRAY + "it doesn't exist and save it.", "", ChatColor.RED + "If the saving process encounter", ChatColor.RED + "any issue, it will be reported to you.", "", ChatColor.YELLOW + "» Click to proceed").addIllegallyGlow().toItemStack(), 16);
		setItem(new ItemBuilder(Material.BARRIER).setName(ChatColor.RED + "CLOSE").toItemStack(), 17);

	}
	
	@Override
	public void onOpen() {
	}

	@Override
	public void onClick(ItemStack item, InventoryClickEvent event) {
		Player clicker = (Player) event.getWhoClicked();
		if (item != null && item.hasItemMeta() && item.getItemMeta() != null && item.getItemMeta().hasDisplayName() && item.getType() != Material.STAINED_GLASS_PANE) {
			ItemMeta meta = item.getItemMeta();
			boolean rightClick = event.getClick() == ClickType.RIGHT;
			boolean leftClick = event.getClick() == ClickType.LEFT;
			if (item.getType() == Material.BANNER) {
				if (meta.getDisplayName().contains("RED")) {
					if (rightClick && this.redSpawns.size() > 0) {
						Location loc = this.redSpawns.get(this.redSpawns.size() - 1);
						this.redSpawns.remove(this.redSpawns.size() - 1);
						this.plugin.versionManager.getParticleFactory().playParticles(Particles.SMOKE_NORMAL, loc, 0.05f, 0.1f, 0.05f, 5, 0.1f);
					} else if (leftClick) {
						Location loc = clicker.getLocation();
						this.redSpawns.add(loc);
						this.plugin.versionManager.getParticleFactory().playParticles(Particles.VILLAGER_HAPPY, loc, 0.05f, 0.1f, 0.05f, 5, 0.1f);
					}
				} else if (meta.getDisplayName().contains("BLUE")) {
					if (rightClick && this.blueSpawns.size() > 0) {
						Location loc = this.blueSpawns.get(this.blueSpawns.size() - 1);
						this.blueSpawns.remove(this.blueSpawns.size() - 1);
						this.plugin.versionManager.getParticleFactory().playParticles(Particles.SMOKE_NORMAL, loc, 0.05f, 0.1f, 0.05f, 5, 0.1f);
					} else if (leftClick) {
						Location loc = clicker.getLocation();
						this.blueSpawns.add(loc);
						this.plugin.versionManager.getParticleFactory().playParticles(Particles.VILLAGER_HAPPY, loc, 0.05f, 0.1f, 0.05f, 5, 0.1f);
					}
				}
				loadItems();
			} else if (item.getType() == Material.WOOL) {
				if (meta.getDisplayName().contains("RED")) {
					ItemStack item0 = clicker.getInventory().getItem(0);
					clicker.getInventory().setItem(0, new ItemBuilder(Material.WOOL).setWoolColor(DyeColor.BLUE).setName(ChatColor.GOLD + "Place me!").addIllegallyGlow().toItemStack());
					clicker.getInventory().setHeldItemSlot(0);
					clicker.getInventory().addItem(item0);
					clicker.sendMessage("<" + ChatColor.BLUE + "WOOL" + ChatColor.WHITE + "> Hi! I'm the blue wool. Place me where you want the red team's players receive one point when they walk on me.");
				} else if (meta.getDisplayName().contains("BLUE")) {
					ItemStack item0 = clicker.getInventory().getItem(0);
					clicker.getInventory().setItem(0, new ItemBuilder(Material.WOOL).setWoolColor(DyeColor.RED).setName(ChatColor.GOLD + "Place me!").addIllegallyGlow().toItemStack());
					clicker.getInventory().setHeldItemSlot(0);
					clicker.getInventory().addItem(item0);
					clicker.sendMessage("<" + ChatColor.RED + "WOOL" + ChatColor.WHITE + "> Hi! I'm the red wool. Place me where you want the blue team's players receive one point when they walk on me.");
				}
			} else if (item.getType() == Material.SKULL_ITEM) {
				if (rightClick && this.maxplayers > 2) {
					this.maxplayers -= 2;
				} else if (leftClick) {
					this.maxplayers += 2;
				}
				loadItems();
			} else if (item.getType() == Material.WATCH) {
				if (rightClick && this.winning_score > 1) {
					this.winning_score -= 1;
				} else if (leftClick) {
					this.winning_score += 1;
				}
				loadItems();
			} else if (item.getType() == Material.GRASS) {
				if (meta.getDisplayName().contains("FIRST")) {
					this.loc1 = clicker.getLocation().subtract(0, 1, 0).getBlock().getLocation();
					Hologram.runHologramTask(ChatColor.AQUA + "✦", this.loc1, 5, this.plugin);
				} else if (meta.getDisplayName().contains("SECOND")) {
					this.loc2 = clicker.getLocation().subtract(0, 1, 0).getBlock().getLocation();
					Hologram.runHologramTask(ChatColor.AQUA + "✦", this.loc1, 5, this.plugin);
				}
			} else if (item.getType() == Material.SLIME_BALL) {
				Arena arena = null;
				if (this.arena == null) {
					try {
						arena = new Arena(plugin, clicker, this.name, this.loc1, this.loc2);
					} catch (IllegalArgumentException e) {
						clicker.sendMessage(HikaBrainPlugin.PREFIX + ChatColor.RED + "Something went wrong during setting-up your arena. Have you set all positions ? Or maybe the name you've give to the arena can't be used.");
						new Exception(e).register(this.plugin, false);
					} catch (AlreadyExistArenaException e) {
						clicker.sendMessage(HikaBrainPlugin.PREFIX + ChatColor.RED + "Something went wrong during setting-up your arena : " + e.getMessage());
						new Exception(e).register(this.plugin, false);
					} catch (InvalidCuboidException e) {
						clicker.sendMessage(HikaBrainPlugin.PREFIX + ChatColor.RED + "Something went wrong during setting-up your arena : " + e.getMessage());
						new Exception(e).register(this.plugin, false);
					}
				} else {
					arena = this.arena;
				}
				try {
					arena.setMaxPlayers(this.maxplayers);
				} catch (InvalidNumberOfMaxPlayersException e) {
					clicker.sendMessage(HikaBrainPlugin.PREFIX + ChatColor.RED + "Something went wrong during editing your arena : " + e.getMessage());
					new Exception(e).register(this.plugin, false);
				}
				try {
					arena.setWinningScore(this.winning_score);
				} catch (InvalidWinningScoreException e) {
					clicker.sendMessage(HikaBrainPlugin.PREFIX + ChatColor.RED + "Something went wrong during editing your arena : " + e.getMessage());
					new Exception(e).register(this.plugin, false);
				}
				try {
					arena.saveArena();
				} catch (NotEnoughSpawnsException e) {
					clicker.sendMessage(HikaBrainPlugin.PREFIX + ChatColor.RED + "Something went wrong during saving your arena : " + e.getMessage());
					new Exception(e).register(this.plugin, false);
				}
			} else if (item.getType() == Material.BARRIER) {
				clicker.closeInventory();
			}
			Sounds.playSound(clicker, null, Sounds.CLICK, 1.0f, 1.0f);
		}
		event.setCancelled(true);
	}
	
	public void loadItems() {
		int red_spawns = (this.redSpawns.size() <= 0 ? -1 : this.redSpawns.size());
		int blue_spawns = (this.blueSpawns.size() <= 0 ? -1 : this.blueSpawns.size());
		int maxplayers = this.maxplayers;
		int winning_score = this.winning_score;

		setItem(new ItemBuilder(Material.BANNER, red_spawns).setBannerColor(DyeColor.RED).setName(ChatColor.RED + "" + ChatColor.BOLD + "ADD RED TEAM'S SPAWN").setLore("", ChatColor.GRAY + "Add a spawn for red team's players.", "", ChatColor.YELLOW + "» Left-Click to add one at your location", ChatColor.YELLOW + "» Right-Click to delete the last one added").toItemStack(), 0);
		setItem(new ItemBuilder(Material.BANNER, blue_spawns).setBannerColor(DyeColor.BLUE).setName(ChatColor.BLUE + "" + ChatColor.BOLD + "ADD BLUE TEAM'S SPAWN").setLore("", ChatColor.GRAY + "Add a spawn for blue team's players.", "", ChatColor.YELLOW + "» Left-Click to add one at your location", ChatColor.YELLOW + "» Right-Click to delete the last one added").toItemStack(), 1);
		setItem(new ItemBuilder(Material.SKULL_ITEM, maxplayers, (byte) SkullType.PLAYER.ordinal()).setName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "MAXIMUM PLAYERS").setLore("", ChatColor.GRAY + "How much players is required", ChatColor.GRAY + "to play the game on that arena.", "", ChatColor.YELLOW + "» Left-Click increase number", ChatColor.YELLOW + "» Right-Click decrease number").toItemStack(), 4);
		setItem(new ItemBuilder(Material.WATCH, winning_score).setName(ChatColor.GOLD + "" + ChatColor.BOLD + "WINNING SCORE").setLore("", ChatColor.GRAY + "How many points have to be", ChatColor.GRAY + "scored for a team to win the game.", "", ChatColor.YELLOW + "» Left-Click increase number", ChatColor.YELLOW + "» Right-Click decrease number").toItemStack(), 5);
	}
}