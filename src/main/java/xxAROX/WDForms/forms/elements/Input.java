package xxAROX.WDForms.forms.elements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jline.internal.Nullable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.function.Consumer;

@Getter
@ToString(exclude = {"handle"})
public class Input extends Element{
    protected String placeholder;
    @JsonProperty("default") private String defaultText;

    @Setter @JsonIgnore private String value;
    @JsonIgnore private Consumer<Input> handle;
    public void handle(){if (handle != null) handle.accept(this);}

    public Input(@NonNull String text, @Nullable String placeholder, @Nullable String defaultText, @Nullable Consumer<Input> handle){
        super(Type.INPUT, text);
        this.placeholder = placeholder;
        this.defaultText = defaultText;
        this.handle = handle;
    }
    public Input(@NonNull String text, @Nullable String placeholder){this(text, placeholder, null, null);}
    public Input(@NonNull String text, @Nullable String placeholder, @Nullable String defaultValue){this(text, placeholder, defaultValue, null);}
    public Input(@NonNull String text, @Nullable String placeholder, @Nullable Consumer<Input> handle){this(text, placeholder, "", handle);}
    public Input(@NonNull String text, @Nullable Consumer<Input> handle){this(text, "", "", handle);}
    public Input(@NonNull String text){
        this(text, "", "", null);
    }
}
