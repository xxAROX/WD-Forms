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
public class Dropdown extends Element{
    @Getter @Setter protected List<String> options = new ArrayList<>();
    @JsonProperty("default") @Getter @Setter private int defaultOptionIndex;
    @JsonIgnore @Getter @Setter private Integer value;
    @JsonIgnore private Consumer<Dropdown> handle;
    @JsonIgnore private BiConsumer<Dropdown, ProxiedPlayer> playerHandler;
    public void handle(ProxiedPlayer player){
        if (handle != null) handle.accept(this);
        if (playerHandler != null) playerHandler.accept(this, player);
    }

    public Dropdown(@NonNull String text, int defaultOptionIndex, @NonNull List<String> options, Consumer<Dropdown> handle){
        super(Type.DROPDOWN, text);
        Preconditions.checkNotNull(options, "The provided dropdown options can not be null");
        Preconditions.checkElementIndex(defaultOptionIndex, options.size(), "Default option index");
        this.defaultOptionIndex = defaultOptionIndex;
        this.options.addAll(options);
        this.handle = handle;
    }
    public Dropdown(@NonNull String text, int defaultOptionIndex, @NonNull List<String> options, BiConsumer<Dropdown, ProxiedPlayer> playerHandler){
        super(Type.DROPDOWN, text);
        Preconditions.checkNotNull(options, "The provided dropdown options can not be null");
        Preconditions.checkElementIndex(defaultOptionIndex, options.size(), "Default option index");
        this.defaultOptionIndex = defaultOptionIndex;
        this.options.addAll(options);
        this.playerHandler = playerHandler;
    }
    public Dropdown(@NonNull String text, @NonNull List<String> options, Integer defaultOptionIndex){
        this(text, defaultOptionIndex, options, (Consumer<Dropdown>) null);
    }
    public Dropdown(@NonNull String text, @NonNull List<String> options, Consumer<Dropdown> handle){
        this(text, 0, options, handle);
    }
    public Dropdown(@NonNull String text, @NonNull List<String> options, BiConsumer<Dropdown, ProxiedPlayer> playerHandler){
        this(text, 0, options, playerHandler);
    }
    public Dropdown(@NonNull String text, @NonNull List<String> options){
        this(text, 0, options, (Consumer<Dropdown>) null);
    }
    public String getSelectedOption(){return value != null ? options.get(value) : null;}
    public String getDropdownOption(int index) {return this.options.get(index);}


    @RequiredArgsConstructor @Getter @ToString
    public static class Response {
        private final int index;
        private final String option;
    }
}
