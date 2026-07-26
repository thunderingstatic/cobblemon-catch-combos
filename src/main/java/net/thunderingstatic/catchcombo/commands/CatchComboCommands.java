package net.thunderingstatic.catchcombo.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.thunderingstatic.catchcombo.combo.ComboData;
import net.thunderingstatic.catchcombo.combo.ComboManager;
import net.thunderingstatic.catchcombo.config.ConfigManager;
import net.thunderingstatic.catchcombo.util.DisplayFormatter;

import java.util.Comparator;
import java.util.List;

public final class CatchComboCommands {
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("catchcombo")
                .executes(context -> show(context.getSource()))
                .then(Commands.literal("stats")
                        .executes(context -> stats(context.getSource())))
                .then(Commands.literal("top")
                        .executes(context -> top(context.getSource())))
                .then(Commands.literal("reset")
                        .executes(context -> reset(context.getSource())))
                .then(Commands.literal("reload")
                        .requires(source -> source.hasPermission(2))
                        .executes(context -> reload(context.getSource()))));
    }

    private static int show(CommandSourceStack source) {
        ServerPlayer player = getPlayer(source);
        if (player == null) return 0;

        ComboData combo = ComboManager.get(player);
        if (!combo.isActive()) {
            player.sendSystemMessage(Component.literal("You do not have an active catch combo."));
            return 0;
        }

        player.sendSystemMessage(Component.literal("Current catch combo: ")
                .append(Component.literal(DisplayFormatter.speciesName(combo.species()))
                        .withStyle(ChatFormatting.YELLOW))
                .append(Component.literal(" ×" + combo.count()).withStyle(ChatFormatting.AQUA)));
        return combo.count();
    }

    private static int stats(CommandSourceStack source) {
        ServerPlayer player = getPlayer(source);
        if (player == null) return 0;

        ComboData combo = ComboManager.get(player);
        long activeMillis = combo.currentActiveMillis(System.currentTimeMillis());

        player.sendSystemMessage(Component.literal("Catch Combo Statistics").withStyle(ChatFormatting.GOLD));

        player.sendSystemMessage(Component.literal("Current Combo").withStyle(ChatFormatting.YELLOW));
        if (combo.isActive()) {
            player.sendSystemMessage(Component.literal("  Species: ")
                    .append(Component.literal(DisplayFormatter.speciesName(combo.species()))
                            .withStyle(ChatFormatting.AQUA)));
            player.sendSystemMessage(Component.literal("  Count: ")
                    .append(Component.literal(Integer.toString(combo.count()))
                            .withStyle(ChatFormatting.AQUA)));
        } else {
            player.sendSystemMessage(Component.literal("  None").withStyle(ChatFormatting.GRAY));
        }

        player.sendSystemMessage(Component.literal("Highest Combo").withStyle(ChatFormatting.YELLOW));
        if (combo.highestCombo() > 0) {
            player.sendSystemMessage(Component.literal("  Species: ")
                    .append(Component.literal(DisplayFormatter.speciesName(combo.highestSpecies()))
                            .withStyle(ChatFormatting.AQUA)));
            player.sendSystemMessage(Component.literal("  Count: ")
                    .append(Component.literal(Integer.toString(combo.highestCombo()))
                            .withStyle(ChatFormatting.AQUA)));
        } else {
            player.sendSystemMessage(Component.literal("  None").withStyle(ChatFormatting.GRAY));
        }

        player.sendSystemMessage(Component.literal("Lifetime Statistics").withStyle(ChatFormatting.YELLOW));
        player.sendSystemMessage(Component.literal("  Combo catches: " + combo.lifetimeCatches()));
        player.sendSystemMessage(Component.literal("  Combo shinies: " + combo.shinyCatches()));
        player.sendSystemMessage(Component.literal("  Active combo time: "
                + DisplayFormatter.duration(activeMillis, ConfigManager.get().general.timeFormat)));
        return 1;
    }

    private static int top(CommandSourceStack source) {
        List<ServerPlayer> ranked = source.getServer().getPlayerList().getPlayers().stream()
                .sorted(Comparator.comparingInt((ServerPlayer player) ->
                        ComboManager.get(player).highestCombo()).reversed())
                .limit(10)
                .toList();

        source.sendSuccess(() -> Component.literal("Catch Combo Leaderboard")
                .withStyle(ChatFormatting.GOLD), false);
        source.sendSuccess(() -> Component.literal("Online players")
                .withStyle(ChatFormatting.DARK_GRAY), false);

        int place = 1;
        for (ServerPlayer player : ranked) {
            ComboData data = ComboManager.get(player);
            if (data.highestCombo() <= 0) continue;

            int currentPlace = place++;
            source.sendSuccess(() -> Component.literal(currentPlace + ". ")
                    .withStyle(ChatFormatting.YELLOW)
                    .append(player.getDisplayName().copy().withStyle(ChatFormatting.WHITE)), false);
            source.sendSuccess(() -> Component.literal("   ")
                    .append(Component.literal(DisplayFormatter.speciesName(data.highestSpecies()))
                            .withStyle(ChatFormatting.AQUA))
                    .append(Component.literal(" ×" + data.highestCombo())
                            .withStyle(ChatFormatting.YELLOW)), false);
        }

        if (place == 1) {
            source.sendSuccess(() -> Component.literal("No recorded combos among online players."), false);
        }
        return place - 1;
    }

    private static int reset(CommandSourceStack source) {
        ServerPlayer player = getPlayer(source);
        if (player == null) return 0;
        ComboManager.reset(player);
        player.sendSystemMessage(Component.literal(
                "Current catch combo reset. Lifetime statistics were preserved."
        ));
        return 1;
    }

    private static int reload(CommandSourceStack source) {
        ConfigManager.load();
        source.sendSuccess(() -> Component.literal("Catch combo configuration reloaded."), true);
        return 1;
    }

    private static ServerPlayer getPlayer(CommandSourceStack source) {
        try {
            return source.getPlayerOrException();
        } catch (Exception exception) {
            source.sendFailure(Component.literal("This command must be run by a player."));
            return null;
        }
    }
}
