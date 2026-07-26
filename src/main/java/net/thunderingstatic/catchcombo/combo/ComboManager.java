package net.thunderingstatic.catchcombo.combo;

import net.minecraft.server.level.ServerPlayer;
import net.thunderingstatic.catchcombo.config.ConfigManager;

public final class ComboManager {
    private ComboManager() {}

    public static ComboData get(ServerPlayer player) {
        return ComboStorage.read(player);
    }

    public static ComboData recordCatch(ServerPlayer player, String species, boolean shiny) {
        ComboData previous = get(player);
        long now = System.currentTimeMillis();
        boolean sameSpecies = previous.isActive() && previous.species().equals(species);

        if (previous.isActive() && !sameSpecies
                && !ConfigManager.get().general.breakOnDifferentSpecies) {
            return previous;
        }

        int nextCount = sameSpecies ? previous.count() + 1 : 1;
        nextCount = Math.min(nextCount, ConfigManager.get().general.maxCombo);

        long totalActiveMillis = previous.totalActiveMillis();
        long activeSinceMillis = previous.activeSinceMillis();
        if (!sameSpecies) {
            totalActiveMillis = previous.currentActiveMillis(now);
            activeSinceMillis = now;
        } else if (activeSinceMillis <= 0L) {
            activeSinceMillis = now;
        }

        int highestCombo = previous.highestCombo();
        String highestSpecies = previous.highestSpecies();
        if (nextCount > highestCombo) {
            highestCombo = nextCount;
            highestSpecies = species;
        }

        ComboData updated = new ComboData(
                species,
                nextCount,
                true,
                highestCombo,
                highestSpecies,
                previous.lifetimeCatches() + 1L,
                previous.shinyCatches() + (shiny ? 1L : 0L),
                totalActiveMillis,
                activeSinceMillis
        );
        ComboStorage.write(player, updated);
        return updated;
    }

    public static void consumePendingShiny(ServerPlayer player) {
        ComboData current = get(player);
        if (current.pendingShiny()) ComboStorage.write(player, current.withPendingShiny(false));
    }

    public static void reset(ServerPlayer player) {
        ComboStorage.resetCurrent(player);
    }

    public static void resetAll(ServerPlayer player) {
        ComboStorage.resetAll(player);
    }
}
