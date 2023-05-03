package xxAROX.WDForms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.scheduler.Task;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomUtils;
import org.cloudburstmc.protocol.bedrock.data.AttributeData;
import org.cloudburstmc.protocol.bedrock.packet.*;
import org.cloudburstmc.protocol.common.PacketSignal;
import xxAROX.WDForms.event.FormResponseEvent;
import xxAROX.WDForms.event.FormSendEvent;
import xxAROX.WDForms.forms.FormValidationError;
import xxAROX.WDForms.forms.types.Form;
import xxAROX.WDForms.forms.types.ProxySettingsForm;
import xxAROX.WDForms.utils.autoback.AutoBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FormPlayerSession {
    @Getter private final ProxiedPlayer player;
    @Setter private Float level;
    @Setter private long entityRuntimeId = -1;

    private int formIdCounter = 0;
    @Getter private final HashMap<Integer, Form<?>> forms = new HashMap<>();
    private Integer settings_ticker = 0;

    public FormPlayerSession(ProxiedPlayer player){
        this.player = player;
        this.player.getPluginPacketHandlers().add(new FormPacketHandler(this));
    }

    public PacketSignal handleProxySettings_response(ProxiedPlayer player){
        if (settings_ticker-- == 0)
            WDForms.getGlobalSettings().forEach(builderFunction -> builderFunction.apply(player).build().sendTo(player));
        else {
            NetworkStackLatencyPacket packet = new NetworkStackLatencyPacket();
            packet.setTimestamp(WDForms.getSettings_ticker_id());
            packet.setFromServer(true);
            player.sendPacket(packet);
        }
        return PacketSignal.HANDLED;
    }
    public void handleProxySettings_request(ProxiedPlayer player){
        settings_ticker = WDForms.getInstance().getConfig().getInt("proxy-settings.send-interval", 15);
        NetworkStackLatencyPacket packet = new NetworkStackLatencyPacket();
        packet.setTimestamp(WDForms.getSettings_ticker_id());
        packet.setFromServer(true);
        player.sendPacket(packet);
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
                Form<?> form = forms.get(packet.getFormId());
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

    /**
     * @internal
     */
    public PacketSignal response(ServerSettingsResponsePacket packet){
        if (!forms.containsKey(packet.getFormId())) {
            WDForms.getInstance().getLogger().debug("Got unexpected response for form " + packet.getFormId());
            return PacketSignal.UNHANDLED;
        }
        if (packet.getFormData() == null)
           packet.setFormData("null");
        try {
            Form<?> form = forms.get(packet.getFormId());
            if (form instanceof ProxySettingsForm) form.handleResponse(player, new JsonMapper().readTree(packet.getFormData().trim()));
        } catch (FormValidationError | JsonProcessingException error) {
            WDForms.getInstance().getLogger().error("Failed to validate form " + forms.getClass().getSimpleName() + ": " + error.getMessage());
            WDForms.getInstance().getLogger().error(error);
        } finally {
            forms.remove(packet.getFormId());
        }
        return PacketSignal.HANDLED;
    }

    public void sendForm(Form<?> form) {
        int formId = nextFormId();
        String formData = null;
        try {
            formData = new JsonMapper().writeValueAsString(form);
        } catch (JsonProcessingException e) {
            WDForms.getInstance().getLogger().error("Error while serializing form: ", e);
        }
        if (formData == null) return;
        forms.put(formId, form);
        if (form instanceof ProxySettingsForm) {
            ServerSettingsResponsePacket serverSettingsResponsePacket = new ServerSettingsResponsePacket();
            serverSettingsResponsePacket.setFormId(formId);
            serverSettingsResponsePacket.setFormData(formData);
            player.sendPacket(serverSettingsResponsePacket);
        } else {
            ModalFormRequestPacket formRequestPacket = new ModalFormRequestPacket();
            formRequestPacket.setFormId(formId);
            formRequestPacket.setFormData(formData);

            FormSendEvent event = new FormSendEvent(player, formRequestPacket);
            ProxyServer.getInstance().getEventManager().callEvent(event);
            if (!event.isCancelled()) {
                AutoBack.handleForm(this, formId, form);
                //if (form instanceof MenuForm) handleImageFix_request(player);
                player.sendPacket(formRequestPacket);
            }
        }
    }

    public Integer nextFormId(){return --formIdCounter;}

    private final HashMap<Long, Runnable> fix_id_callbacks = new HashMap<>();

    public void handleImageFix_request(ProxiedPlayer player) {
        player.getProxy().getScheduler().scheduleDelayed(() -> {
            if (!player.isConnected()) return;
            NetworkStackLatencyPacket latencyPacket = new NetworkStackLatencyPacket();
            latencyPacket.setTimestamp(RandomUtils.nextLong() / 1000 * 1000);
            latencyPacket.setFromServer(true);
            fix_id_callbacks.put(latencyPacket.getTimestamp(), () -> {
                if (!player.isConnected()) return;
                AtomicInteger x = new AtomicInteger(5);
                player.getProxy().getScheduler().scheduleRepeating(new Task() {
                    @Override public void onRun(int current_tick) {
                        player.getProxy().getScheduler().scheduleRepeating(() -> {
                            if (x.getAndDecrement() < 0 || !player.isConnected()) {
                                cancel();
                                return;
                            }
                            List<AttributeData> attributes = new ArrayList<>();
                            attributes.add(new AttributeData("minecraft:player.level", 0.0F, 24791.0F, level, 0.0F));
                            UpdateAttributesPacket packet = new UpdateAttributesPacket();
                            packet.setAttributes(attributes);
                            packet.setTick(0);
                            packet.setRuntimeEntityId(entityRuntimeId);
                            player.sendPacket(packet);

                        }, 10);
                    }

                    @Override public void onCancel() {
                    }
                }, 10);
            });
            player.sendPacket(latencyPacket);
        }, 1);
    }
    public void handleImageFix_response(NetworkStackLatencyPacket latencyPacket) {
        if (fix_id_callbacks.containsKey(latencyPacket.getTimestamp())) fix_id_callbacks.remove(latencyPacket.getTimestamp()).run();
    }
}
