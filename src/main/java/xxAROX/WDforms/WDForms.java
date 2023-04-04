package xxAROX.WDforms;

import com.nukkitx.protocol.bedrock.packet.ModalFormRequestPacket;
import com.nukkitx.protocol.bedrock.packet.ModalFormResponsePacket;
import com.nukkitx.protocol.bedrock.v291.serializer.ModalFormRequestSerializer_v291;
import com.nukkitx.protocol.bedrock.v544.serializer.ModalFormResponseSerializer_v544;
import dev.waterdog.waterdogpe.command.Command;
import dev.waterdog.waterdogpe.command.CommandSender;
import dev.waterdog.waterdogpe.command.CommandSettings;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.ProtocolCodecRegisterEvent;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;
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
        getProxy().getEventManager().subscribe(ProtocolCodecRegisterEvent.class, this::ProtocolCodecRegisterEvent);
        getProxy().getCommandMap().registerCommand(new Command("wdforms", new CommandSettings.Builder().setDescription("To test waterdog forms").build()) {
            @Override
            public boolean onExecute(CommandSender commandSender, String s, String[] strings) {
                if (!(commandSender instanceof ProxiedPlayer player)) {
                    commandSender.sendMessage("Only for players.");
                    return false;
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
                            "text",
                            buttons,
                            player1 -> {
                                player1.sendMessage("Form closed!");
                            }
                    ));
                    player.sendMessage("Form sent!");
                }
                return false;
            }
        });
    }
    @Override public void onDisable() {
        getLogger().info("Disabled!");
    }

    public void PlayerLoginEvent(PlayerLoginEvent event){sessions.put(event.getPlayer(), new FormPlayerSession(event.getPlayer()));}
    public void ProtocolCodecRegisterEvent(ProtocolCodecRegisterEvent event){
        event.getCodecBuilder()
                .registerPacket(ModalFormResponsePacket.class, ModalFormResponseSerializer_v544.INSTANCE, 100)
                .registerPacket(ModalFormRequestPacket.class, ModalFormRequestSerializer_v291.INSTANCE, 101)
        ;
    }
    public static FormPlayerSession getSession(ProxiedPlayer player){return sessions.getOrDefault(player, null);}
}