package xxAROX.WDforms.forms.elements;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jline.internal.Nullable;
import lombok.Getter;
import lombok.Setter;
import xxAROX.WDforms.forms.FormValidationError;

import java.util.Map;

abstract public class Element {
    @Setter @Getter protected String text;
    @Setter @Getter protected Object value;                  // null|String|Integer|Float|Boolean
    @Setter @Getter @Nullable protected Object defaultValue; // null|String|Integer|Float|Boolean

    public Element(String text, @Nullable Object defaultValue){
        this.text = text;
        this.defaultValue = defaultValue;
    }
    public Element(String text){this(text, null);}



    public abstract Type getType();
    public final JsonObject jsonSerialize(){
        JsonObject json = new JsonObject();
        JsonObject json2 = this.serializeElementData();
        json.addProperty("text", text);
        if (this.getType() != null && !this.getType().equals(Type.BUTTON)) json.addProperty("type", this.getType().toString());
        if (this.defaultValue != null) {
            if (this.defaultValue instanceof Boolean) json.addProperty("default", (Boolean) this.defaultValue);
            else if (this.defaultValue instanceof Number) json.addProperty("default", (Number) this.defaultValue);
            else if (this.defaultValue instanceof String) json.addProperty("default", (String) this.defaultValue);
            else throw new FormValidationError("Type: " + this.defaultValue.getClass() + " not supported! [Boolean,String,Number]");
        }
        for (Map.Entry<String, JsonElement> entry : json2.entrySet()) json.add(entry.getKey(), entry.getValue());
        return json;
    }
    abstract protected JsonObject serializeElementData();

    public void validate(@Nullable Object value) {
        if (!(value instanceof Integer)) throw new FormValidationError("Expected int, got " + value.getClass().getSimpleName());
    }

    public enum Type{
        INPUT("input"),
        LABEL("label"),
        TOGGLE("toggle"),
        SLIDER("slider"),
        STEP_SLIDER("stepslider"),
        DROPDOWN("dropdown"),
        BUTTON("button")
        ;
        private final String value;
        Type(String value) {this.value = value;}
        @Override public String toString() {return value;}
    }
}
