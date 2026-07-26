package net.thunderingstatic.catchcombo.api;

import net.minecraft.server.level.ServerPlayer;
import net.thunderingstatic.catchcombo.combo.ComboData;
import net.thunderingstatic.catchcombo.combo.ComboManager;

public final class CatchComboAPI {
    private CatchComboAPI() {}

    public static ComboData getCombo(ServerPlayer player) {
        return ComboManager.get(player);
    }

    public static String getSpecies(ServerPlayer player) {
        return getCombo(player).species();
    }

    public static int getCount(ServerPlayer player) {
        return getCombo(player).count();
    }

    public static int getHighestCombo(ServerPlayer player) {
        return getCombo(player).highestCombo();
    }

    public static long getLifetimeCatches(ServerPlayer player) {
        return getCombo(player).lifetimeCatches();
    }

    public static boolean isChaining(ServerPlayer player, String species) {
        ComboData data = getCombo(player);
        return data.isActive() && data.species().equals(species);
    }

    public static void reset(ServerPlayer player) {
        ComboManager.reset(player);
    }
}
