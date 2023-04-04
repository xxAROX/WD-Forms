package xxAROX.WDforms.forms.elements;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class Image {
    private String data;
    private Type type;

    public static Image textures(String data){return new Image(data, Type.TEXTURES);}
    public static Image url(String url){return new Image(url, Type.URL);}

    protected JsonObject serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type.toString());
        json.addProperty("data", data);
        return json;
    }

    public enum Type{
        TEXTURES("path"),
        URL("url");
        private final String value;
        Type(String value) {this.value = value;}
        @Override public String toString() {return value;}
    }
}
