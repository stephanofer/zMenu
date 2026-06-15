package fr.maxlego08.menu.requirement.actions;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.localization.LocalizedText;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.common.utils.ActionHelper;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class TitleAction extends ActionHelper {

    private final LocalizedText title;
    private final LocalizedText subtitle;
    private final long start;
    private final long duration;
    private final long end;

    public TitleAction(String title, String subtitle, long start, long duration, long end) {
        this(LocalizedText.legacy(title), LocalizedText.legacy(subtitle), start, duration, end);
    }

    public TitleAction(LocalizedText title, LocalizedText subtitle, long start, long duration, long end) {
        this.title = title;
        this.subtitle = subtitle;
        this.start = start;
        this.duration = duration;
        this.end = end;
    }

    @Override
    protected void execute(@NonNull Player player, Button button, @NonNull InventoryEngine inventory, @NonNull Placeholders placeholders) {
        inventory.getPlugin().getMetaUpdater().sendTitle(player, this.papi(placeholders.parse(this.title.resolve(player)), player), this.papi(placeholders.parse(this.subtitle.resolve(player)), player), this.start, this.duration, this.end);
    }

}
