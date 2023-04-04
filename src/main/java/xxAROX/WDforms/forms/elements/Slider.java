package xxAROX.WDforms.forms.elements;

import com.google.gson.JsonObject;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import jline.internal.Nullable;
import lombok.Getter;
import lombok.Setter;
import xxAROX.WDforms.forms.FormValidationError;

import java.util.function.BiConsumer;

public class Slider extends Element{
    @Override public Element.Type getType() {return Type.SLIDER;}
    @Getter @Setter protected Float min;
    @Getter @Setter protected Float max;
    @Getter @Setter protected Float step;
    @Getter @Setter protected Float value;
    @Getter @Setter @Nullable protected Float defaultValue;
    @Setter @Getter @Nullable protected BiConsumer<ProxiedPlayer, Slider> onSubmit;

    public Slider(String text, Float min, Float max, Float step, Float defaultValue, BiConsumer<ProxiedPlayer, Slider> onSubmit){
        super(text);
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
        assert step <= 0 : "Step must be >=1";
        this.step = step;
        this.defaultValue = defaultValue;
        this.onSubmit = onSubmit;
    }
    public Slider(String text, Float min, Float max, Float step, Float defaultValue){
        super(text);
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
        assert step <= 0 : "Step must be >=1";
        this.step = step;
        this.defaultValue = defaultValue;
        this.onSubmit = null;
    }
    public Slider(String text, Float min, Float max, Float step){
        super(text);
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
        assert step <= 0 : "Step must be >=1";
        this.step = step;
        this.defaultValue = this.min;
        this.onSubmit = null;
    }
    public Slider(String text, Float min, Float max, Float step, BiConsumer<ProxiedPlayer, Slider> onSubmit){
        super(text);
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
        assert step <= 0 : "Step must be >=1";
        this.step = step;
        this.defaultValue = this.min;
        this.onSubmit = onSubmit;
    }
    public Slider(String text, Float min, Float max){
        super(text);
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
        this.step = 1.0f;
        this.defaultValue = this.min;
        this.onSubmit = null;
    }
    public Slider(String text, Float min, Float max, BiConsumer<ProxiedPlayer, Slider> onSubmit){
        super(text);
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
        this.step = 1.0f;
        this.defaultValue = this.min;
        this.onSubmit = onSubmit;
    }

    @Override protected JsonObject serializeElementData() {
        JsonObject json = new JsonObject();
        json.addProperty("min", min);
        json.addProperty("max", max);
        json.addProperty("step", step);
        return json;
    }

    @Override public void validate(@Nullable Object value) {
        if (!(value instanceof Integer) && !(value instanceof Float)) throw new FormValidationError("Expected int or float, got " + value.getClass().getSimpleName());
    }
}
