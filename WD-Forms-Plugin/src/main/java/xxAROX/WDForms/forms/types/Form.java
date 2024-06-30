/*
 * Copyright (c) Jan Sohn aka. xxAROX
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package xxAROX.WDForms.forms.types;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import jline.internal.Nullable;
import lombok.*;
import lombok.experimental.Accessors;
import xxAROX.WDForms.WDForms;

import java.util.function.Consumer;

@Getter @Setter @Accessors(chain = true)
@ToString(exclude = {"onSubmit","onClose","onClosePlayer","onError"})
abstract public class Form<FormResponse> {
    private final Type type;
    private String title;
    @JsonIgnore private Consumer<FormResponse> onSubmit;
    @JsonIgnore private Runnable onClose = null;
    @JsonIgnore private Consumer<ProxiedPlayer> onClosePlayer = null;
    @JsonIgnore private Consumer<Throwable> onError;

    public static Consumer<ProxiedPlayer> UNCLOSEABLE(){
        return player -> WDForms.getSession(player).getForms().values().stream().toList().get(0).sendTo(player);
    }

    public Form(@NonNull Type type, @NonNull String title, @Nullable Consumer<FormResponse> onSubmit, @Nullable Runnable onClose, @Nullable Consumer<Throwable> onError){
        this.type = type;
        this.title = title;
        this.onSubmit = onSubmit;
        this.onClose = onClose;
        this.onError = onError;
    }
    public Form(@NonNull Type type, @NonNull String title, @Nullable Consumer<FormResponse> onSubmit, @Nullable Consumer<ProxiedPlayer> onClosePlayer, @Nullable Consumer<Throwable> onError){
        this.type = type;
        this.title = title;
        this.onSubmit = onSubmit;
        this.onClosePlayer = onClosePlayer;
        this.onError = onError;
    }

    public abstract void handleResponse(ProxiedPlayer player, JsonNode node);

    public void submit(ProxiedPlayer player, FormResponse response) {
        if (response == null) close(player);
        else if (onSubmit != null) onSubmit.accept(response);
    }
    public void close(ProxiedPlayer player) {
        if (onClose != null) onClose.run();
        if (onClosePlayer != null) onClosePlayer.accept(player);
    }
    public void error(Throwable error) {
        WDForms.getInstance().getLogger().error(error);
        if (onError != null) onError.accept(error);
    }
    public final void sendTo(ProxiedPlayer player){WDForms.getSession(player).sendForm(this);}


    public static MenuForm.MenuFormBuilder menu(){return new MenuForm.MenuFormBuilder();}
    public static CustomForm.CustomFormBuilder custom(){return new CustomForm.CustomFormBuilder();}
    public static ModalForm.ModalFormBuilder modal(){return new ModalForm.ModalFormBuilder();}
    public static ProxySettingsForm.ProxySettingsFormBuilder settings(){return new ProxySettingsForm.ProxySettingsFormBuilder();}

    @RequiredArgsConstructor public enum Type {
        MODAL("modal"),
        MENU("form"),
        CUSTOM("custom_form");
        private final String value;
        @JsonValue public String getJsonName() {return this.value;}
    }
    @ToString(exclude = {"onClose","onClosePlayer","onSubmit","onError"})
    public static abstract class FormBuilder<F extends Form<FormResponse>, T extends Form.FormBuilder<F, T, FormResponse>, FormResponse> {
        protected String title = "";

        protected Runnable onClose;
        protected Consumer<ProxiedPlayer> onClosePlayer;
        protected Consumer<FormResponse> onSubmit;
        protected Consumer<Throwable> onError;

        public T title(@NonNull String title) {
            this.title = title;
            return self();
        }

        public T onClose(Runnable listener) {
            onClose = listener;
            return self();
        }
        public T onClosePlayer(Consumer<ProxiedPlayer> listener) {
            onClosePlayer = listener;
            return self();
        }
        public T onSubmit(Consumer<FormResponse> listener) {
            onSubmit = listener;
            return self();
        }
        public T onError(Consumer<Throwable> listener) {
            onError = listener;
            return self();
        }

        public abstract F build();
        protected abstract T self();
    }
}
