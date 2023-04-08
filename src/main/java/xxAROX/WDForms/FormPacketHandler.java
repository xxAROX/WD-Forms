package xxAROX.WDForms;

import dev.waterdog.waterdogpe.network.PacketDirection;
import dev.waterdog.waterdogpe.network.protocol.handler.PluginPacketHandler;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormResponsePacket;
import org.cloudburstmc.protocol.common.PacketSignal;

public record FormPacketHandler(FormPlayerSession session) implements PluginPacketHandler {
    @Override public PacketSignal handlePacket(BedrockPacket packet, PacketDirection direction) {
        return direction.equals(PacketDirection.FROM_USER) && (packet instanceof ModalFormResponsePacket modalFormResponsePacket) && modalFormResponsePacket.getFormId() < 0 ? session.response(modalFormResponsePacket) : PacketSignal.UNHANDLED;
    }
}
