package net.thunderingstatic.catchcombo.spawning;

import net.thunderingstatic.catchcombo.config.ConfigManager;

import java.util.Map;

public final class SpawnAttractionManager {
    private SpawnAttractionManager() {}

    public static double bonusFor(int comboCount) {
        if (!ConfigManager.get().spawning.enabled) return 0.0;

        double selected = 0.0;
        int selectedThreshold = Integer.MIN_VALUE;
        for (Map.Entry<Integer, Double> entry : ConfigManager.get().spawning.sameSpeciesWeightBonus.entrySet()) {
            if (entry.getKey() <= comboCount && entry.getKey() >= selectedThreshold) {
                selectedThreshold = entry.getKey();
                selected = Math.max(0.0, entry.getValue());
            }
        }
        return selected;
    }

    public static float multiplierFor(int comboCount) {
        return (float) (1.0 + bonusFor(comboCount));
    }

    public static String displayBonus(int comboCount) {
        if (!ConfigManager.get().spawning.enabled) return "Disabled";
        int percent = (int) Math.round(bonusFor(comboCount) * 100.0);
        return percent <= 0 ? "None" : "+" + percent + "% species weight";
    }
}
