package xxAROX.WDForms.forms.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import jline.internal.Nullable;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.cloudburstmc.protocol.common.util.Preconditions;
import xxAROX.WDForms.forms.FormValidationError;
import xxAROX.WDForms.forms.elements.*;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@ToString
public class CustomForm extends Form<CustomForm.Response>{
    @JsonProperty("content") @Getter protected List<Element> elements;

    @Deprecated
    public CustomForm(Image image, String title, List<Element> elements, Consumer<Response> onSubmit, Runnable onClose, Consumer<Throwable> onError) {
        super(Type.CUSTOM, title, onSubmit, onClose, onError);
        this.elements = elements;
    }
    public CustomForm(String title, List<Element> elements, Consumer<Response> onSubmit, Runnable onClose, Consumer<Throwable> onError) {
        this(null, title, elements, onSubmit, onClose, onError);
    }
    public Element getElement(int index) {return elements.get(index);}

    @Override public void handleResponse(ProxiedPlayer player, JsonNode node) {
        if (node.isNull()) {
            close();
            return;
        }
        if (!node.isArray()) {
            error(new FormValidationError("Expected array, got " + node));
            return;
        }
        submit(new Response(this, node, player));
    }

    @Override
    public void submit(Response response) {
        for (Element element : elements) {
            if (element instanceof Dropdown e) e.handle(response.getPlayer());
            else if (element instanceof Input e) e.handle(response.getPlayer());
            else if (element instanceof Slider e) e.handle(response.getPlayer());
            else if (element instanceof StepSlider e) e.handle(response.getPlayer());
            else if (element instanceof Toggle e) e.handle(response.getPlayer());
        }
        super.submit(response);
    }

    @ToString
    public static class Response {
        private final JsonNode responses;
        @Getter private final ProxiedPlayer player;
        private final List<Dropdown> dropdowns = new ArrayList<>();
        private final List<Input> inputs = new ArrayList<>();
        private final List<Slider> sliders = new ArrayList<>();
        private final List<StepSlider> step_sliders = new ArrayList<>();
        private final List<Toggle> toggles = new ArrayList<>();

        public Response(CustomForm form, JsonNode responses, ProxiedPlayer player){
            this.responses = responses;
            this.player = player;

            for (int i=0; i<form.elements.size(); i++) {
                Element element = form.getElement(i);
                JsonNode node = responses.get(i);
                if (element instanceof Dropdown e) {
                    if (!node.isInt()) wrongValue(i, "dropdown");
                    e.setValue(node.asInt());
                    dropdowns.add(e);
                }
                else if (element instanceof Input e) {
                    if (!node.isTextual()) wrongValue(i, "input");
                    e.setValue(node.asText());
                    inputs.add(e);
                }
                else if (element instanceof Slider e) {
                    if (!node.isDouble()) wrongValue(i, "slider");
                    e.setValue((float) node.asDouble());
                    sliders.add(e);
                }
                else if (element instanceof StepSlider e) {
                    if (!node.isInt()) wrongValue(i, "step_slider");
                    e.setValue(node.asInt());
                    step_sliders.add(e);
                }
                else if (element instanceof Toggle e) {
                    if (!node.isBoolean()) wrongValue(i, "toggle");
                    e.setValue(node.asBoolean());
                    toggles.add(e);
                }

            }
        }

        public Dropdown getDropdown() {
            if ((dropdowns.size() == 0)) wrongValue("dropdown");
            return dropdowns.remove(0);
        }
        public StepSlider getStepSlider() {
            if ((step_sliders.size() == 0)) wrongValue("step_slider");
            return step_sliders.remove(0);
        }
        public Input getInput(){
            if ((inputs.size() == 0)) wrongValue("input");
            return inputs.remove(0);
        }
        public Slider getSlider() {
            if ((sliders.size() == 0)) wrongValue("slider");
            return sliders.remove(0);
        }
        public Toggle getToggle() {
            if ((toggles.size() == 0)) wrongValue("toggle");
            return toggles.remove(0);
        }
        private static void wrongValue(int index, String expected) {throw new IllegalStateException(String.format("Wrong element at index %d expected '%s'", index, expected));}
        private static void wrongValue(String expected) {throw new IllegalStateException(String.format("No element of type '%s' left", expected));}
    }
    @ToString
    public static class CustomFormBuilder extends FormBuilder<CustomForm, CustomFormBuilder, CustomForm.Response> {
        protected final List<Element> elements = new ArrayList<>();

        public CustomFormBuilder label(@NonNull String text) {return element(new Label(text));}

        public CustomFormBuilder dropdown(@NonNull String text, @NonNull List<String> options) {
            return element(new Dropdown(text, options));
        }
        public CustomFormBuilder dropdown(@NonNull String text, @NonNull String[] options) {
            return element(new Dropdown(text, Arrays.asList(options)));
        }
        public CustomFormBuilder dropdown(@NonNull String text, @NonNull List<String> options, @Nullable Consumer<Dropdown> handle) {
            return element(new Dropdown(text, options, handle));
        }
        public CustomFormBuilder dropdown(@NonNull String text, @NonNull List<String> options, @Nullable BiConsumer<Dropdown, ProxiedPlayer> playerHandler) {
            return element(new Dropdown(text, options, playerHandler));
        }
        public CustomFormBuilder dropdown(@NonNull String text, @NonNull String[] options, @Nullable Consumer<Dropdown> handle) {
            return element(new Dropdown(text, Arrays.asList(options), handle));
        }
        public CustomFormBuilder dropdown(@NonNull String text, @NonNull String[] options, @Nullable BiConsumer<Dropdown, ProxiedPlayer> playerHandler) {
            return element(new Dropdown(text, Arrays.asList(options), playerHandler));
        }
        public CustomFormBuilder dropdown(@NonNull String text, int defaultOptionIndex, @NonNull List<String> options) {
            return dropdown(text, defaultOptionIndex, options, (Consumer<Dropdown>) null);
        }
        public CustomFormBuilder dropdown(@NonNull String text, int defaultOptionIndex, @NonNull String[] options) {
            return dropdown(text, defaultOptionIndex, Arrays.stream(options).toList(), (Consumer<Dropdown>) null);
        }
        public CustomFormBuilder dropdown(@NonNull String text, int defaultOptionIndex, @NonNull List<String> options, @Nullable Consumer<Dropdown> handle) {
            Preconditions.checkPositionIndex(defaultOptionIndex, options.size(), "Default option index out of bounds");
            return element(new Dropdown(text, defaultOptionIndex, options, handle));
        }
        public CustomFormBuilder dropdown(@NonNull String text, int defaultOptionIndex, @NonNull List<String> options, @Nullable BiConsumer<Dropdown, ProxiedPlayer> playerHandler) {
            Preconditions.checkPositionIndex(defaultOptionIndex, options.size(), "Default option index out of bounds");
            return element(new Dropdown(text, defaultOptionIndex, options, playerHandler));
        }
        public CustomFormBuilder dropdown(@NonNull String text, int defaultOptionIndex, @NonNull String[] options, @Nullable Consumer<Dropdown> handle) {
            Preconditions.checkPositionIndex(defaultOptionIndex, options.length, "Default option index out of bounds");
            return element(new Dropdown(text, defaultOptionIndex, Arrays.asList(options), handle));
        }
        public CustomFormBuilder dropdown(@NonNull String text, int defaultOptionIndex, @NonNull String[] options, @Nullable BiConsumer<Dropdown, ProxiedPlayer> playerHandler) {
            Preconditions.checkPositionIndex(defaultOptionIndex, options.length, "Default option index out of bounds");
            return element(new Dropdown(text, defaultOptionIndex, Arrays.asList(options), playerHandler));
        }

        public CustomFormBuilder input(@NonNull String text) {
            return element(new Input(text));
        }
        public CustomFormBuilder input(@NonNull String text, @Nullable Consumer<Input> handle) {
            return element(new Input(text, handle));
        }
        public CustomFormBuilder input(@NonNull String text, @Nullable BiConsumer<Input, ProxiedPlayer> playerHandler) {
            return element(new Input(text, playerHandler));
        }
        public CustomFormBuilder input(@NonNull String text, @NonNull String placeholder) {
            return element(new Input(text, placeholder));
        }
        public CustomFormBuilder input(@NonNull String text, @NonNull String placeholder, @Nullable Consumer<Input> handle) {
            return element(new Input(text, placeholder, handle));
        }
        public CustomFormBuilder input(@NonNull String text, @NonNull String placeholder, @Nullable BiConsumer<Input, ProxiedPlayer> playerHandler) {
            return element(new Input(text, placeholder, playerHandler));
        }
        public CustomFormBuilder input(@NonNull String text, @NonNull String placeholder, @NonNull String defaultText) {
            return element(new Input(text, placeholder, defaultText));
        }
        public CustomFormBuilder input(@NonNull String text, @NonNull String placeholder, @NonNull String defaultText, @Nullable Consumer<Input> handle) {
            return element(new Input(text, placeholder, defaultText, handle));
        }
        public CustomFormBuilder input(@NonNull String text, @NonNull String placeholder, @NonNull String defaultText, @Nullable BiConsumer<Input, ProxiedPlayer> playerHandler) {
            return element(new Input(text, placeholder, defaultText, playerHandler));
        }

        public CustomFormBuilder slider(@NonNull String text, float minimum, float maximum) {
            return element(new Slider(text, minimum, maximum));
        }
        public CustomFormBuilder slider(@NonNull String text, float minimum, float maximum, @Nullable Consumer<Slider> handle) {
            return element(new Slider(text, minimum, maximum, handle));
        }
        public CustomFormBuilder slider(@NonNull String text, float minimum, float maximum, @Nullable BiConsumer<Slider, ProxiedPlayer> playerHandler) {
            return element(new Slider(text, minimum, maximum, playerHandler));
        }
        public CustomFormBuilder slider(@NonNull String text, float minimum, float maximum, float stepCount) {
            return element(new Slider(text, minimum, maximum, stepCount));
        }
        public CustomFormBuilder slider(@NonNull String text, float minimum, float maximum, float stepCount, @Nullable Consumer<Slider> handle) {
            return element(new Slider(text, minimum, maximum, stepCount, handle));
        }
        public CustomFormBuilder slider(@NonNull String text, float minimum, float maximum, float stepCount, @Nullable BiConsumer<Slider, ProxiedPlayer> playerHandler) {
            return element(new Slider(text, minimum, maximum, stepCount, playerHandler));
        }
        public CustomFormBuilder slider(@NonNull String text, float minimum, float maximum, float stepCount, float defaultValue) {
            return slider(text, minimum, maximum, stepCount, defaultValue, (Consumer<Slider>) null);
        }
        public CustomFormBuilder slider(@NonNull String text, float minimum, float maximum, float stepCount, float defaultValue, @Nullable Consumer<Slider> handle) {
            return element(new Slider(text, minimum, maximum, stepCount, defaultValue, handle));
        }
        public CustomFormBuilder slider(@NonNull String text, float minimum, float maximum, float stepCount, float defaultValue, @Nullable BiConsumer<Slider, ProxiedPlayer> playerHandler) {
            return element(new Slider(text, minimum, maximum, stepCount, defaultValue, playerHandler));
        }

        public CustomFormBuilder stepSlider(@NonNull String text) {
            return element(new StepSlider(text, new ArrayList<>()));
        }
        public CustomFormBuilder stepSlider(@NonNull String text, @NonNull List<String> stepOptions) {
            return stepSlider(text, stepOptions, (Consumer<StepSlider>) null);
        }
        public CustomFormBuilder stepSlider(@NonNull String text, @NonNull String[] stepOptions) {
            return stepSlider(text, Arrays.asList(stepOptions), (Consumer<StepSlider>) null);
        }
        public CustomFormBuilder stepSlider(@NonNull String text, @NonNull List<String> stepOptions, @Nullable Consumer<StepSlider> handle) {
            return element(new StepSlider(text, stepOptions, handle));
        }
        public CustomFormBuilder stepSlider(@NonNull String text, @NonNull List<String> stepOptions, @Nullable BiConsumer<StepSlider, ProxiedPlayer> playerHandler) {
            return element(new StepSlider(text, stepOptions, playerHandler));
        }
        public CustomFormBuilder stepSlider(@NonNull String text, @NonNull String[] stepOptions, @Nullable Consumer<StepSlider> handle) {
            return element(new StepSlider(text, Arrays.asList(stepOptions), handle));
        }
        public CustomFormBuilder stepSlider(@NonNull String text, @NonNull String[] stepOptions, @Nullable BiConsumer<StepSlider, ProxiedPlayer> playerHandler) {
            return element(new StepSlider(text, Arrays.asList(stepOptions), playerHandler));
        }
        public CustomFormBuilder stepSlider(@NonNull String text, int defaultStepIndex, @NonNull List<String> stepOptions) {
            return element(new StepSlider(text, stepOptions, defaultStepIndex, (Consumer<StepSlider>) null));
        }
        public CustomFormBuilder stepSlider(@NonNull String text, int defaultStepIndex, @NonNull String[] stepOptions) {
            return element(new StepSlider(text, Arrays.asList(stepOptions), defaultStepIndex, (Consumer<StepSlider>) null));
        }
        public CustomFormBuilder stepSlider(@NonNull String text, int defaultStepIndex, @NonNull List<String> stepOptions, @Nullable Consumer<StepSlider> handle) {
            return element(new StepSlider(text, stepOptions, defaultStepIndex, handle));
        }
        public CustomFormBuilder stepSlider(@NonNull String text, int defaultStepIndex, @NonNull List<String> stepOptions, @Nullable BiConsumer<StepSlider, ProxiedPlayer> playerHandler) {
            return element(new StepSlider(text, stepOptions, defaultStepIndex, playerHandler));
        }
        public CustomFormBuilder stepSlider(@NonNull String text, int defaultStepIndex, @NonNull String[] stepOptions, @Nullable Consumer<StepSlider> handle) {
            return element(new StepSlider(text, Arrays.asList(stepOptions), defaultStepIndex, handle));
        }
        public CustomFormBuilder stepSlider(@NonNull String text, int defaultStepIndex, @NonNull String[] stepOptions, @Nullable BiConsumer<StepSlider, ProxiedPlayer> playerHandler) {
            return element(new StepSlider(text, Arrays.asList(stepOptions), defaultStepIndex, playerHandler));
        }

        public CustomFormBuilder toggle(@NonNull String text) {
            return element(new Toggle(text));
        }
        public CustomFormBuilder toggle(@NonNull String text, @Nullable Consumer<Toggle> handle) {
            return element(new Toggle(text, false, handle));
        }
        public CustomFormBuilder toggle(@NonNull String text, @Nullable BiConsumer<Toggle, ProxiedPlayer> playerHandler) {
            return element(new Toggle(text, false, playerHandler));
        }
        public CustomFormBuilder toggle(@NonNull String text, boolean defaultValue) {
            return element(new Toggle(text, defaultValue));
        }
        public CustomFormBuilder toggle(@NonNull String text, boolean defaultValue, @Nullable Consumer<Toggle> handle) {
            return element(new Toggle(text, defaultValue, handle));
        }
        public CustomFormBuilder toggle(@NonNull String text, boolean defaultValue, @Nullable BiConsumer<Toggle, ProxiedPlayer> playerHandler) {
            return element(new Toggle(text, defaultValue, playerHandler));
        }

        public CustomFormBuilder element(@NonNull Element element) {
            elements.add(element);
            return this;
        }
        public CustomFormBuilder elements(@NonNull Element element, @NonNull Element... elements) {
            this.elements.add(element);
            this.elements.addAll(Arrays.asList(elements));
            return this;
        }
        public CustomFormBuilder elements(@NonNull Collection<Element> elements) {
            this.elements.addAll(elements);
            return this;
        }

        @Override public CustomForm build() {return new CustomForm(title, Collections.unmodifiableList(elements), onSubmit, onClose, onError);}
        @Override protected CustomFormBuilder self() {
            return this;
        }
    }
}
