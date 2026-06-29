package com.codingguru.autofish.model;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.codingguru.autofish.AntiAutoFishing;
import com.codingguru.autofish.handlers.CaptchaHandler;
import com.codingguru.autofish.handlers.PlayerHandler;
import com.codingguru.autofish.util.MessagesUtil;

public class FishingData {

	private int fishingAttempts;
	private int failedCaptchas;

	public FishingData(UUID uuid) {
		PlayerHandler.getInstance().addFishingData(uuid, this);
	}

	public void checkForCaptcha(Player player) {
		if (fishingAttempts >= AntiAutoFishing.getInstance().getConfig()
				.getInt("fishing-captcha.fish-caught-until-captcha")) {
			CaptchaHandler.getInstance().openCaptcha(player);
		}
	}

	@SuppressWarnings("deprecation")
	public void checkForFailedCaptcha(Player player) {
		if (failedCaptchas >= AntiAutoFishing.getInstance().getConfig().getInt("fishing-captcha.max-failed-attempts")) {
			player.kickPlayer(MessagesUtil.KICKED_PLAYER.toString());
		}
	}

	public void setFishingAttempts(int fishingAttempts) {
		this.fishingAttempts = fishingAttempts;
	}

	public int getFishingAttempts() {
		return fishingAttempts;
	}

	public void setFailedCaptchas(int failedCaptchas) {
		this.failedCaptchas = failedCaptchas;
	}

	public int getFailedCaptchas() {
		return failedCaptchas;
	}
}