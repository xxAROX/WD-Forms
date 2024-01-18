/*
 * Copyright (c) Jan Sohn aka. xxAROX
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
