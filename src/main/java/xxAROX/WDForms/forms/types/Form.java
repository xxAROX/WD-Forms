package xxAROX.WDForms.forms.types;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import xxAROX.WDForms.WDForms;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Getter
@ToString(exclude = {"onSubmit","onClose","onError"})
abstract public class Form<FormResponse> {
    private final Type type;
    private final String title;
    @JsonIgnore final private Consumer<FormResponse> onSubmit;
    @JsonIgnore final private Runnable onClose;
    @JsonIgnore final private Consumer<Throwable> onError;

    public abstract void handleResponse(ProxiedPlayer player, JsonNode node);

    public void submit(FormResponse response) {
        if (response == null) close();
        else if (onSubmit != null) onSubmit.accept(response);
    }
    public void close() {
        if (onClose != null) onClose.run();
    }
    public void error(Throwable error) {
        WDForms.getInstance().getLogger().error(error);
        if (onError != null) onError.accept(error);
    }
    public final void sendTo(ProxiedPlayer player){WDForms.getSession(player).sendForm(this);}


    public static MenuForm.MenuFormBuilder menu(){return new MenuForm.MenuFormBuilder();}
    public static CustomForm.CustomFormBuilder custom(){return new CustomForm.CustomFormBuilder();}
    public static ModalForm.ModalFormBuilder modal(){return new ModalForm.ModalFormBuilder();}

    @RequiredArgsConstructor public enum Type {
        MODAL("modal"),
        MENU("form"),
        CUSTOM("custom_form");
        private final String value;
        @JsonValue public String getJsonName() {return this.value;}
    }
    public static abstract class FormBuilder<F extends Form<FormResponse>, T extends Form.FormBuilder<F, T, FormResponse>, FormResponse> {
        protected String title = "";

        protected Runnable onClose;
        protected Consumer<FormResponse> onSubmit;
        protected Consumer<Throwable> onError;

        public T title(@NonNull String title) {
            this.title = title;
            return self();
        }

        public T onClose(@NonNull Runnable listener) {
            onClose = listener;
            return self();
        }
        public T onSubmit(@NonNull Consumer<FormResponse> listener) {
            onSubmit = listener;
            return self();
        }
        public T onError(@NonNull Consumer<Throwable> listener) {
            onError = listener;
            return self();
        }

        public abstract F build();
        protected abstract T self();
    }
}
