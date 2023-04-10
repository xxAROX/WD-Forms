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

@Getter
@ToString(exclude = {"handle"})
public class Input extends Element{
    protected String placeholder;
    @JsonProperty("default") private String defaultText;

    @Setter @JsonIgnore private String value;
    @JsonIgnore private Consumer<Input> handle;
    @JsonIgnore private BiConsumer<Input, ProxiedPlayer> playerHandler;
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
