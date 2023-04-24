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
