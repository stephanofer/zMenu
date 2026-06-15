package fr.maxlego08.menu.api.localization;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PlayerLanguageResolver {

    @Nullable
    String resolve(@Nullable Player player);

    @NotNull
    String defaultLanguage();
}
