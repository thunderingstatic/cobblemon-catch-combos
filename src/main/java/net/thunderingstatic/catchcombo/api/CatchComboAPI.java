package net.thunderingstatic.catchcombo.api;

import net.thunderingstatic.catchcombo.combo.ComboData;
import net.thunderingstatic.catchcombo.combo.ComboManager;
import net.minecraft.server.level.ServerPlayer;

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

    public static void reset(ServerPlayer player) {
        ComboManager.reset(player);
    }
}
