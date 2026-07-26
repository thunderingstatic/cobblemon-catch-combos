package net.thunderingstatic.catchcombo.ivs;

import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.thunderingstatic.catchcombo.config.ConfigManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class IVManager {
    private IVManager() {}

    public static int guaranteedPerfectIvs(int combo) {
        if (!ConfigManager.get().ivs.enabled) return 0;
        int result = 0;
        for (Map.Entry<Integer, Integer> entry : ConfigManager.get().ivs.guaranteedPerfect.entrySet()) {
            if (combo >= entry.getKey()) result = Math.max(result, entry.getValue());
        }
        return Math.max(0, Math.min(6, result));
    }

    public static int applyGuaranteedPerfectIvs(Pokemon pokemon, int amount) {
        if (amount <= 0) return 0;
        List<Stat> stats = new ArrayList<>(List.of(
                Stats.HP, Stats.ATTACK, Stats.DEFENCE,
                Stats.SPECIAL_ATTACK, Stats.SPECIAL_DEFENCE, Stats.SPEED
        ));
        Collections.shuffle(stats);
        int changed = 0;
        for (Stat stat : stats) {
            if (changed >= amount) break;
            if (pokemon.getIvs().getOrDefault(stat) < 31) {
                pokemon.setIV(stat, 31);
                changed++;
            }
        }
        return changed;
    }
}
