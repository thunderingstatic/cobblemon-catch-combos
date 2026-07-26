package net.thunderingstatic.catchcombo.combo;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public final class ComboStorage {
    private static final String ROOT = "CatchCombo";
    private ComboStorage() {}

    public static ComboData read(ServerPlayer player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains(ROOT)) return ComboData.EMPTY;
        CompoundTag tag = persistent.getCompound(ROOT);
        String species = tag.getString("Species");
        int count = tag.getInt("Count");
        int highest = tag.contains("HighestCombo") ? tag.getInt("HighestCombo") : count;
        String highestSpecies = tag.contains("HighestSpecies") ? tag.getString("HighestSpecies") : species;
        return new ComboData(
                species,
                count,
                tag.getBoolean("PendingShiny"),
                highest,
                highestSpecies,
                tag.contains("LifetimeCatches") ? tag.getLong("LifetimeCatches") : count,
                tag.getLong("ShinyCatches"),
                tag.getLong("AccumulatedActiveTicks"),
                tag.contains("ActiveStartGameTime") ? tag.getLong("ActiveStartGameTime") : (count > 0 ? player.level().getGameTime() : 0)
        );
    }

    public static void write(ServerPlayer player, ComboData data) {
        CompoundTag tag = new CompoundTag();
        tag.putString("Species", data.species());
        tag.putInt("Count", data.count());
        tag.putBoolean("PendingShiny", data.pendingShiny());
        tag.putInt("HighestCombo", data.highestCombo());
        tag.putString("HighestSpecies", data.highestSpecies());
        tag.putLong("LifetimeCatches", data.lifetimeCatches());
        tag.putLong("ShinyCatches", data.shinyCatches());
        tag.putLong("AccumulatedActiveTicks", data.accumulatedActiveTicks());
        tag.putLong("ActiveStartGameTime", data.activeStartGameTime());
        player.getPersistentData().put(ROOT, tag);
    }

    public static void resetActive(ServerPlayer player) {
        ComboData current = read(player);
        long now = player.level().getGameTime();
        ComboData reset = new ComboData("", 0, false, current.highestCombo(), current.highestSpecies(),
                current.lifetimeCatches(), current.shinyCatches(), current.totalActiveTicks(now), 0);
        write(player, reset);
    }

    public static void copy(ServerPlayer original, ServerPlayer replacement) {
        if (original.getPersistentData().contains(ROOT)) {
            replacement.getPersistentData().put(ROOT, original.getPersistentData().getCompound(ROOT).copy());
        }
    }
}
