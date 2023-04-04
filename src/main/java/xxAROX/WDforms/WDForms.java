package xxAROX.WDforms;

import dev.waterdog.waterdogpe.command.Command;
import dev.waterdog.waterdogpe.command.CommandSender;
import dev.waterdog.waterdogpe.command.CommandSettings;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.network.protocol.ProtocolCodecs;
import dev.waterdog.waterdogpe.network.protocol.updaters.ProtocolCodecUpdater;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ModalFormRequestSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ModalFormResponseSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ServerSettingsRequestSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ServerSettingsResponseSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v544.serializer.ModalFormResponseSerializer_v544;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsResponsePacket;
import xxAROX.WDforms.forms.elements.Button;
import xxAROX.WDforms.forms.elements.Image;
import xxAROX.WDforms.forms.types.MenuForm;

import java.util.ArrayList;
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
                .registerPacket(ModalFormResponsePacket::new, ModalFormResponseSerializer_v291.INSTANCE, 0x65)
                .registerPacket(ModalFormRequestPacket::new, ModalFormRequestSerializer_v291.INSTANCE, 0x64)
                .registerPacket(ServerSettingsRequestPacket::new, ServerSettingsRequestSerializer_v291.INSTANCE, 0x66)
                .registerPacket(ServerSettingsResponsePacket::new, ServerSettingsResponseSerializer_v291.INSTANCE, 0x67)
        );
        getProxy().getCommandMap().registerCommand(new Command("wdforms", new CommandSettings.Builder().setDescription("To test waterdog forms").build()) {
            @Override
            public boolean onExecute(CommandSender commandSender, String s, String[] strings) {
                if (!(commandSender instanceof ProxiedPlayer player)) {
                    commandSender.sendMessage("Only for players.");
                    return true;
                } else {
                    player.sendMessage("Sending form..");
                    FormPlayerSession session = WDForms.getSession(player);
                    ArrayList<Button> buttons = new ArrayList<>();
                    buttons.add(new Button("§cClose"));
                    buttons.add(new Button("Apple", Image.textures("textures/items/apple")));
                    buttons.add(new Button("Ingot", Image.textures("items/iron_ingot")));
                    buttons.add(new Button("Button", ((player1, button) -> player1.sendMessage(button.getText()))));
                    session.sendForm(new MenuForm(
                            "Menu form",
                            "text content",
                            buttons,
                            player1 -> player1.sendMessage("Form closed!")
                    ));
                    player.sendMessage("Form sent!");
                }
                return true;
            }
        });
    }
    @Override public void onDisable() {
        getLogger().info("Disabled!");
    }

    public void PlayerLoginEvent(PlayerLoginEvent event){sessions.put(event.getPlayer(), new FormPlayerSession(event.getPlayer()));}
    public static FormPlayerSession getSession(ProxiedPlayer player){return sessions.getOrDefault(player, null);}
}