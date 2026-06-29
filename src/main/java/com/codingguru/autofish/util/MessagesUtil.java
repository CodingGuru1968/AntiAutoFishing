package com.codingguru.autofish.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.codingguru.autofish.AntiAutoFishing;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public enum MessagesUtil {

	CAPTCHA_SUCCESS("&aVerification successful. Happy fishing!"),
	CAPTCHA_WRONG_ITEM("&cIncorrect item clicked. Please try again."),
	CAPTCHA_CLOSED_INV("&cYou must complete the captcha to continue fishing."),
	KICKED_PLAYER("&cYou were kicked for failing the fishing verification.");

	private String defaultValue;

	MessagesUtil(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefault() {
		return this.defaultValue;
	}

	public String getPath() {
		return this.name();
	}

	@Override
	public String toString() {
		String message;

		if (AntiAutoFishing.getInstance().getSettingsManager().getLang().isSet(this.getPath())) {
			message = AntiAutoFishing.getInstance().getSettingsManager().getLang().getString(this.getPath());
		} else {
			message = defaultValue;
		}

		if (!AntiAutoFishing.getInstance().getConfig().getBoolean("use-mini-message")) {
			message = ColorUtil.replace(message);
		}

		return message;
	}

	public static void broadcast(String message) {
		Bukkit.getOnlinePlayers().stream().forEach(player -> sendMessage(player, message));
	}

	public static void sendMiniMessage(CommandSender sender, String replacedString) {
		Audience audience = AntiAutoFishing.getInstance().getAdventure().sender(sender);
		MiniMessage mm = MiniMessage.miniMessage();
		Component replacedMessage = mm.deserialize(replacedString);
		audience.sendMessage(replacedMessage);
	}

	public static void sendMessage(CommandSender sender, String replacedString) {
		if (replacedString.equalsIgnoreCase(""))
			return;

		if (AntiAutoFishing.getInstance().getConfig().getBoolean("use-mini-message")) {
			sendMiniMessage(sender, replacedString);
			return;
		}

		String[] message = replacedString.split("\\\\n");

		for (String msg : message) {
			sender.sendMessage(msg.replace("\\n", ""));
		}
	}
}