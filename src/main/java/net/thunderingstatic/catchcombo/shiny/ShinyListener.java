package net.thunderingstatic.catchcombo.shiny;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.ShinyChanceCalculationEvent;
import net.thunderingstatic.catchcombo.combo.ComboData;
import net.thunderingstatic.catchcombo.combo.ComboManager;

import java.util.function.Consumer;

public final class ShinyListener {
    private ShinyListener() {}

    public static void register() {
        CobblemonEvents.SHINY_CHANCE_CALCULATION.subscribe(
                (Consumer<ShinyChanceCalculationEvent>) ShinyListener::onShinyChanceCalculation
        );
    }

    private static void onShinyChanceCalculation(ShinyChanceCalculationEvent event) {
        event.addModificationFunction((currentChance, player, pokemon) -> {
            if (player == null || pokemon == null) return currentChance;

            ComboData combo = ComboManager.get(player);
            String species = pokemon.getSpecies().getResourceIdentifier().toString();
            if (!combo.pendingShiny() || !combo.species().equals(species)) {
                return currentChance;
            }

            int rolls = ShinyManager.rollsFor(combo.count());
            if (rolls <= 1) return currentChance;

            ComboManager.consumePendingShiny(player);
            return Math.min(1.0F, currentChance + event.getBaseChance() * (rolls - 1));
        });
    }
}
