package xxAROX.WDForms.forms.elements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.function.Consumer;


@ToString(exclude = {"handle"})
public class Toggle extends Element{
    @JsonProperty("default") private Boolean defaultValue = false;
    @Setter @JsonIgnore private Boolean value = false;
    @JsonIgnore private Consumer<Toggle> handle;
    public void handle(){if (handle != null) handle.accept(this);}

    public Toggle(String text, Boolean defaultValue, Consumer<Toggle> handle) {
        super(Type.TOGGLE, text);
        this.defaultValue = defaultValue;
        this.handle = handle;
    }
    public Toggle(@NonNull String text, Consumer<Toggle> handle) {this(text, false, handle);}
    public Toggle(@NonNull String text, Boolean defaultValue) {this(text, defaultValue, null);}
    public Toggle(@NonNull String text) {this(text, false, null);}
    public Boolean hasChanged(){return value == defaultValue;}
    public Boolean getValue() {return value;}
}
