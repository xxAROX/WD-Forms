package xxAROX.WDFormsTest;

import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;
import org.apache.commons.lang3.RandomUtils;
import xxAROX.WDForms.WDForms;
import xxAROX.WDForms.forms.elements.Image;
import xxAROX.WDForms.forms.types.ProxySettingsForm;
import xxAROX.WDFormsTest.command.TestFormCommand;

public class WDFormsTestPlugin extends Plugin {
    @Override public void onEnable() {
        getProxy().getCommandMap().registerCommand(new TestFormCommand());
        WDForms.getGlobalSettings().add(WDFormsTestPlugin::settings);
    }
    public static ProxySettingsForm.ProxySettingsFormBuilder settings(ProxiedPlayer player){
        return (ProxySettingsForm.ProxySettingsFormBuilder) ProxySettingsForm.settings()
                .image(Image.create("textures/items/diamond"))
                .title("ProxySettingsForm.title")
                .label("ProxySettingsForm.label")
                .toggle("ProxySettingsForm.toggle", RandomUtils.nextBoolean())
                .dropdown("ProxySettingsForm.dropdown", RandomUtils.nextInt(1, 5) -1, new String[]{"a","b","c","d","e"})
                .slider("ProxySettingsForm.slider", RandomUtils.nextInt(1, 100), RandomUtils.nextInt(1, 100), 1, RandomUtils.nextInt(1, 100))
                .input("ProxySettingsForm.input", "ProxySettingsForm.input.placeholder", "ProxySettingsForm.input.default")
                .stepSlider("ProxySettingsForm.step_slider", RandomUtils.nextInt(1, 5) -1, new String[]{"a","b","c","d","e"})
                .onClose(() -> player.sendMessage("You closed the form!"))
                .onError(throwable -> player.sendMessage("Error in form handler: " + throwable.getMessage()))
                .onSubmit(response -> player.sendMessage("Form handled"))
        ;
    }
}
