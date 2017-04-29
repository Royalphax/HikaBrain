package fr.roytreo.hikabrain.core.event.sign;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.SignChangeEvent;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.event.EventListener;
import fr.roytreo.hikabrain.core.manager.ArenaManager;

public class SignChange extends EventListener
{
    public SignChange(final HikaBrainPlugin plugin) {
        super(plugin);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onSignChange(final SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[hikabrain]"))
        {
        	String line1 = event.getLine(1);
        	if (event.getPlayer().hasPermission("hikabrain.signs.create"))
        	{
        		if (ArenaManager.getArena(line1) != null)
            	{
        			//ArenaManager.getArena(line1).addSign(event.getPlayer(), event.getBlock().getLocation());
            	} else {
            		event.setLine(0, ChatColor.DARK_RED + "⚠");
            		event.setLine(1, ChatColor.DARK_RED + "Arena \""+line1+"\"'");
            		event.setLine(2, ChatColor.DARK_RED + "was not find!");
            	}
        	} else {
        		event.setLine(0, ChatColor.DARK_RED + "⚠");
        		event.setLine(1, ChatColor.DARK_RED + "You are not");
        		event.setLine(2, ChatColor.DARK_RED + "allowed to write");
        		event.setLine(3, ChatColor.DARK_RED + "this :(");
        	}
        }
    }
}
