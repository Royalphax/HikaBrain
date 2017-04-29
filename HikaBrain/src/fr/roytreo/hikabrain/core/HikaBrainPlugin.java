package fr.roytreo.hikabrain.core;

import java.lang.reflect.Constructor;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import fr.roytreo.hikabrain.core.event.EventListener;
import fr.roytreo.hikabrain.core.event.player.PlayerDeath;
import fr.roytreo.hikabrain.core.event.player.PlayerInteract;
import fr.roytreo.hikabrain.core.event.sign.SignChange;
import fr.roytreo.hikabrain.core.handler.MinecraftVersion;
import fr.roytreo.hikabrain.core.manager.VersionManager;
import fr.roytreo.hikabrain.core.util.Utils;
import net.md_5.bungee.api.ChatColor;

public class HikaBrainPlugin extends JavaPlugin {

	/**
	 * TODO:
	 * - Système panneau cliquable
	 * - Faire BeginCountdown & Game tasks
	 * - Faire systeme de setup de la map
	 * - Faire differentes game states
	 * - Empecher de move des choses de l'inventaire lorsque on est en waiting room
	 */
	
	private static HikaBrainPlugin instance;
	public static final String PACKAGE = "fr.roytreo.hikabrain";
	public static final String PREFIX = ChatColor.GRAY + "[" + ChatColor.GOLD + "HikaBrain" + ChatColor.GRAY + "] " + ChatColor.RESET;

	public VersionManager versionManager;

	@SuppressWarnings("unchecked")
	@Override
	public void onEnable() {
		instance = this;
		if (!isSpigotServer()) {
			disablePlugin(Level.WARNING, "This server isn't running with Spigot build. The plugin can't be load.");
			return;
		}
		if (!MinecraftVersion.getVersion().inRange(MinecraftVersion.v1_8_R3, MinecraftVersion.v1_9_R1, MinecraftVersion.v1_9_R2, MinecraftVersion.v1_10_R1, MinecraftVersion.v1_11_R1)) {
			disablePlugin(Level.WARNING, "HikaBrain plugin doesn't support your server version (" + MinecraftVersion.getVersion().toString() + ")");
			return;
		}
		try {
			(this.versionManager = new VersionManager(MinecraftVersion.getVersion())).load();
		} catch (ReflectiveOperationException e1) {
			Utils.registerException(e1, true);
			return;
		}
		this.register(PlayerDeath.class, PlayerInteract.class, SignChange.class);
	}

	@Override
	public void onDisable() {
	}

	public void disablePlugin(Level level, String reason) {
		this.getLogger().log(level, reason);
		this.getPluginLoader().disablePlugin(this);
	}

	public static HikaBrainPlugin getInstance() {
		return instance;
	}

	private void register(@SuppressWarnings("unchecked") final Class<? extends EventListener>... classes) {
		try {
			for (final Class<? extends EventListener> clazz : classes) {
				final Constructor<? extends EventListener> constructor = clazz.getConstructor(HikaBrainPlugin.class);
				Bukkit.getPluginManager().registerEvents((Listener) constructor.newInstance(this), (Plugin) this);
			}
		} catch (Throwable ex) {
			Utils.registerException(ex, true);
			try {
				throw ex;
			} catch (Throwable e) {
				Utils.registerException(e, true);
			}
		}
	}

	public static Boolean isSpigotServer() {
		try {
			Class.forName("org.bukkit.entity.Player$Spigot");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
