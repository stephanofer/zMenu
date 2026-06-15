package fr.maxlego08.menu.localization;

import fr.maxlego08.menu.ZMenuPlugin;
import fr.maxlego08.menu.api.localization.LocalizationManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import java.lang.reflect.Method;
import java.util.UUID;

public final class NetworkPlayerSettingsLocalizationBridge implements Listener {

    private final ZMenuPlugin plugin;

    public NetworkPlayerSettingsLocalizationBridge(ZMenuPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        LocalizationManager.setResolver(new NetworkPlayerSettingsLanguageResolver());
        this.registerEvent("com.stephanofer.networkplayersettings.event.PlayerSettingsReadyEvent", this::handleReadyEvent);
        this.registerEvent("com.stephanofer.networkplayersettings.event.PlayerSettingChangeEvent", this::handleChangeEvent);
    }

    @SuppressWarnings("unchecked")
    private void registerEvent(String className, EventExecutor executor) {
        try {
            Class<?> rawClass = Class.forName(className);
            if (Event.class.isAssignableFrom(rawClass)) {
                Bukkit.getPluginManager().registerEvent((Class<? extends Event>) rawClass, this, EventPriority.MONITOR, executor, this.plugin, true);
            }
        } catch (ClassNotFoundException ignored) {
        }
    }

    private void handleReadyEvent(Listener listener, Event event) {
        Player player = this.invoke(event, "player", Player.class);
        if (player != null) {
            this.refresh(player);
        }
    }

    private void handleChangeEvent(Listener listener, Event event) {
        Object settingKey = this.invoke(event, "settingKey", Object.class);
        if (settingKey == null || !"LANGUAGE".equalsIgnoreCase(String.valueOf(settingKey))) {
            return;
        }
        UUID playerId = this.invoke(event, "playerId", UUID.class);
        if (playerId == null) {
            return;
        }
        LocalizationManager.invalidate(playerId);
        Player player = Bukkit.getPlayer(playerId);
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

    private <T> T invoke(Event event, String methodName, Class<T> type) {
        try {
            Method method = event.getClass().getMethod(methodName);
            Object value = method.invoke(event);
            return type.isInstance(value) ? type.cast(value) : null;
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            return null;
        }
    }
}
