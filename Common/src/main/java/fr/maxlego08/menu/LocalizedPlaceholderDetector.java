package fr.maxlego08.menu;

import fr.maxlego08.menu.api.localization.LocalizedText;
import fr.maxlego08.menu.api.localization.LocalizedTextList;

import java.util.List;

final class LocalizedPlaceholderDetector {

    private LocalizedPlaceholderDetector() {
    }

    static boolean containsPlaceholder(LocalizedText localizedText) {
        return containsPlaceholder(localizedText.defaultValue())
                || localizedText.locales().values().stream().anyMatch(LocalizedPlaceholderDetector::containsPlaceholder)
                || containsPlaceholder(localizedText.legacyValue());
    }

    static boolean containsPlaceholder(LocalizedTextList localizedTextList) {
        return containsPlaceholder(localizedTextList.defaultValue())
                || localizedTextList.locales().values().stream().anyMatch(LocalizedPlaceholderDetector::containsPlaceholder)
                || containsPlaceholder(localizedTextList.legacyValue());
    }

    private static boolean containsPlaceholder(List<String> values) {
        return values != null && values.stream().anyMatch(LocalizedPlaceholderDetector::containsPlaceholder);
    }

    private static boolean containsPlaceholder(String value) {
        return value != null && value.contains("%");
    }
}
