package net.thunderingstatic.catchcombo;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.thunderingstatic.catchcombo.commands.CatchComboCommands;
import net.thunderingstatic.catchcombo.config.ConfigManager;
import net.thunderingstatic.catchcombo.events.CatchListener;
import net.thunderingstatic.catchcombo.events.PlayerDataEvents;
import net.thunderingstatic.catchcombo.shiny.ShinyListener;

@Mod(CatchCombo.MOD_ID)
public final class CatchCombo {
    public static final String MOD_ID = "catchcombo";

    public CatchCombo() {
        ConfigManager.load();
        CatchListener.register();
        ShinyListener.register();
        NeoForge.EVENT_BUS.register(new CatchComboCommands());
        NeoForge.EVENT_BUS.register(new PlayerDataEvents());
    }
}
