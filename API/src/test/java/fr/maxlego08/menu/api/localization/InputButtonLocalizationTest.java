package fr.maxlego08.menu.api.localization;

import fr.maxlego08.menu.api.button.dialogs.InputButton;
import fr.maxlego08.menu.api.configuration.Configuration;
import fr.maxlego08.menu.api.enums.dialog.DialogInputType;
import fr.maxlego08.menu.api.utils.dialogs.record.SingleOption;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputButtonLocalizationTest {

    @AfterEach
    void tearDown() {
        Configuration.defaultLanguage = "en";
        LocalizationManager.setResolver(null);
    }

    @Test
    void resolvesVisibleInputFieldsFromLocalizedValues() {
        Configuration.defaultLanguage = "es";
        TestInputButton button = new TestInputButton();

        button.setLabel("Name");
        button.setDefaultText("Steve");
        button.setTextTrue("Enabled");
        button.setTextFalse("Disabled");
        button.setInitialValueBool("true");
        button.setInitialValueRange("5");
        button.setLabelFormat("Value: %s");

        button.setLocalizedLabel(LocalizedText.of(null, Map.of("es", "Nombre"), "Name"));
        button.setLocalizedDefaultText(LocalizedText.of(null, Map.of("es", "Alex"), "Steve"));
        button.setLocalizedTextTrue(LocalizedText.of(null, Map.of("es", "Activado"), "Enabled"));
        button.setLocalizedTextFalse(LocalizedText.of(null, Map.of("es", "Desactivado"), "Disabled"));
        button.setLocalizedInitialValueBool(LocalizedText.of(null, Map.of("es", "false"), "true"));
        button.setLocalizedInitialValueRange(LocalizedText.of(null, Map.of("es", "7"), "5"));
        button.setLocalizedLabelFormat(LocalizedText.of(null, Map.of("es", "Valor: %s"), "Value: %s"));

        assertEquals("Nombre", button.getLabel(null));
        assertEquals("Alex", button.getDefaultText(null));
        assertEquals("Activado", button.getTextTrue(null));
        assertEquals("Desactivado", button.getTextFalse(null));
        assertEquals("false", button.getInitialValueBool(null));
        assertEquals("7", button.getInitialValueRange(null));
        assertEquals("Valor: %s", button.getLabelFormat(null));
    }

    @Test
    void resolvesSingleOptionVisibleDisplayFromLocalizedValue() {
        Configuration.defaultLanguage = "es";
        SingleOption option = new SingleOption("shop", "Shop", false, LocalizedText.of(null, Map.of("es", "Tienda"), "Shop"));

        assertEquals("Tienda", option.display(null));
        assertEquals("Shop", option.display());
    }

    private static final class TestInputButton extends InputButton {
        private TestInputButton() {
            super(DialogInputType.TEXT);
        }
    }
}
