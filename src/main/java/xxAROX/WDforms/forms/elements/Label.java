package xxAROX.WDforms.forms.elements;

import com.google.gson.JsonObject;
import jline.internal.Nullable;
import xxAROX.WDforms.forms.FormValidationError;

public class Label extends Element{
    @Override public Type getType() {return Type.LABEL;}

    public Label(String text){super(text);}

    @Override protected JsonObject serializeElementData() {return new JsonObject();}

    @Override
    public void validate(@Nullable Object value) {
        if (value != null) throw new FormValidationError("Expected null, got " + value.getClass().getSimpleName());
    }
}
