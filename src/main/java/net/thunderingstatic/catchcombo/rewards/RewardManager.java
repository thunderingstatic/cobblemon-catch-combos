package net.thunderingstatic.catchcombo.rewards;

import net.minecraft.server.level.ServerPlayer;
import net.thunderingstatic.catchcombo.config.CatchComboConfig;
import net.thunderingstatic.catchcombo.config.ConfigManager;

public final class RewardManager {
    private RewardManager() {}

    public static void grantMilestoneRewards(ServerPlayer player, String species, int combo) {
        CatchComboConfig.Rewards rewards = ConfigManager.get().rewards;
        if (rewards == null || !rewards.enabled || rewards.milestones == null) return;

        for (CatchComboConfig.Reward reward : rewards.milestones) {
            if (reward == null || reward.combo != combo || reward.commands == null) continue;

            for (String configuredCommand : reward.commands) {
                if (configuredCommand == null || configuredCommand.isBlank()) continue;

                String command = configuredCommand
                        .replace("%player%", player.getGameProfile().getName())
                        .replace("%species%", species)
                        .replace("%combo%", Integer.toString(combo));

                if (command.startsWith("/")) command = command.substring(1);
                player.getServer().getCommands().performPrefixedCommand(
                        player.getServer().createCommandSourceStack().withSuppressedOutput(),
                        command
                );
            }
        }
    }
}
