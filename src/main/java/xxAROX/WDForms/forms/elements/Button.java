package xxAROX.WDForms.forms.elements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import org.cloudburstmc.protocol.common.util.Preconditions;

import java.util.function.Consumer;

@Getter
@ToString(exclude = {"onClick"})
public class Button{
    @JsonIgnore private final Element.Type type = Element.Type.BUTTON;
    @JsonProperty("text") protected String text;
    @JsonProperty("image") protected Image image;
    @JsonIgnore private final Consumer<Button> onClick;
    public void click(){if (onClick != null) onClick.accept(this);}


    public Button(String text, Image image, Consumer<Button> onClick){
        Preconditions.checkNotNull(text, "The provided button text can not be null");
        this.text = text;
        this.image = image;
        this.onClick = onClick;
    }
    public Button(String text, Image image){this(text, image, null);}
    public Button(String text, Consumer<Button> onClick){this(text, null, onClick);}
    public Button(String text){this(text, null, null);}
}
