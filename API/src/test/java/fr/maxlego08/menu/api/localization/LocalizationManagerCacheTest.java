package fr.maxlego08.menu.api.localization;

import fr.maxlego08.menu.api.configuration.Configuration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalizationManagerCacheTest {

    @AfterEach
    void tearDown() {
        Configuration.defaultLanguage = "en";
        LocalizationManager.setResolver(null);
    }

    @Test
    void clearCacheForcesPlayerLanguageToResolveAgain() {
        Player player = player(UUID.randomUUID());
        AtomicInteger calls = new AtomicInteger();
        LocalizationManager.setResolver(new PlayerLanguageResolver() {
            @Override
            public String resolve(Player player) {
                return calls.incrementAndGet() == 1 ? "en" : "es";
            }

            @Override
            public String defaultLanguage() {
                return "en";
            }
        });

        assertEquals("en", LocalizationManager.resolveLanguage(player));
        assertEquals("en", LocalizationManager.resolveLanguage(player));

        LocalizationManager.clearCache();

        assertEquals("es", LocalizationManager.resolveLanguage(player));
        assertEquals(2, calls.get());
    }

    private Player player(UUID uniqueId) {
        return (Player) Proxy.newProxyInstance(Player.class.getClassLoader(), new Class[]{Player.class}, (proxy, method, args) -> {
            if (method.getName().equals("getUniqueId")) {
                return uniqueId;
            }
            if (method.getName().equals("isOnline")) {
                return true;
            }
            throw new UnsupportedOperationException(method.getName());
        });
    }
}
