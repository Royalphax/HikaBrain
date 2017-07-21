package fr.roytreo.hikabrain.core.gui;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.gui.base.GuiScreen;
import fr.roytreo.hikabrain.core.util.ItemBuilder;

public class GuiValidate extends GuiScreen {
	
	public final Player player;
	public Action action;
	public final String question;
	
	public GuiValidate(HikaBrainPlugin plugin, Player player, String question, Action action) {
		super(plugin, ChatColor.DARK_GRAY + "GUI", 1, player, false);
		this.player = player;
		try {
			this.action = action;
		} catch (IllegalArgumentException e) {
			new fr.roytreo.hikabrain.core.handler.Exception(e).register(plugin, true);
		}
		this.question = question;
	}
	
	@Override
	public void drawScreen() {
		
		setItem(new ItemBuilder(Material.WOOL).setWoolColor(DyeColor.RED).setName(ChatColor.RED + "" + ChatColor.BOLD + "I'm NOT SURE").toItemStack(), 11);
		setItem(new ItemBuilder(Material.SIGN).setName(question).setLore("", ChatColor.GRAY + "Please answered the question above.", ChatColor.GRAY + "To do so, click on the wool which represents", ChatColor.GRAY + "bestly what you are thinking about", ChatColor.GRAY + "this important choice.").toItemStack(), 1, 5);
		setItem(new ItemBuilder(Material.WOOL).setWoolColor(DyeColor.LIME).setName(ChatColor.GREEN + "" + ChatColor.BOLD + "I'm SURE").toItemStack(), 15);
	}

	@Override
	public void onOpen() {
	}

	@Override
	public void onClick(ItemStack item, InventoryClickEvent event) {
		if (item != null && item.getItemMeta() != null && item.getItemMeta().hasDisplayName() && item.getType() != Material.STAINED_GLASS_PANE) {
			if (item.getType() == Material.WOOL) {
				boolean b = false;
				if (event.getSlot() == 15) {
					b = true;
				}
				action.then(b);
				player.closeInventory();
			}
		}
		event.setCancelled(true);
	}
	
	public static interface Action {

		public void then(final boolean validate);
		
	}
}