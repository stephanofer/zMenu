package fr.maxlego08.menu.api.localization;

import fr.maxlego08.menu.api.configuration.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalizedTextTest {

    @AfterEach
    void tearDown() {
        Configuration.defaultLanguage = "en";
        LocalizationManager.setResolver(null);
    }

    @Test
    void resolvesPlayerLanguageThenConfiguredDefaultThenLegacy() {
        Configuration.defaultLanguage = "en";
        LocalizedText text = LocalizedText.of("Default", Map.of("es", "Tienda", "en", "Shop"), "Legacy");

        assertEquals("Tienda", text.resolve("es_AR"));
        assertEquals("Shop", text.resolve("fr"));
    }

    @Test
    void fallsBackToLegacyAndSafeEmptyValue() {
        assertEquals("Legacy", LocalizedText.of(null, Map.of(), "Legacy").resolve("es"));
        assertEquals("", LocalizedText.of(null, Map.of(), null).resolve("es"));
    }

    @Test
    void resolvesLocalizedListsWithSameFallbackOrder() {
        Configuration.defaultLanguage = "en";
        LocalizedTextList list = LocalizedTextList.of(List.of("Default"), Map.of("es", List.of("Linea"), "en", List.of("Line")), List.of("Legacy"));

        assertEquals(List.of("Linea"), list.resolve("es"));
        assertEquals(List.of("Line"), list.resolve("de"));
    }

    @Test
    void parsesScalarTextAsLegacyValue() {
        LocalizedText text = LocalizedTextParser.text("Legacy", null);

        assertEquals("Legacy", text.resolve("es"));
    }

    @Test
    void parsesObjectTextDefaultAndLocales() {
        Configuration.defaultLanguage = "en";
        LocalizedText text = LocalizedTextParser.text(Map.of(
                "default", "Default",
                "locales", Map.of("es", "Tienda", "en", "Shop")
        ), "Legacy");

        assertEquals("Tienda", text.resolve("es_AR"));
        assertEquals("Shop", text.resolve("fr"));
    }

    @Test
    void parsesObjectListDefaultAndLocales() {
        Configuration.defaultLanguage = "en";
        LocalizedTextList text = LocalizedTextParser.textList(Map.of(
                "default", List.of("Default"),
                "locales", Map.of("es", List.of("Linea"), "en", List.of("Line"))
        ), List.of("Legacy"));

        assertEquals(List.of("Linea"), text.resolve("es_AR"));
        assertEquals(List.of("Line"), text.resolve("fr"));
    }
}
