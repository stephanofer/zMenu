package fr.maxlego08.menu.api.utils.dialogs.record;

import fr.maxlego08.menu.api.localization.LocalizedText;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SingleOption(@NotNull String id, @NotNull String display, boolean initialValue,
                           @NotNull LocalizedText localizedDisplay) {

    public SingleOption(@NotNull String id, @NotNull String display, boolean initialValue) {
        this(id, display, initialValue, LocalizedText.legacy(display));
    }

    public SingleOption(@NotNull String id, @NotNull String display, boolean initialValue, @Nullable LocalizedText localizedDisplay) {
        this.id = id;
        this.display = display;
        this.initialValue = initialValue;
        this.localizedDisplay = localizedDisplay == null ? LocalizedText.legacy(display) : localizedDisplay;
    }

    public @NotNull String display(@Nullable Player player) {
        return this.localizedDisplay.resolve(player);
    }
}
