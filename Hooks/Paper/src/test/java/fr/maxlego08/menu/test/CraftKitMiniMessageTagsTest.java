package fr.maxlego08.menu.test;

import com.hera.craftkit.paper.minimessage.CraftKitMiniMessageTags;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CraftKitMiniMessageTagsTest {

    @Test
    @DisplayName("Parse craftkit head tag without losing surrounding text")
    void shouldParseCraftKitHeadTag() {
        MiniMessage miniMessage = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(CraftKitMiniMessageTags.playerHead())
                        .build())
                .build();

        String input = "<craftkit_head:c1a6dff7ef4f96f8be24ee808f8e9fb201155101b2567e64f80812df9660035b> VIP";

        String plainText = PlainTextComponentSerializer.plainText().serialize(miniMessage.deserialize(input));

        Assertions.assertTrue(plainText.endsWith(" VIP"));
        Assertions.assertFalse(plainText.contains("craftkit_head"));
    }
}
