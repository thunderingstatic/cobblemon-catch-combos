package net.thunderingstatic.catchcombo.ivs;

import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Pokemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class IVManager {
    private IVManager() {}

    public static int guaranteedPerfectIvs(int combo) {
        if (combo >= 31) return 4;
        if (combo >= 21) return 3;
        if (combo >= 11) return 2;
        if (combo >= 6) return 1;
        return 0;
    }

    public static int applyGuaranteedPerfectIvs(Pokemon pokemon, int amount) {
        if (amount <= 0) return 0;

        List<Stat> stats = new ArrayList<>(List.of(
                Stats.HP,
                Stats.ATTACK,
                Stats.DEFENCE,
                Stats.SPECIAL_ATTACK,
                Stats.SPECIAL_DEFENCE,
                Stats.SPEED
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
