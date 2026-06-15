package fr.maxlego08.menu.api.utils.dialogs.record;

import fr.maxlego08.menu.api.requirement.Requirement;
import fr.maxlego08.menu.api.localization.LocalizedText;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ActionButtonRecord(@NotNull String label, @NotNull String tooltip, int width, @NotNull List<Requirement> actions,
                                 @NotNull LocalizedText localizedLabel,
                                 @NotNull LocalizedText localizedTooltip) {

    public ActionButtonRecord(@NotNull String label, @NotNull String tooltip, int width, @NotNull List<Requirement> actions) {
        this(label, tooltip, width, actions, LocalizedText.legacy(label), LocalizedText.legacy(tooltip));
    }

    public ActionButtonRecord parse(@NotNull Player player) {
        String resolvedLabel = this.localizedLabel.resolve(player);
        String resolvedTooltip = this.localizedTooltip.resolve(player);
        return new ActionButtonRecord(this.parsePlaceholder(resolvedLabel, player), this.parsePlaceholder(resolvedTooltip, player), this.width, this.actions);
    }
    private String parsePlaceholder(@NotNull String text,@NotNull Player player) {
        return text.isEmpty() ? "" : PlaceholderAPI.setPlaceholders(player, text);
    }
}
