package fr.maxlego08.menu.api.button.bedrock;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.enums.bedrock.BedrockImageType;
import fr.maxlego08.menu.api.localization.LocalizedText;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BedrockButton extends Button {
    private String text;
    private LocalizedText localizedText = LocalizedText.legacy("");
    private BedrockImageType imageType = BedrockImageType.NONE;
    private String imageData;

    public BedrockButton(String text) {
        super();
        this.text = text;
        this.localizedText = LocalizedText.legacy(text);
    }

    public BedrockButton(String text, BedrockImageType imageType, String imageData) {
        super();
        this.text = text;
        this.localizedText = LocalizedText.legacy(text);
        this.imageType = imageType;
        this.imageData = imageData;
    }

    public void setText(String text){
        this.text = text;
        this.localizedText = LocalizedText.legacy(text);
    }

    public void setLocalizedText(@Nullable LocalizedText localizedText) {
        this.localizedText = localizedText == null ? LocalizedText.legacy(this.text) : localizedText;
    }

    public String getText(Placeholders placeholders) {
        return placeholders.parse(this.text);
    }

    public String getText(Placeholders placeholders, @Nullable Player player) {
        return placeholders.parse(this.localizedText.resolve(player));
    }

    public String getRawText() {
        return this.text;
    }

    public @NotNull String getRawText(@Nullable Player player) {
        return this.localizedText.resolve(player);
    }

    public void setImageType(BedrockImageType imageType) {
        this.imageType = imageType;
    }

    public BedrockImageType getImageType(Player player) {
        return this.imageType;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public String getImageData() {
        return this.imageData;
    }
}
