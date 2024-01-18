/*
 * Copyright (c) Jan Sohn aka. xxAROX
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
public final class Image {
    @JsonProperty("data") private final String data;
    @JsonProperty("type") private final Type type;

    public static Image textures(String data){return new Image(data, Type.TEXTURES);}
    public static Image url(String url){return new Image(url, Type.URL);}
    public static Image create(String data){return new Image(data, data.startsWith("http") ? Type.URL : Type.TEXTURES);}

    private Image(String data, Type type) {
        this.data = data;
        this.type = type;
    }

    @ToString public enum Type{
        @JsonProperty("path") TEXTURES,
        @JsonProperty("url") URL
    }
    static final class ImageSerializer extends JsonSerializer<Image> {
        @Override public void serialize(Image image, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (image == null || image.getData() == null || image.getData().isEmpty() || image.getType() == null) {
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
