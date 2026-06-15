package com.codingguru.autofish.handlers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.codingguru.autofish.AntiAutoFish;
import com.codingguru.autofish.util.ConsoleUtil;

public class CaptchaHandler {

	private final static CaptchaHandler INSTANCE = new CaptchaHandler();
	private static final Set<UUID> activeCaptchas = new HashSet<>();

	@SuppressWarnings("deprecation")
	public void openCaptcha(Player player) {
		AntiAutoFish plugin = AntiAutoFish.getInstance();

		Material targetItem = getTargetItem();
		int size = plugin.getConfig().getInt("fishing-captcha.inventory-size", 36);
		String title = plugin.getConfig().getString("fishing-captcha.inventory-name", "Verify!");
		Inventory inv = Bukkit.createInventory(null, size, title);

		int correctSlot = ThreadLocalRandom.current().nextInt(size);
		activeCaptchas.add(player.getUniqueId());

		for (int i = 0; i < size; i++) {
			inv.setItem(i, new ItemStack(i == correctSlot ? targetItem : Material.AIR));
		}

		player.openInventory(inv);
	}

	public Material getTargetItem() {
		String itemName = AntiAutoFish.getInstance().getConfig().getString("fishing-captcha.captcha-item", "EMERALD");
		Material targetItem;

		try {
			targetItem = Material.valueOf(itemName.toUpperCase());
		} catch (IllegalArgumentException e) {
			ConsoleUtil.warning("Invalid item type in config: " + itemName + ". Defaulting to EMERALD.");
			targetItem = Material.EMERALD;
		}

		return targetItem;
	}

	public boolean hasPendingCaptcha(Player player) {
		return activeCaptchas.contains(player.getUniqueId());
	}
	
	public void removeCaptcha(Player player) {
		activeCaptchas.remove(player.getUniqueId());
	}

	public void completeCaptcha(Player player) {
		activeCaptchas.remove(player.getUniqueId());
		PlayerHandler.getInstance().removeFishingData(player.getUniqueId());
		player.closeInventory();
	}

	public static CaptchaHandler getInstance() {
		return INSTANCE;
	}
}