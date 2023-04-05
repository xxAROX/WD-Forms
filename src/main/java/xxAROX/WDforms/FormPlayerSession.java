package xxAROX.WDforms;

import com.google.gson.Gson;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import lombok.Getter;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormResponsePacket;
import org.cloudburstmc.protocol.common.PacketSignal;
import xxAROX.WDforms.forms.FormValidationError;
import xxAROX.WDforms.forms.types.Form;

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
        if (!forms.containsKey(packet.getFormId())) {
            WDForms.getInstance().getLogger().debug("Got unexpected response for form " + packet.getFormId());
            return PacketSignal.UNHANDLED;
        }
        try {
            System.out.println("Response: " + packet.getFormData());
            Form form = forms.get(packet.getFormId());
            form.handleResponse(player, packet.getFormData());
        } catch (FormValidationError error) {
            WDForms.getInstance().getLogger().error("Failed to validate form " + forms.getClass().getSimpleName() + ": " + error.getMessage());
            WDForms.getInstance().getLogger().error(error);
        } finally {
            forms.remove(packet.getFormId());
        }
        return PacketSignal.HANDLED;
    }

    public void sendForm(Form form){
        int formId = nextFormId();
        String formData = (new Gson().toJson(form.jsonSerialize()));
        forms.put(formId, form);
        ModalFormRequestPacket formRequestPacket = new ModalFormRequestPacket();
        formRequestPacket.setFormId(formId);
        formRequestPacket.setFormData(formData);
        player.sendPacketImmediately(formRequestPacket);
    }

    public Integer nextFormId(){return --formIdCounter;}
}