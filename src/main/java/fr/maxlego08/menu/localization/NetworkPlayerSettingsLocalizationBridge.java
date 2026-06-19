package fr.maxlego08.menu.localization;

import com.stephanofer.networkplayersettings.api.PlayerSettingsService;
import com.stephanofer.networkplayersettings.api.SettingKey;
import com.stephanofer.networkplayersettings.event.PlayerSettingChangeEvent;
import com.stephanofer.networkplayersettings.event.PlayerSettingsReadyEvent;
import fr.maxlego08.menu.ZMenuPlugin;
import fr.maxlego08.menu.api.localization.LocalizationManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Objects;

public final class NetworkPlayerSettingsLocalizationBridge implements Listener {

    private final ZMenuPlugin plugin;
    private final PlayerSettingsService settingsService;

    private NetworkPlayerSettingsLocalizationBridge(ZMenuPlugin plugin, PlayerSettingsService settingsService) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        this.settingsService = Objects.requireNonNull(settingsService, "settingsService");
    }

    public static NetworkPlayerSettingsLocalizationBridge require(ZMenuPlugin plugin) {
        RegisteredServiceProvider<PlayerSettingsService> provider = plugin.getServer().getServicesManager().getRegistration(PlayerSettingsService.class);
        if (provider == null || provider.getProvider() == null) {
            throw new IllegalStateException("Missing required Bukkit service: " + PlayerSettingsService.class.getName());
        }
        return new NetworkPlayerSettingsLocalizationBridge(plugin, provider.getProvider());
    }

    public void register() {
        LocalizationManager.setResolver(new NetworkPlayerSettingsLanguageResolver(this.settingsService));
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
