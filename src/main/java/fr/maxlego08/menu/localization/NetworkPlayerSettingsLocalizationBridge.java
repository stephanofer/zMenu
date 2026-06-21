package fr.maxlego08.menu.localization;

import com.stephanofer.networkplayersettings.settings.api.PlayerSettingsService;
import com.stephanofer.networkplayersettings.settings.api.SettingKey;
import com.stephanofer.networkplayersettings.settings.event.PlayerSettingChangeEvent;
import com.stephanofer.networkplayersettings.settings.event.PlayerSettingsReadyEvent;
import fr.maxlego08.menu.ZMenuPlugin;
import fr.maxlego08.menu.api.localization.LocalizationManager;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class NetworkPlayerSettingsLocalizationBridge implements Listener {

    private final ZMenuPlugin plugin;
    private final PlayerSettingsService settingsService;

    private NetworkPlayerSettingsLocalizationBridge(
        ZMenuPlugin plugin,
        PlayerSettingsService settingsService
    ) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        this.settingsService = Objects.requireNonNull(
            settingsService,
            "settingsService"
        );
    }

    public static NetworkPlayerSettingsLocalizationBridge require(
        ZMenuPlugin plugin
    ) {
        PlayerSettingsService settingsService = plugin
            .getServer()
            .getServicesManager()
            .load(PlayerSettingsService.class);
        if (settingsService == null) {
            throw new IllegalStateException(
                "Missing required Bukkit service: " +
                    PlayerSettingsService.class.getName()
            );
        }
        return new NetworkPlayerSettingsLocalizationBridge(
            plugin,
            settingsService
        );
    }

    public void register() {
        LocalizationManager.setResolver(
            new NetworkPlayerSettingsLanguageResolver(this.settingsService)
        );
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onPlayerSettingsReady(PlayerSettingsReadyEvent event) {
        this.refresh(event.player());
    }

    @EventHandler
    public void onPlayerSettingChange(PlayerSettingChangeEvent event) {
        if (event.settingKey() != SettingKey.LANGUAGE) {
            return;
        }
        LocalizationManager.invalidate(event.playerId());
        Player player = Bukkit.getPlayer(event.playerId());
        if (player != null) {
            this.refresh(player);
        }
    }

    private void refresh(Player player) {
        LocalizationManager.invalidate(player.getUniqueId());
        this.plugin.getScheduler().runAtEntity(player, scheduledTask -> {
            if (player.isOnline()) {
                this.plugin.getInventoryManager().updateInventory(player);
            }
        });
    }
}
