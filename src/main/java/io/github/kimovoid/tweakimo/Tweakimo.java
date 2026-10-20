package io.github.kimovoid.tweakimo;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.kimovoid.tweakimo.config.TweakimoConfig;
import io.github.kimovoid.tweakimo.hud.HeightLimitHud;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.polyfrost.oneconfig.api.hud.v1.HudManager;
import org.polyfrost.oneconfig.utils.v1.JsonUtils;

import java.util.HashMap;

public class Tweakimo implements ClientModInitializer {

	public static final Logger LOGGER = LogManager.getLogger();
	public static Tweakimo INSTANCE;
	public final HashMap<String, Integer> maps = new HashMap<>();

	@Override
	public void initClient() {
		INSTANCE = this;
		TweakimoConfig.INSTANCE.preload();
		HudManager.register(new HeightLimitHud(), "tweakimo", "assets/tweakimo/icon.svg");
		this.loadLimits();
	}

	private void loadLimits() {
		String url = "https://data-v2.polyfrost.org/hlm/trans-rights-are-human-rights.json";
		JsonElement json = JsonUtils.parseFromUrl(url);

		if (json == null) {
			LOGGER.error("Failed to load bed wars height limits from JSON");
			return;
		}

		maps.clear();

		for (JsonElement element : json.getAsJsonArray()) {
			if (!element.isJsonObject()) continue;
			JsonObject obj = element.getAsJsonObject();

			if (!obj.has("gameType")
					|| !obj.has("name")
					|| !obj.has("maxBuild")
					|| !obj.get("gameType").getAsString().equals("BEDWARS")) continue;

			String name = obj.get("name").getAsString();
			int limit = obj.get("maxBuild").getAsInt();
			maps.put(name, limit);
		}

        LOGGER.info("Loaded {} bed wars height limits from API", maps.size());
	}

	public int getLimit(String mapName) {
		if (mapName != null && this.maps.containsKey(mapName)) {
			return this.maps.get(mapName);
		}
		return 256;
	}
}
