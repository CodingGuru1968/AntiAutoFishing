package com.codingguru.autofish.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;

import com.codingguru.autofish.data.PlayerFishingData;

public class PlayerHandler {

	private final static PlayerHandler INSTANCE = new PlayerHandler();
	private Map<UUID, PlayerFishingData> cachedData = new HashMap<UUID, PlayerFishingData>();
	
	public PlayerFishingData getFishingData(UUID uuid, Location location) {
		if (cachedData.containsKey(uuid)) {
			return cachedData.get(uuid);
		}
		return new PlayerFishingData(uuid, location);	
	}
	
	public boolean hasFishingData(UUID uuid) {
		return cachedData.containsKey(uuid);
	}
	
	public void addFishingData(UUID uuid, PlayerFishingData data) {
		cachedData.put(uuid, data);
	}
	
	public void removeFishingData(UUID uuid) {
		cachedData.remove(uuid);
	}

	public static PlayerHandler getInstance() {
		return INSTANCE;
	}

}
