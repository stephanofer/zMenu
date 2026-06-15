package fr.maxlego08.menu.loader.actions;

import fr.maxlego08.menu.api.configuration.Configuration;
import fr.maxlego08.menu.api.localization.LocalizedTextList;
import fr.maxlego08.menu.api.localization.LocalizationManager;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageLoaderTest {

    @AfterEach
    void tearDown() {
        Configuration.defaultLanguage = "en";
        LocalizationManager.setResolver(null);
    }

    @Test
    void localizedMessagesObjectDoesNotUseUnsafeStringListCast() {
        Configuration.defaultLanguage = "en";
        TypedMapAccessor accessor = new TypedMapAccessor(Map.of(
                "messages", Map.of(
                        "default", List.of("Default"),
                        "locales", Map.of("en", List.of("Hello"), "es", List.of("Hola"))
                )
        ));

        List<String> legacyMessages = assertDoesNotThrow(() -> MessageLoader.extractMessages(accessor));
        LocalizedTextList localizedMessages = MessageLoader.extractLocalizedMessages(accessor, legacyMessages);

        assertEquals(List.of("Hola"), localizedMessages.resolve("es_AR"));
        assertEquals(List.of("Hello"), localizedMessages.resolve("fr"));
    }
}
