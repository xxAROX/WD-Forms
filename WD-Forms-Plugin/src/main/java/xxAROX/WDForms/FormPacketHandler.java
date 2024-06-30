package xxAROX.WDForms;

import dev.waterdog.waterdogpe.network.protocol.handler.PluginPacketHandler;
import org.cloudburstmc.protocol.bedrock.PacketDirection;
import org.cloudburstmc.protocol.bedrock.packet.*;
import org.cloudburstmc.protocol.common.PacketSignal;

public record FormPacketHandler(FormPlayerSession session) implements PluginPacketHandler {
    @Override public PacketSignal handlePacket(BedrockPacket packet, PacketDirection direction) {
        if (direction.equals(PacketDirection.SERVER_BOUND)) { // Before: FROM_USER
            if (packet instanceof NetworkStackLatencyPacket networkStackLatencyPacket) {
                if (networkStackLatencyPacket.getTimestamp() == WDForms.getSettings_ticker_id()) {
                    return session.handleProxySettings_response(session.getPlayer());
                }// else session.handleImageFix_response(networkStackLatencyPacket);
            }
            else if (packet instanceof ModalFormResponsePacket modalFormResponsePacket) return modalFormResponsePacket.getFormId() < 0 ? session.response(modalFormResponsePacket) : PacketSignal.UNHANDLED;
            else if (packet instanceof ServerSettingsResponsePacket serverSettingsResponsePacket) return serverSettingsResponsePacket.getFormId() < 0 ? session.response(serverSettingsResponsePacket) : PacketSignal.UNHANDLED;
            else if (packet instanceof ServerSettingsRequestPacket) session.handleProxySettings_request(session.getPlayer());
        }/* else {
            if (packet instanceof UpdateAttributesPacket updateAttributesPacket) {
                session.setEntityRuntimeId(updateAttributesPacket.getRuntimeEntityId());
                List<AttributeData> attributes = updateAttributesPacket.getAttributes().stream().filter(attributeData -> attributeData.getName().equals("minecraft:player.level")).toList();
                if (attributes.size() == 1) session.setLevel(attributes.get(0).getValue());
            } DOESN'T WORK
        }*/
        return PacketSignal.UNHANDLED;
    }
}
