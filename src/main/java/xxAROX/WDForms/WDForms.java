package xxAROX.WDForms;

import dev.waterdog.waterdogpe.command.Command;
import dev.waterdog.waterdogpe.command.CommandSender;
import dev.waterdog.waterdogpe.command.CommandSettings;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.network.protocol.ProtocolCodecs;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ModalFormRequestSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ServerSettingsRequestSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ServerSettingsResponseSerializer_v291;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsResponsePacket;
import xxAROX.WDForms.forms.types.Form;

import java.util.HashMap;

public class WDForms extends Plugin {
    private static WDForms instance;
    public static WDForms getInstance() {return instance;}
    final private static HashMap<ProxiedPlayer, FormPlayerSession> sessions = new HashMap<>();


    @Override public void onStartup() {
        instance = this;
    }
    @Override public void onEnable() {
        getLogger().info("Enabled!");
        getProxy().getEventManager().subscribe(PlayerLoginEvent.class, (event -> {
            FormPlayerSession session = new FormPlayerSession(event.getPlayer());
            sessions.put(event.getPlayer(), session);
        }));
        ProtocolCodecs.addUpdater((builder, bedrockCodec) -> builder
                .registerPacket(ModalFormResponsePacket::new, ModalFormResponseSerializer.INSTANCE, 0x65)
                .registerPacket(ModalFormRequestPacket::new, ModalFormRequestSerializer_v291.INSTANCE, 0x64)
                .registerPacket(ServerSettingsRequestPacket::new, ServerSettingsRequestSerializer_v291.INSTANCE, 0x66)
                .registerPacket(ServerSettingsResponsePacket::new, ServerSettingsResponseSerializer_v291.INSTANCE, 0x67)
        );
        getProxy().getCommandMap().registerCommand(new Command("wdforms", new CommandSettings.Builder().setUsageMessage("§7/wdforms <menu|custom|modal>").setDescription("To test waterdog forms").build()) {
            @Override public boolean onExecute(CommandSender commandSender, String s, String[] args) {
                if (!(commandSender instanceof ProxiedPlayer player)) {
                    commandSender.sendMessage("Only for players.");
                    return true;
                } else {
                    FormPlayerSession session = WDForms.getSession(player);
                    if (args.length == 1) {
                        if (args[0].equals("menu")) {
                            session.sendForm(Form.menu()
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
                        } else if (args[0].equals("custom")) {
                            session.sendForm(Form.custom()
                                    .title("Custom form")
                                    .label("Label")
                                    .input("Input", input -> player.sendMessage(input.getValue()))
                                    .label("Label")
                                    .dropdown("Dropdown", new String[]{"Option 1","Option 2","Option 3","Option 4","Option 5"}, dropdown -> player.sendMessage(dropdown.getSelectedOption()))
                                    .label("Label")
                                    .stepSlider("StepSlider", new String[]{"Step 1","Step 2","Step 3","Step 4","Step 5"}, (step_slider) -> player.sendMessage(step_slider.getSelectedOption()))
                                    .label("Label")
                                    .slider("Slider", 1f, 5f, 1f, 1f, (slider) -> player.sendMessage(slider.getValue().toString()))
                                    .label("Label")
                                    .toggle("Toggle", (toggle) -> player.sendMessage("toggle: " + (toggle.getValue() ? "true" : "false")))
                                    .onSubmit(response -> player.sendMessage("Form submitted!\n" + response.toString()))
                                    .onClose(() -> player.sendMessage("Form closed!"))
                                    .onError(e -> player.sendMessage("§cError§f: §7" + e.getMessage()))
                                    .build()
                            );
                        } else if (args[0].equals("modal")) {
                            session.sendForm(Form.modal()
                                    .title("Modal form")
                                    .content("text content")
                                    .trueButton("§2true")
                                    .falseButton("§4false")
                                    .onSubmit((value) -> player.sendMessage("§o§7You selected: " + value.toString()))
                                    .onError(e -> player.sendMessage("§cError§f: §7" + e.getMessage()))
                                    .build()
                            );
                        } else return false;
                        return true;
                    }
                }
                return false;
            }
        });
    }
    @Override public void onDisable() {
        getLogger().info("Disabled!");
    }

    public void PlayerLoginEvent(PlayerLoginEvent event){sessions.put(event.getPlayer(), new FormPlayerSession(event.getPlayer()));}
    public static FormPlayerSession getSession(ProxiedPlayer player){return sessions.getOrDefault(player, null);}
}