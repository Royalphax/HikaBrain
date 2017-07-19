package fr.roytreo.hikabrain.core.util;

import java.text.Normalizer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils {
	
	public static Location toLocation(final String string) {
        final String[] splitted = string.split("_");
        World world = Bukkit.getWorld(splitted[0]);
        if (world == null || splitted.length < 6) {
            world = Bukkit.getWorlds().get(0);
        }
        return new Location(world, Double.parseDouble(splitted[1]), Double.parseDouble(splitted[2]), Double.parseDouble(splitted[3]), Float.parseFloat(splitted[4]), Float.parseFloat(splitted[5]));
    }
    
    public static String toString(final Location location) {
        final World world = location.getWorld();
        return String.valueOf(world.getName()) + "_" + location.getX() + "_" + location.getY() + "_" + location.getZ() + "_" + location.getYaw() + "_" + location.getPitch();
    }
    
    
    public static boolean inventoryContains(Player player, Material mat)
    {
    	for (ItemStack item : player.getInventory().getContents())
    	{
    		if (item == null) continue;
    		if (item.getType() == mat)
    			return true;
    	}
    	return false;
    }
    
    public static String getRaw(String input) {
    	String output = Normalizer.normalize(input, Normalizer.Form.NFD);
    	output = output.replaceAll("[^\\p{ASCII}]", "");
    	output = output.replaceAll("[+.^:,%$@*§]","");
    	output = output.replaceAll("/", "");
    	output = output.replaceAll("\\\\", "");
    	output = output.trim();
    	output = output.replaceAll(" ", "_");
    	output = output.toLowerCase();
    	return output;
    }
}
