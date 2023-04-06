package xxAROX.WDForms.forms.elements;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;

@Getter
@ToString
@JsonSerialize(using = Image.ImageSerializer.class)
public class Image {
    @JsonProperty("data") private final String data;
    @JsonProperty("type") private final Type type;

    public static Image textures(String data){return new Image(data, Type.TEXTURES);}
    public static Image url(String url){return new Image(url, Type.URL);}

    private Image(String data, Type type) {
        this.data = data;
        this.type = type;
    }

    @ToString public enum Type{
        @JsonProperty("path") TEXTURES,
        @JsonProperty("url") URL
    }
    static final class ImageSerializer extends JsonSerializer<Image> {
        @Override
        public void serialize(Image image, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (image.getData() == null || image.getData().isEmpty() || image.getType() == null) {
                jsonGenerator.writeNull();
                return;
            }
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("type", image.getType());
            jsonGenerator.writeStringField("data", image.getData());
            jsonGenerator.writeEndObject();
        }
    }
}
