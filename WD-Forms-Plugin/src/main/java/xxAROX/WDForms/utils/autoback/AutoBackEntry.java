package xxAROX.WDForms.utils.autoback;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import xxAROX.WDForms.forms.types.Form;

import java.util.Objects;

@Getter @Setter @Accessors(chain = true) @AllArgsConstructor
public final class AutoBackEntry {
    private Long expireTick;
    private Form<?> previousForm;

    public boolean canGoBack(Form<?> form){
        return !Objects.equals(previousForm, form);
    }
}
