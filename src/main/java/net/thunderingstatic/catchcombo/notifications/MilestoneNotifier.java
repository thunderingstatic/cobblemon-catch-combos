package net.thunderingstatic.catchcombo.notifications;

import net.thunderingstatic.catchcombo.config.ConfigManager;
import net.thunderingstatic.catchcombo.ivs.IVManager;
import net.thunderingstatic.catchcombo.shiny.ShinyManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public final class MilestoneNotifier {
    private MilestoneNotifier() {}

    public static void notifyIfReached(ServerPlayer player, String speciesName, int count) {
        var settings = ConfigManager.get().notifications;
        if (!settings.enabled || !settings.milestones.contains(count)) return;
        int ivs = IVManager.guaranteedPerfectIvs(count);
        int rolls = ShinyManager.rollsFor(count);
        if (settings.chat) {
            player.sendSystemMessage(Component.literal("Catch Combo Milestone Reached").withStyle(ChatFormatting.GOLD));
            player.sendSystemMessage(Component.literal(speciesName + " ×" + count).withStyle(ChatFormatting.YELLOW));
            player.sendSystemMessage(Component.literal(ivs + " guaranteed perfect IVs | " + rolls + " total shiny rolls")
                    .withStyle(ChatFormatting.AQUA));
        }
        if (settings.title) {
            player.connection.send(new ClientboundSetTitlesAnimationPacket(10, 50, 15));
            player.connection.send(new ClientboundSetTitleTextPacket(
                    Component.literal("Catch Combo Milestone").withStyle(ChatFormatting.GOLD)));
            player.connection.send(new ClientboundSetSubtitleTextPacket(
                    Component.literal(speciesName + " ×" + count).withStyle(ChatFormatting.YELLOW)));
        }
        if (settings.sound) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP,
                    SoundSource.PLAYERS, 0.8F, 1.1F);
        }
    }
}
