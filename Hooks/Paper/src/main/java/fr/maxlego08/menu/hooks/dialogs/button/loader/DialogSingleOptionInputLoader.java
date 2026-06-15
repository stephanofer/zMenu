package fr.maxlego08.menu.hooks.dialogs.button.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.menu.api.localization.LocalizedText;
import fr.maxlego08.menu.api.localization.LocalizedTextParser;
import fr.maxlego08.menu.api.utils.dialogs.record.SingleOption;
import fr.maxlego08.menu.hooks.dialogs.button.buttons.ZDialogSingleOptionInput;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class DialogSingleOptionInputLoader extends ButtonLoader {

    public DialogSingleOptionInputLoader(Plugin plugin) {
        super(plugin, "dialog_single_option");
    }

    @Override
    public Button load(@NonNull YamlConfiguration configuration, @NonNull String path, @NonNull DefaultButtonValue defaultButtonValue) {

        String label = configuration.getString(path + ".label", "");
        boolean labelVisible = configuration.getBoolean(path + ".label-visible", true);
        List<SingleOption> singleOptionList = new ArrayList<>();

        if (configuration.isConfigurationSection(path + ".options")) {
            boolean initialAlreadySet = false;
            for (String optionKey : configuration.getConfigurationSection(path + ".options").getKeys(false)) {
                String optionPath = path + ".options." + optionKey;

                String id = configuration.getString(optionPath + ".id", optionKey);
                String display = configuration.getString(optionPath + ".display", "");
                boolean initialValue = configuration.getBoolean(optionPath + ".initial", false);

                if (initialAlreadySet) {
                    initialValue = false;
                } else if (initialValue) {
                    initialAlreadySet = true;
                }

                SingleOption singleOption = new SingleOption(id, display, initialValue, localizedText(configuration, optionPath + ".display", display));
                singleOptionList.add(singleOption);
            }
        }
        return (Button) new ZDialogSingleOptionInput(label, labelVisible, singleOptionList)
                .setLocalizedLabel(localizedText(configuration, path + ".label", label));
    }

    private LocalizedText localizedText(YamlConfiguration configuration, String path, String legacyValue) {
        Object object = configuration.isConfigurationSection(path) ? configuration.getConfigurationSection(path) : configuration.get(path);
        return LocalizedTextParser.text(object, legacyValue);
    }
}
