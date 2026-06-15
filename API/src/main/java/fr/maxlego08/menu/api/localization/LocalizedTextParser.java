package fr.maxlego08.menu.api.localization;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class LocalizedTextParser {

    private LocalizedTextParser() {
    }

    public static @NotNull LocalizedText text(@Nullable Object object, @Nullable String legacyValue) {
        if (object instanceof ConfigurationSection section) {
            return text(section.getValues(false), legacyValue);
        }
        if (object instanceof Map<?, ?> map) {
            String defaultValue = asString(map.get("default"));
            Map<String, String> locales = new LinkedHashMap<>();
            Object localesObject = map.get("locales");
            if (localesObject instanceof ConfigurationSection section) {
                localesObject = section.getValues(false);
            }
            if (localesObject instanceof Map<?, ?> localeMap) {
                localeMap.forEach((language, value) -> {
                    String localeValue = asString(value);
                    if (language != null && localeValue != null) {
                        locales.put(String.valueOf(language), localeValue);
                    }
                });
            }
            return LocalizedText.of(defaultValue, locales, legacyValue);
        }
        return LocalizedText.legacy(asString(object, legacyValue));
    }

    public static @NotNull LocalizedTextList textList(@Nullable Object object, @Nullable List<String> legacyValue) {
        if (object instanceof ConfigurationSection section) {
            return textList(section.getValues(false), legacyValue);
        }
        if (object instanceof Map<?, ?> map) {
            List<String> defaultValue = asStringList(map.get("default"));
            Map<String, List<String>> locales = new LinkedHashMap<>();
            Object localesObject = map.get("locales");
            if (localesObject instanceof ConfigurationSection section) {
                localesObject = section.getValues(false);
            }
            if (localesObject instanceof Map<?, ?> localeMap) {
                localeMap.forEach((language, value) -> {
                    List<String> localeValue = asStringList(value);
                    if (language != null && localeValue != null) {
                        locales.put(String.valueOf(language), localeValue);
                    }
                });
            }
            return LocalizedTextList.of(defaultValue, locales, legacyValue);
        }
        return LocalizedTextList.legacy(asStringList(object, legacyValue));
    }

    public static @Nullable String asString(@Nullable Object object) {
        return asString(object, null);
    }

    public static @Nullable String asString(@Nullable Object object, @Nullable String fallback) {
        if (object == null) {
            return fallback;
        }
        if (object instanceof String string) {
            return string;
        }
        if (object instanceof List<?> list) {
            List<String> strings = asStringList(list, Collections.emptyList());
            return String.join("", strings);
        }
        return String.valueOf(object);
    }

    public static @Nullable List<String> asStringList(@Nullable Object object) {
        return asStringList(object, null);
    }

    public static @Nullable List<String> asStringList(@Nullable Object object, @Nullable List<String> fallback) {
        if (object == null) {
            return fallback;
        }
        if (object instanceof List<?> list) {
            List<String> values = new ArrayList<>(list.size());
            for (Object value : list) {
                if (value != null) {
                    values.add(String.valueOf(value));
                }
            }
            return values;
        }
        if (object instanceof String string) {
            return Arrays.asList(string.split("\\n"));
        }
        return List.of(String.valueOf(object));
    }
}
