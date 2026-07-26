package net.thunderingstatic.catchcombo.spawning;

import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence;
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition;
import net.thunderingstatic.catchcombo.util.ComboFormatting;

public final class CatchComboSpawnInfluence implements SpawningInfluence {
    private final String comboSpecies;
    private final float multiplier;

    public CatchComboSpawnInfluence(String comboSpecies, float multiplier) {
        this.comboSpecies = ComboFormatting.normalizeSpeciesId(comboSpecies);
        this.multiplier = Math.max(1.0F, multiplier);
    }

    @Override
    public float affectWeight(SpawnDetail detail, SpawnablePosition position, float currentWeight) {
        if (!(detail instanceof PokemonSpawnDetail pokemonDetail)) return currentWeight;

        String spawnSpecies = pokemonDetail.getPokemon().getSpecies();
        if (spawnSpecies == null || spawnSpecies.isBlank()) return currentWeight;

        String normalizedSpawnSpecies = ComboFormatting.normalizeSpeciesId(spawnSpecies);
        if (!comboSpecies.equals(normalizedSpawnSpecies)) return currentWeight;

        return currentWeight * multiplier;
    }
}
