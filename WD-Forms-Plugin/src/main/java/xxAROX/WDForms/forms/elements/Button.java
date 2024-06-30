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
import lombok.Setter;
import lombok.ToString;
import org.cloudburstmc.protocol.common.util.Preconditions;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@ToString(exclude = {"onClick","onClickPlayer"})
public class Button{
    @JsonIgnore private final Element.Type type = Element.Type.BUTTON;
    @JsonProperty("text") @Getter @Setter protected String text;
    @JsonProperty("image") @Getter @Setter protected Image image;
    @JsonIgnore @Getter @Setter protected Consumer<Button> onClick;
    @JsonIgnore @Getter @Setter protected BiConsumer<Button, ProxiedPlayer> onClickPlayer;
    public void click(ProxiedPlayer player){
        if (onClick != null) onClick.accept(this);
        if (onClickPlayer != null) onClickPlayer.accept(this, player);
    }


    public Button(String text, Image image, Consumer<Button> onClick){
        Preconditions.checkNotNull(text, "The provided button text can not be null");
        this.text = text;
        this.image = image;
        this.onClick = onClick;
        this.onClickPlayer = null;
    }
    public Button(String text, Image image, BiConsumer<Button, ProxiedPlayer> onClickPlayer){
        Preconditions.checkNotNull(text, "The provided button text can not be null");
        this.text = text;
        this.image = image;
        this.onClick = null;
        this.onClickPlayer = onClickPlayer;
    }
    public Button(String text, Image image){this(text, image, (Consumer<Button>) null);}
    public Button(String text, Consumer<Button> onClick){this(text, null, onClick);}
    public Button(String text, BiConsumer<Button, ProxiedPlayer> onClickPlayer){this(text, null, onClickPlayer);}
    public Button(String text){this(text, null, (Consumer<Button>) null);}
}
