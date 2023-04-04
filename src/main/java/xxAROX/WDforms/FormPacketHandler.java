package xxAROX.WDforms;

import com.nukkitx.protocol.bedrock.packet.ModalFormResponsePacket;
import dev.waterdog.waterdogpe.utils.types.PacketHandler;

public class FormPacketHandler extends PacketHandler {
    protected final FormPlayerSession session;

    public FormPacketHandler(FormPlayerSession session) {
        super(session.getPlayer().getUpstream());
        this.session = session;
    }
    public boolean handle(ModalFormResponsePacket packet) {System.out.println(packet);return session.response(packet);}
}
