package xxAROX.WDforms.forms.elements;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import jline.internal.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.BiConsumer;

public class Dropdown extends Element{
    @Override public Type getType() {return Type.DROPDOWN;}

    @Setter @Getter protected String[] options;
    @Getter @Setter protected Integer value;
    @Getter @Setter @Nullable protected Integer defaultValue;
    @Setter @Getter @Nullable protected BiConsumer<ProxiedPlayer, Dropdown> onSubmit;

    public Dropdown(String text, String[] options, Integer defaultValue, BiConsumer<ProxiedPlayer, Dropdown> onSubmit){
        super(text);
        this.options = options;
        this.defaultValue = defaultValue;
        this.onSubmit = onSubmit;
    }
    public Dropdown(String text, String[] options, Integer defaultValue){
        super(text);
        this.options = options;
        this.defaultValue = defaultValue;
        this.onSubmit = null;
    }
    public Dropdown(String text, String[] options, BiConsumer<ProxiedPlayer, Dropdown> onSubmit){
        super(text);
        this.options = options;
        this.defaultValue = 0;
        this.onSubmit = onSubmit;
    }
    public Dropdown(String text, String[] options){
        super(text);
        this.options = options;
        this.defaultValue = 0;
        this.onSubmit = null;
    }

    @Override protected JsonObject serializeElementData() {
        JsonObject json = new JsonObject();
        JsonArray options = new JsonArray();
        for (String option : this.options) options.add(option);
        json.add("options", options);
        return json;
    }

    public String getSelectedOption(){return getValue() != null ? Arrays.stream(options).toList().get(getValue()) : null;}
}
