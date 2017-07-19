package fr.roytreo.hikabrain.core.event.arena;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.arena.Arena;
import fr.roytreo.hikabrain.core.arena.event.ArenaPlayerRespawnEvent;
import fr.roytreo.hikabrain.core.event.EventListener;
import fr.roytreo.hikabrain.core.handler.Team;

public class ArenaPlayerRespawn extends EventListener {

	public ArenaPlayerRespawn(final HikaBrainPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerRespawn(final ArenaPlayerRespawnEvent event) {
		Player player = event.getPlayer();
		Arena arena = event.getArena();
		player.setHealth(player.getMaxHealth());
		PlayerInventory inv = player.getInventory();
		inv.all(new ItemStack(Material.RED_SANDSTONE, 64));
		player.setFallDistance(0.0f);
		if (arena.getTeam(player) == Team.BLUE) {
			player.teleport(arena.getBlueSpawns().get(arena.getPlayerIndex(player)));
		}
		if (arena.getTeam(player) == Team.RED) {
			player.teleport(arena.getRedSpawns().get(arena.getPlayerIndex(player)));
		}
	}
}