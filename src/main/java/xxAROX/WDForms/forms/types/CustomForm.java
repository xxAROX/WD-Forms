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
import java.util.function.Consumer;

@Getter
@ToString
public class CustomForm extends Form<CustomForm.Response>{
    protected Image image;
    @JsonProperty("content") protected List<Element> elements;

    public CustomForm(Image image, String title, List<Element> elements, Consumer<Response> onSubmit, Runnable onClose, Consumer<Throwable> onError) {
        super(Type.CUSTOM, title, onSubmit, onClose, onError);
        this.image = image;
        this.elements = elements;
    }
    public CustomForm(String title, List<Element> elements, Consumer<Response> onSubmit, Runnable onClose, Consumer<Throwable> onError) {this(null, title, elements, onSubmit, onClose, onError);}
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
        submit(new Response(this, node));
    }

    @Override
    public void submit(Response response) {
        for (Element element : elements) {
            if (element instanceof Dropdown e) e.handle();
            else if (element instanceof Input e) e.handle();
            else if (element instanceof Slider e) e.handle();
            else if (element instanceof StepSlider e) e.handle();
            else if (element instanceof Toggle e) e.handle();
        }
        super.submit(response);
    }

    @ToString(exclude = "form")
    public static class Response {
        private final CustomForm form;
        private final JsonNode responses;
        private JsonNode get(int index) {return responses.get(index);}

        public Response(CustomForm form, JsonNode responses){
            this.form = form;
            this.responses = responses;

            for (int i=0; i<form.elements.size(); i++) {
                Element element = form.getElement(i);
                JsonNode node = get(i);
                if (element instanceof Dropdown) {
                    if (!node.isInt()) wrongValue(i, "dropdown");
                    ((Dropdown) element).setValue(node.asInt());
                }
                else if (element instanceof Input) {
                    if (!node.isTextual()) wrongValue(i, "input");
                    ((Input) element).setValue(node.asText());
                }
                else if (element instanceof Slider) {
                    if (!node.isDouble()) wrongValue(i, "slider");
                    ((Slider) element).setValue((float) node.asDouble());
                }
                else if (element instanceof StepSlider) {
                    if (!node.isInt()) wrongValue(i, "step_slider");
                    ((StepSlider) element).setValue(node.asInt());
                }
                else if (element instanceof Toggle) {
                    if (!node.isBoolean()) wrongValue(i, "toggle");
                    ((Toggle) element).setValue(node.asBoolean());
                }

            }
        }

        public Dropdown.Response getDropdown(int index) {
            JsonNode node = get(index);
            if (!node.isInt()) wrongValue(index, "dropdown");
            return new Dropdown.Response(index, ((Dropdown) form.getElement(index)).getDropdownOption(node.asInt()));
        }
        public StepSlider.Response getStepSlider(int index) {
            JsonNode node = get(index);
            if (!node.isInt()) wrongValue(index, "step slider");
            return new StepSlider.Response(index, ((StepSlider) form.getElement(index)).getStep(node.asInt()));
        }
        public String getInput(int index) {
            JsonNode node = get(index);
            if (!node.isTextual()) wrongValue(index, "input");
            return node.asText();
        }
        public float getSlider(int index) {
            JsonNode node = get(index);
            if (!node.isDouble()) wrongValue(index, "slider");
            return (float) node.asDouble();
        }
        public boolean getToggle(int index) {
            JsonNode node = get(index);
            if (!node.isBoolean()) wrongValue(index, "toggle");
            return node.asBoolean();
        }
        private static void wrongValue(int index, String expected) {throw new IllegalStateException(String.format("Wrong element at index %d expected '%s'", index, expected));}
    }
    public static class CustomFormBuilder extends FormBuilder<CustomForm, CustomFormBuilder, CustomForm.Response> {
        private final List<Element> elements = new ArrayList<>();
        private Image image = null;
        
        public CustomFormBuilder label(@NonNull String text) {return element(new Label(text));}
        public CustomFormBuilder image(@NonNull Image image) {
            this.image = image;
            return this;
        }

        public CustomFormBuilder dropdown(@NonNull String text, @NonNull List<String> options) {
            return element(new Dropdown(text, options));
        }
        public CustomFormBuilder dropdown(@NonNull String text, @NonNull String[] options) {
            return element(new Dropdown(text, Arrays.asList(options)));
        }
        public CustomFormBuilder dropdown(@NonNull String text, @NonNull List<String> options, @Nullable Consumer<Dropdown> handle) {
            return element(new Dropdown(text, options, handle));
        }
        public CustomFormBuilder dropdown(@NonNull String text, @NonNull String[] options, @Nullable Consumer<Dropdown> handle) {
            return element(new Dropdown(text, Arrays.asList(options), handle));
        }
        public CustomFormBuilder dropdown(@NonNull String text, int defaultOptionIndex, @NonNull List<String> options) {
            return dropdown(text, defaultOptionIndex, options, null);
        }
        public CustomFormBuilder dropdown(@NonNull String text, int defaultOptionIndex, @NonNull String[] options) {
            return dropdown(text, defaultOptionIndex, Arrays.stream(options).toList(), null);
        }
        public CustomFormBuilder dropdown(@NonNull String text, int defaultOptionIndex, @NonNull List<String> options, @Nullable Consumer<Dropdown> handle) {
            Preconditions.checkPositionIndex(defaultOptionIndex, options.size(), "Default option index out of bounds");
            return element(new Dropdown(text, defaultOptionIndex, options, handle));
        }
        public CustomFormBuilder dropdown(@NonNull String text, int defaultOptionIndex, @NonNull String[] options, @Nullable Consumer<Dropdown> handle) {
            Preconditions.checkPositionIndex(defaultOptionIndex, options.length, "Default option index out of bounds");
            return element(new Dropdown(text, defaultOptionIndex, Arrays.asList(options), handle));
        }

        public CustomFormBuilder input(@NonNull String text, Consumer<Input> handle) {
            return element(new Input(text, handle));
        }
        public CustomFormBuilder input(@NonNull String text) {
            return element(new Input(text));
        }
        public CustomFormBuilder input(@NonNull String text, @NonNull String placeholder) {
            return element(new Input(text, placeholder));
        }
        public CustomFormBuilder input(@NonNull String text, @NonNull String placeholder, @Nullable Consumer<Input> handle) {
            return element(new Input(text, placeholder, handle));
        }
        public CustomFormBuilder input(@NonNull String text, @NonNull String placeholder, @NonNull String defaultText) {
            return element(new Input(text, placeholder, defaultText));
        }
        public CustomFormBuilder input(@NonNull String text, @NonNull String placeholder, @NonNull String defaultText, @Nullable Consumer<Input> handle) {
            return element(new Input(text, placeholder, defaultText, handle));
        }

        public CustomFormBuilder slider(@NonNull String text, float minimum, float maximum) {
            return element(new Slider(text, minimum, maximum));
        }
        public CustomFormBuilder slider(@NonNull String text, float minimum, float maximum, @Nullable Consumer<Slider> handle) {
            return element(new Slider(text, minimum, maximum, handle));
        }
        public CustomFormBuilder slider(@NonNull String text, float minimum, float maximum, float stepCount) {
            return element(new Slider(text, minimum, maximum, stepCount));
        }
        public CustomFormBuilder slider(@NonNull String text, float minimum, float maximum, float stepCount, @Nullable Consumer<Slider> handle) {
            return element(new Slider(text, minimum, maximum, stepCount, handle));
        }
        public CustomFormBuilder slider(@NonNull String text, float minimum, float maximum, float stepCount, float defaultValue) {
            return slider(text, minimum, maximum, stepCount, defaultValue, null);
        }
        public CustomFormBuilder slider(@NonNull String text, float minimum, float maximum, float stepCount, float defaultValue, @Nullable Consumer<Slider> handle) {
            return element(new Slider(text, minimum, maximum, stepCount, defaultValue, handle));
        }

        public CustomFormBuilder stepSlider(@NonNull String text) {
            return element(new StepSlider(text, new ArrayList<>()));
        }
        public CustomFormBuilder stepSlider(@NonNull String text, @NonNull List<String> stepOptions) {
            return stepSlider(text, stepOptions, null);
        }
        public CustomFormBuilder stepSlider(@NonNull String text, @NonNull String[] stepOptions) {
            return stepSlider(text, Arrays.asList(stepOptions), null);
        }
        public CustomFormBuilder stepSlider(@NonNull String text, @NonNull List<String> stepOptions, @Nullable Consumer<StepSlider> handle) {
            return element(new StepSlider(text, stepOptions, handle));
        }
        public CustomFormBuilder stepSlider(@NonNull String text, @NonNull String[] stepOptions, @Nullable Consumer<StepSlider> handle) {
            return element(new StepSlider(text, Arrays.asList(stepOptions), handle));
        }
        public CustomFormBuilder stepSlider(@NonNull String text, int defaultStepIndex, @NonNull List<String> stepOptions) {
            return element(new StepSlider(text, stepOptions, defaultStepIndex, null));
        }
        public CustomFormBuilder stepSlider(@NonNull String text, int defaultStepIndex, @NonNull String[] stepOptions) {
            return element(new StepSlider(text, Arrays.asList(stepOptions), defaultStepIndex, null));
        }
        public CustomFormBuilder stepSlider(@NonNull String text, int defaultStepIndex, @NonNull List<String> stepOptions, @Nullable Consumer<StepSlider> handle) {
            return element(new StepSlider(text, stepOptions, defaultStepIndex, handle));
        }
        public CustomFormBuilder stepSlider(@NonNull String text, int defaultStepIndex, @NonNull String[] stepOptions, @Nullable Consumer<StepSlider> handle) {
            return element(new StepSlider(text, Arrays.asList(stepOptions), defaultStepIndex, handle));
        }

        public CustomFormBuilder toggle(@NonNull String text) {
            return element(new Toggle(text));
        }
        public CustomFormBuilder toggle(@NonNull String text, boolean defaultValue) {
            return element(new Toggle(text, defaultValue));
        }
        public CustomFormBuilder toggle(@NonNull String text, boolean defaultValue, @Nullable Consumer<Toggle> handle) {
            return element(new Toggle(text, defaultValue, handle));
        }
        public CustomFormBuilder toggle(@NonNull String text, @Nullable Consumer<Toggle> handle) {
            return element(new Toggle(text, false, handle));
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

        @Override public CustomForm build() {return new CustomForm(image, title, Collections.unmodifiableList(elements), onSubmit, onClose, onError);}
        @Override protected CustomFormBuilder self() {
            return this;
        }
    }
}
