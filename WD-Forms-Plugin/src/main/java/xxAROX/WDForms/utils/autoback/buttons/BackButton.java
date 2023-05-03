package xxAROX.WDForms.utils.autoback.buttons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import jline.internal.Nullable;
import lombok.NonNull;
import xxAROX.WDForms.forms.elements.Button;
import xxAROX.WDForms.forms.elements.Image;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BackButton extends Button {
    @JsonIgnore public final static BackButton DEFAULT = new BackButton("§cBack", Image.textures("textures/ui/refresh_light"), (Consumer<Button>) null);
    private final Boolean wd_button_back = true;

    public BackButton(@NonNull String text, @Nullable Image image, @Nullable Consumer<Button> onClick) {
        super(text, image, onClick);
    }
    public BackButton(@NonNull String text, Image image, BiConsumer<Button, ProxiedPlayer> onClickPlayer) {
        super(text, image, onClickPlayer);
    }
    public BackButton(@NonNull String text, Image image) {
        this(text, image, (Consumer<Button>) null);
    }
    public BackButton(@NonNull String text, Consumer<Button> onClick) {
        this(text, null, onClick);
    }
    public BackButton(@NonNull String text, BiConsumer<Button, ProxiedPlayer> onClickPlayer) {
        this(text, null, onClickPlayer);
    }
    public BackButton(@NonNull String text) {
        this(text, null, (Consumer<Button>) null);
    }
}
