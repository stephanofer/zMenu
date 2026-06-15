package fr.maxlego08.menu;

import fr.maxlego08.menu.api.localization.LocalizedText;
import fr.maxlego08.menu.api.localization.LocalizedTextList;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ZMenuItemStackLocalizationCacheTest {

    @Test
    void detectsPlaceholderInLocalizedDisplayNameLocale() {
        LocalizedText text = LocalizedText.of("Static default", Map.of("es", "Hola %player_name%"), "Legacy");

        assertTrue(LocalizedPlaceholderDetector.containsPlaceholder(text));
    }

    @Test
    void detectsPlaceholderInLocalizedLoreLocale() {
        LocalizedTextList textList = LocalizedTextList.of(List.of("Static default"), Map.of("es", List.of("Jugador: %player_name%")), List.of("Legacy"));

        assertTrue(LocalizedPlaceholderDetector.containsPlaceholder(textList));
    }

    @Test
    void keepsStaticLocalizedItemsCacheable() {
        LocalizedText text = LocalizedText.of("Static default", Map.of("es", "Tienda"), "Legacy");
        LocalizedTextList textList = LocalizedTextList.of(List.of("Default"), Map.of("es", List.of("Linea")), List.of("Legacy"));

        assertFalse(LocalizedPlaceholderDetector.containsPlaceholder(text));
        assertFalse(LocalizedPlaceholderDetector.containsPlaceholder(textList));
    }
}
