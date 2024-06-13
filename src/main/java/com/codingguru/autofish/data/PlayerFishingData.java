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

		if (AntiAutoFish.getInstance().getConfig().getBoolean("max-fish-distance.enabled")
				&& getLocation().distance(player.getLocation()) > AntiAutoFish.getInstance().getConfig()
						.getInt("max-fish-distance.distance")) {
			PlayerHandler.getInstance().removeFishingData(player.getUniqueId());
			return false;
		} else {
			if (!getLocation().equals(player.getLocation())) {
				PlayerHandler.getInstance().removeFishingData(player.getUniqueId());
				return false;
			}
		}

		setExploitNumber(getExploitNumber() + 1);

		if (AntiAutoFish.getInstance().getConfig().getBoolean("fishing-cooldown.enabled") && AntiAutoFish.getInstance()
				.getConfig().getIntegerList("fishing-cooldown.exploit-numbers").contains(getExploitNumber())) {
			long total = 1000 * AntiAutoFish.getInstance().getConfig().getInt("fishing-cooldown.length");
			setNextUse(System.currentTimeMillis() + total);
		}

		if (AntiAutoFish.getInstance().getConfig().getBoolean("fishing-messages.enabled") && AntiAutoFish.getInstance()
				.getConfig().getIntegerList("fishing-messages.exploit-numbers").contains(getExploitNumber())) {
			player.sendMessage(
					ColorUtil.replace(AntiAutoFish.getInstance().getConfig().getString("fishing-messages.message")));
		}

		if (AntiAutoFish.getInstance().getConfig().getBoolean("disallow-fish.enabled") && AntiAutoFish.getInstance()
				.getConfig().getIntegerList("disallow-fish.exploit-numbers").contains(getExploitNumber())) {
			return true;
		}

		if (AntiAutoFish.getInstance().getConfig().getBoolean("kicking-players.enabled") && AntiAutoFish.getInstance()
				.getConfig().getIntegerList("kicking-players.exploit-numbers").contains(getExploitNumber())) {
			player.kickPlayer(
					ColorUtil.replace(AntiAutoFish.getInstance().getConfig().getString("kicking-players.message")));
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