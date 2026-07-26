package net.thunderingstatic.catchcombo.rewards;

import net.thunderingstatic.catchcombo.config.CatchComboConfig;
import net.thunderingstatic.catchcombo.config.ConfigManager;
import net.minecraft.server.level.ServerPlayer;

public final class RewardManager {
    private RewardManager() {}

    public static void grant(ServerPlayer player, String speciesId, int count) {
        if (!ConfigManager.get().rewards.enabled) return;
        for (CatchComboConfig.RewardMilestone reward : ConfigManager.get().rewards.milestones) {
            if (!matches(reward, count)) continue;
            for (String raw : reward.commands) {
                if (raw == null || raw.isBlank()) continue;
                String command = raw
                        .replace("%player%", player.getGameProfile().getName())
                        .replace("%uuid%", player.getUUID().toString())
                        .replace("%species%", speciesId)
                        .replace("%combo%", Integer.toString(count));
                if (command.startsWith("/")) command = command.substring(1);
                player.getServer().getCommands().performPrefixedCommand(
                        player.getServer().createCommandSourceStack().withSuppressedOutput(), command);
            }
        }
    }

    private static boolean matches(CatchComboConfig.RewardMilestone reward, int count) {
        if (reward.combo <= 0) return false;
        if (count == reward.combo) return true;
        return reward.repeatEvery > 0 && count > reward.combo && (count - reward.combo) % reward.repeatEvery == 0;
    }
}
