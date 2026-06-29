package com.codingguru.autofish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import com.codingguru.autofish.handlers.PlayerHandler;
import com.codingguru.autofish.model.FishingData;

public class PlayerFish implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerFish(PlayerFishEvent e) {
		if (e.isCancelled())
			return;

		if (e.getCaught() == null)
			return;
		
		Player player = (Player) e.getPlayer();

		FishingData fishingData = PlayerHandler.getInstance().getFishingData(player.getUniqueId());
		fishingData.setFishingAttempts(fishingData.getFishingAttempts() + 1);
		fishingData.checkForCaptcha(player);
	}
}