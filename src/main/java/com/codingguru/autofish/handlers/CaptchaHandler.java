package com.codingguru.autofish.handlers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.codingguru.autofish.AntiAutoFishing;
import com.codingguru.autofish.util.ConsoleUtil;
import com.codingguru.autofish.util.XMaterialUtil;

public class CaptchaHandler {

	private final static CaptchaHandler INSTANCE = new CaptchaHandler();
	private final Set<UUID> activeCaptchas = new HashSet<>();

	@SuppressWarnings("deprecation")
	public void openCaptcha(Player player) {
		AntiAutoFishing plugin = AntiAutoFishing.getInstance();
		String title = plugin.getConfig().getString("fishing-captcha.inventory-name", "Verify: Click the Emerald");

		if (player.getOpenInventory() != null && player.getOpenInventory().getTitle().equalsIgnoreCase(title)
				&& activeCaptchas.contains(player.getUniqueId()))
			return;

		Material targetItem = getTargetItem().parseMaterial();
		Material fillerItem = getFillerItem().parseMaterial();
		int size = plugin.getConfig().getInt("fishing-captcha.inventory-size", 36);
		Inventory inv = Bukkit.createInventory(null, size, title);

		int correctSlot = ThreadLocalRandom.current().nextInt(size);
		activeCaptchas.add(player.getUniqueId());

		for (int i = 0; i < size; i++) {
			inv.setItem(i, new ItemStack(i == correctSlot ? targetItem : fillerItem));
		}

		player.openInventory(inv);
	}

	public XMaterialUtil getTargetItem() {
		String itemName = AntiAutoFishing.getInstance().getConfig().getString("fishing-captcha.captcha-item",
				"EMERALD");
		Optional<XMaterialUtil> xMat = XMaterialUtil.matchXMaterial(itemName);

		return xMat.orElseGet(() -> {
			ConsoleUtil.warning("Invalid item type in config: " + itemName + ". Defaulting to EMERALD.");
			return XMaterialUtil.EMERALD;
		});
	}

	public XMaterialUtil getFillerItem() {
		String itemName = AntiAutoFishing.getInstance().getConfig().getString("fishing-captcha.filler-item", "AIR");

		if (itemName == null || itemName.isEmpty())
			return XMaterialUtil.AIR;

		Optional<XMaterialUtil> xMat = XMaterialUtil.matchXMaterial(itemName);

		return xMat.orElseGet(() -> {
			ConsoleUtil.warning("Invalid item type in config: " + itemName + ". Defaulting to AIR.");
			return XMaterialUtil.AIR;
		});
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