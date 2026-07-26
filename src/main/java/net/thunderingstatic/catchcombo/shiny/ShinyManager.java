package net.thunderingstatic.catchcombo.shiny;

public final class ShinyManager {
    private ShinyManager() {}

    public static int rollsFor(int combo) {
        if (combo >= 31) return 6;
        if (combo >= 21) return 5;
        if (combo >= 11) return 4;
        if (combo >= 6) return 3;
        if (combo >= 1) return 2;
        return 1;
    }
}
