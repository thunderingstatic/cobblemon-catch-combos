package net.thunderingstatic.catchcombo.spawning;

import com.cobblemon.mod.common.api.spawning.influence.detector.SpawningInfluenceDetector;

public final class SpawnAttractionListener {
    private static boolean registered;

    private SpawnAttractionListener() {}

    public static void register() {
        if (registered) return;
        SpawningInfluenceDetector.getDetectors().add(new CatchComboInfluenceDetector());
        registered = true;
    }
}
