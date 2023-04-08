package xxAROX.WDForms.forms.types;

import com.fasterxml.jackson.databind.JsonNode;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.cloudburstmc.protocol.common.util.Preconditions;
import xxAROX.WDForms.forms.FormValidationError;
import xxAROX.WDForms.forms.elements.Button;
import xxAROX.WDForms.forms.elements.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@Getter
@ToString
public class MenuForm extends Form<MenuForm.Response> {
    private final String content;
    private final List<Button> buttons = new ArrayList<>();

    public MenuForm(String title, String content, List<Button> buttons, Runnable onClose, Consumer<Throwable> onError){
        super(Type.MENU, title, null, onClose, onError);
        this.content = content;
        this.buttons.addAll(buttons);
    }
    public MenuForm(String title, String text, List<Button> buttons){this(title, text, buttons, null, null);}
    public MenuForm(String title, String text){this(title, text, new ArrayList<>(), null, null);}
    public MenuForm(String title, List<Button> buttons){this(title, "", buttons, null, null);}
    public MenuForm(String title){this(title, "", new ArrayList<>());}

    @Override public void handleResponse(ProxiedPlayer player, JsonNode node) {
        if (node.isNull()) {
            close();
            return;
        }
        if (!node.isInt()) {
            error(new FormValidationError("Button with index '" + node + "' does not exist"));
            return;
        }
        int index = node.intValue();
        Button button = this.buttons.get(index);
        if (button == null) {
            error(new FormValidationError("Button with index '" + node + "' does not exist"));
            return;
        }
        button.click();
        submit(new Response(index, button));
    }

    public record Response(int index, Button button) {
        @Override public Button button() {return button;}
        @Override public int index() {return index;}
        @Override public String toString() {return "Response{index=" + index + ", button=" + button + "}";}
    }
    public static class MenuFormBuilder extends FormBuilder<MenuForm, MenuFormBuilder, MenuForm.Response> {
        private String content = "";
        private final List<Button> buttons = new ArrayList<>();

        public MenuFormBuilder content(@NonNull String content) {
            Preconditions.checkNotNull(content, "content must not be null");
            this.content = content;
            return this;
        }

        public MenuFormBuilder button(@NonNull String text) {
            this.buttons.add(new Button(text));
            return this;
        }
        public MenuFormBuilder button(@NonNull String text, Consumer<Button> handle) {
            this.buttons.add(new Button(text, handle));
            return this;
        }
        public MenuFormBuilder button(@NonNull String text, Image image) {
            this.buttons.add(new Button(text, image));
            return this;
        }
        public MenuFormBuilder button(@NonNull String text, Image image, Consumer<Button> handle) {
            this.buttons.add(new Button(text, image, handle));
            return this;
        }
        public MenuFormBuilder buttons(@NonNull Button... buttons) {
            this.buttons.addAll(Arrays.asList(buttons));
            return this;
        }
        public MenuFormBuilder buttons(@NonNull Collection<Button> buttons) {
            this.buttons.addAll(buttons);
            return this;
        }

        @Override public MenuForm build() {return new MenuForm(title, content, buttons, onClose, onError);}
        @Override protected MenuFormBuilder self() {return this;}
    }
}
