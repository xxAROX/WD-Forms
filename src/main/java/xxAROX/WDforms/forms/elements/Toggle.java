package xxAROX.WDforms.forms.elements;

import com.google.gson.JsonObject;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import jline.internal.Nullable;
import lombok.Getter;
import lombok.Setter;
import xxAROX.WDforms.forms.FormValidationError;

import java.util.function.BiConsumer;

public class Toggle extends Element{
    @Override public Type getType() {return Type.TOGGLE;}
    @Getter @Setter protected Boolean value;
    @Getter @Setter @Nullable protected Boolean defaultValue = null;
    @Setter @Getter @Nullable protected BiConsumer<ProxiedPlayer, Toggle> onSubmit;

    public Toggle(String text, Boolean defaultValue) {super(text, defaultValue);}
    public Toggle(String text) {super(text, false);}
    public Boolean hasChanged(){return defaultValue == value;}

    @Override protected JsonObject serializeElementData() {return new JsonObject();}
    @Override public void validate(Object value) {
        if (!(value instanceof Boolean)) throw new FormValidationError("Expected bool, got " + value.getClass().getSimpleName());
    }
}
