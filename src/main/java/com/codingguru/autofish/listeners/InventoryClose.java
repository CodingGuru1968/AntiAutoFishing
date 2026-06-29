package com.codingguru.autofish.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.codingguru.autofish.handlers.CaptchaHandler;
import com.codingguru.autofish.handlers.PlayerHandler;
import com.codingguru.autofish.model.FishingData;
import com.codingguru.autofish.util.MessagesUtil;

public class InventoryClose implements Listener {

	private final JavaPlugin plugin;

	public InventoryClose(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (e.getPlayer().getType() != EntityType.PLAYER)
			return;

		Player player = (Player) e.getPlayer();

		if (!CaptchaHandler.getInstance().hasPendingCaptcha(player))
			return;

		String configTitle = plugin.getConfig().getString("fishing-captcha.inventory-name",
				"Verify: Click the Emerald");

		if (!e.getView().getTitle().equals(configTitle))
			return;

		FishingData fishingData = PlayerHandler.getInstance().getFishingData(e.getPlayer().getUniqueId());
		fishingData.setFailedCaptchas(fishingData.getFailedCaptchas() + 1);
		fishingData.checkForFailedCaptcha(player);

		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			if (player.isOnline() && CaptchaHandler.getInstance().hasPendingCaptcha(player)) {
				CaptchaHandler.getInstance().openCaptcha(player);
				MessagesUtil.sendMessage(player, MessagesUtil.CAPTCHA_CLOSED_INV.toString());
			}
		}, 1L);
	}
}