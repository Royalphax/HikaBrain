package fr.roytreo.hikabrain.core.event.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.arena.Arena;
import fr.roytreo.hikabrain.core.event.EventListener;
import fr.roytreo.hikabrain.core.handler.Team;

public class PlayerMove extends EventListener {

	public PlayerMove(final HikaBrainPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerMove(final org.bukkit.event.player.PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (Arena.isPlayerInArena(player)) {
			Arena arena = Arena.getPlayerArena(player);
			if (arena.getCuboid().hasPlayerInside(player))
				return;
			Location playerLoc = player.getLocation();
			double x = playerLoc.getX();
			double y = playerLoc.getY();
			double z = playerLoc.getZ();
			if ((x > arena.getCuboid().getXmax() || x < arena.getCuboid().getXmin()) || (z > arena.getCuboid().getZmax() || z < arena.getCuboid().getZmin()) || (playerLoc.clone().add(0, 2, 0).getY() >= arena.getCuboid().getYmax())) {
				player.teleport(event.getFrom());
				player.setVelocity(player.getVelocity().multiply(-1));
				player.setVelocity(player.getVelocity().zero());
			} else if (y < arena.getCuboid().getYmin()) {
				arena.respawn(player);
			}
			if (player.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.WOOL) {
				Block block = player.getLocation().subtract(0, 1, 0).getBlock();
				if ((block.getData() == (byte) 14) && arena.getTeam(player) == Team.BLUE) {
					arena.updateScore(Team.BLUE, player);
				}
				if ((block.getData() == (byte) 11) && arena.getTeam(player) == Team.RED) {
					arena.updateScore(Team.RED, player);
				}
			}
		}
	}
}
