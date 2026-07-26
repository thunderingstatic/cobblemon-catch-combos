package net.thunderingstatic.catchcombo.shiny;

import net.thunderingstatic.catchcombo.config.ConfigManager;

import java.util.Map;

public final class ShinyManager {
    private ShinyManager() {}

    public static int rollsFor(int combo) {
        if (!ConfigManager.get().shiny.enabled) return 1;
        int result = 1;
        for (Map.Entry<Integer, Integer> entry : ConfigManager.get().shiny.rolls.entrySet()) {
            if (combo >= entry.getKey()) result = Math.max(result, entry.getValue());
        }
        return Math.max(1, result);
    }
}
