package net.thunderingstatic.catchcombo.shiny;

import net.thunderingstatic.catchcombo.config.ConfigManager;

public final class ShinyManager {
    private ShinyManager() {}

    public static int rollsFor(int combo) {
        if (!ConfigManager.get().shiny.enabled) return 1;
        return Math.max(1, ConfigManager.valueAtThreshold(
                ConfigManager.get().shiny.rolls, combo, 1
        ));
    }
}
