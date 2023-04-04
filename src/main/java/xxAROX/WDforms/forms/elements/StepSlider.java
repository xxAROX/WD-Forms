package xxAROX.WDforms.forms.elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import jline.internal.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class StepSlider extends Element{
    @Override public Type getType() {return Type.STEP_SLIDER;}


    @Setter @Getter protected String[] steps;
    @Getter @Setter protected Integer value;
    @Getter @Setter @Nullable protected Integer defaultValue;
    @Setter @Getter @Nullable protected BiConsumer<ProxiedPlayer, StepSlider> onSubmit;

    public StepSlider(String text, String[] steps, Integer defaultValue, BiConsumer<ProxiedPlayer, StepSlider> onSubmit){
        super(text);
        this.steps = steps;
        this.defaultValue = defaultValue;
        this.onSubmit = onSubmit;
    }
    public StepSlider(String text, String[] steps, Integer defaultValue){
        super(text);
        this.steps = steps;
        this.defaultValue = defaultValue;
        this.onSubmit = null;
    }
    public StepSlider(String text, String[] steps, BiConsumer<ProxiedPlayer, StepSlider> onSubmit){
        super(text);
        this.steps = steps;
        this.defaultValue = 0;
        this.onSubmit = onSubmit;
    }
    public StepSlider(String text, String[] steps){
        super(text);
        this.steps = steps;
        this.defaultValue = 0;
        this.onSubmit = null;
    }

    @Override protected JsonObject serializeElementData() {
        JsonObject json = new JsonObject();
        JsonArray steps = new JsonArray();
        for (String step : this.steps) steps.add(step);
        json.add("steps", steps);
        return json;
    }

    public String getSelectedOption(){return getValue() != null ? Arrays.stream(steps).toList().get(getValue()) : null;}
}
