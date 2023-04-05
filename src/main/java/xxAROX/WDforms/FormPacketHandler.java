package xxAROX.WDforms;

import dev.waterdog.waterdogpe.network.PacketDirection;
import dev.waterdog.waterdogpe.network.protocol.handler.PluginPacketHandler;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.NetworkStackLatencyPacket;
import org.cloudburstmc.protocol.common.PacketSignal;
import xxAROX.WDforms.util.FormImageFix;

public record FormPacketHandler(FormPlayerSession session) implements PluginPacketHandler {
    @Override public PacketSignal handlePacket(BedrockPacket packet, PacketDirection direction) {
        if (direction.equals(PacketDirection.FROM_USER)) {
            if (packet instanceof NetworkStackLatencyPacket)
                FormImageFix.receiveNetworkStackLatency(session.getPlayer(), (NetworkStackLatencyPacket) packet);
            else if (packet instanceof ModalFormResponsePacket)
                return session.response((ModalFormResponsePacket) packet);
        } else {
            if (packet instanceof ModalFormRequestPacket) FormImageFix.fixModalFormRequest(session.getPlayer());
        }
        return PacketSignal.UNHANDLED;
    }
}
