package net.thunderingstatic.catchcombo.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.thunderingstatic.catchcombo.combo.ComboData;
import net.thunderingstatic.catchcombo.combo.ComboManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class CatchComboCommands {
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("catchcombo")
                .executes(context -> show(context.getSource()))
                .then(Commands.literal("reset")
                        .executes(context -> reset(context.getSource()))));
    }

    private static int show(CommandSourceStack source) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            ComboData combo = ComboManager.get(player);
            if (!combo.isActive()) {
                player.sendSystemMessage(Component.literal("You do not have an active catch combo."));
                return 0;
            }

            player.sendSystemMessage(Component.literal(
                    "Current catch combo: " + combo.species() + " ×" + combo.count()
            ));
            return combo.count();
        } catch (Exception exception) {
            source.sendFailure(Component.literal("This command must be run by a player."));
            return 0;
        }
    }

    private static int reset(CommandSourceStack source) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            ComboManager.reset(player);
            player.sendSystemMessage(Component.literal("Catch combo reset."));
            return 1;
        } catch (Exception exception) {
            source.sendFailure(Component.literal("This command must be run by a player."));
            return 0;
        }
    }
}
