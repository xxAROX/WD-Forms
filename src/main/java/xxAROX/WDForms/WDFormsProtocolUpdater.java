package xxAROX.WDForms;

import dev.waterdog.waterdogpe.network.protocol.updaters.ProtocolCodecUpdater;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketDefinition;
import org.cloudburstmc.protocol.bedrock.packet.ToastRequestPacket;

public class CodecUpdater implements ProtocolCodecUpdater {

    @Override
    public BedrockCodec.Builder updateCodec(BedrockCodec.Builder builder, BedrockCodec baseCodec) {
        BedrockPacketDefinition<ModalFormResponsePacket> modalFormResponseDefinition = baseCodec.getPacketDefinition(ModalFormResponsePacket.class);
        BedrockPacketDefinition<ModalFormRequestPacket> modalFormRequestDefinition = baseCodec.getPacketDefinition(ModalFormRequestPacket.class);
        BedrockPacketDefinition<ServerSettingsRequestPacket> serverSettingsRequestDefinition = baseCodec.getPacketDefinition(ServerSettingsRequestPacket.class);
        BedrockPacketDefinition<ServerSettingsResponsePacket> serverSettingsResponseDefinition = baseCodec.getPacketDefinition(ServerSettingsResponsePacket.class);
        builder.registerPacket(ModalFormResponsePacket::new, modalFormResponseDefinition.getSerializer(), modalFormResponseDefinition.getId());
        builder.registerPacket(ModalFormRequestPacket::new, modalFormRequestDefinition.getSerializer(), modalFormRequestDefinition.getId());
        builder.registerPacket(ServerSettingsRequestPacket::new, serverSettingsRequestDefinition.getSerializer(), serverSettingsRequestDefinition.getId());
        builder.registerPacket(ServerSettingsResponsePacket::new, serverSettingsResponseDefinition.getSerializer(), serverSettingsResponseDefinition.getId());
        return builder;
    }
}
