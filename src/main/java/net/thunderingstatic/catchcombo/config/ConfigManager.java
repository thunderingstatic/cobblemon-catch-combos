package net.thunderingstatic.catchcombo.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class ConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("CatchCombo/Config");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("catchcombo.json");

    private static volatile CatchComboConfig config = new CatchComboConfig();

    private ConfigManager() {}

    public static CatchComboConfig get() {
        return config;
    }

    public static synchronized void load() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());

            if (!Files.exists(CONFIG_PATH)) {
                config = new CatchComboConfig();
                save();
                LOGGER.info("Created default catch combo config at {}", CONFIG_PATH);
                return;
            }

            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                CatchComboConfig loaded = GSON.fromJson(reader, CatchComboConfig.class);
                config = loaded == null ? new CatchComboConfig() : loaded;
                sanitize(config);
            }
            LOGGER.info("Loaded catch combo config from {}", CONFIG_PATH);
        } catch (Exception exception) {
            LOGGER.error("Unable to load catch combo config; using defaults", exception);
            config = new CatchComboConfig();
        }
    }

    public static synchronized void save() throws IOException {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(config, writer);
        }
    }

    public static int valueAtThreshold(Map<Integer, Integer> thresholds, int combo, int fallback) {
        if (thresholds == null || thresholds.isEmpty()) return fallback;

        int result = fallback;
        int bestThreshold = Integer.MIN_VALUE;
        for (Map.Entry<Integer, Integer> entry : thresholds.entrySet()) {
            Integer threshold = entry.getKey();
            Integer value = entry.getValue();
            if (threshold != null && value != null && threshold <= combo && threshold > bestThreshold) {
                bestThreshold = threshold;
                result = value;
            }
        }
        return result;
    }

    private static void sanitize(CatchComboConfig value) {
        if (value.general == null) value.general = new CatchComboConfig.General();
        if (value.hud == null) value.hud = new CatchComboConfig.Hud();
        if (value.shiny == null) value.shiny = new CatchComboConfig.Shiny();
        if (value.ivs == null) value.ivs = new CatchComboConfig.Ivs();

        value.general.maxCombo = Math.max(1, value.general.maxCombo);
        if (value.shiny.rolls == null) value.shiny.rolls = new CatchComboConfig.Shiny().rolls;
        if (value.ivs.guaranteedPerfect == null) {
            value.ivs.guaranteedPerfect = new CatchComboConfig.Ivs().guaranteedPerfect;
        }
    }
}
