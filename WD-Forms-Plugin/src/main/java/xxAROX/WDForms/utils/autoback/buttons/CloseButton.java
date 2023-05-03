package xxAROX.WDForms.utils.autoback.buttons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import jline.internal.Nullable;
import lombok.NonNull;
import xxAROX.WDForms.forms.elements.Button;
import xxAROX.WDForms.forms.elements.Image;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CloseButton extends Button {
    @JsonIgnore public final static CloseButton DEFAULT = new CloseButton("§cClose", Image.textures("textures/ui/cancel"), (Consumer<Button>) null);

    public CloseButton(@NonNull String text, @Nullable Image image, @Nullable Consumer<Button> onClick) {
        super(text, image, onClick);
    }
    public CloseButton(@NonNull String text, Image image, BiConsumer<Button, ProxiedPlayer> onClickPlayer) {
        super(text, image, onClickPlayer);
    }
    public CloseButton(@NonNull String text, Image image) {
        this(text, image, (Consumer<Button>) null);
    }
    public CloseButton(@NonNull String text, Consumer<Button> onClick) {
        this(text, null, onClick);
    }
    public CloseButton(@NonNull String text, BiConsumer<Button, ProxiedPlayer> onClickPlayer) {
        this(text, null, onClickPlayer);
    }
    public CloseButton(@NonNull String text) {
        this(text, null, (Consumer<Button>) null);
    }
}
