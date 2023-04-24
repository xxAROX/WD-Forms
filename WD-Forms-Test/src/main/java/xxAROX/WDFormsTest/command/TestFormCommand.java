package xxAROX.WDFormsTest.command;

import dev.waterdog.waterdogpe.command.Command;
import dev.waterdog.waterdogpe.command.CommandSender;
import dev.waterdog.waterdogpe.command.CommandSettings;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import org.apache.commons.lang3.RandomUtils;
import xxAROX.WDForms.forms.elements.Button;
import xxAROX.WDForms.forms.elements.Image;
import xxAROX.WDForms.forms.types.CustomForm;
import xxAROX.WDForms.forms.types.MenuForm;
import xxAROX.WDForms.forms.types.ModalForm;

import java.util.ArrayList;
import java.util.List;

public class TestFormCommand extends Command {
    public TestFormCommand() {
        super("testwdforms", CommandSettings.builder().setUsageMessage("/testwdforms <menu|custom|modal>").setDescription("To test WD-Forms plugin functionality").build());
    }
    @Override public boolean onExecute(CommandSender commandSender, String aliasUsed, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer player)) {
            commandSender.sendMessage("Only for players!");
            return true;
        }
        if (strings.length == 0) return false;
        switch (strings[0]) {
            default -> {return false;}
            case "menu" -> menu(player);
            case "custom" -> custom(player);
            case "modal" -> modal(player);
        }
        return true;
    }
    public static void menu(ProxiedPlayer player){
        List<Button> buttons = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            buttons.add(new Button("MenuForm.button." + finalI, Image.create("https://picsum.photos/512"), button -> player.sendMessage("You clicked on button " + finalI)));
        }
        MenuForm.menu()
                .title("MenuForm.title")
                .content("MenuForm.content")
                .buttons(buttons)
                .onClose(() -> player.sendMessage("You closed the form!"))
                .onError(throwable -> player.sendMessage("Error in form handler: " + throwable.getMessage()))
                .onSubmit(response -> player.sendMessage("Form response: " + response.toString()))
                .build()
                .sendTo(player)
        ;
    }
    public static void custom(ProxiedPlayer player){
        CustomForm.custom()
                .title("CustomForm.title")
                .label("CustomForm.label")
                .toggle("CustomForm.toggle", RandomUtils.nextBoolean())
                .dropdown("CustomForm.dropdown", RandomUtils.nextInt(1, 5) -1, new String[]{"a","b","c","d","e"})
                .slider("CustomForm.slider", RandomUtils.nextInt(1, 100), RandomUtils.nextInt(1, 100), 1, RandomUtils.nextInt(1, 100))
                .input("CustomForm.input", "CustomForm.input.placeholder", "CustomForm.input.default")
                .stepSlider("CustomForm.step_slider", RandomUtils.nextInt(1, 5) -1, new String[]{"a","b","c","d","e"})
                .onClose(() -> player.sendMessage("You closed the form!"))
                .onError(throwable -> player.sendMessage("Error in form handler: " + throwable.getMessage()))
                .onSubmit(response -> player.sendMessage("Form response: " + response.toString()))
                .build()
                .sendTo(player)
        ;
    }
    public static void modal(ProxiedPlayer player){
        ModalForm.modal()
                .title("ModalForm.title")
                .content("ModalForm.label")
                .trueButton("ModalForm.true")
                .falseButton("ModalForm.false")
                .onError(throwable -> player.sendMessage("Error in form handler: " + throwable.getMessage()))
                .onSubmit(response -> player.sendMessage("Form response: " + response.toString()))
                .build()
                .sendTo(player)
        ;
    }
}
