package xxAROX.WDForms.forms.types.defaults;

import xxAROX.WDForms.forms.types.ModalForm;

import java.util.function.Consumer;

public final class AgreeForm extends ModalForm {
    public AgreeForm(String whatToConfirm, Consumer<Boolean> onSubmit, Consumer<Throwable> onError) {
        super("Do you agree?", whatToConfirm, "§l§2Yes, i agree!", "§l§cNo, i don't!", onSubmit, onError);
    }
    public AgreeForm(String whatToConfirm, Consumer<Boolean> onSubmit) {this(whatToConfirm, onSubmit, null);}
}
