package xxAROX.WDforms;

import dev.waterdog.waterdogpe.network.PacketDirection;
import dev.waterdog.waterdogpe.network.protocol.handler.PluginPacketHandler;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormResponsePacket;
import org.cloudburstmc.protocol.common.PacketSignal;

public class FormPacketHandler implements PluginPacketHandler {
    protected final FormPlayerSession session;

    public FormPacketHandler(FormPlayerSession session) {
        this.session = session;
    }

    @Override
    public PacketSignal handlePacket(BedrockPacket packet, PacketDirection direction) {
        if (packet instanceof ModalFormResponsePacket && direction.equals(PacketDirection.FROM_USER)) return session.response((ModalFormResponsePacket) packet);
        return PacketSignal.UNHANDLED;
    }
}
