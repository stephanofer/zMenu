package fr.maxlego08.menu.hooks.bedrock.button.loader;

import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.button.bedrock.BedrockButton;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.menu.api.localization.LocalizedText;
import fr.maxlego08.menu.api.localization.LocalizedTextParser;
import fr.maxlego08.menu.hooks.bedrock.button.buttons.ZBedrockButton;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

public class BedrockModalButtonLoader extends ButtonLoader {

    public BedrockModalButtonLoader(Plugin plugin) {
        super(plugin, "bedrock_modal_button");
    }

    @Override
    public BedrockButton load(@NonNull YamlConfiguration configuration, @NonNull String path, @NonNull DefaultButtonValue defaultButtonValue) {
        String text = configuration.getString(path + ".text", "");

        BedrockButton button = new ZBedrockButton(text);
        button.setLocalizedText(localizedText(configuration, path + ".text", text));
        return button;
    }

    private LocalizedText localizedText(YamlConfiguration configuration, String path, String legacyValue) {
        Object object = configuration.isConfigurationSection(path) ? configuration.getConfigurationSection(path) : configuration.get(path);
        return LocalizedTextParser.text(object, legacyValue);
    }
}
