package net.thunderingstatic.catchcombo.util;

import java.util.Locale;

public final class DisplayFormatter {
    private DisplayFormatter() {}

    public static String speciesName(String identifier) {
        if (identifier == null || identifier.isBlank()) return "None";

        String path = identifier;
        int namespaceSeparator = path.indexOf(':');
        if (namespaceSeparator >= 0 && namespaceSeparator + 1 < path.length()) {
            path = path.substring(namespaceSeparator + 1);
        }

        String[] words = path.split("[_\\- ]+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.isBlank()) continue;
            if (!result.isEmpty()) result.append(' ');
            result.append(Character.toUpperCase(word.charAt(0)));
            if (word.length() > 1) {
                result.append(word.substring(1).toLowerCase(Locale.ROOT));
            }
        }
        return result.isEmpty() ? path : result.toString();
    }

    public static String duration(long millis, String format) {
        long seconds = Math.max(0L, millis / 1000L);
        long hours = seconds / 3600L;
        long minutes = (seconds % 3600L) / 60L;
        long remainingSeconds = seconds % 60L;

        if ("compact".equalsIgnoreCase(format)) {
            if (hours > 0L) {
                return String.format(Locale.ROOT, "%dh %02dm %02ds", hours, minutes, remainingSeconds);
            }
            if (minutes > 0L) {
                return String.format(Locale.ROOT, "%dm %02ds", minutes, remainingSeconds);
            }
            return remainingSeconds + "s";
        }

        return String.format(Locale.ROOT, "%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }
}
