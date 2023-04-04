package xxAROX.WDforms.forms.types;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import jline.internal.Nullable;

import java.util.Map;

abstract public class Form {
    private String title;

    public Form(String title) {
        this.title = title;
    }

    public final String getTitle() {return title;}
    public final void setTitle(String title) {this.title = title;}
    abstract Type getType();

    final public JsonObject jsonSerialize(){
        JsonObject json = new JsonObject();
        JsonObject json2 = this.serializeFormData();
        json.addProperty("title", this.getTitle());
        json.addProperty("type", this.getType().toString());
        if (json2 != null) {
            for (Map.Entry<String, JsonElement> entry : json2.entrySet()) {
                json.add(entry.getKey(), entry.getValue());
            }
        }
        return json;
    }
    abstract protected JsonObject serializeFormData();
    public abstract void handleResponse(ProxiedPlayer player, @Nullable String response);

    public enum Type {
        MODAL("modal"),
        MENU("form"),
        CUSTOM("custom_form");

        private final String value;
        Type(String value) {this.value = value;}
        @Override public String toString() {return this.value;}
    }
}
