package xxAROX.WDforms.forms.types;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import jline.internal.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import xxAROX.WDforms.forms.FormValidationError;
import xxAROX.WDforms.forms.elements.Button;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MenuForm extends Form{
    @Getter @Setter private String text = "";
    @Getter @Setter private ArrayList<Button> buttons = new ArrayList<>();
    @Getter @Setter @Nullable private Consumer<ProxiedPlayer> onClose;

    public MenuForm(String title, String text, ArrayList<Button> buttons, Consumer<ProxiedPlayer> onClose){
        super(title);
        this.text = text;
        this.buttons.addAll(buttons);
        this.onClose = onClose;
    }
    public MenuForm(String title, String text, ArrayList<Button> buttons){this(title, text, buttons, null);}
    public MenuForm(String title, String text){this(title, text, new ArrayList<>(), null);}
    public MenuForm(String title, ArrayList<Button> buttons){this(title, "", buttons, null);}
    public MenuForm(String title){this(title, "", new ArrayList<>());}
    @Override Type getType() {return Type.MENU;}

    @Override public void handleResponse(ProxiedPlayer player, @Nullable String response) {
        if (response == null) {
            if (onClose != null) onClose.accept(player);
        } else {
            Integer buttonIndex = (new Gson().fromJson(response, Integer.class));
            Button button = this.buttons.get(buttonIndex);
            if (button == null) throw new FormValidationError("Button with index " + buttonIndex + " does not exist");
            button.setValue(buttonIndex);
        }
    }

    @Override
    protected JsonObject serializeFormData() {
        JsonObject json = new JsonObject();
        json.addProperty("content", text);
        JsonArray jsonArray = new JsonArray();
        for (Button btn : buttons) jsonArray.add(btn.jsonSerialize());
        json.add("buttons", jsonArray);
        return json;
    }
}
