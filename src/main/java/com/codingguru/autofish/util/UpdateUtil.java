package com.codingguru.autofish.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import com.codingguru.autofish.AntiAutoFish;

public class UpdateUtil {

	private final int RESOURCE_ID = 116664;

	public boolean hasNewUpdate() {
		if (!AntiAutoFish.getInstance().getConfig().getBoolean("check-for-updates", true)) {
			return false;
		}

		String currentVersion = AntiAutoFish.getInstance().getDescription().getVersion();

		try (InputStream inputStream = new URI("https://api.spigotmc.org/legacy/update.php?resource=" + RESOURCE_ID)
				.toURL().openStream(); Scanner scanner = new Scanner(inputStream)) {

			if (scanner.hasNext()) {
				String updatedVersion = scanner.next();
				if (!currentVersion.equalsIgnoreCase(updatedVersion)) {
					return true; // found update
				}
			}

		} catch (IOException exception) {
		} catch (URISyntaxException e) {
		}

		return false;
	}
}