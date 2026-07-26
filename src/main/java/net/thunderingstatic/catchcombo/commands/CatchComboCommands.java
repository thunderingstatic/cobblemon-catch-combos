package net.thunderingstatic.catchcombo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.thunderingstatic.catchcombo.combo.ComboData;
import net.thunderingstatic.catchcombo.combo.ComboManager;
import net.thunderingstatic.catchcombo.config.ConfigManager;
import net.thunderingstatic.catchcombo.hud.HudManager;
import net.thunderingstatic.catchcombo.ivs.IVManager;
import net.thunderingstatic.catchcombo.shiny.ShinyManager;
import net.thunderingstatic.catchcombo.spawning.SpawnAttractionManager;
import net.thunderingstatic.catchcombo.util.ComboFormatting;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Comparator;

public final class CatchComboCommands {
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("catchcombo")
                .executes(context -> show(context.getSource()))
                .then(Commands.literal("info").executes(context -> info(context.getSource())))
                .then(Commands.literal("stats").executes(context -> stats(context.getSource())))
                .then(Commands.literal("top").executes(context -> top(context.getSource())))
                .then(Commands.literal("reload")
                        .requires(source -> source.hasPermission(2))
                        .executes(context -> reload(context.getSource())))
                .then(Commands.literal("reset").executes(context -> resetSelf(context.getSource()))
                        .then(Commands.argument("player", EntityArgument.player())
                                .requires(source -> source.hasPermission(2))
                                .executes(context -> resetOther(context.getSource(), EntityArgument.getPlayer(context, "player")))))
                .then(Commands.literal("set")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("species", StringArgumentType.word())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                                .executes(context -> set(
                                                        context.getSource(),
                                                        EntityArgument.getPlayer(context, "player"),
                                                        StringArgumentType.getString(context, "species"),
                                                        IntegerArgumentType.getInteger(context, "amount")))))))
                .then(Commands.literal("add")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(context -> add(
                                                context.getSource(),
                                                EntityArgument.getPlayer(context, "player"),
                                                IntegerArgumentType.getInteger(context, "amount"))))))
                .then(Commands.literal("inspect")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> inspect(context.getSource(), EntityArgument.getPlayer(context, "player"))))));
    }

    private static int show(CommandSourceStack source) {
        ServerPlayer player = requirePlayer(source);
        if (player == null) return 0;
        ComboData combo = ComboManager.get(player);
        if (!combo.isActive()) {
            player.sendSystemMessage(Component.literal("You do not have an active catch combo."));
            return 0;
        }
        player.sendSystemMessage(Component.literal("Catch Combo: ").withStyle(ChatFormatting.GOLD)
                .append(Component.literal(ComboFormatting.speciesName(combo.species())).withStyle(ChatFormatting.YELLOW))
                .append(Component.literal(" ×" + combo.count()).withStyle(HudManager.colorForCombo(combo.count()))));
        return combo.count();
    }

    private static int info(CommandSourceStack source) {
        ServerPlayer player = requirePlayer(source);
        if (player == null) return 0;
        ComboData combo = ComboManager.get(player);
        if (!combo.isActive()) {
            player.sendSystemMessage(Component.literal("You do not have an active catch combo."));
            return 0;
        }
        int next = combo.count() < 11 ? 11 : combo.count() < 32 ? 32 : combo.count() < 52 ? 52 : -1;
        player.sendSystemMessage(Component.literal("Current Combo").withStyle(ChatFormatting.GOLD));
        player.sendSystemMessage(Component.literal("Species: " + ComboFormatting.speciesName(combo.species())));
        player.sendSystemMessage(Component.literal("Count: " + combo.count()));
        player.sendSystemMessage(Component.empty());
        player.sendSystemMessage(Component.literal("Current Bonuses").withStyle(ChatFormatting.GOLD));
        player.sendSystemMessage(Component.literal("Guaranteed Perfect IVs: " + IVManager.guaranteedPerfectIvs(combo.count())));
        player.sendSystemMessage(Component.literal("Total Shiny Rolls: " + ShinyManager.rollsFor(combo.count())));
        player.sendSystemMessage(Component.literal("Spawn Attraction: " + SpawnAttractionManager.displayBonus(combo.count())));
        if (next > 0) {
            player.sendSystemMessage(Component.empty());
            player.sendSystemMessage(Component.literal("Next Milestone").withStyle(ChatFormatting.GOLD));
            player.sendSystemMessage(Component.literal(next + " catches (" + (next - combo.count()) + " remaining)"));
        } else {
            player.sendSystemMessage(Component.literal("Maximum bonus tier reached.").withStyle(ChatFormatting.GREEN));
        }
        return combo.count();
    }

    private static int stats(CommandSourceStack source) {
        ServerPlayer player = requirePlayer(source);
        if (player == null) return 0;
        sendStats(player, player);
        return 1;
    }

    private static int inspect(CommandSourceStack source, ServerPlayer target) {
        sendStats(source, target);
        return 1;
    }

    private static void sendStats(CommandSourceStack recipient, ServerPlayer target) {
        ComboData combo = ComboManager.get(target);
        recipient.sendSystemMessage(Component.literal("Catch Combo Statistics: " + target.getGameProfile().getName())
                .withStyle(ChatFormatting.GOLD));
        recipient.sendSystemMessage(Component.literal("Current: " + (combo.isActive()
                ? ComboFormatting.speciesName(combo.species()) + " ×" + combo.count() : "None")));
        recipient.sendSystemMessage(Component.literal("Highest: " + (combo.highestCombo() > 0
                ? ComboFormatting.speciesName(combo.highestSpecies()) + " ×" + combo.highestCombo() : "None")));
        recipient.sendSystemMessage(Component.literal("Lifetime Combo Catches: " + combo.lifetimeCatches()));
        recipient.sendSystemMessage(Component.literal("Shiny Catches During Combos: " + combo.shinyCatches()));
        recipient.sendSystemMessage(Component.literal("Active Combo Time: " + ComboFormatting.duration(
                combo.totalActiveTicks(target.level().getGameTime()), ConfigManager.get().general.timeFormat)));
    }

    private static void sendStats(ServerPlayer recipient, ServerPlayer target) {
        ComboData combo = ComboManager.get(target);
        recipient.sendSystemMessage(Component.literal("Current Combo").withStyle(ChatFormatting.GOLD));
        recipient.sendSystemMessage(Component.literal("Species: " + (combo.isActive() ? ComboFormatting.speciesName(combo.species()) : "None")));
        recipient.sendSystemMessage(Component.literal("Count: " + combo.count()));
        recipient.sendSystemMessage(Component.empty());
        recipient.sendSystemMessage(Component.literal("Highest Combo").withStyle(ChatFormatting.GOLD));
        recipient.sendSystemMessage(Component.literal("Species: " + (combo.highestCombo() > 0 ? ComboFormatting.speciesName(combo.highestSpecies()) : "None")));
        recipient.sendSystemMessage(Component.literal("Count: " + combo.highestCombo()));
        recipient.sendSystemMessage(Component.empty());
        recipient.sendSystemMessage(Component.literal("Lifetime Statistics").withStyle(ChatFormatting.GOLD));
        recipient.sendSystemMessage(Component.literal("Combo Catches: " + combo.lifetimeCatches()));
        recipient.sendSystemMessage(Component.literal("Combo Shinies: " + combo.shinyCatches()));
        recipient.sendSystemMessage(Component.literal("Active Combo Time: " + ComboFormatting.duration(
                combo.totalActiveTicks(target.level().getGameTime()), ConfigManager.get().general.timeFormat)));
    }

    private static int top(CommandSourceStack source) {
        var players = source.getServer().getPlayerList().getPlayers().stream()
                .sorted(Comparator.comparingInt((ServerPlayer player) -> ComboManager.get(player).highestCombo()).reversed())
                .limit(10).toList();
        source.sendSystemMessage(Component.literal("Catch Combo Leaderboard (online players)").withStyle(ChatFormatting.GOLD));
        int rank = 1;
        for (ServerPlayer player : players) {
            ComboData data = ComboManager.get(player);
            if (data.highestCombo() <= 0) continue;
            source.sendSystemMessage(Component.literal(rank + ". " + player.getGameProfile().getName()).withStyle(ChatFormatting.YELLOW));
            source.sendSystemMessage(Component.literal("   " + ComboFormatting.speciesName(data.highestSpecies()) + " ×" + data.highestCombo()));
            rank++;
        }
        if (rank == 1) source.sendSystemMessage(Component.literal("No recorded combos for online players."));
        return rank - 1;
    }

    private static int resetSelf(CommandSourceStack source) {
        ServerPlayer player = requirePlayer(source);
        if (player == null) return 0;
        ComboManager.reset(player);
        player.sendSystemMessage(Component.literal("Catch combo reset."));
        return 1;
    }

    private static int resetOther(CommandSourceStack source, ServerPlayer target) {
        ComboManager.reset(target);
        source.sendSuccess(() -> Component.literal("Reset " + target.getGameProfile().getName() + "'s catch combo."), false);
        return 1;
    }

    private static int set(CommandSourceStack source, ServerPlayer target, String species, int amount) {
        String normalized = ComboFormatting.normalizeSpeciesId(species);
        ComboManager.set(target, normalized, amount);
        source.sendSuccess(() -> Component.literal("Set " + target.getGameProfile().getName() + "'s combo to "
                + ComboFormatting.speciesName(normalized) + " ×" + amount + "."), false);
        return amount;
    }

    private static int add(CommandSourceStack source, ServerPlayer target, int amount) {
        ComboData updated = ComboManager.add(target, amount);
        if (!updated.isActive()) {
            source.sendFailure(Component.literal(target.getGameProfile().getName() + " has no active combo. Use /catchcombo set first."));
            return 0;
        }
        source.sendSuccess(() -> Component.literal("Updated " + target.getGameProfile().getName() + "'s combo to ×" + updated.count() + "."), false);
        return updated.count();
    }

    private static int reload(CommandSourceStack source) {
        ConfigManager.load();
        source.sendSuccess(() -> Component.literal("Catch combo configuration reloaded."), true);
        return 1;
    }

    private static ServerPlayer requirePlayer(CommandSourceStack source) {
        try {
            return source.getPlayerOrException();
        } catch (Exception exception) {
            source.sendFailure(Component.literal("This command must be run by a player."));
            return null;
        }
    }

}
