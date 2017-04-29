package fr.roytreo.hikabrain.core.manager;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.handler.Cuboid;
import fr.roytreo.hikabrain.core.util.Utils;
import net.md_5.bungee.api.ChatColor;

public class ArenaManager {

	private static final String PREFIX = HikaBrainPlugin.PREFIX;
	private static ArrayList<ArenaManager> arenas;
	
	static {
		arenas = new ArrayList<>();
	}
	
	private ArrayList<Player> redPlayers;
	private ArrayList<Player> bluePlayers;
	
	private String displayName;
	private String rawName;
	private FileManager fileManager;
	private Cuboid cubo;
	
	public ArenaManager(String name, Location pos1, Location pos2, Player creator) {
		this.displayName = name;
		this.rawName = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("[+.^:,%$@*§]","").replaceAll("/", "").replaceAll("\\\\", "").trim().replaceAll(" ", "_");
		
		if (alreadyExistArena()) {
			creator.sendMessage(PREFIX + ChatColor.RED + "An arena already exists with that name :(");
			return;
		}
		
		this.redPlayers = new ArrayList<>();
		this.bluePlayers = new ArrayList<>();
		
		this.cubo = new Cuboid(pos1, pos2);
		
		for (Block block : this.cubo.getWalls()) {
			block.setType(Material.OBSIDIAN);
		}
		
		arenas.add(this);
	}
	
	public boolean join(Player player) {
		return true;
	}
	
	public boolean quit(Player player) {
		return true;
	}
	
	public boolean alreadyExistArena() {
		for (ArenaManager arena : arenas)
			if (arena.rawName.equals(this.rawName))
				return true;
		return false;
	}
	
	private static class FileManager {
		
		public FileManager(String fileName) {
			HikaBrainPlugin instance = HikaBrainPlugin.getInstance();
			File arenaFile = new File(instance.getDataFolder(), fileName);
			
			if (!arenaFile.exists()) {
				try {
					arenaFile.createNewFile();
				} catch (IOException e) {
					Utils.registerException(e, true);
				}
			}
		}
	}
	
	public static boolean isPlayerInArena(Player player) {
		return true;
	}
	
}
