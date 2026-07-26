package net.thunderingstatic.catchcombo.combo;

public record ComboData(
        String species,
        int count,
        boolean pendingShiny,
        int highestCombo,
        String highestSpecies,
        long lifetimeCatches,
        long shinyCatches,
        long accumulatedActiveTicks,
        long activeStartGameTime
) {
    public static final ComboData EMPTY = new ComboData("", 0, false, 0, "", 0, 0, 0, 0);

    public boolean isActive() {
        return count > 0 && !species.isBlank();
    }

    public ComboData withPendingShiny(boolean pending) {
        return new ComboData(species, count, pending, highestCombo, highestSpecies,
                lifetimeCatches, shinyCatches, accumulatedActiveTicks, activeStartGameTime);
    }

    public long totalActiveTicks(long now) {
        if (!isActive() || activeStartGameTime <= 0) return accumulatedActiveTicks;
        return accumulatedActiveTicks + Math.max(0, now - activeStartGameTime);
    }
}
