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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import jline.internal.Nullable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@ToString(exclude = {"handle","playerHandler"})
public class Input extends Element{
    @Getter @Setter protected String placeholder;
    @JsonProperty("default") @Getter @Setter protected String defaultText;
    @JsonIgnore @Getter @Setter protected String value;
    @JsonIgnore @Getter @Setter protected Consumer<Input> handle;
    @JsonIgnore @Getter @Setter protected BiConsumer<Input, ProxiedPlayer> playerHandler;
    public void handle(ProxiedPlayer player){
        if (handle != null) handle.accept(this);
        if (playerHandler != null) playerHandler.accept(this, player);
    }

    public Input(@NonNull String text, @Nullable String placeholder, @Nullable String defaultText, @Nullable Consumer<Input> handle){
        super(Type.INPUT, text);
        this.placeholder = placeholder;
        this.defaultText = defaultText;
        this.handle = handle;
    }
    public Input(@NonNull String text, @Nullable String placeholder, @Nullable String defaultText, @Nullable BiConsumer<Input, ProxiedPlayer> playerHandler){
        super(Type.INPUT, text);
        this.placeholder = placeholder;
        this.defaultText = defaultText;
        this.playerHandler = playerHandler;
    }
    public Input(@NonNull String text, @Nullable String placeholder){
        this(text, placeholder, null, (Consumer<Input>) null);
    }
    public Input(@NonNull String text, @Nullable String placeholder, @Nullable String defaultValue){
        this(text, placeholder, defaultValue, (Consumer<Input>) null);
    }
    public Input(@NonNull String text, @Nullable String placeholder, @Nullable Consumer<Input> handle){
        this(text, placeholder, "", handle);
    }
    public Input(@NonNull String text, @Nullable String placeholder, @Nullable BiConsumer<Input, ProxiedPlayer> playerHandler){
        this(text, placeholder, "", playerHandler);
    }
    public Input(@NonNull String text, @Nullable Consumer<Input> handle){
        this(text, "", "", handle);
    }
    public Input(@NonNull String text, @Nullable BiConsumer<Input, ProxiedPlayer> playerHandler){
        this(text, "", "", playerHandler);
    }
    public Input(@NonNull String text){
        this(text, "", "", (Consumer<Input>) null);
    }
}
