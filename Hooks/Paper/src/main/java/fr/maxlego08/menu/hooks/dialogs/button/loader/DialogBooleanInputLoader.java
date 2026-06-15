package fr.maxlego08.menu.hooks.dialogs.button.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.menu.api.localization.LocalizedText;
import fr.maxlego08.menu.api.localization.LocalizedTextParser;
import fr.maxlego08.menu.hooks.dialogs.button.buttons.ZDialogBooleanInput;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

public class DialogBooleanInputLoader extends ButtonLoader {

    public DialogBooleanInputLoader(Plugin plugin) {
        super(plugin, "dialog_boolean");
    }

    @Override
    public Button load(@NonNull YamlConfiguration configuration, @NonNull String path, @NonNull DefaultButtonValue defaultButtonValue) {
        String label = configuration.getString(path + ".label", "");
        String defaultValue = configuration.getString(path + ".initial-value", String.valueOf(true));
        String textTrue = configuration.getString(path + ".text-true", "True");
        String textFalse = configuration.getString(path + ".text-false", "False");

        return (Button) new ZDialogBooleanInput(label, defaultValue, textTrue, textFalse)
                .setLocalizedLabel(localizedText(configuration, path + ".label", label))
                .setLocalizedInitialValueBool(localizedText(configuration, path + ".initial-value", defaultValue))
                .setLocalizedTextTrue(localizedText(configuration, path + ".text-true", textTrue))
                .setLocalizedTextFalse(localizedText(configuration, path + ".text-false", textFalse));
    }

    private LocalizedText localizedText(YamlConfiguration configuration, String path, String legacyValue) {
        Object object = configuration.isConfigurationSection(path) ? configuration.getConfigurationSection(path) : configuration.get(path);
        return LocalizedTextParser.text(object, legacyValue);
    }
}
