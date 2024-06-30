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
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.function.BiConsumer;
import java.util.function.Consumer;


@ToString(exclude = {"handle","playerHandler"})
public class Toggle extends Element{
    @JsonProperty("default") @Getter @Setter protected Boolean defaultValue;
    @JsonIgnore @Getter @Setter protected Boolean value = false;
    @JsonIgnore @Getter @Setter protected Consumer<Toggle> handle;
    @JsonIgnore @Getter @Setter protected BiConsumer<Toggle, ProxiedPlayer> playerHandler;
    public void handle(ProxiedPlayer player){
        if (handle != null) handle.accept(this);
        if (playerHandler != null) playerHandler.accept(this, player);
    }

    public Toggle(String text, Boolean defaultValue, Consumer<Toggle> handle) {
        super(Type.TOGGLE, text);
        this.defaultValue = defaultValue;
        this.handle = handle;
    }
    public Toggle(String text, Boolean defaultValue, BiConsumer<Toggle, ProxiedPlayer> playerHandler) {
        super(Type.TOGGLE, text);
        this.defaultValue = defaultValue;
        this.playerHandler = playerHandler;
    }
    public Toggle(@NonNull String text, Consumer<Toggle> handle) {this(text, false, handle);}
    public Toggle(@NonNull String text, Boolean defaultValue) {this(text, defaultValue, (Consumer<Toggle>) null);}
    public Toggle(@NonNull String text) {this(text, false, (Consumer<Toggle>) null);}
    public Boolean hasChanged(){return value == defaultValue;}
}
