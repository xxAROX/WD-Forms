package xxAROX.WDForms;

import dev.waterdog.waterdogpe.network.PacketDirection;
import dev.waterdog.waterdogpe.network.protocol.handler.PluginPacketHandler;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsResponsePacket;
import org.cloudburstmc.protocol.common.PacketSignal;

public record FormPacketHandler(FormPlayerSession session) implements PluginPacketHandler {
    @Override public PacketSignal handlePacket(BedrockPacket packet, PacketDirection direction) {
        if (direction.equals(PacketDirection.FROM_USER)) {
            if (packet instanceof ModalFormResponsePacket modalFormResponsePacket) {
               return modalFormResponsePacket.getFormId() < 0 ? session.response(modalFormResponsePacket) : PacketSignal.UNHANDLED;
            } else if (packet instanceof ServerSettingsResponsePacket serverSettingsResponsePacket) {
               return serverSettingsResponsePacket.getFormId() < 0 ? session.response(serverSettingsResponsePacket) : PacketSignal.UNHANDLED;
            } else if (packet instanceof ServerSettingsRequestPacket) {
                WDForms.getGlobalSettings().forEach(proxiedPlayerProxySettingsFormFunction -> proxiedPlayerProxySettingsFormFunction.apply(session.getPlayer()).build().sendTo(session.getPlayer()));
            }
        }
        return PacketSignal.UNHANDLED;
    }
}
