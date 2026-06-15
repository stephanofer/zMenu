package fr.maxlego08.menu.hooks.bedrock.button.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.menu.api.localization.LocalizedText;
import fr.maxlego08.menu.api.localization.LocalizedTextParser;
import fr.maxlego08.menu.hooks.bedrock.button.buttons.ZBedrockSliderInput;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

public class BedrockSliderInputLoader extends ButtonLoader {

    public BedrockSliderInputLoader(Plugin plugin) {
        super(plugin, "bedrock_slider");
    }

    @Override
    public Button load(@NonNull YamlConfiguration configuration, @NonNull String path, @NonNull DefaultButtonValue defaultButtonValue) {
        String text = configuration.getString(path + ".text", "");
        float start = (float) configuration.getDouble(path + ".start", 0);
        float end = (float) configuration.getDouble(path + ".end", 100);
        float step = (float) configuration.getDouble(path + ".step", 1);
        String initialValue = configuration.getString(path + ".initial-value", String.valueOf((end + start) / 2));

        return (Button) new ZBedrockSliderInput(text, start, end, step, initialValue)
                .setLocalizedLabel(localizedText(configuration, path + ".text", text))
                .setLocalizedInitialValueRange(localizedText(configuration, path + ".initial-value", initialValue));
    }

    private LocalizedText localizedText(YamlConfiguration configuration, String path, String legacyValue) {
        Object object = configuration.isConfigurationSection(path) ? configuration.getConfigurationSection(path) : configuration.get(path);
        return LocalizedTextParser.text(object, legacyValue);
    }
}
