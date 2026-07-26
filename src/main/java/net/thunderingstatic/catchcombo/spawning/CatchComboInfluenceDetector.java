package net.thunderingstatic.catchcombo.spawning;

import com.cobblemon.mod.common.api.spawning.influence.SpawningZoneInfluence;
import com.cobblemon.mod.common.api.spawning.influence.UnconditionalSpawningZoneInfluence;
import com.cobblemon.mod.common.api.spawning.influence.detector.SpawningInfluenceDetector;
import com.cobblemon.mod.common.api.spawning.spawner.Spawner;
import com.cobblemon.mod.common.api.spawning.spawner.SpawningZoneInput;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.thunderingstatic.catchcombo.combo.ComboData;
import net.thunderingstatic.catchcombo.combo.ComboManager;
import net.thunderingstatic.catchcombo.config.ConfigManager;

import java.util.List;

public final class CatchComboInfluenceDetector implements SpawningInfluenceDetector {
    @Override
    public List<SpawningZoneInfluence> detectFromInput(Spawner spawner, SpawningZoneInput input) {
        if (!ConfigManager.get().spawning.enabled) return List.of();
        if (!(input.getCause().getEntity() instanceof ServerPlayer player)) return List.of();

        ComboData combo = ComboManager.get(player);
        if (!combo.isActive()) return List.of();

        float multiplier = SpawnAttractionManager.multiplierFor(combo.count());
        if (multiplier <= 1.0F) return List.of();

        return List.of(new UnconditionalSpawningZoneInfluence(
                new CatchComboSpawnInfluence(combo.species(), multiplier)
        ));
    }

    @Override
    public List<SpawningZoneInfluence> detectFromBlock(ServerLevel world, BlockPos pos, BlockState state) {
        return List.of();
    }
}
