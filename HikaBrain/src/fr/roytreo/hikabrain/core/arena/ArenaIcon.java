package fr.roytreo.hikabrain.core.arena;

import org.bukkit.Material;

import fr.roytreo.hikabrain.core.handler.Messages;
import fr.roytreo.hikabrain.core.util.ItemBuilder;

public class ArenaIcon {

	private Arena arena;
	
	public ArenaIcon(Arena arena) {
		this.arena = arena;
	}
	
	public ItemBuilder getItemBuilder() {
		return new ItemBuilder(Material.STAINED_CLAY, 1, this.arena.getGameState().getColor());
	}
	
	public String[] getItemLore() {
		switch (this.arena.getGameState()) {
			case ENDING :
				return Messages.INVENTORY_ARENA_ITEM_LORE_ENDING.getMessage(this.arena).split("\n");
			case INGAME :
				return Messages.INVENTORY_ARENA_ITEM_LORE_INGAME.getMessage(this.arena).split("\n");
			case STARTING :
				return Messages.INVENTORY_ARENA_ITEM_LORE_STARTING.getMessage(this.arena).split("\n");
			case WAITING :
				return Messages.INVENTORY_ARENA_ITEM_LORE_WAITING.getMessage(this.arena).split("\n");
		}
		return null;
	}
}
