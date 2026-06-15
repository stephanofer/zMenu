package fr.maxlego08.menu.hooks.dialogs.button.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.menu.api.localization.LocalizedText;
import fr.maxlego08.menu.api.localization.LocalizedTextParser;
import fr.maxlego08.menu.hooks.dialogs.button.buttons.ZDialogNumberRangeInput;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

public class DialogNumberRangeInputLoader extends ButtonLoader {

    public DialogNumberRangeInputLoader(Plugin plugin) {
        super(plugin, "dialog_number_range");
    }

    @Override
    public Button load(@NonNull YamlConfiguration configuration, @NonNull String path, @NonNull DefaultButtonValue defaultButtonValue) {
        String label = configuration.getString(path + ".label", "");
        int width = configuration.getInt(path + ".width", 200);
        float start = (float) configuration.getDouble(path + ".start", 0);
        float end = (float) configuration.getDouble(path + ".end", 100);
        float step = (float) configuration.getDouble(path + ".step", 1);
        String initialValue = configuration.getString(path + ".initial-value", String.valueOf((end + start) / 2));
        String labelFormat = configuration.getString(path + ".label-format", "options.generic_value");

        return (Button) new ZDialogNumberRangeInput(label, start, end, step, initialValue, width, labelFormat)
                .setLocalizedLabel(localizedText(configuration, path + ".label", label))
                .setLocalizedInitialValueRange(localizedText(configuration, path + ".initial-value", initialValue))
                .setLocalizedLabelFormat(localizedText(configuration, path + ".label-format", labelFormat));
    }

    private LocalizedText localizedText(YamlConfiguration configuration, String path, String legacyValue) {
        Object object = configuration.isConfigurationSection(path) ? configuration.getConfigurationSection(path) : configuration.get(path);
        return LocalizedTextParser.text(object, legacyValue);
    }
}
