package fr.maxlego08.menu.localization;

import com.stephanofer.networkplayersettings.settings.api.PlayerSettingsService;
import fr.maxlego08.menu.api.configuration.Configuration;
import fr.maxlego08.menu.api.localization.LocalizationManager;
import fr.maxlego08.menu.api.localization.PlayerLanguageResolver;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class NetworkPlayerSettingsLanguageResolver
    implements PlayerLanguageResolver
{

    private final PlayerSettingsService settingsService;

    public NetworkPlayerSettingsLanguageResolver(
        PlayerSettingsService settingsService
    ) {
        this.settingsService = Objects.requireNonNull(
            settingsService,
            "settingsService"
        );
    }

    @Override
    public @Nullable String resolve(@Nullable Player player) {
        if (
            player == null ||
            !player.isOnline() ||
            !this.settingsService.isReady(player.getUniqueId())
        ) {
            return this.defaultLanguage();
        }
        return this.settingsService.resolvedLanguage(player).code();
    }

    @Override
    public @NotNull String defaultLanguage() {
        return LocalizationManager.normalize(
            Configuration.defaultLanguage,
            "en"
        );
    }
}
