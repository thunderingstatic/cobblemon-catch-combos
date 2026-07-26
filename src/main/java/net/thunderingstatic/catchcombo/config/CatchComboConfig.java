package net.thunderingstatic.catchcombo.config;

import java.util.LinkedHashMap;
import java.util.Map;

public final class CatchComboConfig {
    public General general = new General();
    public Hud hud = new Hud();
    public Shiny shiny = new Shiny();
    public Ivs ivs = new Ivs();

    public static final class General {
        public boolean breakOnDifferentSpecies = true;
        public boolean breakOnLogout = false;
        public boolean breakOnDeath = false;
        public int maxCombo = 999;
    }

    public static final class Hud {
        public boolean enabled = true;
        public boolean showPerfectIvs = true;
    }

    public static final class Shiny {
        public boolean enabled = true;
        public Map<Integer, Integer> rolls = defaultShinyRolls();

        private static Map<Integer, Integer> defaultShinyRolls() {
            Map<Integer, Integer> values = new LinkedHashMap<>();
            values.put(1, 2);
            values.put(6, 3);
            values.put(11, 4);
            values.put(21, 5);
            values.put(31, 6);
            return values;
        }
    }

    public static final class Ivs {
        public boolean enabled = true;
        public Map<Integer, Integer> guaranteedPerfect = defaultPerfectIvs();

        private static Map<Integer, Integer> defaultPerfectIvs() {
            Map<Integer, Integer> values = new LinkedHashMap<>();
            values.put(6, 1);
            values.put(11, 2);
            values.put(21, 3);
            values.put(31, 4);
            return values;
        }
    }
}
