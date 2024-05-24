package com.codingguru.autofish.data;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.codingguru.autofish.AntiAutoFish;
import com.codingguru.autofish.handlers.PlayerHandler;
import com.codingguru.autofish.util.ColorUtil;

public class PlayerFishingData {

	private UUID uuid;
	private Location location;
	private long nextUse;
	private int exploitNumber;

	public PlayerFishingData(UUID uuid, Location location) {
		this.uuid = uuid;
		this.location = location;
		PlayerHandler.getInstance().addFishingData(uuid, this);
	}

	public boolean isFishCancelled(Player player) {
		if (nextUse != 0 && System.currentTimeMillis() < nextUse)
			return true;

		if (!getLocation().getWorld().getName().equalsIgnoreCase(player.getLocation().getWorld().getName())) {
			PlayerHandler.getInstance().removeFishingData(player.getUniqueId());
			return false;
		}

		if (AntiAutoFish.getInstance().getConfig().getBoolean("MAX_FISH_DISTANCE.ENABLED")
				&& getLocation().distance(player.getLocation()) > AntiAutoFish.getInstance().getConfig()
						.getInt("MAX_FISH_DISTANCE.DISTANCE")) {
			PlayerHandler.getInstance().removeFishingData(player.getUniqueId());
			return false;
		} else {
			if (!getLocation().equals(player.getLocation())) {
				PlayerHandler.getInstance().removeFishingData(player.getUniqueId());
				return false;
			}
		}

		setExploitNumber(getExploitNumber() + 1);

		if (AntiAutoFish.getInstance().getConfig().getBoolean("FISHING_COOLDOWN.ENABLED") && AntiAutoFish.getInstance()
				.getConfig().getIntegerList("FISHING_COOLDOWN.EXPLOIT_NUMBERS").contains(getExploitNumber())) {
			long total = 1000 * AntiAutoFish.getInstance().getConfig().getInt("FISHING_COOLDOWN.LENGTH");
			setNextUse(System.currentTimeMillis() + total);
		}

		if (AntiAutoFish.getInstance().getConfig().getBoolean("FISHING_MESSAGES.ENABLED") && AntiAutoFish.getInstance()
				.getConfig().getIntegerList("FISHING_MESSAGES.EXPLOIT_NUMBERS").contains(getExploitNumber())) {
			player.sendMessage(
					ColorUtil.replace(AntiAutoFish.getInstance().getConfig().getString("FISHING_MESSAGES.MESSAGE")));
		}

		if (AntiAutoFish.getInstance().getConfig().getBoolean("DISALLOW_FISH.ENABLED") && AntiAutoFish.getInstance()
				.getConfig().getIntegerList("DISALLOW_FISH.EXPLOIT_NUMBERS").contains(getExploitNumber())) {
			return true;
		}

		if (AntiAutoFish.getInstance().getConfig().getBoolean("KICKING_PLAYERS.ENABLED") && AntiAutoFish.getInstance()
				.getConfig().getIntegerList("KICKING_PLAYERS.EXPLOIT_NUMBERS").contains(getExploitNumber())) {
			player.kickPlayer(
					ColorUtil.replace(AntiAutoFish.getInstance().getConfig().getString("KICKING_PLAYERS.MESSAGE")));
			return false;
		}

		return false;
	}

	public void setExploitNumber(int exploitNumber) {
		this.exploitNumber = exploitNumber;
	}

	public int getExploitNumber() {
		return exploitNumber;
	}

	public void setNextUse(long nextUse) {
		this.nextUse = nextUse;
	}

	public long getNextUse() {
		return nextUse;
	}

	public UUID getUUID() {
		return uuid;
	}

	public Location getLocation() {
		return location;
	}

}