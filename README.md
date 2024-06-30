# WD-forms
Forms plugin library for @WaterdogPE.

> #### ![GitHub all releases](https://img.shields.io/github/downloads/xxAROX/WD-Forms/total?color=violet&label=Downloads&style=flat-square)

<details>
<summary>How to use?</summary>

```java
package xxAROX.WDForms.documentation;

import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;
import xxAROX.WDForms.FormPlayerSession;
import xxAROX.WDForms.forms.elements.Image;
import xxAROX.WDForms.forms.types.CustomForm;
import xxAROX.WDForms.forms.types.Form;
import xxAROX.WDForms.forms.types.ModalForm;

public class Documentation extends Plugin {
    public void sendMenuForm(ProxiedPlayer player) {
        MenuForm form = MenuForm.menu()
                .title("Menu form")
                .content("Content")
                .button("§cClose")
                .button("Button #1", (button) -> player.sendMessage("You pressed: " + button.getText()))
                .button("Button #2", (button) -> player.sendMessage("You pressed: " + button.getText()))
                .button("Button #3", (button) -> player.sendMessage("You pressed: " + button.getText()))
                .button("Button #4", Image.url("https://avatars.githubusercontent.com/u/57589973?s=128&v=4"), (button) -> player.sendMessage("You pressed: " + button.getText()))
                .button("Button #5", Image.textures("textures/items/iron_ingot"), (button) -> player.sendMessage("You pressed: " + button.getText()))
                .onSubmit((response) -> player.sendMessage("Clicked: " + response.button() + "!"))
                .onClose(() -> player.sendMessage("Form closed!"))
                .onError((e) -> player.sendMessage("§cError§f: §7" + e.getMessage()))
                .build();
        form.sendTo(player);
    }

    public void sendModalForm(ProxiedPlayer player) {
        ModalForm form = ModalForm.modal()
                .title("Modal form")
                .content("Content")
                .trueButton("true")
                .falseButton("false")
                .onSubmit((response) -> player.sendMessage("Clicked: " + response + "!"))
                .onError((e) -> player.sendMessage("§cError§f: §7" + e.getMessage()))
                .build();
        form.sendTo(player);
    }

    public void sendCustomForm(ProxiedPlayer player) {
        CustomForm form = CustomForm.custom()
                .title("Custom form")
                .label("Label")
                .input("Input", input -> player.sendMessage(input.getValue()))
                .dropdown("Dropdown", new String[]{"Option 1", "Option 2", "Option 3", "Option 4", "Option 5"}, dropdown -> player.sendMessage(dropdown.getSelectedOption()))
                .stepSlider("StepSlider", new String[]{"Step 1", "Step 2", "Step 3", "Step 4", "Step 5"}, (step_slider) -> player.sendMessage(step_slider.getSelectedOption()))
                .slider("Slider", 1f, 5f, 1f, 1f, (slider) -> player.sendMessage(slider.getValue().toString()))
                .toggle("Toggle", (toggle) -> player.sendMessage("toggle: " + (toggle.getValue() ? "true" : "false")))
                .onSubmit(response -> player.sendMessage("Form submitted!\n" + response.toString()))
                .onClose(() -> player.sendMessage("Form closed!"))
                .onError(e -> player.sendMessage("§cError§f: §7" + e.getMessage()))
                .build();
        form.sendTo(player);
    }
}
```

</details>