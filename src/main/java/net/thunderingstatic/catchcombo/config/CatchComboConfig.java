package net.thunderingstatic.catchcombo.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class CatchComboConfig {
    public General general = new General();
    public Hud hud = new Hud();
    public Shiny shiny = new Shiny();
    public Ivs ivs = new Ivs();
    public Spawning spawning = new Spawning();
    public Notifications notifications = new Notifications();
    public Rewards rewards = new Rewards();

    public static final class General {
        public boolean breakOnDifferentSpecies = true;
        public boolean breakOnLogout = false;
        public boolean breakOnDeath = false;
        public int maxCombo = 999;
        public String timeFormat = "clock";
    }

    public static final class Hud {
        public boolean enabled = true;
        public boolean showPerfectIvs = true;
        public boolean milestoneColors = true;
    }

    public static final class Shiny {
        public boolean enabled = true;
        public Map<Integer, Integer> rolls = defaultRolls();

        private static Map<Integer, Integer> defaultRolls() {
            Map<Integer, Integer> values = new LinkedHashMap<>();
            values.put(0, 1);
            values.put(11, 2);
            values.put(32, 4);
            values.put(52, 6);
            return values;
        }
    }

    public static final class Ivs {
        public boolean enabled = true;
        public Map<Integer, Integer> guaranteedPerfect = defaultPerfectIvs();

        private static Map<Integer, Integer> defaultPerfectIvs() {
            Map<Integer, Integer> values = new LinkedHashMap<>();
            values.put(0, 0);
            values.put(11, 2);
            values.put(32, 3);
            values.put(52, 4);
            return values;
        }
    }

    public static final class Spawning {
        public boolean enabled = true;
        public Map<Integer, Double> sameSpeciesWeightBonus = defaultWeightBonuses();

        private static Map<Integer, Double> defaultWeightBonuses() {
            Map<Integer, Double> values = new LinkedHashMap<>();
            values.put(0, 0.0);
            values.put(11, 0.25);
            values.put(32, 0.50);
            values.put(52, 0.85);
            return values;
        }
    }

    public static final class Notifications {
        public boolean enabled = true;
        public boolean chat = true;
        public boolean title = true;
        public boolean sound = true;
        public List<Integer> milestones = new ArrayList<>(List.of(11, 32, 52));
    }

    public static final class Rewards {
        public boolean enabled = true;
        public List<RewardMilestone> milestones = new ArrayList<>();
    }

    public static final class RewardMilestone {
        public int combo = 0;
        public int repeatEvery = 0;
        public List<String> commands = new ArrayList<>();
    }
}
