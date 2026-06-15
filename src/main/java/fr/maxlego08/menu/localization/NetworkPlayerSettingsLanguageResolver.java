package fr.maxlego08.menu.localization;

import fr.maxlego08.menu.api.configuration.Configuration;
import fr.maxlego08.menu.api.localization.LocalizationManager;
import fr.maxlego08.menu.api.localization.PlayerLanguageResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public final class NetworkPlayerSettingsLanguageResolver implements PlayerLanguageResolver {

    private static final String SERVICE_CLASS_NAME = "com.stephanofer.networkplayersettings.api.PlayerSettingsService";
    private Class<?> serviceClass;
    private Method isReadyMethod;
    private Method resolvedLanguageMethod;
    private Method codeMethod;

    @Override
    public @Nullable String resolve(@Nullable Player player) {
        if (player == null || !player.isOnline()) {
            return this.defaultLanguage();
        }
        try {
            Object service = this.loadService();
            if (service == null) {
                return this.defaultLanguage();
            }
            Object ready = this.isReadyMethod.invoke(service, player.getUniqueId());
            if (!(ready instanceof Boolean) || !((Boolean) ready)) {
                return this.defaultLanguage();
            }
            Object language = this.resolvedLanguageMethod.invoke(service, player);
            if (language == null) {
                return this.defaultLanguage();
            }
            Object code = this.codeMethod.invoke(language);
            return code == null ? this.defaultLanguage() : String.valueOf(code);
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            return this.defaultLanguage();
        }
    }

    @Override
    public @NotNull String defaultLanguage() {
        return LocalizationManager.normalize(Configuration.defaultLanguage, "en");
    }

    private @Nullable Object loadService() throws ReflectiveOperationException {
        if (this.serviceClass == null) {
            this.serviceClass = Class.forName(SERVICE_CLASS_NAME);
            this.isReadyMethod = this.serviceClass.getMethod("isReady", java.util.UUID.class);
            this.resolvedLanguageMethod = this.serviceClass.getMethod("resolvedLanguage", Player.class);
        }
        Object service = Bukkit.getServicesManager().load(this.serviceClass);
        if (service != null && this.codeMethod == null) {
            Class<?> languageClass = Class.forName("com.stephanofer.networkplayersettings.api.Language");
            this.codeMethod = languageClass.getMethod("code");
        }
        return service;
    }
}
