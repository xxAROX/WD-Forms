package xxAROX.WDForms.event;

import dev.waterdog.waterdogpe.event.CancellableEvent;
import dev.waterdog.waterdogpe.event.Event;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormRequestPacket;

@AllArgsConstructor
public class FormSendEvent extends Event implements CancellableEvent {
    @Getter protected ProxiedPlayer player;
    @Getter protected ModalFormRequestPacket formRequestPacket;
}
