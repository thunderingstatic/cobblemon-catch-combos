package net.thunderingstatic.catchcombo.combo;

public record ComboData(
        String species,
        int count,
        boolean pendingShiny,
        int highestCombo,
        String highestSpecies,
        long lifetimeCatches,
        long shinyCatches,
        long totalActiveMillis,
        long activeSinceMillis
) {
    public static final ComboData EMPTY = new ComboData("", 0, false, 0, "", 0L, 0L, 0L, 0L);

    public boolean isActive() {
        return count > 0 && !species.isBlank();
    }

    public ComboData withPendingShiny(boolean pending) {
        return new ComboData(
                species, count, pending, highestCombo, highestSpecies,
                lifetimeCatches, shinyCatches, totalActiveMillis, activeSinceMillis
        );
    }

    public long currentActiveMillis(long now) {
        return isActive() && activeSinceMillis > 0L
                ? totalActiveMillis + Math.max(0L, now - activeSinceMillis)
                : totalActiveMillis;
    }
}
