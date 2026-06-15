package fr.maxlego08.menu.api.localization;

import fr.maxlego08.menu.api.configuration.Configuration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class LocalizationManager {

    private static final PlayerLanguageResolver FALLBACK_RESOLVER = new PlayerLanguageResolver() {
        @Override
        public @Nullable String resolve(@Nullable Player player) {
            return defaultLanguage();
        }

        @Override
        public @NotNull String defaultLanguage() {
            return normalize(Configuration.defaultLanguage, "en");
        }
    };

    private static final Map<UUID, String> PLAYER_LANGUAGE_CACHE = new ConcurrentHashMap<>();
    private static volatile PlayerLanguageResolver resolver = FALLBACK_RESOLVER;

    private LocalizationManager() {
    }

    public static void setResolver(@Nullable PlayerLanguageResolver playerLanguageResolver) {
        resolver = playerLanguageResolver == null ? FALLBACK_RESOLVER : playerLanguageResolver;
        clearCache();
    }

    public static @NotNull String resolveLanguage(@Nullable Player player) {
        if (player == null) {
            return defaultLanguage();
        }
        return PLAYER_LANGUAGE_CACHE.computeIfAbsent(player.getUniqueId(), ignored -> normalize(resolver.resolve(player), defaultLanguage()));
    }

    public static @NotNull String defaultLanguage() {
        return normalize(resolver.defaultLanguage(), normalize(Configuration.defaultLanguage, "en"));
    }

    public static void invalidate(@Nullable UUID playerId) {
        if (playerId != null) {
            PLAYER_LANGUAGE_CACHE.remove(playerId);
        }
    }

    public static void clearCache() {
        PLAYER_LANGUAGE_CACHE.clear();
    }

    public static @NotNull String normalize(@Nullable String language, @NotNull String fallback) {
        if (language == null || language.isBlank()) {
            return fallback.toLowerCase(Locale.ROOT);
        }
        String normalized = language.trim().replace('-', '_').toLowerCase(Locale.ROOT);
        int separator = normalized.indexOf('_');
        return separator > 0 ? normalized.substring(0, separator) : normalized;
    }
}
