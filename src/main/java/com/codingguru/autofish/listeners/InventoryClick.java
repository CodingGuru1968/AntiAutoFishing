package com.codingguru.autofish.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.codingguru.autofish.AntiAutoFish;
import com.codingguru.autofish.handlers.CaptchaHandler;
import com.codingguru.autofish.util.ColorUtil;

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

		String configTitle = plugin.getConfig().getString("fishing-captcha.inventory-name");

		if (!e.getView().getTitle().equals(configTitle))
			return;

		Player player = (Player) e.getWhoClicked();

		e.setCancelled(true);

		ItemStack clickedItem = e.getCurrentItem();
		Material targetItem = CaptchaHandler.getInstance().getTargetItem();

		if (clickedItem != null && clickedItem.getType() == targetItem) {
			player.sendMessage(ColorUtil
					.replace(AntiAutoFish.getInstance().getConfig().getString("fishing-captcha.success-message")));
			CaptchaHandler.getInstance().completeCaptcha(player);
		}
	}
}