package fr.roytreo.hikabrain.core.event.player;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.arena.Arena;
import fr.roytreo.hikabrain.core.arena.event.PlayerClickArenaSignEvent;
import fr.roytreo.hikabrain.core.event.EventListener;
import fr.roytreo.hikabrain.core.handler.Messages;
import fr.roytreo.hikabrain.core.handler.Team;

public class PlayerInteract extends EventListener {

	public PlayerInteract(final HikaBrainPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(final org.bukkit.event.player.PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (Arena.isPlayerInArena(player)) {
			Arena arena = Arena.getPlayerArena(player);
			if (event.getAction().toString().contains("RIGHT") && event.hasItem() && event.getItem() != null && event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName()) {
				if (event.getItem().getItemMeta().getDisplayName().equals(Messages.TEAM_BLUE_ITEM_NAME.getMessage())) {
					if (arena.getTeam(player) != Team.BLUE) {
						if (arena.getBlueTeamSize() < Math.floor(arena.getMaxPlayers()/2)) {
							arena.setTeam(player, Team.BLUE);
							Messages.TEAM_BLUE_JOIN.sendMessage(player, player);
						} else {
							Messages.TEAM_FULL.sendMessage(player, player);
						}
					} else {
						Messages.TEAM_ALREADY_IN_IT.sendMessage(player, player);
					}
				}
				if (event.getItem().getItemMeta().getDisplayName().equals(Messages.TEAM_RED_ITEM_NAME.getMessage())) {
					if (arena.getTeam(player) != Team.RED) {
						if (arena.getRedTeamSize() < Math.floor(arena.getMaxPlayers()/2)) {
							arena.setTeam(player, Team.RED);
							Messages.TEAM_RED_JOIN.sendMessage(player, player);
						} else {
							Messages.TEAM_FULL.sendMessage(player, player);
						}
					} else {
						Messages.TEAM_ALREADY_IN_IT.sendMessage(player, player);
					}
				}
				if (event.getItem().getItemMeta().getDisplayName().equals(Messages.LEAVE_GAME_ITEM_NAME.getMessage())) {
					arena.quit(player);
					Messages.LEAVE_GAME.sendMessage(player, player);
				}
			}
		}
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN)) {
			Sign sign = (Sign) event.getClickedBlock().getState();
			PlayerClickArenaSignEvent event2 = new PlayerClickArenaSignEvent(sign, player);
			this.plugin.getServer().getPluginManager().callEvent(event2);
		}
	}
}
