package fr.maxlego08.menu.api.localization;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public final class LocalizedText {

    private final String defaultValue;
    private final Map<String, String> locales;
    private final String legacyValue;

    private LocalizedText(@Nullable String defaultValue, @NotNull Map<String, String> locales, @Nullable String legacyValue) {
        this.defaultValue = defaultValue;
        this.locales = Collections.unmodifiableMap(new LinkedHashMap<>(locales));
        this.legacyValue = legacyValue;
    }

    public static @NotNull LocalizedText legacy(@Nullable String value) {
        return new LocalizedText(null, Collections.emptyMap(), value);
    }

    public static @NotNull LocalizedText of(@Nullable String defaultValue, @NotNull Map<String, String> locales, @Nullable String legacyValue) {
        Map<String, String> normalized = new LinkedHashMap<>();
        locales.forEach((language, value) -> {
            if (language != null && value != null) {
                String key = language.trim().replace('-', '_').toLowerCase(Locale.ROOT);
                normalized.put(key, value);
                normalized.putIfAbsent(LocalizationManager.normalize(key, key), value);
            }
        });
        return new LocalizedText(defaultValue, normalized, legacyValue);
    }

    public @NotNull String resolve(@Nullable Player player) {
        return this.resolve(LocalizationManager.resolveLanguage(player));
    }

    public @NotNull String resolve(@Nullable String language) {
        String normalizedLanguage = LocalizationManager.normalize(language, LocalizationManager.defaultLanguage());
        String configuredDefault = LocalizationManager.defaultLanguage();
        String value = this.findLocale(normalizedLanguage);
        if (value == null) {
            value = this.findLocale(configuredDefault);
        }
        if (value == null) {
            value = this.defaultValue;
        }
        if (value == null) {
            value = this.legacyValue;
        }
        return value == null ? "" : value;
    }

    private @Nullable String findLocale(@NotNull String language) {
        String value = this.locales.get(language);
        if (value != null) {
            return value;
        }
        for (Map.Entry<String, String> entry : this.locales.entrySet()) {
            if (LocalizationManager.normalize(entry.getKey(), entry.getKey()).equals(language)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public boolean isLocalized() {
        return this.defaultValue != null || !this.locales.isEmpty();
    }

    public @Nullable String defaultValue() {
        return this.defaultValue;
    }

    public @NotNull Map<String, String> locales() {
        return this.locales;
    }

    public @Nullable String legacyValue() {
        return this.legacyValue;
    }
}
