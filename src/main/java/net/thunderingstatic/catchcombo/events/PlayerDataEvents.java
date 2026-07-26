package net.thunderingstatic.catchcombo.events;

import net.thunderingstatic.catchcombo.combo.ComboStorage;
import net.thunderingstatic.catchcombo.config.ConfigManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public final class PlayerDataEvents {
    @SubscribeEvent
    public void clonePlayer(PlayerEvent.Clone event) {
        if (event.getOriginal() instanceof ServerPlayer original && event.getEntity() instanceof ServerPlayer replacement) {
            ComboStorage.copy(original, replacement);
        }
    }

    @SubscribeEvent
    public void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (ConfigManager.get().general.breakOnLogout && event.getEntity() instanceof ServerPlayer player) {
            ComboStorage.resetActive(player);
        }
    }

    @SubscribeEvent
    public void death(LivingDeathEvent event) {
        if (ConfigManager.get().general.breakOnDeath && event.getEntity() instanceof ServerPlayer player) {
            ComboStorage.resetActive(player);
        }
    }
}
