package fr.roytreo.hikabrain.core.handler;

import org.bukkit.DyeColor;
import org.bukkit.Material;

import fr.roytreo.hikabrain.core.util.ItemBuilder;

public enum Team {
	
	RED(Messages.TEAM_RED_NAME.getMessage(), new ItemBuilder(Material.INK_SACK).setDyeColor(DyeColor.RED)), 
	BLUE(Messages.TEAM_BLUE_NAME.getMessage(), new ItemBuilder(Material.INK_SACK).setDyeColor(DyeColor.BLUE)), 
	NONE("", new ItemBuilder());
	
	private String name;
	private ItemBuilder icon;
	
	private Team(String name, ItemBuilder icon) {
		this.name = name;
		this.icon = icon;
	}
	
	public String getName() {
		return this.name;
	}
	
	public ItemBuilder getIcon() {
		return this.icon;
	}
}
