package net.thunderingstatic.catchcombo.combo;

public record ComboData(String species, int count, boolean pendingShiny) {
    public static final ComboData EMPTY = new ComboData("", 0, false);

    public boolean isActive() {
        return count > 0 && !species.isBlank();
    }

    public ComboData withPendingShiny(boolean pending) {
        return new ComboData(species, count, pending);
    }
}
