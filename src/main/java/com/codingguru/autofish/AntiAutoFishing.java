package com.codingguru.autofish;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.codingguru.autofish.listeners.InventoryClick;
import com.codingguru.autofish.listeners.InventoryClose;
import com.codingguru.autofish.listeners.PlayerFish;
import com.codingguru.autofish.listeners.PlayerQuit;
import com.codingguru.autofish.managers.SettingsManager;
import com.codingguru.autofish.util.ConsoleUtil;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public class AntiAutoFishing extends JavaPlugin {

	private static AntiAutoFishing INSTANCE;
	private SettingsManager settingsManager;
	private BukkitAudiences adventureAPI;

	public void onEnable() {
		INSTANCE = this;
		
		ConsoleUtil.sendPluginSetup();

		saveDefaultConfig();

		settingsManager = new SettingsManager();
		settingsManager.setup(this);
		
		if (getConfig().getBoolean("use-mini-message")) {
			this.adventureAPI = BukkitAudiences.create(this);
		}
		
		Bukkit.getPluginManager().registerEvents(new PlayerFish(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryClose(this), this);
		Bukkit.getPluginManager().registerEvents(new InventoryClick(this), this);
	}
	
	public void onDisable() {
		closeAdventure();
	}

	public static AntiAutoFishing getInstance() {
		return INSTANCE;
	}
	
	public SettingsManager getSettingsManager() {
		return settingsManager;
	}
	
	private void closeAdventure() {
		if (this.adventureAPI == null)
			return;

		this.adventureAPI.close();
		this.adventureAPI = null;
	}

	public BukkitAudiences getAdventure() {
		return this.adventureAPI;
	}

}