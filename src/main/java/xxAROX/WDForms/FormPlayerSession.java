package xxAROX.WDForms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import lombok.Getter;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormResponsePacket;
import org.cloudburstmc.protocol.common.PacketSignal;
import xxAROX.WDForms.event.FormResponseEvent;
import xxAROX.WDForms.event.FormSendEvent;
import xxAROX.WDForms.forms.FormValidationError;
import xxAROX.WDForms.forms.types.Form;

import java.util.HashMap;

public class FormPlayerSession {
    @Getter private final ProxiedPlayer player;

    private int formIdCounter = 0;
    private final HashMap<Integer, Form> forms = new HashMap<>();

    public FormPlayerSession(ProxiedPlayer player){
        this.player = player;
        this.player.getPluginPacketHandlers().add(new FormPacketHandler(this));
    }

    /**
     * @internal
     */
    public PacketSignal response(ModalFormResponsePacket packet){
        FormResponseEvent event = new FormResponseEvent(player, packet);
        ProxyServer.getInstance().getEventManager().callEvent(event);
        if (!event.isCancelled()) {
            if (!forms.containsKey(packet.getFormId())) {
                WDForms.getInstance().getLogger().debug("Got unexpected response for form " + packet.getFormId());
                return PacketSignal.UNHANDLED;
            }
            if (packet.getFormData() == null)
               packet.setFormData("null");
            try {
                Form form = forms.get(packet.getFormId());
                form.handleResponse(player, new JsonMapper().readTree(packet.getFormData().trim()));
            } catch (FormValidationError | JsonProcessingException error) {
                WDForms.getInstance().getLogger().error("Failed to validate form " + forms.getClass().getSimpleName() + ": " + error.getMessage());
                WDForms.getInstance().getLogger().error(error);
            } finally {
                forms.remove(packet.getFormId());
            }
        }
        return PacketSignal.HANDLED;
    }

    public void sendForm(Form form) {
        int formId = nextFormId();
        String formData = null;
        try {
            formData = new JsonMapper().writeValueAsString(form);
        } catch (JsonProcessingException e) {
            WDForms.getInstance().getLogger().error("Error while serializing form: ", e);
        }
        if (formData == null) return;
        forms.put(formId, form);
        ModalFormRequestPacket formRequestPacket = new ModalFormRequestPacket();
        formRequestPacket.setFormId(formId);
        formRequestPacket.setFormData(formData);
        
        FormSendEvent event = new FormSendEvent(player, formRequestPacket);
        ProxyServer.getInstance().getEventManager().callEvent(event);
        if (!event.isCancelled()) player.sendPacket(formRequestPacket);
    }

    public Integer nextFormId(){return --formIdCounter;}
}
