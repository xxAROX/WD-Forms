package xxAROX.WDForms.forms.elements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.cloudburstmc.protocol.common.util.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
@ToString(exclude = {"handle"})
public class StepSlider extends Element{
    private List<String> steps = new ArrayList<>();
    @JsonProperty("default") private Integer defaultStepIndex;

    @JsonIgnore @Setter private Integer value;
    @JsonIgnore private Consumer<StepSlider> handle;
    public void handle(){if (handle != null) handle.accept(this);}

    public StepSlider(@NonNull String text, @NonNull List<String> steps, Integer defaultStepIndex, Consumer<StepSlider> handle){
        super(Type.STEP_SLIDER, text);
        Preconditions.checkNotNull(steps, "The provided steps can not be null");
        this.steps.addAll(steps);
        this.defaultStepIndex = defaultStepIndex;
        this.handle = handle;
    }
    public StepSlider(@NonNull String text, @NonNull List<String> steps, Integer defaultStepIndex){this(text, steps, defaultStepIndex, null);}
    public StepSlider(@NonNull String text, @NonNull List<String> steps, Consumer<StepSlider> handle){this(text, steps, 0, handle);}
    public StepSlider(@NonNull String text, @NonNull List<String> steps){this(text, steps, 0, null);}

    public String getStep(int index) {return steps.get(index);}
    public String getSelectedOption(){return getValue() != null ? steps.get(getValue()) : null;}

    @RequiredArgsConstructor @Getter @ToString public static class Response {
        private final int index;
        private final String step;
    }
}
