package net.thunderingstatic.catchcombo.combo;

import net.minecraft.server.level.ServerPlayer;

public final class ComboManager {
    private ComboManager() {}

    public static ComboData get(ServerPlayer player) {
        return ComboStorage.read(player);
    }

    public static ComboData recordCatch(ServerPlayer player, String species) {
        ComboData previous = get(player);
        int count = previous.species().equals(species) ? previous.count() + 1 : 1;
        ComboData updated = new ComboData(species, count, true);
        ComboStorage.write(player, updated);
        return updated;
    }

    public static void consumePendingShiny(ServerPlayer player) {
        ComboData current = get(player);
        if (current.pendingShiny()) {
            ComboStorage.write(player, current.withPendingShiny(false));
        }
    }

    public static void reset(ServerPlayer player) {
        ComboStorage.reset(player);
    }
}
