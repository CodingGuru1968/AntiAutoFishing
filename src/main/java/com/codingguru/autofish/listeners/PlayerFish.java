package com.codingguru.autofish.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import com.codingguru.autofish.data.PlayerFishingData;
import com.codingguru.autofish.handlers.PlayerHandler;

public class PlayerFish implements Listener {

	@EventHandler
	public void onPlayerFish(PlayerFishEvent e) {
		if (e.isCancelled())
			return;

		if (e.getCaught() == null)
			return;

		PlayerFishingData cachedData = PlayerHandler.getInstance().getFishingData(e.getPlayer().getUniqueId(),
				e.getPlayer().getLocation());

		e.setCancelled(cachedData.isFishCancelled(e.getPlayer()));
	}
}