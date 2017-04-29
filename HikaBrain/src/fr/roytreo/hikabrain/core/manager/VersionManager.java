package fr.roytreo.hikabrain.core.manager;

import java.lang.reflect.Constructor;

import org.bukkit.entity.Player;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.handler.MinecraftVersion;
import fr.roytreo.hikabrain.core.util.ReflectionUtils;
import fr.roytreo.hikabrain.core.version.AAnvilGUI;
import fr.roytreo.hikabrain.core.version.ATitleUtils;
import fr.roytreo.hikabrain.core.version.INMSUtils;
import fr.roytreo.hikabrain.core.version.IParticleSpawner;

public class VersionManager {
    private MinecraftVersion version;
    private Constructor<? extends AAnvilGUI> anvilGUIConstructor;
    private ATitleUtils ATitleUtils;
    private INMSUtils INMSUtils;
    private IParticleSpawner IParticleSpawner;

    public VersionManager(MinecraftVersion version) {
        this.version = version;
    }

    @SuppressWarnings("unchecked")
    public void load() throws ReflectiveOperationException {
    	this.ATitleUtils = loadModule("TitleUtils");
    	this.INMSUtils = loadModule("NMSUtils");
    	this.IParticleSpawner = loadModule("ParticleSpawner");
    	this.anvilGUIConstructor = (Constructor<AAnvilGUI>) ReflectionUtils.getConstructor(Class.forName(HikaBrainPlugin.PACKAGE + "." + version + ".AnvilGUI") , Player.class, HikaBrainPlugin.class, AAnvilGUI.AnvilClickEventHandler.class, String.class, String[].class);
        this.anvilGUIConstructor.setAccessible(true);
    }
    @SuppressWarnings("unchecked")
    private <T> T loadModule(String name) throws ReflectiveOperationException{
        return (T) ReflectionUtils.instantiateObject(Class.forName(HikaBrainPlugin.PACKAGE + "." + version.toString() + "." + name));
    }

    public AAnvilGUI newAnvilGUI(final Player player, final HikaBrainPlugin plugin, final AAnvilGUI.AnvilClickEventHandler handler, final String itemName, final String... itemLore){
        try {
            return anvilGUIConstructor.newInstance(player, plugin, handler, itemName, itemLore);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public ATitleUtils getTitleUtils()
    {
    	return this.ATitleUtils;
    }
    
    public INMSUtils getNMSUtils()
    {
    	return this.INMSUtils;
    }
    
    public IParticleSpawner getParticleFactory()
    {
    	return this.IParticleSpawner;
    }
    
    public MinecraftVersion getVersion()
    {
    	return this.version;
    }
}