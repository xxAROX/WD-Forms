package xxAROX.WDForms.forms.elements;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.cloudburstmc.protocol.common.util.Preconditions;

abstract public class Element {
    @JsonProperty("type") @Getter private final Type type;
    @JsonProperty("text") @Getter @Setter private String text;

    public Element(@NonNull Type type, @NonNull String text){
        Preconditions.checkNotNull(type, "The provided type can not be null");
        Preconditions.checkNotNull(text, "The provided text can not be null");
        this.type = type;
        this.text = text;
    }

    public enum Type{
        @JsonProperty("button") BUTTON,
        @JsonProperty("dropdown") DROPDOWN,
        @JsonProperty("input") INPUT,
        @JsonProperty("label") LABEL,
        @JsonProperty("slider") SLIDER,
        @JsonProperty("step_slider") STEP_SLIDER,
        @JsonProperty("toggle") TOGGLE;
    }
}
