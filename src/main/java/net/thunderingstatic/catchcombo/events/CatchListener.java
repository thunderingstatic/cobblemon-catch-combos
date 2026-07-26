package net.thunderingstatic.catchcombo.events;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.PokemonCapturedEvent;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.server.level.ServerPlayer;
import net.thunderingstatic.catchcombo.combo.ComboData;
import net.thunderingstatic.catchcombo.combo.ComboManager;
import net.thunderingstatic.catchcombo.hud.HudManager;
import net.thunderingstatic.catchcombo.ivs.IVManager;
import net.thunderingstatic.catchcombo.rewards.RewardManager;

import java.util.function.Consumer;

public final class CatchListener {
    private CatchListener() {}

    public static void register() {
        CobblemonEvents.POKEMON_CAPTURED.subscribe(
                (Consumer<PokemonCapturedEvent>) CatchListener::onPokemonCaptured
        );
    }

    private static void onPokemonCaptured(PokemonCapturedEvent event) {
        ServerPlayer player = event.getPlayer();
        Pokemon pokemon = event.getPokemon();
        String species = pokemon.getSpecies().getResourceIdentifier().toString();

        ComboData combo = ComboManager.recordCatch(player, species, pokemon.getShiny());
        int improved = IVManager.applyGuaranteedPerfectIvs(
                pokemon,
                IVManager.guaranteedPerfectIvs(combo.count())
        );

        RewardManager.grantMilestoneRewards(player, species, combo.count());
        HudManager.showCatch(player, pokemon, combo.count(), improved);
    }
}
