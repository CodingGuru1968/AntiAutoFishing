package com.codingguru.autofish.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.codingguru.autofish.handlers.PlayerHandler;

public class PlayerQuit implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		if (!PlayerHandler.getInstance().hasFishingData(e.getPlayer().getUniqueId()))
			return;

		PlayerHandler.getInstance().removeFishingData(e.getPlayer().getUniqueId());
	}

}