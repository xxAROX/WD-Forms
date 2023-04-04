package xxAROX.WDforms.forms.elements;

import com.google.gson.JsonObject;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import jline.internal.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.util.function.BiConsumer;

public class Button extends Element{
    @Getter @Setter @Nullable protected Image image;
    @Getter @Setter protected Integer value;
    @Getter @Setter @Nullable protected Integer defaultValue = null;
    @Setter @Getter @Nullable protected BiConsumer<ProxiedPlayer, Button> onSubmit;


    public Button(String text, @Nullable Image image, @Nullable BiConsumer<ProxiedPlayer, Button> onSubmit){
        super(text, null);
        this.image = image;
        this.onSubmit = onSubmit;
    }
    public Button(String text, @Nullable Image image){
        super(text, null);
        this.image = image;
        this.onSubmit = null;
    }
    public Button(String text, @Nullable BiConsumer<ProxiedPlayer, Button> onSubmit){
        super(text, null);
        image = null;
        this.onSubmit = onSubmit;
    }
    public Button(String text){
        super(text, null);
        onSubmit = null;
        image = null;
    }

    @Override
    protected JsonObject serializeElementData() {
        JsonObject json = new JsonObject();
        json.addProperty("text", text);
        if (image != null) json.add("image", image.serialize());
        return json;
    }

    @Override public Type getType() {return Type.BUTTON;}
}
