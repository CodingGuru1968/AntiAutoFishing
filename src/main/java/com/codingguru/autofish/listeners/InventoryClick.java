package com.codingguru.autofish.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.codingguru.autofish.handlers.CaptchaHandler;
import com.codingguru.autofish.handlers.PlayerHandler;
import com.codingguru.autofish.model.FishingData;
import com.codingguru.autofish.util.MessagesUtil;
import com.codingguru.autofish.util.XMaterialUtil;

public class InventoryClick implements Listener {

	private final JavaPlugin plugin;

	public InventoryClick(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player))
			return;

		String configTitle = plugin.getConfig().getString("fishing-captcha.inventory-name",
				"Verify: Click the Emerald");

		if (!e.getView().getTitle().equals(configTitle))
			return;

		Player player = (Player) e.getWhoClicked();

		e.setCancelled(true);

		ItemStack clickedItem = e.getCurrentItem();

		if (clickedItem == null || clickedItem.getType() == Material.AIR) {
			return;
		}

		XMaterialUtil targetItem = CaptchaHandler.getInstance().getTargetItem();
		boolean isCorrect = false;

		try {
			isCorrect = (XMaterialUtil.matchXMaterial(clickedItem) == targetItem);
		} catch (IllegalArgumentException ex) {
			isCorrect = false;
		}

		if (isCorrect) {
			MessagesUtil.sendMessage(player, MessagesUtil.CAPTCHA_SUCCESS.toString());
			CaptchaHandler.getInstance().completeCaptcha(player);
		} else {
			MessagesUtil.sendMessage(player, MessagesUtil.CAPTCHA_WRONG_ITEM.toString());
			FishingData fishingData = PlayerHandler.getInstance().getFishingData(player.getUniqueId());
			fishingData.setFailedCaptchas(fishingData.getFailedCaptchas() + 1);
			fishingData.checkForFailedCaptcha(player);
		}
	}
}