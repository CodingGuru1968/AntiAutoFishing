package com.codingguru.autofish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.codingguru.autofish.AntiAutoFish;
import com.codingguru.autofish.handlers.CaptchaHandler;
import com.codingguru.autofish.util.ColorUtil;

public class InventoryClose implements Listener {

	private final JavaPlugin plugin;

	public InventoryClose(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (!(e.getPlayer() instanceof Player))
			return;

		Player player = (Player) e.getPlayer();

		if (!CaptchaHandler.getInstance().hasPendingCaptcha(player))
			return;

		new BukkitRunnable() {
			@Override
			public void run() {
				CaptchaHandler.getInstance().openCaptcha(player);
				player.sendMessage(ColorUtil.replace(AntiAutoFish.getInstance().getConfig().getString(
						"fishing-captcha.closed-inv-message", "You must complete the captcha to continue!")));
			}
		}.runTaskLater(plugin, 1L);
	}
}