package com.codingguru.autofish.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.codingguru.autofish.model.FishingData;

public class PlayerHandler {

	private final static PlayerHandler INSTANCE = new PlayerHandler();
	private final Map<UUID, FishingData> fishingData = new HashMap<UUID, FishingData>();

	public FishingData getFishingData(UUID uuid) {
		if (fishingData.containsKey(uuid)) {
			return fishingData.get(uuid);
		}
		return new FishingData(uuid);	
	}
	
	public boolean hasFishingData(UUID uuid) {
		return fishingData.containsKey(uuid);
	}
	
	public void addFishingData(UUID uuid, FishingData data) {
		fishingData.put(uuid, data);
	}
	
	public void removeFishingData(UUID uuid) {
		fishingData.remove(uuid);
	}

	public static PlayerHandler getInstance() {
		return INSTANCE;
	}

}
