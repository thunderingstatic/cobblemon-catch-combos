package net.thunderingstatic.catchcombo.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FMLPaths.CONFIGDIR.get().resolve("catchcombo.json");
    private static CatchComboConfig config = new CatchComboConfig();

    private ConfigManager() {}

    public static CatchComboConfig get() {
        return config;
    }

    public static synchronized void load() {
        try {
            Files.createDirectories(PATH.getParent());
            if (Files.exists(PATH)) {
                try (Reader reader = Files.newBufferedReader(PATH)) {
                    CatchComboConfig loaded = GSON.fromJson(reader, CatchComboConfig.class);
                    config = loaded == null ? new CatchComboConfig() : loaded;
                }
            } else {
                config = new CatchComboConfig();
            }
            sanitize();
            save();
        } catch (Exception exception) {
            System.err.println("[Catch Combo] Failed to load config: " + exception.getMessage());
            config = new CatchComboConfig();
        }
    }

    public static synchronized void save() throws IOException {
        try (Writer writer = Files.newBufferedWriter(PATH)) {
            GSON.toJson(config, writer);
        }
    }

    private static void sanitize() {
        CatchComboConfig defaults = new CatchComboConfig();
        if (config.general == null) config.general = defaults.general;
        if (config.hud == null) config.hud = defaults.hud;
        if (config.shiny == null) config.shiny = defaults.shiny;
        if (config.ivs == null) config.ivs = defaults.ivs;
        if (config.notifications == null) config.notifications = defaults.notifications;
        if (config.rewards == null) config.rewards = defaults.rewards;
        if (config.shiny.rolls == null || config.shiny.rolls.isEmpty()) config.shiny.rolls = defaults.shiny.rolls;
        if (config.ivs.guaranteedPerfect == null || config.ivs.guaranteedPerfect.isEmpty()) config.ivs.guaranteedPerfect = defaults.ivs.guaranteedPerfect;
        if (config.notifications.milestones == null) config.notifications.milestones = defaults.notifications.milestones;
        if (config.rewards.milestones == null) config.rewards.milestones = defaults.rewards.milestones;
        config.general.maxCombo = Math.max(1, config.general.maxCombo);
    }
}
