package fr.maxlego08.menu.api.localization;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class LocalizedTextList {

    private final List<String> defaultValue;
    private final Map<String, List<String>> locales;
    private final List<String> legacyValue;

    private LocalizedTextList(@Nullable List<String> defaultValue, @NotNull Map<String, List<String>> locales, @Nullable List<String> legacyValue) {
        this.defaultValue = defaultValue == null ? null : List.copyOf(defaultValue);
        Map<String, List<String>> normalized = new LinkedHashMap<>();
        locales.forEach((language, value) -> {
            if (language != null && value != null) {
                String key = language.trim().replace('-', '_').toLowerCase(Locale.ROOT);
                List<String> copy = List.copyOf(value);
                normalized.put(key, copy);
                normalized.putIfAbsent(LocalizationManager.normalize(key, key), copy);
            }
        });
        this.locales = Collections.unmodifiableMap(normalized);
        this.legacyValue = legacyValue == null ? null : List.copyOf(legacyValue);
    }

    public static @NotNull LocalizedTextList legacy(@Nullable List<String> value) {
        return new LocalizedTextList(null, Collections.emptyMap(), value == null ? Collections.emptyList() : value);
    }

    public static @NotNull LocalizedTextList of(@Nullable List<String> defaultValue, @NotNull Map<String, List<String>> locales, @Nullable List<String> legacyValue) {
        return new LocalizedTextList(defaultValue, locales, legacyValue);
    }

    public @NotNull List<String> resolve(@Nullable Player player) {
        return this.resolve(LocalizationManager.resolveLanguage(player));
    }

    public @NotNull List<String> resolve(@Nullable String language) {
        String normalizedLanguage = LocalizationManager.normalize(language, LocalizationManager.defaultLanguage());
        String configuredDefault = LocalizationManager.defaultLanguage();
        List<String> value = this.findLocale(normalizedLanguage);
        if (value == null) {
            value = this.findLocale(configuredDefault);
        }
        if (value == null) {
            value = this.defaultValue;
        }
        if (value == null) {
            value = this.legacyValue;
        }
        return value == null ? Collections.emptyList() : value;
    }

    private @Nullable List<String> findLocale(@NotNull String language) {
        List<String> value = this.locales.get(language);
        if (value != null) {
            return value;
        }
        for (Map.Entry<String, List<String>> entry : this.locales.entrySet()) {
            if (LocalizationManager.normalize(entry.getKey(), entry.getKey()).equals(language)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public boolean isLocalized() {
        return this.defaultValue != null || !this.locales.isEmpty();
    }

    public @Nullable List<String> defaultValue() {
        return this.defaultValue;
    }

    public @NotNull Map<String, List<String>> locales() {
        return this.locales;
    }

    public @NotNull List<String> legacyValue() {
        return this.legacyValue == null ? Collections.emptyList() : this.legacyValue;
    }
}
