package xxAROX.WDForms;

import dev.waterdog.waterdogpe.network.protocol.updaters.ProtocolCodecUpdater;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketDefinition;
import org.cloudburstmc.protocol.bedrock.data.PacketRecipient;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsResponsePacket;

public class WDFormsProtocolUpdater implements ProtocolCodecUpdater {
    @Override public BedrockCodec.Builder updateCodec(BedrockCodec.Builder builder, BedrockCodec baseCodec) {
        BedrockPacketDefinition<ModalFormResponsePacket> modalFormResponseDefinition = baseCodec.getPacketDefinition(ModalFormResponsePacket.class);
        BedrockPacketDefinition<ModalFormRequestPacket> modalFormRequestDefinition = baseCodec.getPacketDefinition(ModalFormRequestPacket.class);
        BedrockPacketDefinition<ServerSettingsRequestPacket> serverSettingsRequestDefinition = baseCodec.getPacketDefinition(ServerSettingsRequestPacket.class);
        BedrockPacketDefinition<ServerSettingsResponsePacket> serverSettingsResponseDefinition = baseCodec.getPacketDefinition(ServerSettingsResponsePacket.class);
        builder.registerPacket(ModalFormResponsePacket::new, modalFormResponseDefinition.getSerializer(), modalFormResponseDefinition.getId(), PacketRecipient.BOTH);               // SERVER
        builder.registerPacket(ModalFormRequestPacket::new, modalFormRequestDefinition.getSerializer(), modalFormRequestDefinition.getId(), PacketRecipient.BOTH);                  // CLIENT
        builder.registerPacket(ServerSettingsRequestPacket::new, serverSettingsRequestDefinition.getSerializer(), serverSettingsRequestDefinition.getId(), PacketRecipient.BOTH);   // CLIENT
        builder.registerPacket(ServerSettingsResponsePacket::new, serverSettingsResponseDefinition.getSerializer(), serverSettingsResponseDefinition.getId(), PacketRecipient.BOTH);// SERVER
        return builder;
    }
}
