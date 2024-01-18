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
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.cloudburstmc.protocol.common.util.Preconditions;

@ToString
abstract public class Element {
    @JsonProperty("type") @Getter protected final Type type;
    @JsonProperty("text") @Getter @Setter protected String text;

    public Element(@NonNull Type type, @NonNull String text){
        Preconditions.checkNotNull(type, "The provided type can not be null");
        Preconditions.checkNotNull(text, "The provided text can not be null");
        this.type = type;
        this.text = text;
    }

    public enum Type{
        @JsonProperty("button") BUTTON,
        @JsonProperty("dropdown") DROPDOWN,
        @JsonProperty("input") INPUT,
        @JsonProperty("label") LABEL,
        @JsonProperty("slider") SLIDER,
        @JsonProperty("step_slider") STEP_SLIDER,
        @JsonProperty("toggle") TOGGLE;
    }
}
