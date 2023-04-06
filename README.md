# WD-forms
Forms for WaterdogPE

<details>
<summary>How to use?</summary>

```java
package xxAROX.WDForms.documentation;

import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;
import xxAROX.WDForms.FormPlayerSession;
import xxAROX.WDForms.forms.types.Form;

public class plugin extends Plugin {
    public FormPlayerSession getSession(ProxiedPlayer player){return WDForms.getSession(player);}

    public void sendMenuForm(ProxiedPlayer player) {
        getSession(player).sendForm(Form.menu()
                .title("Menu form")
                .content("Content")
                .button("§cClose")
                .button("Button #1", (button) -> player.sendMessage("You pressed: " + button.getText()))
                .button("Button #2", (button) -> player.sendMessage("You pressed: " + button.getText()))
                .button("Button #3", (button) -> player.sendMessage("You pressed: " + button.getText()))
                .button("Button #4", (button) -> player.sendMessage("You pressed: " + button.getText()))
                .button("Button #5", (button) -> player.sendMessage("You pressed: " + button.getText()))
                .onSubmit((response) -> player.sendMessage("Clicked: " + response.button() + "!"))
                .onClose(() -> player.sendMessage("Form closed!"))
                .onError((e) -> player.sendMessage("§cError§f: §7" + e.getMessage()))
                .build()
        );
    }
    
    public void sendModalForm(ProxiedPlayer player) {
        getSession(player).sendForm(Form.modal()
                .title("Modal form")
                .content("Content")
                .trueButton("true")
                .falseButton("false")
                .onSubmit((response) -> player.sendMessage("Clicked: " + response + "!"))
                .onError((e) -> player.sendMessage("§cError§f: §7" + e.getMessage()))
                .build()
        );
    }
    
    public void sendCustomForm(ProxiedPlayer player) {
        getSession(player).sendForm(Form.custom()
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
                .build()
        );
    }
}
```

</details>