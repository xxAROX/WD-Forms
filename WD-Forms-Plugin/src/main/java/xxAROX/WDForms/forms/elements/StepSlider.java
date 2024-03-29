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
import lombok.*;
import org.cloudburstmc.protocol.common.util.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@ToString(exclude = {"handle","playerHandler"})
public class StepSlider extends Element{
    @Getter @Setter protected List<String> steps = new ArrayList<>();
    @JsonProperty("default") @Getter @Setter protected Integer defaultStepIndex;
    @JsonIgnore @Getter @Setter protected Integer value;
    @JsonIgnore @Getter @Setter protected Consumer<StepSlider> handle;
    @JsonIgnore @Getter @Setter protected BiConsumer<StepSlider, ProxiedPlayer> playerHandler;
    public void handle(ProxiedPlayer player){
        if (handle != null) handle.accept(this);
        if (playerHandler != null) playerHandler.accept(this, player);
    }

    public StepSlider(@NonNull String text, @NonNull List<String> steps, Integer defaultStepIndex, Consumer<StepSlider> handle){
        super(Type.STEP_SLIDER, text);
        Preconditions.checkNotNull(steps, "The provided steps can not be null");
        this.steps.addAll(steps);
        this.defaultStepIndex = defaultStepIndex;
        this.handle = handle;
    }
    public StepSlider(@NonNull String text, @NonNull List<String> steps, Integer defaultStepIndex, BiConsumer<StepSlider, ProxiedPlayer> playerHandler){
        super(Type.STEP_SLIDER, text);
        Preconditions.checkNotNull(steps, "The provided steps can not be null");
        this.steps.addAll(steps);
        this.defaultStepIndex = defaultStepIndex;
        this.playerHandler = playerHandler;
    }
    public StepSlider(@NonNull String text, @NonNull List<String> steps, Integer defaultStepIndex){
        this(text, steps, defaultStepIndex, (Consumer<StepSlider>) null);
    }
    public StepSlider(@NonNull String text, @NonNull List<String> steps, Consumer<StepSlider> handle){
        this(text, steps, 0, handle);
    }
    public StepSlider(@NonNull String text, @NonNull List<String> steps, BiConsumer<StepSlider, ProxiedPlayer> handle){
        this(text, steps, 0, handle);
    }
    public StepSlider(@NonNull String text, @NonNull List<String> steps){
        this(text, steps, 0, (Consumer<StepSlider>) null);
    }

    public String getStep(int index) {return steps.get(index);}
    public String getSelectedOption(){return getValue() != null ? steps.get(getValue()) : null;}

    @RequiredArgsConstructor @Getter @ToString public static class Response {
        private final int index;
        private final String step;
    }
}
