package xxAROX.WDforms.forms.elements;

import com.google.gson.JsonObject;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import jline.internal.Nullable;
import lombok.Getter;
import lombok.Setter;
import xxAROX.WDforms.forms.FormValidationError;

import java.util.function.BiConsumer;

public class Input extends Element{
    @Override public Type getType() {return Type.INPUT;}

    @Setter @Getter @Nullable protected String placeholder;
    @Getter @Setter protected String value;
    @Getter @Setter @Nullable protected String defaultValue = null;
    @Setter @Getter @Nullable protected BiConsumer<ProxiedPlayer, Input> onSubmit;

    public Input(String text, @Nullable String placeholder, @Nullable String defaultValue, @Nullable BiConsumer<ProxiedPlayer, Input> onSubmit){
        super(text, defaultValue);
        this.placeholder = placeholder;
        this.onSubmit = onSubmit;
    }
    public Input(String text, @Nullable BiConsumer<ProxiedPlayer, Input> onSubmit){
        super(text, null);
        this.placeholder = null;
        this.onSubmit = onSubmit;
    }

    @Override protected JsonObject serializeElementData() {
        JsonObject json = new JsonObject();
        json.addProperty("placeholder", placeholder);
        return json;
    }

    @Override public void validate(@Nullable Object value) {
        if (!(value instanceof String)) throw new FormValidationError("Expected string, got " + value.getClass().getSimpleName());
    }

}
