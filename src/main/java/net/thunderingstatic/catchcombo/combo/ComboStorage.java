package net.thunderingstatic.catchcombo.combo;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public final class ComboStorage {
    private static final String ROOT = "CatchCombo";
    private static final String SPECIES = "Species";
    private static final String COUNT = "Count";
    private static final String PENDING_SHINY = "PendingShiny";

    private ComboStorage() {}

    public static ComboData read(ServerPlayer player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains(ROOT)) {
            return ComboData.EMPTY;
        }

        CompoundTag tag = persistent.getCompound(ROOT);
        return new ComboData(
                tag.getString(SPECIES),
                tag.getInt(COUNT),
                tag.getBoolean(PENDING_SHINY)
        );
    }

    public static void write(ServerPlayer player, ComboData data) {
        CompoundTag tag = new CompoundTag();
        tag.putString(SPECIES, data.species());
        tag.putInt(COUNT, data.count());
        tag.putBoolean(PENDING_SHINY, data.pendingShiny());
        player.getPersistentData().put(ROOT, tag);
    }

    public static void reset(ServerPlayer player) {
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
