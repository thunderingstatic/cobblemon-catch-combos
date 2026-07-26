package net.thunderingstatic.catchcombo.combo;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public final class ComboStorage {
    private static final String ROOT = "CatchCombo";
    private static final String SPECIES = "Species";
    private static final String COUNT = "Count";
    private static final String PENDING_SHINY = "PendingShiny";
    private static final String HIGHEST_COMBO = "HighestCombo";
    private static final String HIGHEST_SPECIES = "HighestSpecies";
    private static final String LIFETIME_CATCHES = "LifetimeCatches";
    private static final String SHINY_CATCHES = "ShinyCatches";
    private static final String TOTAL_ACTIVE_MILLIS = "TotalActiveMillis";
    private static final String ACTIVE_SINCE_MILLIS = "ActiveSinceMillis";

    private ComboStorage() {}

    public static ComboData read(ServerPlayer player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains(ROOT)) return ComboData.EMPTY;

        CompoundTag tag = persistent.getCompound(ROOT);
        String species = tag.getString(SPECIES);
        int count = tag.getInt(COUNT);
        int highestCombo = tag.contains(HIGHEST_COMBO) ? tag.getInt(HIGHEST_COMBO) : count;
        String highestSpecies = tag.contains(HIGHEST_SPECIES) ? tag.getString(HIGHEST_SPECIES) : species;

        return new ComboData(
                species,
                count,
                tag.getBoolean(PENDING_SHINY),
                highestCombo,
                highestSpecies,
                tag.getLong(LIFETIME_CATCHES),
                tag.getLong(SHINY_CATCHES),
                tag.getLong(TOTAL_ACTIVE_MILLIS),
                tag.getLong(ACTIVE_SINCE_MILLIS)
        );
    }

    public static void write(ServerPlayer player, ComboData data) {
        CompoundTag tag = new CompoundTag();
        tag.putString(SPECIES, data.species());
        tag.putInt(COUNT, data.count());
        tag.putBoolean(PENDING_SHINY, data.pendingShiny());
        tag.putInt(HIGHEST_COMBO, data.highestCombo());
        tag.putString(HIGHEST_SPECIES, data.highestSpecies());
        tag.putLong(LIFETIME_CATCHES, data.lifetimeCatches());
        tag.putLong(SHINY_CATCHES, data.shinyCatches());
        tag.putLong(TOTAL_ACTIVE_MILLIS, data.totalActiveMillis());
        tag.putLong(ACTIVE_SINCE_MILLIS, data.activeSinceMillis());
        player.getPersistentData().put(ROOT, tag);
    }

    public static void resetCurrent(ServerPlayer player) {
        ComboData current = read(player);
        long now = System.currentTimeMillis();
        ComboData reset = new ComboData(
                "", 0, false,
                current.highestCombo(), current.highestSpecies(),
                current.lifetimeCatches(), current.shinyCatches(),
                current.currentActiveMillis(now), 0L
        );
        write(player, reset);
    }

    public static void resetAll(ServerPlayer player) {
        player.getPersistentData().remove(ROOT);
    }

    public static void copy(ServerPlayer original, ServerPlayer replacement) {
        if (original.getPersistentData().contains(ROOT)) {
            replacement.getPersistentData().put(
                    ROOT,
                    original.getPersistentData().getCompound(ROOT).copy()
            );
        }
    }
}
