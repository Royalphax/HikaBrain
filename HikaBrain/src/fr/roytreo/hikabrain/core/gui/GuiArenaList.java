package fr.roytreo.hikabrain.core.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.arena.Arena;
import fr.roytreo.hikabrain.core.arena.ArenaIcon;
import fr.roytreo.hikabrain.core.gui.base.GuiScreen;
import fr.roytreo.hikabrain.core.handler.Messages;
import fr.roytreo.hikabrain.core.util.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class GuiArenaList extends GuiScreen {
	public final Player player;
	public final GuiAction action;
	public final int page;
	public OrderingType order;
	public final ArrayList<Arena> arenas;
	public boolean crescent;
	public String teamName;

	public GuiArenaList(HikaBrainPlugin plugin, Player player, GuiAction action) {
		super(plugin, ChatColor.DARK_GRAY + "Arena GUI", 4, player, false);
		this.player = player;
		this.action = action;
		this.page = 1;
		this.order = OrderingType.DEFAULT;
		this.arenas = new ArrayList<>();
	}

	@Override
	public void drawScreen() {

		setItemLine(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 7).setName(ChatColor.DARK_GRAY + "✖").toItemStack(), 4);
		setItem(new ItemBuilder(Material.HOPPER, 1).setName(Messages.INVENTORY_ORDERING_ITEM_NAME.getMessage()).setLore(getHopperLore()).toItemStack(), 4, 1);
		setItem(new ItemBuilder(Material.BARRIER, 1).setName(Messages.INVENTORY_CLOSE_ITEM_NAME.getMessage()).toItemStack(), 4, 9);
	
		loadPage();
	}

	@Override
	public void onOpen() {
	}

	@Override
	public void onClick(ItemStack item, InventoryClickEvent event) {
		Player clicker = (Player) event.getWhoClicked();
		if (item != null && item.getItemMeta() != null && item.getItemMeta().hasDisplayName() && item.getType() != Material.STAINED_GLASS_PANE) {
			ItemMeta itemMeta = item.getItemMeta();
			if (itemMeta.getDisplayName().equals(Messages.INVENTORY_ORDERING_ITEM_NAME.getMessage())) {
				if (event.getClick() == ClickType.RIGHT) {
					this.crescent = !this.crescent;
				} else if (event.getClick() == ClickType.LEFT) {
					this.order = this.order.next();
				}
				setItem(new ItemBuilder(Material.HOPPER, 1).setName(Messages.INVENTORY_ORDERING_ITEM_NAME.getMessage()).setLore(getHopperLore()).toItemStack(), 4, 1);
			} else if (itemMeta.getDisplayName().equals(Messages.INVENTORY_CLOSE_ITEM_NAME.getMessage())) {
				clearInventory();
				clicker.closeInventory();
			} else if (item.getType() == Material.STAINED_CLAY) {
				Arena arena = arenas.get(event.getSlot());
				switch (action) {
					case DELETE :
						arena.delete(clicker, false);
						break;
					case JOIN :
						arena.join(clicker);
						break;
					case TELEPORT :
						clicker.teleport(arena.getLobby());
						break;
					case EDIT :
						arena.editArena(true, clicker);
						break;
				}
			}
		}
		event.setCancelled(true);
	}
	
	public void loadPage() {
		int slot = 0;
		arenas.clear();
		for (int i = (page == 1 ? 0 : ((page - 1) * 26) + 1); i < Arena.getArenas().size(); i++) {
			arenas.add(Arena.getArenas().get(i));
			slot++;
			if (slot == 27)
				break;
		}
		slot = 0;
		switch (order) {
			case GAME_STATE :
				arenas.sort(Comparator.comparing(Arena::getGameState));
				break;
			case NAME :
				arenas.sort(Comparator.comparing(Arena::getDisplayName));
				break;
			case PLAYERS :
				arenas.sort((o1, o2) -> (o1.getPlayers().size() - o2.getPlayers().size()));
				break;
			default :
				break;
		}
		if (!crescent)
			Collections.reverse(arenas);
		for (Arena arena : arenas) {
			ArenaIcon ico = arena.getArenasIcon();
			setItem(ico.getItemBuilder().setName(Messages.INVENTORY_ARENA_ITEM_NAME.getMessage(arena)).setLore(ico.getItemLore()).toItemStack(), slot);
			slot++;
			if (slot == 27) {
				setItem(new ItemBuilder(Material.ARROW, 1).setName(Messages.INVENTORY_NEXT_PAGE.getMessage()).toItemStack(), 4, 6);
				break;
			}
		}
		if (slot < 27)
			setItem(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 7).setName(ChatColor.DARK_GRAY + "✖").toItemStack(), 32);
		if (page > 1) {
			setItem(new ItemBuilder(Material.ARROW, 1).setName(Messages.INVENTORY_PREVIOUS_PAGE.getMessage()).toItemStack(), 30);
		} else {
			setItem(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 7).setName(ChatColor.DARK_GRAY + "✖").toItemStack(), 30);
		}
	}
	
	public List<String> getHopperLore() {
		String way = crescent ? "» " : "« ";
		List<String> output = new ArrayList<>();
		output.add("");
		output.add((order == OrderingType.DEFAULT ? ChatColor.GREEN : ChatColor.DARK_GRAY) + way + Messages.ORDERING_BY_DEFAULT.getMessage());
		output.add((order == OrderingType.NAME ? ChatColor.GREEN : ChatColor.DARK_GRAY) + way + Messages.ORDERING_BY_NAME.getMessage());
		output.add((order == OrderingType.GAME_STATE ? ChatColor.GREEN : ChatColor.DARK_GRAY) + way + Messages.ORDERING_BY_GAME_STATE.getMessage());
		output.add((order == OrderingType.PLAYERS ? ChatColor.GREEN : ChatColor.DARK_GRAY) + way + Messages.ORDERING_BY_PLAYERS.getMessage());
		output.add("");
		output.add(Messages.INVENTORY_ORDERING_ITEM_INSTRUCTIONS.getMessage());
		return output;
	}
	
	public enum OrderingType {
		DEFAULT(0, 1),
		NAME(1, 2),
		GAME_STATE(2, 3),
		PLAYERS(3, 0);
		
		private int id;
		private int next;
		
		private OrderingType(int id, int next) {
			this.id = id;
			this.next = next;
		}
		
		public OrderingType next() {
			return get(this.next);
		}
		
		public static OrderingType get(int id) {
			for (OrderingType order : OrderingType.values())
				if (id == order.id)
					return order;
			return null;
		}
	}
	
	public enum GuiAction {
		JOIN(),
		TELEPORT(),
		EDIT(),
		DELETE();
	}
}
