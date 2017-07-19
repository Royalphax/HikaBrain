package fr.roytreo.hikabrain.core.event.block;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.arena.Arena;
import fr.roytreo.hikabrain.core.event.EventListener;
import fr.roytreo.hikabrain.core.handler.Messages;
import fr.roytreo.hikabrain.core.handler.Sounds;

public class BlockBreak extends EventListener {

	public BlockBreak(final HikaBrainPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDeath(final org.bukkit.event.block.BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (event.getBlock().getType() == Material.OBSIDIAN)
			for (Arena arena : Arena.getArenas())
				if (arena.getCuboid().hasBlockInsideWalls(event.getBlock())) {
					event.setCancelled(true);
					Sounds.playSound(player, event.getBlock().getLocation(), Sounds.VILLAGER_NO, 1.0f, 1.0f);
					player.sendMessage(Messages.PLAYER_CANT_BREAK_ARENA_WALLS.getMessage());
					break;
				}
	}
}
