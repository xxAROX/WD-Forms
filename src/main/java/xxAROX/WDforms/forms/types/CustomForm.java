package xxAROX.WDforms.forms.types;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import jline.internal.Nullable;
import lombok.Getter;
import lombok.Setter;
import xxAROX.WDforms.forms.elements.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CustomForm extends Form{
    @Getter protected ArrayList<? extends Element> elements;
    @Getter @Setter @Nullable private Consumer<ProxiedPlayer> onClose;
    @Getter @Setter @Nullable private BiConsumer<ProxiedPlayer, Response> onSubmit;

    public CustomForm(String title, ArrayList<? extends Element> elements, Consumer<ProxiedPlayer> onClose, BiConsumer<ProxiedPlayer, Response> onSubmit){
        super(title);
        this.elements = elements;
        this.onClose = onClose;
        this.onSubmit = onSubmit;
    }
    public CustomForm(String title, ArrayList<? extends Element> elements, Consumer<ProxiedPlayer> onClose){
        super(title);
        this.elements = elements;
        this.onClose = onClose;
        this.onSubmit = null;
    }
    public CustomForm(String title, ArrayList<? extends Element> elements){
        super(title);
        this.elements = elements;
        this.onClose = null;
        this.onSubmit = null;
    }

    @Override
    protected JsonObject serializeFormData() {
        return null;
    }

    @Override public void handleResponse(ProxiedPlayer player, @Nullable String response) {
        if (response == null) {
            if (onClose != null) onClose.accept(player);
        } else {
            ArrayList<Object> results = (new Gson()).fromJson(response, ArrayList.class);
            System.out.println(results);
            CustomForm.Response response1 = new Response(this);
            elements.forEach(element -> {
                if (element instanceof Dropdown) ((Dropdown) element).getOnSubmit().accept(player, (Dropdown) element);
                else if (element instanceof Input) ((Input) element).getOnSubmit().accept(player, (Input) element);
                else if (element instanceof Slider) ((Slider) element).getOnSubmit().accept(player, (Slider) element);
                else if (element instanceof StepSlider) ((StepSlider) element).getOnSubmit().accept(player, (StepSlider) element);
                else if (element instanceof Toggle) ((Toggle) element).getOnSubmit().accept(player, (Toggle) element);
            });
            if (onSubmit != null) onSubmit.accept(player, response1);
        }
    }

    @Override Type getType() {return Type.CUSTOM;}

    public static class Response {
        @Getter private ArrayList<? extends Element> elements;

        public Response(CustomForm form) {
            elements = form.getElements();
        }

        public Element tryGet(Element.Type type){
            if (type.equals(Element.Type.BUTTON) || type.equals(Element.Type.LABEL)) return null;
            for (Element element: elements) {
                if (element.getType().equals(type)) {
                    elements.remove(element);
                    return element;
                }
            }
            return null;
        }
        public Dropdown getDropdown(){return (Dropdown) tryGet(Element.Type.DROPDOWN);}
        public Input getInput(){return (Input) tryGet(Element.Type.INPUT);}
        public Slider getSlider(){return (Slider) tryGet(Element.Type.SLIDER);}
        public StepSlider getStepSlider(){return (StepSlider) tryGet(Element.Type.STEP_SLIDER);}
        public Toggle getToggle(){return (Toggle) tryGet(Element.Type.TOGGLE);}
        public ArrayList<Object> getValues(){
            ArrayList<Object> values = new ArrayList<>();
            for (Element element: elements) {
                values.add(element instanceof Dropdown ? ((Dropdown) element).getSelectedOption() : element.getValue());
            }
            return values;
        }
    }
}
