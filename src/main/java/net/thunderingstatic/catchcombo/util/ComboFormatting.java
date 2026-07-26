package net.thunderingstatic.catchcombo.util;

import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public final class ComboFormatting {
    private ComboFormatting() {}

    public static String speciesName(String speciesId) {
        if (speciesId == null || speciesId.isBlank()) return "None";
        ResourceLocation id = ResourceLocation.tryParse(speciesId);
        String path = id == null ? speciesId : id.getPath();
        String[] words = path.replace('-', '_').split("_");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.isBlank()) continue;
            if (!result.isEmpty()) result.append(' ');
            result.append(word.substring(0, 1).toUpperCase(Locale.ROOT));
            if (word.length() > 1) result.append(word.substring(1).toLowerCase(Locale.ROOT));
        }
        return result.isEmpty() ? speciesId : result.toString();
    }

    public static String normalizeSpeciesId(String input) {
        String value = input.trim().toLowerCase(Locale.ROOT);
        return value.contains(":") ? value : "cobblemon:" + value;
    }

    public static String duration(long ticks, String format) {
        long seconds = Math.max(0L, ticks / 20L);
        long hours = seconds / 3600L;
        long minutes = (seconds % 3600L) / 60L;
        long remainder = seconds % 60L;
        if ("compact".equalsIgnoreCase(format)) {
            if (hours > 0) return String.format(Locale.ROOT, "%dh %02dm %02ds", hours, minutes, remainder);
            return String.format(Locale.ROOT, "%dm %02ds", minutes, remainder);
        }
        return String.format(Locale.ROOT, "%02d:%02d:%02d", hours, minutes, remainder);
    }
}
