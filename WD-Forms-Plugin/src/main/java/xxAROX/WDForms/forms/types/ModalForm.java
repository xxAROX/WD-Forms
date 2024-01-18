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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.function.Consumer;


@Getter @Setter @Accessors(chain = true)
@ToString
public class ModalForm extends Form<Boolean>{
    protected String content;
    @JsonProperty("button1") protected String trueButton;
    @JsonProperty("button2") protected String falseButton;

    public ModalForm(String title, String content, String trueButton, String falseButton, Consumer<Boolean> onSubmit, Consumer<Throwable> onError) {
        super(Type.MODAL, title, onSubmit, (Runnable) null, onError);
        this.content = content;
        this.trueButton = trueButton;
        this.falseButton = falseButton;
    }
    public ModalForm(String title, String content, String trueButton, String falseButton, Consumer<Boolean> onSubmit){this(title, content, trueButton, falseButton, onSubmit, null);}

    @Override public void handleResponse(ProxiedPlayer player, JsonNode response) {
        boolean v = false;
        if (response.isBoolean()) v = response.booleanValue();
        submit(player, v);
    }


    @ToString
    public static class ModalFormBuilder extends FormBuilder<ModalForm, ModalFormBuilder, Boolean> {
        private String content = "";
        private String trueButton = "true";
        private String falseButton = "false";

        public ModalFormBuilder content(@NonNull String content) {
            this.content = content;
            return this;
        }
        public ModalFormBuilder trueButton(@NonNull String text) {
            this.trueButton = text;
            return this;
        }
        public ModalFormBuilder falseButton(@NonNull String text) {
            this.falseButton = text;
            return this;
        }

        @Override public ModalFormBuilder onClose(@NonNull Runnable listener) {return self();}

        @Override public ModalForm build() {return new ModalForm(title, content, trueButton, falseButton, onSubmit, onError);}
        @Override protected ModalFormBuilder self() {return this;}
    }
}
