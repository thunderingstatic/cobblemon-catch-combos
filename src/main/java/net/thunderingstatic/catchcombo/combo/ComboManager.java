package net.thunderingstatic.catchcombo.combo;

import net.thunderingstatic.catchcombo.config.ConfigManager;
import net.minecraft.server.level.ServerPlayer;

public final class ComboManager {
    private ComboManager() {}

    public static ComboData get(ServerPlayer player) {
        return ComboStorage.read(player);
    }

    public static ComboData recordCatch(ServerPlayer player, String species, boolean shiny) {
        ComboData previous = get(player);
        boolean same = previous.isActive() && previous.species().equals(species);
        int count = same ? previous.count() + 1 : 1;
        count = Math.min(count, ConfigManager.get().general.maxCombo);
        long now = player.level().getGameTime();
        long accumulated = previous.accumulatedActiveTicks();
        long activeStart = previous.activeStartGameTime();
        if (!same) {
            accumulated = previous.totalActiveTicks(now);
            activeStart = now;
        }
        int highest = Math.max(previous.highestCombo(), count);
        String highestSpecies = count >= previous.highestCombo() ? species : previous.highestSpecies();
        ComboData updated = new ComboData(species, count, true, highest, highestSpecies,
                previous.lifetimeCatches() + 1, previous.shinyCatches() + (shiny ? 1 : 0), accumulated, activeStart);
        ComboStorage.write(player, updated);
        return updated;
    }

    public static ComboData set(ServerPlayer player, String species, int count) {
        ComboData previous = get(player);
        int bounded = Math.max(0, Math.min(count, ConfigManager.get().general.maxCombo));
        long now = player.level().getGameTime();
        if (bounded == 0) {
            reset(player);
            return get(player);
        }
        int highest = Math.max(previous.highestCombo(), bounded);
        String highestSpecies = bounded >= previous.highestCombo() ? species : previous.highestSpecies();
        ComboData updated = new ComboData(species, bounded, true, highest, highestSpecies,
                previous.lifetimeCatches(), previous.shinyCatches(), previous.totalActiveTicks(now), now);
        ComboStorage.write(player, updated);
        return updated;
    }

    public static ComboData add(ServerPlayer player, int amount) {
        ComboData current = get(player);
        if (!current.isActive()) return current;
        return set(player, current.species(), current.count() + amount);
    }

    public static void consumePendingShiny(ServerPlayer player) {
        ComboData current = get(player);
        if (current.pendingShiny()) ComboStorage.write(player, current.withPendingShiny(false));
    }

    public static void reset(ServerPlayer player) {
        ComboStorage.resetActive(player);
    }
}
