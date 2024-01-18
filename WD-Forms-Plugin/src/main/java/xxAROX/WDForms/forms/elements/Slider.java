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
public class Slider extends Element{
    @Getter @Setter protected float min;
    @Getter @Setter protected float max;
    @Getter @Setter protected float step;
    @JsonProperty("default") @Getter @Setter protected Float defaultValue;
    @JsonIgnore @Getter @Setter protected Float value;
    @JsonIgnore @Getter @Setter protected Consumer<Slider> handle;
    @JsonIgnore @Getter @Setter protected BiConsumer<Slider, ProxiedPlayer> playerHandler;
    public void handle(ProxiedPlayer player){
        if (handle != null) handle.accept(this);
        if (playerHandler != null) playerHandler.accept(this, player);
    }

    public Slider(@NonNull String text, Float min, Float max, Float step, Float defaultValue, Consumer<Slider> handle){
        super(Type.SLIDER, text);
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
        assert step <= 0 : "Step must be >=1";
        this.step = step;
        this.defaultValue = defaultValue == null ? this.min : defaultValue;
        this.handle = handle;
    }
    public Slider(@NonNull String text, Float min, Float max, Float step, Float defaultValue, BiConsumer<Slider, ProxiedPlayer> playerHandler){
        super(Type.SLIDER, text);
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
        assert step <= 0 : "Step must be >=1";
        this.step = step;
        this.defaultValue = defaultValue == null ? this.min : defaultValue;
        this.playerHandler = playerHandler;
    }
    public Slider(@NonNull String text, Float min, Float max, Float step, Float defaultValue){
        this(text, min, max, step, defaultValue, (Consumer<Slider>) null);
    }
    public Slider(@NonNull String text, Float min, Float max, Float step){
        this(text, min, max, step, null, (Consumer<Slider>) null);
    }
    public Slider(@NonNull String text, Float min, Float max, Float step, Consumer<Slider> handle){
        this(text, min, max, step, null, handle);
    }
    public Slider(@NonNull String text, Float min, Float max, Float step, BiConsumer<Slider, ProxiedPlayer> playerHandler){
        this(text, min, max, step, null, playerHandler);
    }
    public Slider(@NonNull String text, Float min, Float max){
        this(text, min, max, 1f, 0f, (Consumer<Slider>) null);
    }
    public Slider(@NonNull String text){
        this(text, 0f, 100f, 1f, null, (Consumer<Slider>) null);
    }
    public Slider(@NonNull String text, Float min, Float max, Consumer<Slider> handle){
        this(text, min, max, 1f, null, handle);
    }
    public Slider(@NonNull String text, Float min, Float max, BiConsumer<Slider, ProxiedPlayer> playerHandler){
        this(text, min, max, 1f, null, playerHandler);
    }
}
