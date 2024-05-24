package com.codingguru.autofish;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.codingguru.autofish.listeners.PlayerFish;
import com.codingguru.autofish.listeners.PlayerQuit;
import com.codingguru.autofish.util.ConsoleUtil;

public class AntiAutoFish extends JavaPlugin {

	private static AntiAutoFish INSTANCE;

	public void onEnable() {
		INSTANCE = this;
		
		ConsoleUtil.sendPluginSetup();

		saveDefaultConfig();

		Bukkit.getPluginManager().registerEvents(new PlayerFish(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
	}

	public static AntiAutoFish getInstance() {
		return INSTANCE;
	}

}