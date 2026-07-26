package net.thunderingstatic.catchcombo.events;

import net.thunderingstatic.catchcombo.combo.ComboStorage;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public final class PlayerDataEvents {
    @SubscribeEvent
    public void clonePlayer(PlayerEvent.Clone event) {
        if (event.getOriginal() instanceof ServerPlayer original
                && event.getEntity() instanceof ServerPlayer replacement) {
            ComboStorage.copy(original, replacement);
        }
    }
}
