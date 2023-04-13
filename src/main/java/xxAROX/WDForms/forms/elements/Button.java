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
    @JsonIgnore private final Consumer<Button> onClick;
    @JsonIgnore private final BiConsumer<Button, ProxiedPlayer> onClickPlayer;
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
