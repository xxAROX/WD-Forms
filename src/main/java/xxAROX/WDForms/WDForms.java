package xxAROX.WDForms;

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
    }
    @Override public void onDisable() {
        getLogger().info("Disabled!");
    }

    public void PlayerLoginEvent(PlayerLoginEvent event){sessions.put(event.getPlayer(), new FormPlayerSession(event.getPlayer()));}
    public static FormPlayerSession getSession(ProxiedPlayer player){return sessions.getOrDefault(player, null);}
}