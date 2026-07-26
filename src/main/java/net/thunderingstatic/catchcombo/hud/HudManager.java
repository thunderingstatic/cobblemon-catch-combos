package net.thunderingstatic.catchcombo.hud;

import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.thunderingstatic.catchcombo.config.ConfigManager;

public final class HudManager {
    private HudManager() {}

    public static void showCatch(ServerPlayer player, Pokemon pokemon, int count, int improvedIvs) {
        if (!ConfigManager.get().hud.enabled) return;

        MutableComponent message = Component.literal("Catch Combo: ")
                .withStyle(ChatFormatting.GOLD)
                .append(pokemon.getSpecies().getTranslatedName().copy().withStyle(ChatFormatting.YELLOW))
                .append(Component.literal(" ×" + count).withStyle(ChatFormatting.AQUA));

        if (ConfigManager.get().hud.showPerfectIvs && improvedIvs > 0) {
            message.append(Component.literal(
                    "  •  " + improvedIvs + " perfect IV" + (improvedIvs == 1 ? "" : "s")
            ).withStyle(ChatFormatting.GREEN));
        }

        player.displayClientMessage(message, true);
    }
}
