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

import jline.internal.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import xxAROX.WDForms.forms.elements.Element;
import xxAROX.WDForms.forms.elements.Image;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@ToString
public class ProxySettingsForm extends CustomForm{
    @Getter @Setter protected Image image;

    public ProxySettingsForm(Image image, String title, List<Element> elements, Consumer<Response> onSubmit, Runnable onClose, Consumer<Throwable> onError) {
        super(title, elements, onSubmit, onClose, onError);
        this.image = image;
    }
    @ToString
    public static class ProxySettingsFormBuilder extends CustomFormBuilder {
        private Image image = null;
        public ProxySettingsFormBuilder image(@Nullable Image image){
            this.image = image;
            return self();
        }
        @Override public ProxySettingsForm build() {return new ProxySettingsForm(image, title, Collections.unmodifiableList(elements), onSubmit, onClose, onError);}
        @Override protected ProxySettingsForm.ProxySettingsFormBuilder self() {
            return this;
        }
    }
}
