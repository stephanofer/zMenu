package fr.maxlego08.menu.api.button.dialogs;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.enums.dialog.DialogInputType;
import fr.maxlego08.menu.api.localization.LocalizedText;
import fr.maxlego08.menu.api.utils.dialogs.record.SingleOption;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class InputButton extends Button {
    private final DialogInputType inputType;
    private String key; // Unique identifier for the input button
    private String label;
    private LocalizedText localizedLabel = LocalizedText.legacy(null);

    private boolean labelVisible = true;

    // Text input properties
    private int width = 200;
    private String defaultText = "";
    private int maxLength = 32; // Default max length for text input
    private int multilineMaxLines; // Optional positive integer. If present, limits maximum lines.
    private int multilineHeight; // Value between 1 and 512 — Height of input.
    private Supplier<String> defaultTextSupplier;
    private LocalizedText localizedDefaultText = LocalizedText.legacy("");

    // Single option input properties
    private List<SingleOption> singleOptions;

    // Don't use Label Visible

    // Boolean input properties
    private String initialValueBool = String.valueOf(true);
    private Supplier<Boolean> initialValueSupplier;
    private String textTrue = "";
    private String textFalse = "";
    private LocalizedText localizedTextTrue = LocalizedText.legacy("");
    private LocalizedText localizedTextFalse = LocalizedText.legacy("");

    // Number range input properties
    // use width
    private float start = (float) 0; // Default start value
    private float end = (float) 100; // Default end value
    private float step = (float) 1; // Default step value
    private String initialValueRange = String.valueOf(50); // Default initial value
    private Supplier<Float> initialValueRangeSupplier;
    private String labelFormat = "options.generic_value"; // Default label format
    private LocalizedText localizedInitialValueBool = LocalizedText.legacy(String.valueOf(true));
    private LocalizedText localizedInitialValueRange = LocalizedText.legacy(String.valueOf(50));
    private LocalizedText localizedLabelFormat = LocalizedText.legacy("options.generic_value");

    public InputButton(DialogInputType inputType) {
        super();
        this.inputType = inputType;
    }

    @Contract(pure = true)
    public DialogInputType getInputType() {
        return this.inputType;
    }

    public String getLabel() {
        return this.label;
    }

    public @NotNull String getLabel(@Nullable Player player) {
        return this.localizedLabel.resolve(player);
    }

    @Contract("_ -> this")
    public InputButton setLabel(@Nullable String label) {
        this.label = label;
        this.localizedLabel = LocalizedText.legacy(label);
        return this;
    }

    @Contract("_ -> this")
    public InputButton setLocalizedLabel(@Nullable LocalizedText localizedLabel) {
        this.localizedLabel = localizedLabel == null ? LocalizedText.legacy(this.label) : localizedLabel;
        return this;
    }

    @Contract(pure = true)
    public boolean isLabelVisible() {
        return this.labelVisible;
    }

    @Contract("_ -> this")
    public InputButton setLabelVisible(boolean labelVisible) {
        this.labelVisible = labelVisible;
        return this;
    }

    @Contract(pure = true)
    public int getWidth() {
        return this.width;
    }

    @Contract("_ -> this")
    public InputButton setWidth(int width) {
        this.width = width;
        return this;
    }

    @Contract(pure = true)
    @NotNull
    public String getDefaultText() {
        return this.defaultText;
    }

    public @NotNull String getDefaultText(@Nullable Player player) {
        return this.localizedDefaultText.resolve(player);
    }

    @Contract("_ -> this")
    public InputButton setDefaultText(@NotNull String defaultText) {
        this.defaultText = defaultText;
        this.localizedDefaultText = LocalizedText.legacy(defaultText);
        return this;
    }

    @Contract("_ -> this")
    public InputButton setLocalizedDefaultText(@Nullable LocalizedText localizedDefaultText) {
        this.localizedDefaultText = localizedDefaultText == null ? LocalizedText.legacy(this.defaultText) : localizedDefaultText;
        return this;
    }

    @Contract(pure = true)
    public int getMaxLength() {
        return this.maxLength;
    }

    @Contract("_ -> this")
    public InputButton setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    @Contract(pure = true)
    public int getMultilineMaxLines() {
        return this.multilineMaxLines;
    }

    @Contract("_ -> this")
    public InputButton setMultilineMaxLines(int multilineMaxLines) {
        this.multilineMaxLines = multilineMaxLines;
        return this;
    }

    @Contract(pure = true)
    public int getMultilineHeight() {
        return this.multilineHeight;
    }

    @Contract("_ -> this")
    public InputButton setMultilineHeight(int multilineHeight) {
        this.multilineHeight = multilineHeight;
        return this;
    }

    @Contract(pure = true)
    @Nullable
    public List<SingleOption> getSigleOptions() {
        return this.singleOptions;
    }

    @Contract("_ -> this")
    public InputButton setSigleOptions(@Nullable List<SingleOption> options) {
        this.singleOptions = options;
        return this;
    }

    @Contract(pure = true)
    @NotNull
    public String getInitialValueBool() {
        return this.initialValueBool;
    }

    public @NotNull String getInitialValueBool(@Nullable Player player) {
        return this.localizedInitialValueBool.resolve(player);
    }

    @Contract("_ -> this")
    public InputButton setInitialValueBool(@NotNull String initialValueBool) {
        this.initialValueBool = initialValueBool;
        this.localizedInitialValueBool = LocalizedText.legacy(initialValueBool);
        return this;
    }

    @Contract("_ -> this")
    public InputButton setLocalizedInitialValueBool(@Nullable LocalizedText localizedInitialValueBool) {
        this.localizedInitialValueBool = localizedInitialValueBool == null ? LocalizedText.legacy(this.initialValueBool) : localizedInitialValueBool;
        return this;
    }

    @Contract(pure = true)
    @NotNull
    public String getTextTrue() {
        return this.textTrue;
    }

    public @NotNull String getTextTrue(@Nullable Player player) {
        return this.localizedTextTrue.resolve(player);
    }

    @Contract("_ -> this")
    public InputButton setTextTrue(@NotNull String textTrue) {
        this.textTrue = textTrue;
        this.localizedTextTrue = LocalizedText.legacy(textTrue);
        return this;
    }

    @Contract("_ -> this")
    public InputButton setLocalizedTextTrue(@Nullable LocalizedText localizedTextTrue) {
        this.localizedTextTrue = localizedTextTrue == null ? LocalizedText.legacy(this.textTrue) : localizedTextTrue;
        return this;
    }

    @Contract(pure = true)
    @NotNull
    public String getTextFalse() {
        return this.textFalse;
    }

    public @NotNull String getTextFalse(@Nullable Player player) {
        return this.localizedTextFalse.resolve(player);
    }

    @Contract("_ -> this")
    public InputButton setTextFalse(@NotNull String textFalse) {
        this.textFalse = textFalse;
        this.localizedTextFalse = LocalizedText.legacy(textFalse);
        return this;
    }

    @Contract("_ -> this")
    public InputButton setLocalizedTextFalse(@Nullable LocalizedText localizedTextFalse) {
        this.localizedTextFalse = localizedTextFalse == null ? LocalizedText.legacy(this.textFalse) : localizedTextFalse;
        return this;
    }

    @Contract(pure = true)
    public float getStart() {
        return this.start;
    }

    @Contract("_ -> this")
    public InputButton setStart(float start) {
        this.start = start;
        return this;
    }

    @Contract(pure = true)
    public float getEnd() {
        return this.end;
    }

    @Contract("_ -> this")
    public InputButton setEnd(float end) {
        this.end = end;
        return this;
    }

    @Contract(pure = true)
    public float getStep() {
        return this.step;
    }

    @Contract("_ -> this")
    public InputButton setStep(float step) {
        this.step = step;
        return this;
    }

    @Contract(pure = true)
    @NotNull
    public String getInitialValueRange() {
        return this.initialValueRange;
    }

    public @NotNull String getInitialValueRange(@Nullable Player player) {
        return this.localizedInitialValueRange.resolve(player);
    }

    @Contract("_ -> this")
    public InputButton setInitialValueRange(@NotNull String initialValueRange) {
        this.initialValueRange = initialValueRange;
        this.localizedInitialValueRange = LocalizedText.legacy(initialValueRange);
        return this;
    }

    @Contract("_ -> this")
    public InputButton setLocalizedInitialValueRange(@Nullable LocalizedText localizedInitialValueRange) {
        this.localizedInitialValueRange = localizedInitialValueRange == null ? LocalizedText.legacy(this.initialValueRange) : localizedInitialValueRange;
        return this;
    }

    @Contract(pure = true)
    @Nullable
    public String getKey() {
        return this.key;
    }

    @Contract("_ -> this")
    public InputButton setKey(@Nullable String key) {
        this.key = key;
        return this;
    }

    @Contract(pure = true)
    @NotNull
    public String getLabelFormat() {
        return this.labelFormat;
    }

    public @NotNull String getLabelFormat(@Nullable Player player) {
        return this.localizedLabelFormat.resolve(player);
    }

    @Contract("_ -> this")
    public InputButton setLabelFormat(@NotNull String labelFormat) {
        this.labelFormat = labelFormat;
        this.localizedLabelFormat = LocalizedText.legacy(labelFormat);
        return this;
    }

    @Contract("_ -> this")
    public InputButton setLocalizedLabelFormat(@Nullable LocalizedText localizedLabelFormat) {
        this.localizedLabelFormat = localizedLabelFormat == null ? LocalizedText.legacy(this.labelFormat) : localizedLabelFormat;
        return this;
    }

    @Contract(pure = true)
    @NotNull
    public Optional<Boolean> getInitialValueSupplier() {
        if (this.initialValueSupplier != null) {
            return Optional.ofNullable(this.initialValueSupplier.get());
        }
        return Optional.empty();
    }

    @Contract("_ -> this")
    public InputButton setInitialValueSupplier(@Nullable Supplier<Boolean> initialValueSupplier) {
        this.initialValueSupplier = initialValueSupplier;
        return this;
    }

    @Contract(pure = true)
    @NotNull
    public Optional<Float> getInitialValueRangeSupplier() {
        if (this.initialValueRangeSupplier != null) {
            return Optional.ofNullable(this.initialValueRangeSupplier.get());
        }
        return Optional.empty();
    }

    @Contract("_ -> this")
    public InputButton setInitialValueRangeSupplier(@Nullable Supplier<Float> initialValueRangeSupplier) {
        this.initialValueRangeSupplier = initialValueRangeSupplier;
        return this;
    }

    @Contract(pure = true)
    @NotNull
    public Optional<String> getDefaultTextSupplier() {
        if (this.defaultTextSupplier != null) {
            return Optional.ofNullable(this.defaultTextSupplier.get());
        }
        return Optional.empty();
    }

    @Contract("_ -> this")
    public InputButton setDefaultTextSupplier(@Nullable Supplier<String> defaultTextSupplier) {
        this.defaultTextSupplier = defaultTextSupplier;
        return this;
    }
}
